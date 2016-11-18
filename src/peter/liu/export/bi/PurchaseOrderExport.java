package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.enums.PurchaseOrderLineStatus;
import com.wuerth.phoenix.Phxbasic.enums.PurchaseOrderStatus;
import com.wuerth.phoenix.Phxbasic.models.Demand;
import com.wuerth.phoenix.Phxbasic.models.Product;
import com.wuerth.phoenix.Phxbasic.models.PurchaseOrder;
import com.wuerth.phoenix.Phxbasic.models.PurchaseOrderLine;
import com.wuerth.phoenix.Phxbasic.models.WarehouseProductAllowance;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;
import com.wuerth.phoenix.util.PDate;
import com.wuerth.phoenix.util.money.MoneyController;

/**
 * PurchaseOrderExport
 * 
 * @author pcnsh197
 * 
 */
public class PurchaseOrderExport extends BatchRunner {

	private int						NUMBER_OF_BATCH_TO_FECTCH	= 100;

	List<POHeaderInformationBean>	orderHeaderInfoList			= new ArrayList<POHeaderInformationBean>();

	List<POItemInformationBean>		poItemInfoList				= new ArrayList<POItemInformationBean>();

	private String					dataSource					= "../../etc/exportSAP/OpenPO_Template.xlsx";

//	private String					target						= "../../var/exportSAP/OpenPO_.xlsx";
	
	
	private HashSet<String>		gzWH									= new HashSet<String>();

	private HashSet<String>		szWH									= new HashSet<String>();

	private HashSet<String>		cqWH									= new HashSet<String>();

	private HashSet<String>		cdWH									= new HashSet<String>();

	private String				project;																					// GZ,SZ,CQ,CD

	@Override
	protected void processargs(String[] args) {
		for (String argstr : args) {
			if (argstr.equalsIgnoreCase("commit")) {
				willcommit = true;
			} else {
				project = argstr;
			}
		}

		System.out.println("###################project: " + project);
	}
	
	

	@Override
	protected void batchMethod() throws TimestampException, PUserException,
			IOException {
		gzWH.add("A");
		gzWH.add("C");
		gzWH.add("D");
		gzWH.add("A-S");
		gzWH.add("A-W");
		

		szWH.add("SZ-A");
		szWH.add("SZ-B");
		szWH.add("CQ-A");

		cdWH.add("CD-A");
		cdWH.add("CD-B");
		cdWH.add("CD-GZ");
		cdWH.add("CD-D");
		cqWH.add("CQ-A");
		cqWH.add("CQ-B");
		cqWH.add("CQ-GZ");
		cqWH.add("CQ-D");
		exportOrder();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PurchaseOrderExport().startBatch(args);
	}

	private void exportOrder() throws TimestampException, PUserException,
			IOException {
		searchPurchaseOrder();
		writePOInfoToExcel();
	}

