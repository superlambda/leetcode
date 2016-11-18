package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.models.CompanyPeriod;
import com.wuerth.phoenix.Phxbasic.models.CompanyYear;
import com.wuerth.phoenix.Phxbasic.models.PhxbasicController;
import com.wuerth.phoenix.Phxbasic.models.SalesArea;
import com.wuerth.phoenix.Phxbasic.models.SalesPlan;
import com.wuerth.phoenix.Phxbasic.models.SalesStructureElement;
import com.wuerth.phoenix.Phxbasic.models.Salesman;
import com.wuerth.phoenix.Phxbasic.models.SalesmanAssignment;
import com.wuerth.phoenix.basic.etnax.sap.webservice.service.SylvestrixHelper;
import com.wuerth.phoenix.basic.etnax.utilities.Periods;
import com.wuerth.phoenix.bcutil.IteratorFactory;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.util.PDate;

public class SalesPlanExportBatch {
	private PhxbasicController _controller;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private Properties propertiesForMapping;
	private String dataSource = "../../etc/exportSAP/Planning_figures_template.xlsx";
	private String target = "../../var/exportSAP/Planning_figures_template_out.xlsx";
	private String rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private void init() {
		_controller = SylvestrixHelper.get().getController();
		propertiesForMapping = MappingParam.getInstance().getMappingProperties();
	}
	
	public int column(String s) {

		if (s.length() == 1) {
			return rows.indexOf(s);
		}

		return 26 * (rows.indexOf(s.substring(0, 1)) + 1)
				+ column(s.substring(1, s.length()));
	}
	
	
	private String  getSalesArea(Salesman salesman, PDate firstDate, PDate lastDate) {
		List salesmanAssignmentList = salesman.getAllSalesmanAssignment();
		String area = "";
		for (Iterator iter = salesmanAssignmentList.iterator(); iter.hasNext();) {
			SalesmanAssignment salesmanAssignment = (SalesmanAssignment) iter.next();
			if (Periods.isOverlapping(firstDate, lastDate, salesmanAssignment.getDateFrom(), salesmanAssignment.getDateTo())) {
				SalesStructureElement sse = salesmanAssignment.getParentSalesStructureElement();
				if(sse instanceof SalesArea) {
					area = sse.getId()  + ",";
				}
			}
		}
		if(!area.equals("")) {
			area = area.substring(0, area.length() - 1);
		}
		return area;
	}
	
	private  List<SalesPlanExportBean> exportSalesPlan() {
		List<SalesPlanExportBean> list = new ArrayList<SalesPlanExportBean>();
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(SalesPlan.class);
		qh.addAscendingOrdering(SalesPlan.PERIODNUMBER);
		PEnumeration penum = null;
        Condition condition = qh.condition();
        IteratorFactory fac = (IteratorFactory) _controller.createIteratorFactory();
        try {
			penum = fac.getCursor(condition);
			while (penum.hasMoreElements()) {
				
				SalesPlan sp = (SalesPlan) penum.nextElement();
				System.out.println(sp.getPeriodNumber() + "####################");
				Salesman salesman = sp.getParentSalesman();
				int year = sp.getPeriodNumber() / 100;
				int month = sp.getPeriodNumber() % 100;
				CompanyYear cy = _controller.lookupCompanyYear((short) year);
				if(cy != null) {
					CompanyPeriod cp = cy.lookupCompanyPeriod(month);
					if(cp != null) {
						PDate firstDate = cp.getFrom();
						PDate lastDate = cp.getTo();
						SalesPlanExportBean speb = new SalesPlanExportBean();
						speb.setMonth(year + "" + month);
						speb.setSalesOrganization("");
						speb.setSalesRep(salesman.getRegisterNumber() + "");
						speb.setPlanHierarchy("");
						speb.setSalesSectorSalesRep("");
						speb.setSalesArea(getSalesArea(salesman, firstDate, lastDate));
						speb.setSalesAreaFunctionNo("");
						speb.setPlanKF("");
						speb.setPlanTurnover(sp.getPlanTurnover().getAmount()+ "");
						speb.setCurrency(sp.getPlanTurnover().getCurrencyCode());
						list.add(speb);
					} else {
						continue;
					}

				} else {
					continue;
				}
				

			}
			return list;
		} catch (PUserException e) {
			e.printStackTrace();
		} finally {
			penum.destroy();
		}
        return null;
	}
	
	private XSSFRow getXSSFRow(XSSFSheet sheet, int i) {
		XSSFRow row = sheet.getRow(i);
		if(row == null) {
			row = sheet.createRow(i);
		}
		return row;
	}
	
	private XSSFCell getXSSFCell(XSSFRow row, int i) {
		XSSFCell cell = row.getCell(i);
		if(cell == null) {
			cell = row.createCell(i);
		}
		return cell;
	}
	
	private void insertExcel() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(target);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(dataSource));
			List<SalesPlanExportBean> list = exportSalesPlan();
			int rowNum = 1;
			XSSFSheet sheet = twb.getSheet("Tabelle1");
			for (SalesPlanExportBean speb : list) {
				XSSFRow row = getXSSFRow(sheet, rowNum);
				XSSFCell cellA = getXSSFCell(row, column("A"));
				cellA.setCellValue(speb.getMonth());
				XSSFCell cellB = getXSSFCell(row, column("B"));
				cellB.setCellValue(speb.getPlanHierarchy());
				XSSFCell cellC = getXSSFCell(row, column("C"));
				cellC.setCellValue(speb.getSalesOrganization());
				XSSFCell cellD = getXSSFCell(row, column("D"));
				cellD.setCellValue(speb.getSalesSectorSalesRep());
				XSSFCell cellE = getXSSFCell(row, column("E"));
				cellE.setCellValue(speb.getSalesRegion());
				XSSFCell cellF = getXSSFCell(row, column("F"));
				cellF.setCellValue(speb.getBusinessLocation());
				XSSFCell cellG = getXSSFCell(row, column("G"));
				cellG.setCellValue(speb.getSalesChannel());
				XSSFCell cellH = getXSSFCell(row, column("H"));
				cellH.setCellValue(speb.getSalesRep());
				XSSFCell cellI = getXSSFCell(row, column("I"));
				cellI.setCellValue(speb.getBranchOffice());
				XSSFCell cellJ = getXSSFCell(row, column("J"));
				cellJ.setCellValue(speb.getSalesAreaManagerNo());
				XSSFCell cellK = getXSSFCell(row, column("K"));
				cellK.setCellValue(speb.getSalesAreaFunctionNo());
				XSSFCell cellL = getXSSFCell(row, column("L"));
				cellL.setCellValue(speb.getSalesArea());
				XSSFCell cellM = getXSSFCell(row, column("M"));
				cellM.setCellValue(speb.getPlanTurnover());
				XSSFCell cellN = getXSSFCell(row, column("N"));
				cellN.setCellValue(speb.getPlanKF());
				XSSFCell 	cellO = getXSSFCell(row, column("O"));
				cellO.setCellValue(speb.getCurrency());
				rowNum++;
			}
			twb.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		SalesPlanExportBatch salesPlanExportBatch = new SalesPlanExportBatch();
		salesPlanExportBatch.init();
		salesPlanExportBatch.insertExcel();
	}

}
