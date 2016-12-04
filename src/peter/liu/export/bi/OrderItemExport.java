package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.wuerth.phoenix.Phxbasic.enums.CustomerOrderStatus;
import com.wuerth.phoenix.Phxbasic.models.CustomerOrderLine;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerOrderLine;
import com.wuerth.phoenix.Phxbasic.models.OwnCompany;
import com.wuerth.phoenix.Phxbasic.models.Product;
import com.wuerth.phoenix.basic.etnax.common.utilsdir.DoubleUtils;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;
import com.wuerth.phoenix.util.PDate;
import com.wuerth.phoenix.util.weight.Weight;
import com.wuerth.phoenix.util.weight.WeightController;

/**
 * OrderItemExport
 * 
 * @author pcnsh197
 * 
 */
public class OrderItemExport extends BatchRunner {

	private int NUMBER_OF_BATCH_TO_FECTCH = 1000;

	LinkedList<OrderItemInformationBean> orderItemInfoList = new LinkedList<OrderItemInformationBean>();

	private String targetTxt2015 = "../../var/exportSAP/MappingOrderItems-2015.csv";
	private String targetTxt2016 = "../../var/exportSAP/MappingOrderItems-2016.csv";
	private String targetTxt2017 = "../../var/exportSAP/MappingOrderItems-2017.csv";
	private boolean isTest = false;
	private boolean isAll = false;
	private boolean isFirstRound = false;
	private boolean isFinalRound = false;
	private int year = 2015;
	private PrintWriter out1 = null;
	private int numberOfOrderItems = 0;
	private OwnCompany oc;

	@Override
	protected void batchMethod() throws TimestampException, PUserException, IOException {
		oc = _controller.getSingletonOwnCompany();
		BIDateMapping.readProductMappingFromExcelForBI();
		if (isTest) {
			searchOrderItem(year, targetTxt2015);
		} else if (isFirstRound) {
			searchOrderItem(2015, targetTxt2015);
		} else if (isFinalRound) {
			searchOrderItem(2017, targetTxt2017);
		} else if (isAll) {
			searchOrderItem(2015, targetTxt2015);
			searchOrderItem(2016, targetTxt2016);
			searchOrderItem(2017, targetTxt2017);
		} else {
			searchOrderItem(year, targetTxt2015);
		}
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new OrderItemExport().startBatch(args);
	}

	protected void processargs(String[] args) {
		for (String argstr : args) {
			if (argstr.equalsIgnoreCase("commit")) {
				willcommit = true;
			} else if (argstr.equalsIgnoreCase("test")) {
				isTest = true;
				targetTxt2015 = "../../var/exportSAP/MappingOrderItems-test-2015.csv";
			} else if (argstr.equalsIgnoreCase("all")) {
				isAll = true;
			} else if (argstr.equalsIgnoreCase("first")) {
				isFirstRound = true;
				targetTxt2015 = "../../var/exportSAP/MappingOrderItems-201512.csv";
			} else if (argstr.equalsIgnoreCase("final")) {
				isFinalRound = true;
				targetTxt2017 = "../../var/exportSAP/MappingOrderItems-201702.csv";
				year = 2017;
			} else if (argstr.startsWith("201")) {
				if (argstr == "2015") {
					year = 2015;
				} else if (argstr == "2016") {
					year = 2016;
				} else if (argstr == "2017") {
					year = 2017;
				}
			} else {
				throw new RuntimeException("Arguments is not supported by the tool!");
			}
		}
	}

	protected void usage() {
		System.out.println("-args test \n\ttest : Only output order lines in date 20151201-20151204");
		System.out.println("-args first \n\tfirst : Only output order lines in date 20151201-20151231");
		System.out.println("-args final \n\tfinal : Only output order lines in date 20170201-20170228");
		System.out.println(
				"-args all \n\tall : Output order lines between year 2015 and 2016, each year has a output file");
		System.out.println("-args 2015, 2016 or 2017 \n\tall : Output order lines in 2015 or 2016 or 2017");
		System.out.println("output file are in path ../../var/exportSAP/ start with MappingOrderItems-");
	}