	private QueryPredicate getQP(QueryPredicate p, QueryHelper qh, String wh) {
		QueryPredicate temp = qh
				.attr(PurchaseOrder.WAREHOUSENUMBER).eq()
				.val(wh).predicate();
		return p = p == null ? temp : p.or(temp);
	}
	
	
	private void searchPurchaseOrder() throws TimestampException,
			PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p1 = qh.attr(PurchaseOrder.STATUS).eq()
				.val(PurchaseOrderStatus.SENT).predicate();
//		QueryPredicate p2 = qh.attr(PurchaseOrder.STATUS).ne()
//				.val(PurchaseOrderStatus.DELETED).predicate();
//		QueryPredicate p3 = qh.attr(PurchaseOrder.STATUS).ne()
//				.val(PurchaseOrderStatus.MERGED).predicate();
		QueryPredicate p = null;
		if (project.equals("GZ")) {
			for (String wh : gzWH) {
				p = getQP(p, qh, wh);
			}
		}
		if (project.equals("SZ")) {
			for (String wh : szWH) {
				p = getQP(p, qh, wh);
			}
		}
		if (project.equals("CQ")) {
			for (String wh : cqWH) {
				p = getQP(p, qh, wh);
			}
		}
		if (project.equals("CD")) {
			for (String wh : cdWH) {
				p = getQP(p, qh, wh);
			}
		}
		qh.setClass(PurchaseOrder.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(PurchaseOrder.ORDERNUMBER);
		Condition cond = qh.condition(p1.and(p));
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<PurchaseOrder> list = new ArrayList<PurchaseOrder>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			for (PurchaseOrder order : list) {
				
				POHeaderInformationBean pohib = new POHeaderInformationBean();
				pohib.setOrderNumber(order.getOrderNumber());
				pohib.setOrderDate(order.getOrderDate());
				pohib.setSupplierNumber(order.getSupplier().getAccountNumber());
				pohib.setCurrency(MoneyController.getCurrencyManager()
						.getCurrencyCode(order.getCurrencyIntNumber()));
				orderHeaderInfoList.add(pohib);
				List<PurchaseOrderLine> lineList = new ArrayList<PurchaseOrderLine>(
						order.getAllChildPurchaseOrderLine());
				for (PurchaseOrderLine purchaseOrderLine : lineList) {
					if (!purchaseOrderLine.getStatus().equals(
							PurchaseOrderLineStatus.CLOSED)
							&& !purchaseOrderLine.getStatus().equals(
									PurchaseOrderLineStatus.INVOICED)) {
						POItemInformationBean poiib = new POItemInformationBean();
						poiib.setOrderNumber(order.getOrderNumber());
						poiib.setLineNumber(purchaseOrderLine.getLineNumber());
						
						if (project.equals("CD")) {
							poiib.setPlant("BN00");
						} else if (project.equals("CQ")) {
							poiib.setPlant("BP00");
						} else if (project.equals("GZ")) {
							poiib.setPlant("BL00");
						} else if (project.equals("SZ")) {
							poiib.setPlant("BL10");
						}
						poiib.setDeliveryDate(order.getDeliveryDate());
						poiib.setPrice(purchaseOrderLine.getPriceStructure()
								.getPrice().getAmount());
						poiib.setPriceUnit(purchaseOrderLine.getPriceQuantity().getAmount());
						poiib.setProductNumber(purchaseOrderLine
								.getCompanyProductNumber());
						poiib.setQuantity(purchaseOrderLine
								.getUndeliveredQuantity().getQuantityUnit()
								.getAmount()
								+ purchaseOrderLine
										.getIncomingQuantity(
												purchaseOrderLine
														.getCompanyProductNumber())
										.getQuantityUnit().getAmount());
						poiib.setTextOnPO(purchaseOrderLine.getInternalRemark());
						poiib.setWs1ProductNumber(BIDateMapping.productMap
								.get(poiib.getProductNumber()));
						poiib.setSupplierProductNumber(purchaseOrderLine
								.getSupplierProductNumber());
						poItemInfoList.add(poiib);
					}
				}
			}
		}
		penum.destroy();
	}

	

	private void writePOInfoToExcel() throws PUserException {
		System.out.println("\n(!) Read excel file  start.\n");
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					dataSource));
			XSSFSheet sheet = workbook.getSheetAt(0);
			System.out.println("sheet1: " + sheet);
			int startRow = 2;
			for (int i = 0; i < orderHeaderInfoList.size(); i++) {
				POHeaderInformationBean pohib = orderHeaderInfoList.get(i);
				XSSFRow row = sheet.getRow(startRow);
				if (row == null) {
					row = sheet.createRow(startRow);
				}

				XSSFCell cellPONumber = row.getCell(0);
				if (cellPONumber == null) {
					cellPONumber = row.createCell(0);
				}
				XSSFCell cellDate = row.getCell(1);
				if (cellDate == null) {
					cellDate = row.createCell(1);
				}

				XSSFCell cellSupplierNumber = row.getCell(2);
				if (cellSupplierNumber == null) {
					cellSupplierNumber = row.createCell(2);
				}

				XSSFCell cellCurrency = row.getCell(3);
				if (cellCurrency == null) {
					cellCurrency = row.createCell(3);
				}

				cellPONumber.setCellValue(pohib.getOrderNumber());
				cellDate.setCellValue(BIDateMapping.dateFormat.format(pohib
						.getOrderDate()));
				cellSupplierNumber.setCellValue(pohib.getSupplierNumber());
				cellCurrency.setCellValue(pohib.getCurrency());
				startRow++;
				System.out.println("\n Number of PO writed in sheet 1: "
						+ (i + 1));
			}

			// ---------------sheet 2--------------------------------
			XSSFSheet sheet1 = workbook.getSheetAt(1);
			System.out.println("sheet2: " + sheet1);
			startRow = 2;
			for (int i = 0; i < poItemInfoList.size(); i++) {
				POItemInformationBean poiib = poItemInfoList
						.get(i);
				XSSFRow row = sheet1.getRow(startRow);
				if (row == null) {
					row = sheet1.createRow(startRow);
				}

				XSSFCell cellPONumber = row.getCell(0);
				if (cellPONumber == null) {
					cellPONumber = row.createCell(0);
				}
				XSSFCell cellItem = row.getCell(1);
				if (cellItem == null) {
					cellItem = row.createCell(1);
				}

				XSSFCell cellWS1ProductNumber = row.getCell(2);
				if (cellWS1ProductNumber == null) {
					cellWS1ProductNumber = row.createCell(2);
				}

				XSSFCell cellPlant = row.getCell(3);
				if (cellPlant == null) {
					cellPlant = row.createCell(3);
				}
				
				XSSFCell cellDeliveryDate = row.getCell(4);
				if (cellDeliveryDate == null) {
					cellDeliveryDate = row.createCell(4);
				}
				
				XSSFCell cellTextOnPO = row.getCell(5);
				if (cellTextOnPO == null) {
					cellTextOnPO = row.createCell(5);
				}
				
				XSSFCell cellQuantity = row.getCell(6);
				if (cellQuantity == null) {
					cellQuantity = row.createCell(6);
				}
				XSSFCell cellPrice = row.getCell(7);
				if (cellPrice == null) {
					cellPrice = row.createCell(7);
				}
				
				XSSFCell cellPriceUnit = row.getCell(8);
				if (cellPriceUnit == null) {
					cellPriceUnit = row.createCell(8);
				}
				
				XSSFCell cellProductNumber = row.getCell(9);
				if (cellProductNumber == null) {
					cellProductNumber = row.createCell(9);
				}
				
				XSSFCell cellSupplierProductNumber = row.getCell(16);
				if (cellSupplierProductNumber == null) {
					cellSupplierProductNumber = row.createCell(16);
				}
				cellSupplierProductNumber.setCellValue(poiib.getSupplierProductNumber());

				cellPONumber.setCellValue(poiib.getOrderNumber());

				cellItem.setCellValue(poiib.getLineNumber());

				cellWS1ProductNumber.setCellValue(poiib.getWs1ProductNumber());

				cellPlant.setCellValue(poiib.getPlant());

				cellDeliveryDate.setCellValue(BIDateMapping.dateFormat
						.format(poiib.getDeliveryDate()));

				cellTextOnPO.setCellValue(poiib.getTextOnPO());

				cellQuantity.setCellValue(poiib.getQuantity());

				cellPrice.setCellValue(poiib.getPrice());
				cellPriceUnit.setCellValue(poiib.getPriceUnit());
				cellProductNumber.setCellValue(poiib.getProductNumber());
				if (poiib.getWs1ProductNumber() == null
						|| poiib.getWs1ProductNumber().trim().equals("")) {
					XSSFCell cellProductMappingNotFound = row.getCell(10);
					if (cellProductMappingNotFound == null) {
						cellProductMappingNotFound = row.createCell(10);
					}
					cellProductMappingNotFound.setCellValue("x");
					Product product = _controller.lookupProduct(poiib
							.getProductNumber());
					poiib.setMap(product.getStandardCostPrice().getAmount());
					poiib.setSalesUnit(1);
					poiib.roundMAP();

					XSSFCell cellProductDescription = row.getCell(11);
					if (cellProductDescription == null) {
						cellProductDescription = row.createCell(11);
					}

					XSSFCell cellCreationDate = row.getCell(12);
					if (cellCreationDate == null) {
						cellCreationDate = row.createCell(12);
					}
					XSSFCell cellLastSupplier = row.getCell(13);
					if (cellLastSupplier == null) {
						cellLastSupplier = row.createCell(13);
					}
					
					XSSFCell cellSalesUnit = row.getCell(14);
					if (cellSalesUnit == null) {
						cellSalesUnit = row.createCell(14);
					}
					XSSFCell cellMap = row.getCell(15);
					if (cellMap == null) {
						cellMap = row.createCell(15);
					}
					
					cellProductDescription.setCellValue(product.getShortName());
					cellCreationDate.setCellValue(BIDateMapping.dateFormat
							.format(product.getProductCreationDate()));
					cellLastSupplier
							.setCellValue("" + getLastSupplier(product));
					cellSalesUnit.setCellValue(poiib.getSalesUnit());
					cellMap.setCellValue(poiib.getMap());

				}
				
				startRow++;
				System.out.println("\n Number of PO lines writed in sheet 2: "
						+ (i + 1));
			}
			String target = "../../var/exportSAP/OpenPO_"+project+".xlsx";
			FileOutputStream fos = new FileOutputStream(target);
			BufferedOutputStream bout = new BufferedOutputStream(fos);
			workbook.write(bout);
			bout.flush();
			bout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n(*) Read excel file  end.");

	}
	
	
	
	private String getLastSupplier(Product product) throws PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p1 = qh.attr(PurchaseOrderLine.COMPANYPRODUCTNUMBER)
				.eq().val(product.getProductNumber()).predicate();
		QueryPredicate p2 = qh
				.attr(PurchaseOrderLine.PARENT_PURCHASEORDER_REF
						+ PurchaseOrder.ORDERDATE).lte().val(new PDate())
				.predicate();
		qh.setClass(PurchaseOrderLine.class);
		qh.addDescendingOrdering(PurchaseOrderLine.CREATEDATE);
		qh.setDeepSelect(true);
		Condition cond = qh.condition(p1.and(p2));
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		if (penum.hasMoreElements()) {
			PurchaseOrderLine pol = (PurchaseOrderLine) penum.nextElement();
			penum.destroy();
			return pol.getParentPurchaseOrder().getSupplier().getName();
		}

		return "";
	}
	
}
