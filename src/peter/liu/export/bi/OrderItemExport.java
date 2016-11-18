package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.wuerth.phoenix.Phxbasic.enums.CustomerOrderStatus;
import com.wuerth.phoenix.Phxbasic.models.CustomerOrderLine;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerOrderLine;
import com.wuerth.phoenix.Phxbasic.models.OwnCompany;
import com.wuerth.phoenix.Phxbasic.models.PickingReservation;
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

	private int								NUMBER_OF_BATCH_TO_FECTCH	= 1000;

	LinkedList<OrderItemInformationBean>	orderItemInfoList			= new LinkedList<OrderItemInformationBean>();

	private String							targetTxt2012				= "../../var/exportSAP/MappingOrderItems-2012.txt";

	private String							targetTxt2013				= "../../var/exportSAP/MappingOrderItems-2013.txt";
	private String							targetTxt2014				= "../../var/exportSAP/MappingOrderItems-2014.txt";

	private PrintWriter						out1						= null;

	private String							csvSeperator				= ",";

	private int								numberOfOrderItems		= 0;

	private String							ownCompanyName;
	

	@Override
	protected void batchMethod() throws TimestampException, PUserException,
			IOException {
		ownCompanyName = _controller.getSingletonOwnCompany().getName();
		searchOrderItem(2012, targetTxt2012);
		searchOrderItem(2013, targetTxt2013);
		searchOrderItem(2014, targetTxt2014);
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new OrderItemExport().startBatch(args);
	}

	private void searchOrderItem(int year, String targetTxt)
			throws TimestampException, PUserException, IOException {
		FileWriter outFile = new FileWriter(targetTxt);
		out1 = new PrintWriter(outFile);

		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(DWCustomerOrderLine.class);
		QueryPredicate p1 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE)
				.gte().val(new PDate(year, 0, 1)).predicate();
		QueryPredicate p2 = qh.attr(DWCustomerOrderLine.CUSTOMERORDERDATE)
				.lte().val(new PDate(year, 11, 31)).predicate();
		QueryPredicate p3 = qh.attr(DWCustomerOrderLine.ORDERSTATUS).ne()
				.val(CustomerOrderStatus.DELETED).predicate();

		qh.setDeepSelect(true);
		qh.addAscendingOrdering(DWCustomerOrderLine.CUSTOMERORDERNUMBER);
		qh.addAscendingOrdering(DWCustomerOrderLine.LINENUMBER);
		
		Condition cond = null;
		if(year!=2014){
			cond=qh.condition(p1.and(p2).and(p3));
		}else{
			cond=qh.condition(p1.and(p3));
		}
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<DWCustomerOrderLine> list = new ArrayList<DWCustomerOrderLine>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			orderItemInfoList = new LinkedList<OrderItemInformationBean>();
			System.out.println("\n(!) Write txt file  start.\n");
			for (DWCustomerOrderLine orderLine : list) {
				CustomerOrderLine col = _controller.lookupCustomerOrder(
						orderLine.getCustomerOrderNumber())
						.lookupCustomerOrderLine(orderLine.getLineNumber());

				// That means the customer order line was deleted but dwcustomerorderline not synchronized.
				if (col == null) {
					continue;
				}

				OrderItemInformationBean oiib = new OrderItemInformationBean();
				oiib.setDocumentType("ZTA");
				oiib.setPrice(orderLine.getPrice().getAmount()
						/ orderLine.getPrice().getUnit());

				oiib.setNetValue(orderLine.getCcNetAmountCustomerOrderLine()
						.getAmount());
				oiib.setGrossValue(oiib.getNetValue() * 1.17);

				oiib.setCustomerNumber(orderLine.getDWCustomerOrder()
						.getDWCustomer().getAccountNumber());
				
				oiib.setOrderCreditNoteSign('A');
				String productNumber=orderLine.getDWProduct().getProductNumber();
				String eeeeProductNumber=BIDateMapping.productMap.get(productNumber);
				if(eeeeProductNumber!=null&&!eeeeProductNumber.trim().equals("")){
					oiib.setArticleNumber(eeeeProductNumber);
				}else{
					oiib.setArticleNumber(BIDateMapping.dummyMaterialNumber);
				}
				
				oiib.setRegisterNumber(orderLine.getDWCustomerOrder()
						.getDWSalesman().getRegisterNumber());
				Product product = _controller.lookupProduct(orderLine
						.getDWProduct().getProductNumber());
				oiib.setPriceUnit(orderLine.getPrice().getUnit());

				oiib.setOrderItem(orderLine.getLineNumber());
				oiib.setOrderNumber(orderLine.getCustomerOrderNumber());
				oiib.setOrderDate(orderLine.getCustomerOrderDate());

				oiib.setOrderQuantity(orderLine.getOrderQuantity().getAmount());
				OwnCompany oc = _controller.getSingletonOwnCompany();
				Weight weight = WeightController.getNewWeight(0, oc
						.getDefaultWeightPerUnit().getWeightMeasureUnit());
				weight = weight
						.add(product
								.getOwnCompanyProductSalesUnit()
								.getWeight()
								.getNormalizedAmount()
								.multiply(
										orderLine.getDeliveredQuantity()
												.getAmount()));
				oiib.setWeight(weight.getAmount());
				// oiib.setWeightUnit(product.getOwnCompanyProductSalesUnit()
				// .getWeight().getWeightMeasureUnit()
				// .getDescription());
				if (col.getWarehouseOrderLineMain() != null
						&& col.getWarehouseOrderLineMain()
								.getAllPickingReservation() != null
						&& col.getWarehouseOrderLineMain()
								.getAllPickingReservation().size() > 0) {
					PickingReservation pr = (PickingReservation) col
							.getWarehouseOrderLineMain()
							.getAllPickingReservation().get(0);
					oiib.setStorageLocation(pr.getFullLocationName());
				} else {
					oiib.setStorageLocation(" ");
				}

				double glep = orderLine.getCostPrice().getAmount();
				// oiib.setGlep(glep);
				// oiib.setPfep(glep * 1.085);
				oiib.setCogsglep(glep * oiib.getOrderQuantity());
				oiib.setCogspfep(oiib.getCogsglep() * 1.085);
				//TODO How to calculate discount
				//oiib.setDiscount(orderLine.getDiscount());
				if(oiib.getPriceUnit()==10000){
					oiib.setPriceUnit(1000);
					oiib.setPrice(oiib.getPrice()/10);
				}
				fillWS1Information(oiib);
				orderItemInfoList.addLast(oiib);
				System.out.println("\n Number of order items loaded: "
						+ orderItemInfoList.size());
			}
			writeOrderItemInfoToTxt();
			_context.commit();
		}
		penum.destroy();
		out1.close();
		System.out.println("\n(*) Write txt file  end.");

	}
	
	private void fillWS1Information(OrderItemInformationBean oiib) {
		String customerOrg = BIDateMapping.customerOrgMap.get(oiib
				.getCustomerNumber());
		String defaultWS1RegisterNumber=null;
		if (customerOrg != null) {
			String[] customerOrgArray = customerOrg.split(",");
			oiib.setWs1CustomerNumber(customerOrgArray[0]);
			oiib.setWs1SalesOrganisation(customerOrgArray[1]);
			defaultWS1RegisterNumber=customerOrgArray[2];
		} else {
			if (ownCompanyName.equals("伍尔特（重庆）五金工具有限公司")) {
				oiib.setWs1CustomerNumber("0000999921");
				oiib.setWs1SalesOrganisation("8807");
			} else {
				oiib.setWs1CustomerNumber("0000999921");
				oiib.setWs1SalesOrganisation("8805");
			}
			System.out.println("\n Customer mapping can not find : "
					+ oiib.getCustomerNumber());
		}

		oiib.setPlant(BIDateMapping.getPlantBasedOnWarehouse(oiib
				.getWs1SalesOrganisation()));

		String ws1RegisterNumber = BIDateMapping.getWS1RegisterNumber(
				ownCompanyName, oiib.getRegisterNumber());
		if (BIDateMapping.isWS1RUDUmmyRU(ws1RegisterNumber)
				&& defaultWS1RegisterNumber != null) {
			ws1RegisterNumber = defaultWS1RegisterNumber;
		}
		if (oiib.getWs1CustomerNumber().equals("0000999921")) {
			ws1RegisterNumber = BIDateMapping.getDummyWS1RegisterNumber();
		}
		oiib.setWs1RegisterNumber(ws1RegisterNumber);
	}
	
