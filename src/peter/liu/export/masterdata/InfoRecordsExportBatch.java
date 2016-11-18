package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

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

import com.wuerth.phoenix.Phxbasic.models.ConversionDefinition;
import com.wuerth.phoenix.Phxbasic.models.PhxbasicController;
import com.wuerth.phoenix.Phxbasic.models.SupplierPriceList;
import com.wuerth.phoenix.Phxbasic.models.SupplierPriceListPosition;
import com.wuerth.phoenix.Phxbasic.models.SupplierProduct;
import com.wuerth.phoenix.Phxbasic.models.SupplierProductPurchaseUnit;
import com.wuerth.phoenix.basic.etnax.common.utilsdir.DoubleUtils;
import com.wuerth.phoenix.basic.etnax.sap.webservice.service.SylvestrixHelper;
import com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi.BIDateMapping;
import com.wuerth.phoenix.bcutil.IteratorFactory;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;

public class InfoRecordsExportBatch {
	private PhxbasicController _controller;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private Properties propertiesForMapping;
	private String dataSource = "../../etc/exportSAP/Info_record_template.xlsx";
	private String partA = "../../var/exportSAP/InfoRecords_PARTA.xlsx";
	private String partB = "../../var/exportSAP/InfoRecords_PARTB.xlsx";
	private String rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private List<InfoRecordsExportBean> list = new ArrayList<InfoRecordsExportBean>();
	private String								ownCompanyName;
	private void init() {
		_controller = SylvestrixHelper.get().getController();
		propertiesForMapping = MappingParam.getInstance().getMappingProperties();
		ownCompanyName = _controller.getSingletonOwnCompany().getName();
		list = export();
		
	}
	
	public int column(String s) {

		if (s.length() == 1) {
			return rows.indexOf(s);
		}

		return 26 * (rows.indexOf(s.substring(0, 1)) + 1)
				+ column(s.substring(1, s.length()));
	}
	
	
	public static void main(String[] args) {
		InfoRecordsExportBatch batch = new InfoRecordsExportBatch();
		batch.init();
		batch.insertExcel_PARTA();
		batch.insertExcel_PARTB();

	}

