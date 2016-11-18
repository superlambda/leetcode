package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.models.PhxbasicController;
import com.wuerth.phoenix.Phxbasic.models.PricelistPosition;
import com.wuerth.phoenix.Phxbasic.models.PricelistVersion;
import com.wuerth.phoenix.Phxbasic.models.ProductPricePosition;
import com.wuerth.phoenix.basic.etnax.common.utilsdir.DoubleUtils;
import com.wuerth.phoenix.basic.etnax.sap.webservice.service.SylvestrixHelper;
import com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi.BIDateMapping;
import com.wuerth.phoenix.bcutil.IteratorFactory;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.PhxLog;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.util.PDate;

public class PricingExportBatch {
	private PhxbasicController _controller;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private Properties propertiesForMapping;
	private String dataSource = "../../etc/exportSAP/Template_Pricing.xlsx";
	private String target = "../../var/exportSAP/PriceList_.xlsx";
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
	
	private List<PricingExportBean> export() throws PUserException {
		List<PricingExportBean> list = new ArrayList<PricingExportBean>();
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(PricelistPosition.class);
		qh.addAscendingOrdering(PricelistPosition.PARENT_PRODUCTPRICEPOSITION_REF + ProductPricePosition.PRODUCTNUMBER);
		PEnumeration penum = null;
        Condition condition = qh.condition();
		PhxLog.BUSINESS.message(PhxLog.INFORMATION, " query condition: ",
				"QUERY--->>>>" + Query.getQueryString(condition));
        IteratorFactory fac = (IteratorFactory) _controller.createIteratorFactory();
        PDate validToDate=new PDate(2013, 11, 31);
        try {
			penum = fac.getCursor(condition);
			while (penum.hasMoreElements()) {
				PricelistPosition pp  = (PricelistPosition) penum.nextElement();
				ProductPricePosition ppp = pp.getParentProductPricePosition();
				PricelistVersion pv=ppp.getParentPricelistVersion();
				if(pv.getValidTo()!=null&&pv.getValidTo().before(validToDate)){
					continue;
				}
				PricingExportBean pe = new PricingExportBean();
				pe.setDistChl("XA");
//				pe.setLegacyProductNumber(pp.getParentProductPricePosition().getProductNumber());
				pe.setProductNumber(ppp.getProductNumber());
//				PricelistVersion pv = ppp.getParentPricelistVersion();
				pe.setValidFrom(sdf.format(pv.getValidFrom()));
				if(pv.getValidTo() != null) {
					pe.setValidTo(sdf.format(pv.getValidTo()));
				} else {
					pe.setValidTo("31.12.9999");
				}
				pe.setNetPrice(pp.getNetPrice().getPricePerUnit().getAmount());
				pe.setVatPrice(pp.getNetPrice().getPricePerUnit().getAmount()*1.17);
				pe.setPriceUnit(pp.getNetPrice().getPricePerUnit().getUnit());
				list.add(pe);
				System.out.println("\n Number of PricelistPositions loaded: "
						+ list.size());
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
	
	private void insertExcel() throws PUserException  {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(target);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(dataSource));
			XSSFSheet sheetSheet1 = twb.getSheet("Sheet1");
			int rowNum = 1;
			List<PricingExportBean> list = export();
			for (PricingExportBean peb : list) {
				// sheet Sheet1
				XSSFRow rowSheet1 = getXSSFRow(sheetSheet1, rowNum);
				XSSFCell cellA_Sheet1 = getXSSFCell(rowSheet1, column("A"));
				cellA_Sheet1.setCellValue(peb.getDistChl());
				XSSFCell cellB_Sheet1 = getXSSFCell(rowSheet1, column("B"));

				String eeeeProductNumber = BIDateMapping.productMap.get(peb
						.getProductNumber());
				if (eeeeProductNumber != null
						&& !eeeeProductNumber.trim().equals("")) {
					cellB_Sheet1.setCellValue(eeeeProductNumber);
				} else {
					cellB_Sheet1.setCellValue(peb.getProductNumber());
					XSSFCell cellH_Sheet1 = getXSSFCell(rowSheet1, column("H"));
					cellH_Sheet1.setCellValue("x");
				}
				XSSFCell cellC_Sheet1 = getXSSFCell(rowSheet1, column("C"));
				cellC_Sheet1.setCellValue(peb.getValidFrom());
				XSSFCell cellD_Sheet1 = getXSSFCell(rowSheet1, column("D"));
				cellD_Sheet1.setCellValue(peb.getValidTo());
				XSSFCell cellE_Sheet1 = getXSSFCell(rowSheet1, column("E"));
				cellE_Sheet1.setCellValue(peb.getNetPrice());
				XSSFCell cellF_Sheet1 = getXSSFCell(rowSheet1, column("F"));
				cellF_Sheet1.setCellValue(peb.getVatPrice());

				XSSFCell cellG_Sheet1 = getXSSFCell(rowSheet1, column("G"));
				cellG_Sheet1.setCellValue(peb.getPriceUnit());
				rowNum++;
			}

			BufferedOutputStream bout = new BufferedOutputStream(fos);
			twb.write(bout);
			bout.flush();
			bout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
	
	public static void main(String[] args) throws PUserException {
		PricingExportBatch batch = new PricingExportBatch();
		batch.init();
		batch.insertExcel();

	}
}