//	CSALESORG	Sales Organization
//	CTERREP	Sales Rep WS1
//	CBOD	Branch Office Did the Deal WS1
//	CPLT	Delivery Plant WS1
//	CCUST	Customer Number (Sold-to-Party) WS1
//	CCBILLTO	Customer Number (Bill-to-Party) WS1
//	CCSHIPTO	Customer Number (Ship-to-Party) WS1
//	CCPAYER	Customer Number (Payer) WS1
//	DORDENTRY	Order Document Entry Date
//	CORDNO	Order Document
//	CORDREAS	Order Reason
//	CORDCATS	Order Category (Statistic)
//	CDOCTYPE	Sales Document Type
//	CDOCCAT	Sales Document Category
//	CMAT	Article Number WS1
//	CORDITM	Order Document Item
//	QOXQUOR	Order Quantity
//	NOXORIT	Number of Order Items
//	CPRKEY	Price Key
//	CORDCRED	Order/Credit Note
//	CREACOMP	Complaint Reason
//	LOITO	Order Value
//	LOGTO	Gross Value
//	LONTO	Net Value
//	LONDC	Discount
//	LON_PS	Price Increase Surcharge
//	LOXPRB	Basis Price
//	LON_FR	Freight Costs
//	LONPP	Cost Value PFEP
//	LONMP	Cost Value GLD
//	LOX_TAX	Tax Amount
//	QOWTNTKG	Gross Weight in Kilogramms
	
	private void writeOrderItemInfoToTxt() {
		for (int i = 0; i < orderItemInfoList.size(); i++) {
			OrderItemInformationBean oiib = orderItemInfoList.get(i);
			StringBuffer sb = new StringBuffer();
			sb.append(oiib.getWs1SalesOrganisation()).append(csvSeperator);
			sb.append(oiib.getWs1RegisterNumber()).append(csvSeperator);
			sb.append(oiib.getPlant()).append(csvSeperator);
			//TODO CPLT	Delivery Plant WS1
			sb.append(oiib.getWs1CustomerNumber()).append(csvSeperator);
			
			// TODO CCBILLTO Customer Number (Bill-to-Party) WS1
			// CCSHIPTO Customer Number (Ship-to-Party) WS1
			// CCPAYER Customer Number (Payer) WS1
			if (oiib.getOrderDate() != null) {
				sb.append(BIDateMapping.dateFormat.format(oiib.getOrderDate()))
						.append(csvSeperator);
			} else {
				sb.append(" ").append(csvSeperator);
			}
			sb.append(
					BIDateMapping.fillWS1OrderOrInvoiceNumber(oiib
							.getOrderNumber())).append(csvSeperator);
			// TODO CORDREAS Order Reason
			// CORDCATS Order Category (Statistic)
			// CDOCTYPE Sales Document Type
			// CDOCCAT Sales Document Category
			sb.append(oiib.getArticleNumber()).append(csvSeperator);
			sb.append(oiib.getOrderItem()).append(csvSeperator);
			sb.append(oiib.getOrderQuantity()).append(csvSeperator);
			//NOXORIT	Number of Order Items
			sb.append("1").append(csvSeperator);
			sb.append(BIDateMapping.getPriceUnitMapping(oiib.getPriceUnit())).append(csvSeperator);
			sb.append(oiib.getOrderCreditNoteSign()).append(csvSeperator);
			//TODO CREACOMP	Complaint Reason
			sb.append(" ").append(csvSeperator);
			//TODO LOITO	Order Value
			sb.append(DoubleUtils.getRoundedAmount(oiib.getOrderValue())).append(csvSeperator);
			//TODO LOGTO	Gross Value oiib.setGrossValue(oiib.getNetValue() * 1.17);
			sb.append(DoubleUtils.getRoundedAmount(oiib.getGrossValue())).append(csvSeperator);
			sb.append(DoubleUtils.getRoundedAmount(oiib.getNetValue())).append(csvSeperator);
			//TODO LONDC	Discount
			sb.append(DoubleUtils.getRoundedAmount(oiib.getDiscount())).append(csvSeperator);
			sb.append(DoubleUtils.getRoundedAmount(oiib.getGrossValue()-oiib.getNetValue())).append(csvSeperator);
			sb.append(DoubleUtils.getRoundedAmount(oiib.getPrice())).append(csvSeperator);
			//TODO LON_FR	Freight Costs
			sb.append(oiib.getCogspfep());
			sb.append(oiib.getCogsglep()).append(csvSeperator);
			sb.append(FormatHelper.getWeightFormat().format(oiib.getWeight())).append(csvSeperator);
			//TODO LOX_TAX	Tax Amount
			sb.append(oiib.getDocumentType()).append(csvSeperator);
			out1.println(sb.toString());
			numberOfOrderItems++;
			System.out.println("\n Number of Order Items writed in txt 1: "
					+ numberOfOrderItems);
		}
	}
	
	