	private void insertExcel_PARTA() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(partA);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(dataSource));
			XSSFSheet sheetTabelle1 = twb.getSheet("Tabelle1");
			int rowNum = 5;
			for (InfoRecordsExportBean ireb : list) {
				XSSFRow rowTabelle1 = getXSSFRow(sheetTabelle1, rowNum);
				XSSFCell cellA_Tabelle1 = getXSSFCell(rowTabelle1, column("A"));
				cellA_Tabelle1.setCellValue(ireb.getVendorNumber());
				XSSFCell cellB_Tabelle1 = getXSSFCell(rowTabelle1, column("B"));
				cellB_Tabelle1.setCellValue(ireb.getSylProductNumber());
				XSSFCell cellC_Tabelle1 = getXSSFCell(rowTabelle1, column("C"));
				cellC_Tabelle1.setCellValue(ireb.getSapProductNumber());
				XSSFCell cellD_Tabelle1 = getXSSFCell(rowTabelle1, column("D"));
				cellD_Tabelle1.setCellValue(ireb.getReminder1());
				XSSFCell cellE_Tabelle1 = getXSSFCell(rowTabelle1, column("E"));
				cellE_Tabelle1.setCellValue(ireb.getReminder2());
				XSSFCell cellF_Tabelle1 = getXSSFCell(rowTabelle1, column("F"));
				cellF_Tabelle1.setCellValue(ireb.getReminder3());
				XSSFCell cellG_Tabelle1 = getXSSFCell(rowTabelle1, column("G"));
				cellG_Tabelle1.setCellValue(ireb.getVendorMaterialNumber());
				XSSFCell cellH_Tabelle1 = getXSSFCell(rowTabelle1, column("H"));
				cellH_Tabelle1.setCellValue("/");
				XSSFCell cellI_Tabelle1 = getXSSFCell(rowTabelle1, column("I"));
				cellI_Tabelle1.setCellValue("/");
				XSSFCell cellJ_Tabelle1 = getXSSFCell(rowTabelle1, column("J"));
				cellJ_Tabelle1.setCellValue("/");
				XSSFCell cellK_Tabelle1 = getXSSFCell(rowTabelle1, column("K"));
				cellK_Tabelle1.setCellValue("/");
				XSSFCell cellL_Tabelle1 = getXSSFCell(rowTabelle1, column("L"));
				cellL_Tabelle1.setCellValue("/");
				XSSFCell cellM_Tabelle1 = getXSSFCell(rowTabelle1, column("M"));
				cellM_Tabelle1.setCellValue("/");
				
				XSSFCell cellN_Tabelle1 = getXSSFCell(rowTabelle1, column("N"));
				cellN_Tabelle1.setCellValue(ireb.getPlannedDeliveryTimeinDays());
				XSSFCell cellO_Tabelle1 = getXSSFCell(rowTabelle1, column("O"));
				cellO_Tabelle1.setCellValue(ireb.getUnderdeliveryToleranceLimit());
				XSSFCell cellP_Tabelle1 = getXSSFCell(rowTabelle1, column("P"));
				cellP_Tabelle1.setCellValue("");
				XSSFCell cellQ_Tabelle1 = getXSSFCell(rowTabelle1, column("Q"));
				cellQ_Tabelle1.setCellValue(ireb.getOverdeliberyToleranceLimit());
				XSSFCell cellR_Tabelle1 = getXSSFCell(rowTabelle1, column("R"));
				cellR_Tabelle1.setCellValue("/");
				System.out.println("Row:" + rowNum);
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
			if(fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
	
	private void insertExcel_PARTB() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(partB);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(dataSource));
			XSSFSheet sheetTabelle1 = twb.getSheet("Tabelle1");
			int rowNum = 5;
			for (InfoRecordsExportBean ireb : list) {
				XSSFRow rowTabelle1 = getXSSFRow(sheetTabelle1, rowNum);
				XSSFCell cellS_Tabelle1 = getXSSFCell(rowTabelle1, column("S"));
				cellS_Tabelle1.setCellValue(ireb.getMinimumOrderQuantity());
				XSSFCell cellT_Tabelle1 = getXSSFCell(rowTabelle1, column("T"));
				cellT_Tabelle1.setCellValue("/");
				XSSFCell cellU_Tabelle1 = getXSSFCell(rowTabelle1, column("U"));
				cellU_Tabelle1.setCellValue(ireb.getConfirmationControlKey());
				XSSFCell cellV_Tabelle1 = getXSSFCell(rowTabelle1, column("V"));
				cellV_Tabelle1.setCellValue("/");
				XSSFCell cellW_Tabelle1 = getXSSFCell(rowTabelle1, column("W"));
				cellW_Tabelle1.setCellValue(ireb.getGR_based_Invoice_Verification());
				XSSFCell cellX_Tabelle1 = getXSSFCell(rowTabelle1, column("X"));
				cellX_Tabelle1.setCellValue("/");
				XSSFCell cellY_Tabelle1 = getXSSFCell(rowTabelle1, column("Y"));
				cellY_Tabelle1.setCellValue(ireb.getNetPrice());
				XSSFCell cellZ_Tabelle1 = getXSSFCell(rowTabelle1, column("Z"));
				cellZ_Tabelle1.setCellValue(ireb.getCurrencyKey());
				XSSFCell cellAA_Tabelle1 = getXSSFCell(rowTabelle1, column("AA"));
				cellAA_Tabelle1.setCellValue(ireb.getPriceUnit());
				XSSFCell cellAB_Tabelle1 = getXSSFCell(rowTabelle1, column("AB"));
				cellAB_Tabelle1.setCellValue(ireb.getOrderPriceUnit());
				XSSFCell cellAC_Tabelle1 = getXSSFCell(rowTabelle1, column("AC"));
				cellAC_Tabelle1.setCellValue("/");
				XSSFCell cellAD_Tabelle1 = getXSSFCell(rowTabelle1, column("AD"));
				cellAD_Tabelle1.setCellValue("/");
				XSSFCell cellAE_Tabelle1 = getXSSFCell(rowTabelle1, column("AE"));
				cellAE_Tabelle1.setCellValue("/");
				XSSFCell cellAF_Tabelle1 = getXSSFCell(rowTabelle1, column("AF"));
				cellAF_Tabelle1.setCellValue("/");

				XSSFCell cellAG_Tabelle1 = getXSSFCell(rowTabelle1, column("AG"));
				cellAG_Tabelle1.setCellValue(ireb.getValidFrom());
				XSSFCell cellAH_Tabelle1 = getXSSFCell(rowTabelle1, column("AH"));
				cellAH_Tabelle1.setCellValue(ireb.getValidTo());
				System.out.println("Row:" + rowNum);
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

	private List<InfoRecordsExportBean> export() {
		List<InfoRecordsExportBean> list = new ArrayList<InfoRecordsExportBean>();
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(SupplierPriceListPosition.class);
		qh.addAscendingOrdering(SupplierPriceListPosition.ASS_SUPPLIERPRODUCT_REF + SupplierProduct.SUPPLIERPRODUCTNUMBER);
		PEnumeration penum = null;
        Condition condition = qh.condition();
        IteratorFactory fac = (IteratorFactory) _controller.createIteratorFactory();
        try {
			penum = fac.getCursor(condition);
			while (penum.hasMoreElements()) {
				SupplierPriceListPosition spp = (SupplierPriceListPosition) penum.nextElement();
				InfoRecordsExportBean ireb = new InfoRecordsExportBean();
				SupplierPriceList spl = spp.getParentSupplierPriceList();
//				if (ownCompanyName.equals("伍尔特（沈阳）五金工具有限公司")) {
//					if (MasterDateMapping.syExistingVender.contains(spl
//							.getParentSupplier().getAccountNumber())) {
//						continue;
//					}
//				}
//				if (ownCompanyName.equals("伍尔特（天津）国际贸易有限公司")) {
//					if (MasterDateMapping.tjExistingVender.contains(spl
//							.getParentSupplier().getAccountNumber())) {
//						continue;
//					}
//				}
				ireb.setVendorNumber(spl.getParentSupplier().getAccountNumber());
				ireb.setSylProductNumber(spp.getSupplierProduct().getCompanyProductNumber());
				String sapProductNumber=BIDateMapping.productMap.get(ireb.getSylProductNumber());
				sapProductNumber=sapProductNumber==null?"":sapProductNumber;
				ireb.setSapProductNumber(sapProductNumber);
				ireb.setReminder1(3);
				ireb.setReminder2(11);
				ireb.setReminder3(19);
				ireb.setPlannedDeliveryTimeinDays(spp.getSupplierProduct().getDeliveryDays());
				ireb.setUnderdeliveryToleranceLimit(0);
				ireb.setOverdeliberyToleranceLimit(0);
				ireb.setPurchasingGroup("");
				ireb.setMinimumOrderQuantity(spp.getMinOrderQuantityForPrice().getAmount());
				ireb.setConfirmationControlKey("Z001");
				ireb.setGR_based_Invoice_Verification("X");
				ireb.setNetPrice(DoubleUtils.getRoundedAmount(spp.getNetPrice().getAmount()));
				ireb.setCurrencyKey(spp.getNetPrice().getCurrencyCode());
				ireb.setPriceUnit(spp.getPriceQuantity().getAmount());
				ireb.setOrderPriceUnit(spp.getPurchasePriceForOBU().getPricePerUnit().getUnit());
				ireb.setValidFrom(sdf.format(spl.getValidFrom()));
				SupplierProductPurchaseUnit sppu = spp.getSupplierProduct().getSupplierProductPurchaseUnit();
				if(sppu != null) {
					ConversionDefinition cd = sppu.getConversionDefinition();
					if(cd != null) {
						ireb.setNumeratorConversion(cd.getConversionNumerator());
						ireb.setDenominatorConversion(cd.getConversionDenominator());
					}
				}
				
				if(spl.getValidTo() != null) {
					ireb.setValidTo(sdf.format(spl.getValidTo()));
				} else {
					ireb.setValidTo("31.12.9999");
				}
				list.add(ireb);
				
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

}