	private void searchOrderItem(int year, String targetTxt) throws TimestampException, PUserException, IOException {
		FileWriter outFile = new FileWriter(targetTxt);
		out1 = new PrintWriter(outFile);
		writeInvoiceItemHeaderToCSV(out1);
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(DWCustomerOrderLine.class);
		QueryPredicate p1 = null;
		QueryPredicate p2 = null;
		if (isTest) {
			p1 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE).gte().val(new PDate(year, 11, 1)).predicate();
			p2 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE).lte().val(new PDate(year, 11, 4)).predicate();
		} else if (isFirstRound) {
			p1 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE).gte().val(new PDate(year, 11, 1)).predicate();
			p2 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE).lte().val(new PDate(year, 11, 31)).predicate();
		} else if (isFinalRound) {
			p1 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE).gte().val(new PDate(year, 1, 1)).predicate();
			p2 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE).lte().val(new PDate(year, 1, 28)).predicate();
		} else if (year == 2017 && !isFinalRound) {
			p1 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE).gte().val(new PDate(year, 0, 1)).predicate();
			p2 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE).lte().val(new PDate(year, 0, 31)).predicate();
		} else {
			p1 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE).gte().val(new PDate(year, 0, 1)).predicate();
			p2 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE).lte().val(new PDate(year, 11, 31)).predicate();
		}

		QueryPredicate p3 = qh.attr(DWCustomerOrderLine.ORDERSTATUS).ne().val(CustomerOrderStatus.DELETED).predicate();

		qh.setDeepSelect(true);
		qh.addAscendingOrdering(DWCustomerOrderLine.CUSTOMERORDERNUMBER);
		qh.addAscendingOrdering(DWCustomerOrderLine.LINENUMBER);

		Condition cond = qh.condition(p1.and(p2).and(p3));
		System.out.println("QueryString " + Query.getQueryString(cond));
		PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<DWCustomerOrderLine> list = new ArrayList<DWCustomerOrderLine>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			orderItemInfoList = new LinkedList<OrderItemInformationBean>();
			System.out.println("\n(!) Write txt file  start.\n");
			for (DWCustomerOrderLine orderLine : list) {
				CustomerOrderLine col = _controller.lookupCustomerOrder(orderLine.getCustomerOrderNumber())
						.lookupCustomerOrderLine(orderLine.getLineNumber());

				// That means the customer order line was deleted but
				// dwcustomerorderline not synchronized.
				if (col == null) {
					continue;
				}

				OrderItemInformationBean oiib = new OrderItemInformationBean();
				oiib.setDocumentType("ZTA");
				oiib.setPrice(orderLine.getPrice().getAmount() / orderLine.getPrice().getUnit());
				oiib.setCustomerNumber(orderLine.getDWCustomerOrder().getDWCustomer().getAccountNumber());
				oiib.setOrderCreditNoteSign('A');
				oiib.setName1(orderLine.getDWCustomerOrder().getDWCustomer().getName1());
				oiib.setGoodsRecipient(col.getParentCustomerOrder().getGoodsRecipient().getId());
				oiib.setDebtor(col.getParentCustomerOrder().getDebitor().getId());
				oiib.setGoodsRecipientName(col.getParentCustomerOrder().getGoodsRecipient().getName());
				oiib.setDebtorName(col.getParentCustomerOrder().getDebitor().getName());
				oiib.setWarehouseNumber(col.getParentCustomerOrder().getWarehouseNumber());
				double orderValue = orderLine.getPrice().getMoney().divide((double) orderLine.getPrice().getUnit())
						.multiply(orderLine.getOrderQuantity().getAmount()).getAmount();
				oiib.setOrderValue(orderValue);
				oiib.setDiscount(orderValue* (orderLine.getDiscount()+orderLine.getPromotionDiscount()) / 100);
				oiib.setNetValue(orderLine.getCcNetAmountCustomerOrderLine().getAmount());
				oiib.setGrossValue(oiib.getNetValue());
				if (orderLine.getLineNumber() == 1) {
					oiib.setFreightCost(orderLine.getDWCustomerOrder().getCcFreightCost().getAmount());
				}
				oiib.setTaxAmount(oiib.getNetValue()*0.15);
				String productNumber = orderLine.getDWProduct().getProductNumber();
				String eeeeProductNumber = BIDateMapping.productMap.get(productNumber);
				if (eeeeProductNumber != null && !eeeeProductNumber.trim().equals("")) {
					oiib.setArticleNumber(eeeeProductNumber);
				} else {
					oiib.setArticleNumber(BIDateMapping.dummyMaterialNumber);
				}

				oiib.setRegisterNumber(orderLine.getDWCustomerOrder().getDWSalesman().getRegisterNumber());
				oiib.setPriceUnit(orderLine.getPrice().getUnit());

				oiib.setOrderItem(orderLine.getLineNumber());
				oiib.setOrderNumber(orderLine.getCustomerOrderNumber());
				oiib.setOrderDate(orderLine.getCustomerOrderDate());

				oiib.setOrderQuantity((int)orderLine.getOrderQuantity().getAmount());
				Product product = _controller.lookupProduct(orderLine.getDWProduct().getProductNumber());
				Weight weight = WeightController.getNewWeight(0, oc.getDefaultWeightPerUnit().getWeightMeasureUnit());
				weight = weight.add(product.getOwnCompanyProductSalesUnit().getWeight().getNormalizedAmount()
						.multiply(orderLine.getDeliveredQuantity().getAmount()));
				oiib.setWeight(weight.getAmount());
//				if (col.getWarehouseOrderLineMain() != null
//						&& col.getWarehouseOrderLineMain().getAllPickingReservation() != null
//						&& col.getWarehouseOrderLineMain().getAllPickingReservation().size() > 0) {
//					PickingReservation pr = (PickingReservation) col.getWarehouseOrderLineMain()
//							.getAllPickingReservation().get(0);
//					oiib.setStorageLocation(pr.getFullLocationName());
//				} else {
//					oiib.setStorageLocation(" ");
//				}

				double glep = orderLine.getCostPrice().getAmount();
				oiib.setCogsglep(glep * oiib.getOrderQuantity());
				oiib.setCogspfep(oiib.getCogsglep() / 1.1);
				if (oiib.getPriceUnit() == 10000) {
					oiib.setPriceUnit(1000);
					oiib.setPrice(oiib.getPrice() / 10);
				}
				fillWS1Information(oiib);
				orderItemInfoList.addLast(oiib);
			}
			System.out.println("\n Number of order items loaded: " + orderItemInfoList.size());
			writeOrderItemInfoToTxt();
			_context.commit();
		}
		penum.destroy();
		out1.close();
		System.out.println("\n(*) Write txt file  end.");

	}

	private void fillWS1Information(OrderItemInformationBean oiib) {
		oiib.setWs1SalesOrganisation("3120");
		oiib.setWs1CustomerNumber(BIDateMapping.getWS1CustomerNumber(oiib.getCustomerNumber(), oiib.getName1()));
		oiib.setPayer(BIDateMapping.getWS1CustomerNumber(oiib.getDebtor(),oiib.getDebtorName()));
		String shipToCustomer=BIDateMapping.getWS1CustomerNumber(oiib.getGoodsRecipient(),oiib.getGoodsRecipientName());
		if(shipToCustomer.length()<6){
			shipToCustomer=oiib.getWs1CustomerNumber();
		}
		oiib.setShipToCustomer(shipToCustomer);
		oiib.setWs1RegisterNumber("0000" + oiib.getRegisterNumber());
		oiib.setPlant(BIDateMapping.getPlantBasedOnWarehouse(oiib.getWarehouseNumber()));
		oiib.setDeliveryPlant(BIDateMapping.getDeliveryPlantBasedOnWarehouse(oiib.getWarehouseNumber()));
		oiib.setOrderReason("001");
		oiib.setOrderCategory("1");
		oiib.setSalesDocumentType("");
		oiib.setDocumentCategory("C");
	}

	// CSALESORG Sales Organization
	// CTERREP Sales Rep WS1
	// CBOD Branch Office Did the Deal WS1
	// CPLT Delivery Plant WS1
	// CCUST Customer Number (Sold-to-Party) WS1
	// CCBILLTO Customer Number (Bill-to-Party) WS1
	// CCSHIPTO Customer Number (Ship-to-Party) WS1
	// CCPAYER Customer Number (Payer) WS1
	// DORDENTRY Order Document Entry Date
	// CORDNO Order Document
	// CORDREAS Order Reason
	// CORDCATS Order Category (Statistic)
	// CDOCTYPE Sales Document Type
	// CDOCCAT Sales Document Category
	// CMAT Article Number WS1
	// CORDITM Order Document Item
	// QOXQUOR Order Quantity
	// NOXORIT Number of Order Items
	// CPRKEY Price Key
	// CORDCRED Order/Credit Note
	// CREACOMP Complaint Reason
	// LOITO Order Value
	// LOGTO Gross Value
	// LONTO Net Value
	// LONDC Discount
	// LON_PS Price Increase Surcharge
	// LOXPRB Basis Price
	// LON_FR Freight Costs
	// LONPP Cost Value PFEP
	// LONMP Cost Value GLD
	// LOX_TAX Tax Amount
	// QOWTNTKG Gross Weight in Kilogramms

	private void writeOrderItemInfoToTxt() {
		for (int i = 0; i < orderItemInfoList.size(); i++) {
			OrderItemInformationBean oiib = orderItemInfoList.get(i);
			StringBuffer sb = new StringBuffer();
			sb.append(oiib.getWs1SalesOrganisation()).append(BIDateMapping.csvSeperator);
			sb.append(oiib.getWs1RegisterNumber()).append(BIDateMapping.csvSeperator);
			sb.append(oiib.getPlant()).append(BIDateMapping.csvSeperator);
			sb.append(oiib.getDeliveryPlant()).append(BIDateMapping.csvSeperator);
			sb.append(oiib.getWs1CustomerNumber()).append(BIDateMapping.csvSeperator);
			// CCBILLTO Customer Number (Bill-to-Party) WS1
			sb.append(oiib.getWs1CustomerNumber()).append(BIDateMapping.csvSeperator);
			// Customer Number (Ship-to-Party) WS1
			sb.append(oiib.getShipToCustomer()).append(BIDateMapping.csvSeperator);
			// Customer Number (Payer) WS1
			sb.append(oiib.getPayer()).append(BIDateMapping.csvSeperator);
			if (oiib.getOrderDate() != null) {
				sb.append(BIDateMapping.dateFormat.format(oiib.getOrderDate())).append(BIDateMapping.csvSeperator);
			} else {
				sb.append(" ").append(BIDateMapping.csvSeperator);
			}
			sb.append(oiib.getOrderNumber()).append(BIDateMapping.csvSeperator);
			// TODO CORDREAS Order Reason
			sb.append(oiib.getOrderReason()).append(BIDateMapping.csvSeperator);
			// TODO Order Category (Statistic)
			sb.append(oiib.getOrderCategory()).append(BIDateMapping.csvSeperator);
			// TODO Sales Document Type
			sb.append(oiib.getSalesDocumentType()).append(BIDateMapping.csvSeperator);
			// TODO Sales Document Category
			sb.append(oiib.getDocumentCategory()).append(BIDateMapping.csvSeperator);

			sb.append(oiib.getArticleNumber()).append(BIDateMapping.csvSeperator);
			sb.append(oiib.getOrderItem()).append(BIDateMapping.csvSeperator);
			sb.append(oiib.getOrderQuantity()).append(BIDateMapping.csvSeperator);
			// NOXORIT Number of Order Items
			sb.append("1").append(BIDateMapping.csvSeperator);
			sb.append(BIDateMapping.getPriceUnitMapping(oiib.getPriceUnit())).append(BIDateMapping.csvSeperator);
			sb.append(oiib.getOrderCreditNoteSign()).append(BIDateMapping.csvSeperator);
			// CREACOMP Complaint Reason
			sb.append(" ").append(BIDateMapping.csvSeperator);
			sb.append(BIDateMapping.convertDotToComma(FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(oiib.getOrderValue())))).append(BIDateMapping.csvSeperator);
			//LOGTO Gross Value
			sb.append(BIDateMapping.convertDotToComma(FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(oiib.getGrossValue())))).append(BIDateMapping.csvSeperator);
			sb.append(BIDateMapping.convertDotToComma(FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(oiib.getNetValue())))).append(BIDateMapping.csvSeperator);
			sb.append(BIDateMapping.convertDotToComma(FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(oiib.getDiscount())))).append(BIDateMapping.csvSeperator);
			sb.append("0,00").append(BIDateMapping.csvSeperator);
			sb.append(BIDateMapping.convertDotToComma(FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(oiib.getPrice())))).append(BIDateMapping.csvSeperator);
			// LON_FR Freight Costs
			sb.append(BIDateMapping.convertDotToComma(FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(oiib.getFreightCost())))).append(BIDateMapping.csvSeperator);
			sb.append(BIDateMapping.convertDotToComma(FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(oiib.getCogspfep())))).append(BIDateMapping.csvSeperator);
			sb.append(BIDateMapping.convertDotToComma(FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(oiib.getCogsglep())))).append(BIDateMapping.csvSeperator);
			// LOX_TAX Tax Amount
			sb.append(BIDateMapping.convertDotToComma(FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(oiib.getTaxAmount())))).append(BIDateMapping.csvSeperator);
			sb.append(BIDateMapping.convertDotToComma(FormatHelper.getWeightFormat().format(oiib.getWeight()))).append(BIDateMapping.csvSeperator);
			out1.println(sb.toString());
			numberOfOrderItems++;
		}
		System.out.println("\n Number of Order Items writed in txt 1: " + numberOfOrderItems);
	}

	private void writeInvoiceItemHeaderToCSV(PrintWriter out1) {
		StringBuffer sb = new StringBuffer();
		String[] header1 = { "CSALESORG", "CTERREP", "CBOD", "CPLT", "CCUST", "CCBILLTO", "CCSHIPTO", "CCPAYER",
				"DORDENTRY", "CORDNO", "CORDREAS", "CORDCATS", "CDOCTYPE", "CDOCCAT", "CMAT", "CORDITM", "QOXQUOR",
				"NOXORIT", "CPRKEY", "CORDCRED", "CREACOMP", "LOITO", "LOGTO", "LONTO", "LONDC", "LON_PS", "LOXPRB",
				"LON_FR", "LONPP", "LONMP", "LOX_TAX", "QOWTNTKG " };
		for (int i = 0; i < header1.length; i++) {
			sb.append(header1[i]).append(BIDateMapping.csvSeperator);
		}
		out1.println(sb.toString());

		sb = new StringBuffer();
		String[] header2 = { "Sales Organization", "Sales Rep WS1", "Branch Office Did the Deal WS1",
				"Delivery Plant WS1", "Customer Number (Sold-to-Party) WS1", "Customer Number (Bill-to-Party) WS1",
				"Customer Number (Ship-to-Party) WS1", "Customer Number (Payer) WS1", "Order Document Entry Date",
				"Order Document", "Order Reason", "Order Category (Statistic)", "Sales Document Type",
				"Sales Document Category", "Article Number WS1", "Order Document Item", "Order Quantity",
				"Number of Order Items", "Price Key", "Order/Credit Note", "Complaint Reason", "Order Value",
				"Gross Value", "Net Value", "Discount", "Price Increase Surcharge", "Basis Price", "Freight Costs",
				"Cost Value PFEP", "Cost Value GLD", "Tax Amount", "Gross Weight in Kilogramms" };
		for (int i = 0; i < header2.length; i++) {
			sb.append(header2[i]).append(BIDateMapping.csvSeperator);
		}
		out1.println(sb.toString());
		out1.flush();
	}
}