//	private void writeOrderItemInfoToTxt() {
//		for (int i = 0; i < orderItemInfoList.size(); i++) {
//			OrderItemInformationBean oiib = orderItemInfoList.get(i);
//			StringBuffer sb = new StringBuffer();
//			sb.append(oiib.getWs1SalesOrganisation()).append(csvSeperator);
//			sb.append(oiib.getOrderItem()).append(csvSeperator);
//			sb.append(oiib.getDocumentType()).append(csvSeperator);
//			sb.append(" ").append(csvSeperator);
//			sb.append(
//					BIDateMapping.fillWS1OrderOrInvoiceNumber(oiib
//							.getOrderNumber())).append(csvSeperator);
//			
//			if (oiib.getOrderDate() != null) {
//				sb.append(BIDateMapping.dateFormat.format(oiib.getOrderDate()))
//						.append(csvSeperator);
//			} else {
//				sb.append(" ").append(csvSeperator);
//			}
//			sb.append(oiib.getOrderQuantity()).append(csvSeperator);
//			sb.append(DoubleUtils.getRoundedAmount(oiib.getPrice(),10)).append(csvSeperator);
//			sb.append(df.format(oiib.getWeight())).append(csvSeperator);
//
//			sb.append(DoubleUtils.getRoundedAmount(oiib.getGrossValue())).append(csvSeperator);
//			sb.append(DoubleUtils.getRoundedAmount(oiib.getNetValue())).append(csvSeperator);
//			sb.append(oiib.getWs1CustomerNumber()).append(csvSeperator);
//			sb.append(oiib.getArticleNumber()).append(csvSeperator);
//			sb.append(oiib.getWs1RegisterNumber()).append(csvSeperator);
//			sb.append(BIDateMapping.getPriceUnitMapping(oiib.getPriceUnit())).append(csvSeperator);
//			sb.append(
//					BIDateMapping.fillWS1OrderOrInvoiceNumber(oiib
//							.getOrderNumber())).append(csvSeperator);
//			sb.append(" ").append(csvSeperator);
//			sb.append(oiib.getPlant()).append(csvSeperator);
//			sb.append(oiib.getCogsglep()).append(csvSeperator);
//			sb.append(oiib.getCogspfep());
//			out1.println(sb.toString());
//			numberOfInvoiceItems++;
//			System.out.println("\n Number of Order Items writed in txt 1: "
//					+ numberOfInvoiceItems);
//		}
//	}
}
