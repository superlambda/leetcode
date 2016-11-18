package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.wuerth.phoenix.Phxbasic.enums.ComplaintLineType;
import com.wuerth.phoenix.Phxbasic.enums.DocType;
import com.wuerth.phoenix.Phxbasic.models.CustomerAccount;
import com.wuerth.phoenix.Phxbasic.models.CustomerInvoiceLine;
import com.wuerth.phoenix.Phxbasic.models.CustomerOrderLine;
import com.wuerth.phoenix.Phxbasic.models.DWCreditNoteLine;
import com.wuerth.phoenix.Phxbasic.models.DWPriceLine;
import com.wuerth.phoenix.Phxbasic.models.DWRetoureLine;
import com.wuerth.phoenix.Phxbasic.models.DebitNoteNumber;
import com.wuerth.phoenix.Phxbasic.models.DeliveryLine;
import com.wuerth.phoenix.Phxbasic.models.InvoiceNumber;
import com.wuerth.phoenix.Phxbasic.models.OwnCompany;
import com.wuerth.phoenix.Phxbasic.models.Product;
import com.wuerth.phoenix.Phxbasic.models.SalesArea;
import com.wuerth.phoenix.Phxbasic.models.Salesman;
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
 * 
 * @author pcnsh197
 * 
 */
public class CreditNoteItemExport extends BatchRunner {

	private int								NUMBER_OF_BATCH_TO_FECTCH	= 1000;
	LinkedList<InvoiceItemInformationBean>	invoiceItemInfoList			= new LinkedList<InvoiceItemInformationBean>();
	
	private String							targetTxt2015				= "../../var/exportSAP/MappingCreditNoteItems-2015.txt";
	private String							targetTxt2016				= "../../var/exportSAP/MappingCreditNoteItems-2016.txt";
	private String							targetTxt2017				= "../../var/exportSAP/MappingCreditNoteItems-2017.txt";
	private PrintWriter						out1						= null;
	private String							csvSeperator				= ",";
	private int								numberOfInvoiceItems		= 0;
	private String							ownCompanyName;
	private int 							year						= 2015;
	private boolean 						isAll 						= false;
	private boolean 						isFirstRound				= false;
	private boolean 						isFinalRound				= false;
	private boolean 						isTest 						= false;
	
	@Override
	protected void batchMethod() throws TimestampException, PUserException,
			IOException {
		
		ownCompanyName = _controller.getSingletonOwnCompany().getName();
		if(isTest){
			searchCreditNoteItem(year,targetTxt2015);
		}else if(isFirstRound){
			searchCreditNoteItem(2015, targetTxt2015);
		}else if(isFinalRound){
			searchCreditNoteItem(2017, targetTxt2017);
		}else if (isAll){
			searchCreditNoteItem(2015, targetTxt2015);
			searchCreditNoteItem(2016, targetTxt2016);
			searchCreditNoteItem(2017, targetTxt2017);
		}else{
			searchCreditNoteItem(year, targetTxt2015);
		}
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CreditNoteItemExport().startBatch(args);
	}
	
	
	protected void processargs(String[] args) {
		for (String argstr : args) {
			if(argstr.equalsIgnoreCase("commit")){
				willcommit = true;
			}else if (argstr.equalsIgnoreCase("test")){
				isTest =  true;
				targetTxt2015 = "../../var/exportSAP/MappingCreditNoteItems-test-2015.txt";
			}else if (argstr.equalsIgnoreCase("all")){
				isAll =  true;
			}else if (argstr.equalsIgnoreCase("first")){
				isFirstRound = true;
				targetTxt2015 = "../../var/exportSAP/MappingCreditNoteItems-201512.txt";
			}else if (argstr.equalsIgnoreCase("final")){
				isFinalRound = true;
				targetTxt2017 = "../../var/exportSAP/MappingCreditNoteItems-201702.txt";
				year = 2017;
			}else if(argstr.startsWith("201")){
				if(argstr=="2015"){
					year=2015;
				}else if(argstr=="2016"){
					year=2016;
				}else if(argstr=="2017"){
					year=2017;
				}
			}
		}
	}
	
	protected void usage() {
		System.out.println("-args test \n\ttest : Output credit note lines in date 20150101-20150131");
		System.out.println("-args first \n\tfirst : Only output credit note lines in date 20151201-20151231");
		System.out.println("-args final \n\tfinal : Only output credit note lines in date 20170201-20170228");
		System.out.println("-args all \n\tall : Output credit note lines between year 2014 and 2016");
		System.out.println("-args 2014 or 2015 or 2016 n\201x : Output invoice lines in 2014 or 2015 or 2016");
		System.out.println("Output file are in path ../../var/exportSAP/ start with MappingCreditNoteItems-");
	}

	private void searchCreditNoteItem(int year,String targetTxt)
			throws TimestampException, PUserException, IOException {
		FileWriter outFile = new FileWriter(targetTxt);
		out1 = new PrintWriter(outFile);

		QueryHelper qh = Query.newQueryHelper();
		
		QueryPredicate p1 = null;
		QueryPredicate p2 = null;
		if (isTest){
			 p1 = qh.attr(DWCreditNoteLine.DATE).gte()
					.val(new PDate(2015, 0, 1)).predicate();
			 p2 = qh.attr(DWCreditNoteLine.DATE).lte()
					.val(new PDate(2015, 0, 4)).predicate();
		}else if (isFirstRound){
			 p1 = qh.attr(DWCreditNoteLine.DATE).gte()
					.val(new PDate(year, 11, 1)).predicate();
			 p2 = qh.attr(DWCreditNoteLine.DATE).lte()
					.val(new PDate(year, 11, 31)).predicate();
		}else if(year==2017 && !isFinalRound){
			p1 = qh.attr(DWCreditNoteLine.DATE).gte()
					.val(new PDate(year, 0, 1)).predicate();
			 p2 = qh.attr(DWCreditNoteLine.DATE).lte()
					.val(new PDate(year, 0, 31)).predicate();
		}else{
			 p1 = qh.attr(DWCreditNoteLine.DATE).gte()
					.val(new PDate(year, 0, 1)).predicate();
			 p2 = qh.attr(DWCreditNoteLine.DATE).lte()
					.val(new PDate(year, 11, 31)).predicate();
		}
		
		qh.setClass(DWCreditNoteLine.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(DWCreditNoteLine.CREDITNOTENUMBER);
		qh.addAscendingOrdering(DWCreditNoteLine.LINENUMBER);
		
		Condition cond =  qh.condition(p1.and(p2));
		
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<DWCreditNoteLine> list = new ArrayList<DWCreditNoteLine>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			invoiceItemInfoList = new LinkedList<InvoiceItemInformationBean>();
			System.out.println("\n(!) Write txt file  start.\n");
			for (DWCreditNoteLine creditNoteLine : list) {
				
				InvoiceItemInformationBean iiib = new InvoiceItemInformationBean();
				iiib.setInvoiceItem(creditNoteLine.getLineNumber());
				iiib.setInvoiceNumber(creditNoteLine.getCreditNoteNumber());
				iiib.setInvoiceDate(creditNoteLine.getDate());
				iiib.setNumberOfCreditNoteItems(1);
				iiib.setNumberOfInvoiceDocumentItems(0);
				iiib.setOrderCreditNoteSign('G');
				iiib.setTaxAmount(creditNoteLine.getCcTaxCreditAmount().getAmount());
				if (creditNoteLine.getFromComplaitLineType().equals(
						ComplaintLineType.RETOURELINE)) {
					DWRetoureLine retoureLine = (DWRetoureLine) creditNoteLine;
//					InvoiceItemInformationBean iiib = new InvoiceItemInformationBean();
//					iiib.setInvoiceItem(creditNoteLine.getLineNumber());
//					iiib.setInvoiceNumber(retoureLine.getCreditNoteNumber());
//					iiib.setInvoiceDate(retoureLine.getDate());
					iiib.setDocumentType("ZRE");
					iiib.setPrice(retoureLine.getCcPrice().getAmount()
							/ retoureLine.getCcPrice().getUnit());

					iiib.setInvoiceQuantity(retoureLine.getQuantityReturned()
							.getAmount());
					iiib.setGrossValue(retoureLine.getCcBrutCreditAmount()
							.getAmount()
							+ retoureLine.getCcDiscountCreditAmount()
									.getAmount()
							+ retoureLine.getCcTaxCreditAmount().getAmount());
					iiib.setNetValue(retoureLine.getCcBrutCreditAmount()
							.getAmount()
							+ retoureLine.getCcDiscountCreditAmount()
									.getAmount());

					// iiib.setDiscount(retoureLine.getDiscount());

					iiib.setCustomerNumber(retoureLine.getDWCustomer()
							.getAccountNumber());
					String productNumber=retoureLine.getDWProduct().getProductNumber();
					String eeeeProductNumber=BIDateMapping.productMap.get(productNumber);
					if(eeeeProductNumber!=null&&!eeeeProductNumber.trim().equals("")){
						iiib.setArticleNumber(eeeeProductNumber);
					}else{
						iiib.setArticleNumber(BIDateMapping.dummyMaterialNumber);
					}
					
					iiib.setRegisterNumber(retoureLine.getDWSalesman()
							.getRegisterNumber());
					Product product = _controller.lookupProduct(retoureLine
							.getDWProduct().getProductNumber());
					iiib.setPriceUnit(retoureLine.getCcPrice().getUnit());

					DeliveryLine deliveryLine = _controller.lookupDelivery(
							retoureLine.getDeliveryNumber())
							.lookupDeliveryLine(
									retoureLine.getDeliveryNumberLine());

					CustomerOrderLine col = deliveryLine
							.getCustomerOrderLineMain();
					iiib.setOrderItem(col.getLineNumber());
					iiib.setOrderNumber(col.getParentCustomerOrder()
							.getOrderNumber());
					iiib.setOrderDate(col.getParentCustomerOrder()
							.getOrderDate());
					iiib.setOrderQuantity(col.getOrderquantity().getAmount());
					OwnCompany oc = _controller.getSingletonOwnCompany();
					Weight weight = WeightController.getNewWeight(0, oc
							.getDefaultWeightPerUnit().getWeightMeasureUnit());
					weight = weight.add(product
							.getOwnCompanyProductSalesUnit()
							.getWeight()
							.getNormalizedAmount()
							.multiply(
									retoureLine.getQuantityReturned()
											.getAmount()));
					iiib.setWeight(weight.getAmount());
//					iiib.setStorageLocation("1000");
//					if (col.getWarehouseOrderLineMain()
//							.getAllPickingReservation().size() > 0) {
//						PickingReservation pr = (PickingReservation) col
//								.getWarehouseOrderLineMain()
//								.getAllPickingReservation().get(0);
//						iiib.setStorageLocation(pr.getFullLocationName()
//								.replace("-", ""));
//					}

					double glep = deliveryLine.getCostPrice().getAmount();
					iiib.setGlep(glep);
					iiib.setPfep(glep * 1.085);
					iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
					iiib.setCogspfep(iiib.getPfep() * iiib.getInvoiceQuantity());
					fillWS1Information(iiib); 
					invoiceItemInfoList.addLast(iiib);
					System.out
							.println("\n Number of credit note items loaded: "
									+ invoiceItemInfoList.size());
				} else if (creditNoteLine.getFromComplaitLineType().equals(
						ComplaintLineType.PRICEREDUCTIONLINE)) {

					DWPriceLine priceLine = (DWPriceLine) creditNoteLine;
//					InvoiceItemInformationBean iiib = new InvoiceItemInformationBean();
//					iiib.setInvoiceItem(creditNoteLine.getLineNumber());
//					iiib.setInvoiceNumber(priceLine.getCreditNoteNumber());
//					iiib.setInvoiceDate(priceLine.getDate());
					iiib.setDocumentType("ZG2");

					iiib.setPrice((priceLine.getOldPrice().getAmount()
							/ priceLine.getOldPrice().getUnit() - priceLine
							.getNewPrice().getAmount()
							/ priceLine.getNewPrice().getUnit())
							* priceLine.getNewPrice().getUnit());

					iiib.setInvoiceQuantity(priceLine.getNewQuantity()
							.getAmount());
					iiib.setGrossValue(priceLine.getCcBrutCreditAmount()
							.getAmount()
							+ priceLine.getCcDiscountCreditAmount().getAmount()
							+ priceLine.getCcTaxCreditAmount().getAmount());
					iiib.setNetValue(priceLine.getCcBrutCreditAmount()
							.getAmount()
							+ priceLine.getCcDiscountCreditAmount().getAmount());

					// iiib.setDiscount(retoureLine.getDiscount());

					iiib.setCustomerNumber(priceLine.getDWCustomer()
							.getAccountNumber());
					
					String productNumber=priceLine.getDWProduct().getProductNumber();
					String eeeeProductNumber=BIDateMapping.productMap.get(productNumber);
					if(eeeeProductNumber!=null&&!eeeeProductNumber.trim().equals("")){
						iiib.setArticleNumber(eeeeProductNumber);
					}else{
						iiib.setArticleNumber(BIDateMapping.dummyMaterialNumber);
					}
					
					iiib.setRegisterNumber(priceLine.getDWSalesman()
							.getRegisterNumber());
					Product product = _controller.lookupProduct(priceLine
							.getDWProduct().getProductNumber());
					iiib.setPriceUnit(priceLine.getNewPrice().getUnit());

					CustomerInvoiceLine cil = getCustomerInvoiceLine(
							DocType.NORMALINVOICE,
							priceLine.getCustomerInvoiceNumber(),
							priceLine.getCustomerInvoiceLine());

					if (cil == null) {
						cil = getCustomerInvoiceLine(DocType.DEBITNOTE,
								priceLine.getCustomerInvoiceNumber(),
								priceLine.getCustomerInvoiceLine());
					}

					if (cil.getParentCustomerInvoice().getDocumentType()
							.equals(DocType.NORMALINVOICE)) {
						CustomerOrderLine col = cil.getDeliveryLine()
								.getCustomerOrderLineMain();
						iiib.setOrderItem(col.getLineNumber());
						iiib.setOrderNumber(col.getParentCustomerOrder()
								.getOrderNumber());
						iiib.setOrderDate(col.getParentCustomerOrder()
								.getOrderDate());
						iiib.setOrderQuantity(col.getOrderquantity()
								.getAmount());
						OwnCompany oc = _controller.getSingletonOwnCompany();
						Weight weight = WeightController.getNewWeight(0, oc
								.getDefaultWeightPerUnit()
								.getWeightMeasureUnit());
						weight = weight.add(product
								.getOwnCompanyProductSalesUnit()
								.getWeight()
								.getNormalizedAmount()
								.multiply(
										cil.getDeliveryLine()
												.getDeliveredQuantity()
												.getAmount()));
						iiib.setWeight(weight.getAmount());
						// iiib.setWeightUnit(product.getOwnCompanyProductSalesUnit()
						// .getWeight().getWeightMeasureUnit()
						// .getDescription());
//						if (col.getWarehouseOrderLineMain()
//								.getAllPickingReservation().size() > 0) {
//							PickingReservation pr = (PickingReservation) col
//									.getWarehouseOrderLineMain()
//									.getAllPickingReservation().get(0);
//							iiib.setStorageLocation(pr.getFullLocationName()
//									.replace("-", ""));
//						}

						double glep = cil.getDeliveryLine().getCostPrice()
								.getAmount();
						iiib.setGlep(glep);
						iiib.setPfep(glep * 1.085);
						iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
						iiib.setCogspfep(iiib.getPfep()
								* iiib.getInvoiceQuantity());
					} else {
						CustomerOrderLine col = cil
								.getCustomerOrderLineDebitOrder();
						iiib.setOrderItem(col.getLineNumber());
						iiib.setOrderNumber(col.getParentCustomerOrder()
								.getOrderNumber());
						iiib.setOrderDate(col.getParentCustomerOrder()
								.getOrderDate());
						iiib.setOrderQuantity(col.getOrderquantity()
								.getAmount());
//						iiib.setStorageLocation("");

						double glep = col.getCostPrice().getAmount();
						iiib.setGlep(glep);
						iiib.setPfep(glep * 1.085);
						iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
						iiib.setCogspfep(iiib.getPfep()
								* iiib.getInvoiceQuantity());
					}
//					iiib.setStorageLocation("1000");
					fillWS1Information(iiib); 
					invoiceItemInfoList.addLast(iiib);
					System.out
							.println("\n Number of credit note items loaded: "
									+ invoiceItemInfoList.size());

				}else {
//					if (creditNoteLine.getFromComplaitLineType().equals(
//						ComplaintLineType.MANUALLINE)) {
//					DWManualLine manualLine = (DWManualLine) creditNoteLine;
//					InvoiceItemInformationBean iiib = new InvoiceItemInformationBean();
//					iiib.setInvoiceItem(creditNoteLine.getLineNumber());
//					iiib.setInvoiceNumber(creditNoteLine.getCreditNoteNumber());
//					iiib.setInvoiceDate(creditNoteLine.getDate());
					iiib.setDocumentType("ZG2");
					iiib.setPrice(creditNoteLine.getCcBrutCreditAmount()
							.getAmount()
							+ creditNoteLine.getCcDiscountCreditAmount()
									.getAmount());

					iiib.setInvoiceQuantity(1);
					iiib.setGrossValue(creditNoteLine.getCcBrutCreditAmount()
							.getAmount()
							+ creditNoteLine.getCcDiscountCreditAmount()
									.getAmount()
							+ creditNoteLine.getCcTaxCreditAmount().getAmount());
					iiib.setNetValue(creditNoteLine.getCcBrutCreditAmount()
							.getAmount()
							+ creditNoteLine.getCcDiscountCreditAmount()
									.getAmount());
					iiib.setCustomerNumber(creditNoteLine.getDWCustomer()
							.getAccountNumber());
					iiib.setArticleNumber(BIDateMapping.dummyMaterialNumber);
					if (creditNoteLine.getDWSalesman() != null) {
						iiib.setRegisterNumber(creditNoteLine.getDWSalesman()
								.getRegisterNumber());
					} else {
						CustomerAccount customer = _controller
								.lookupCustomerAccount(iiib.getCustomerNumber());
						if (customer != null) {
							SalesArea sa = customer.getSalesArea(iiib
									.getInvoiceDate());
							if (sa != null) {
								Salesman salesman = sa
										.getResponsibleSalesman(iiib
												.getInvoiceDate());
								if (salesman != null) {
									iiib.setRegisterNumber(salesman
											.getRegisterNumber());
								}
							}
						}
					}
					
					iiib.setPriceUnit(1);
					iiib.setWeight(0);

					iiib.setOrderItem(1);
					iiib.setOrderNumber(999990000);
					iiib.setOrderDate(creditNoteLine.getDate());
					iiib.setOrderQuantity(1);
					iiib.setGlep(0);
					iiib.setPfep(0);
					iiib.setCogsglep(0);
					iiib.setCogspfep(0);
					fillWS1Information(iiib);
					invoiceItemInfoList.addLast(iiib);
					System.out
							.println("\n Number of credit note items loaded: "
									+ invoiceItemInfoList.size());
				}
				

			}
			writeInvoiceItemInfoToTxt();
			_context.commit();
		}
		penum.destroy();
		out1.close();
		System.out.println("\n(*) Write txt file  end.");

	}
	
	private void fillWS1Information(InvoiceItemInformationBean iiib) {
		String customerOrg = BIDateMapping.customerOrgMap.get(iiib
				.getCustomerNumber());
		String defaultWS1RegisterNumber=null;
		if (customerOrg != null) {
			String[] customerOrgArray = customerOrg.split(",");
			iiib.setWs1SalesOrganisation(customerOrgArray[1]);
			iiib.setWs1CustomerNumber(customerOrgArray[0]);
			defaultWS1RegisterNumber=customerOrgArray[2];
		} else {
			if (ownCompanyName.equals("伍尔特（重庆）五金工具有限公司")) {
				iiib.setWs1CustomerNumber("0000999921");
				iiib.setWs1SalesOrganisation("8807");
			} else {
				iiib.setWs1CustomerNumber("0000999921");
				iiib.setWs1SalesOrganisation("8805");
			}
			System.out.println("\n Customer mapping can not find : "
					+ iiib.getCustomerNumber());
		}

		iiib.setPlant(BIDateMapping.getPlantBasedOnWarehouse(iiib
				.getWs1SalesOrganisation()));

		String ws1RegisterNumber = BIDateMapping.getWS1RegisterNumber(
				ownCompanyName, iiib.getRegisterNumber());
		if (BIDateMapping.isWS1RUDUmmyRU(ws1RegisterNumber)
				&& defaultWS1RegisterNumber != null) {
			ws1RegisterNumber = defaultWS1RegisterNumber;
		}
		if (iiib.getWs1CustomerNumber().equals("0000999921")) {
			ws1RegisterNumber = BIDateMapping.getDummyWS1RegisterNumber();
		}
		iiib.setWs1RegisterNumber(ws1RegisterNumber);

		if (iiib.getPriceUnit() == 10000) {
			iiib.setPriceUnit(1000);
			iiib.setPrice(iiib.getPrice() / 10);
		}
	}

	private CustomerInvoiceLine getCustomerInvoiceLine(DocType docType,
			int invoiceNumber, int invoiceLineNumber) throws PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p1 = null;
		if (docType.equals(DocType.NORMALINVOICE)) {
			p1 = qh.attr(InvoiceNumber.NUMBER).eq().val(invoiceNumber)
					.predicate();
			qh.setClass(InvoiceNumber.class);
			qh.setDeepSelect(true);
			Condition cond = qh.condition(p1);
			PEnumeration penum = _controller.createIteratorFactory()
					.getCursorFetch(cond);
			InvoiceNumber invoiceNum = null;
			if (penum.hasMoreElements()) {
				invoiceNum = ((InvoiceNumber) penum.nextElement());
			}
			penum.destroy();
			if (invoiceNum != null) {
				return invoiceNum.getNormalInvoice().lookupCustomerInvoiceLine(
						invoiceLineNumber);
			} else {
				return null;
			}
		} else {
			p1 = qh.attr(DebitNoteNumber.NUMBER).eq().val(invoiceNumber)
					.predicate();
			qh.setClass(DebitNoteNumber.class);
			qh.setDeepSelect(true);
			Condition cond = qh.condition(p1);
			PEnumeration penum = _controller.createIteratorFactory()
					.getCursorFetch(cond);
			DebitNoteNumber debitNoteNumber = null;
			if (penum.hasMoreElements()) {
				debitNoteNumber = ((DebitNoteNumber) penum.nextElement());
			}
			penum.destroy();
			if (debitNoteNumber != null) {
				return debitNoteNumber.getDebitNote()
						.lookupCustomerInvoiceLine(invoiceLineNumber);
			} else {
				return null;
			}
		}

	}
	
	
//	Sales Organization
//	Sales Rep WS1
//	Branch Office Did the Deal WS1
//	Delivery Plant WS1
//	Customer Number (Sold-to-Party) WS1
//	Customer Number (Bill-to-Party) WS1
//	Customer Number (Ship-to-Party) WS1
//	Customer Number (Payer) WS1
//	Order Document Entry Date
//	Order Document
//	Order Reason
//	Order Category (Statistic)
//	Sales Document Type
//	Sales Document Category
//	Article Number WS1
//	Order Document Item
//	Order Quantity
//	Price Key
//	Billing Date
//	Billing Document Number
//	Billing Type
//	Billing Item
//	Billing Quantity
//	Number of Invoice Document Items
//	Order/Credit Note
//	Number of Credit Note Items
//	Complaint Reason
//	Turnover
//	Gross Value
//	Net Value/Customer Turnover
//	Discount
//	Price Increase Surcharge
//	Basis Price
//	Freight Costs
//	Cost of Goods PFEP
//	Cost of Goods GLEP
//	Tax Amount
//	Netto Weight in Kilogramms
	
	
	private void writeInvoiceItemInfoToTxt() {
		for (int i = 0; i < invoiceItemInfoList.size(); i++) {
			InvoiceItemInformationBean iiib = invoiceItemInfoList.get(i);
			StringBuffer sb = new StringBuffer();
			// Sales Organization
			sb.append(iiib.getWs1SalesOrganisation()).append(csvSeperator);
			
            // Sales Rep WS1
			sb.append(iiib.getWs1RegisterNumber()).append(csvSeperator);
            // Branch Office Did the Deal WS1
			sb.append(iiib.getPlant()).append(csvSeperator);
			//TODO 	Delivery Plant WS1
            //Customer Number (Sold-to-Party) WS1
			sb.append(iiib.getWs1CustomerNumber()).append(csvSeperator);
			// TODO Customer Number (Bill-to-Party) WS1
			// TODO Customer Number (Ship-to-Party) WS1
			// TODO Customer Number (Payer) WS1
			
			// Order Document Entry Date
			if (iiib.getOrderDate() != null) {
				sb.append(BIDateMapping.dateFormat.format(iiib.getOrderDate()))
						.append(csvSeperator);
			} else {
				sb.append(" ").append(csvSeperator);
			}
			
            // Order Document
			sb.append(iiib.getOrderNumber()).append(csvSeperator);
			// TODO Order Reason
			// TODO Order Category (Statistic)
			// TODO Sales Document Type
			// TODO Sales Document Category
			
			//Article Number WS1
			sb.append(iiib.getArticleNumber()).append(csvSeperator);
			
            // Order Document Item
			sb.append(iiib.getOrderItem()).append(csvSeperator);
			// Order Quantity
			sb.append(iiib.getOrderQuantity()).append(csvSeperator);
			// Price Key
			sb.append(BIDateMapping.getPriceUnitMapping(iiib.getPriceUnit())).append(csvSeperator);
			
			// Billing Date
			if (iiib.getInvoiceDate() != null) {
				sb.append(
						BIDateMapping.dateFormat.format(iiib.getInvoiceDate()))
						.append(csvSeperator);
			} else {
				sb.append(" ").append(csvSeperator);
			}
			// Billing Document Number
			sb.append(BIDateMapping.fillWS1OrderOrInvoiceNumber(iiib.getInvoiceNumber())).append(csvSeperator);
			// Billing Type
			sb.append(iiib.getDocumentType()).append(csvSeperator);
			// Billing Item
			sb.append(iiib.getInvoiceItem()).append(csvSeperator);
			// Billing Quantity
			sb.append("-"+iiib.getInvoiceQuantity()).append(csvSeperator);
			// TODO Number of Invoice Document Items Field needs to be filled with '1' when data row is an Invoice Item, else put in a '0'
			sb.append(iiib.getNumberOfInvoiceDocumentItems()).append(csvSeperator);
			// Order/Credit Note, If it is an Order then 'A', if Credit Note then 'G'
			sb.append(iiib.getOrderCreditNoteSign()).append(csvSeperator);
			// Number of Credit Note Items
			sb.append(iiib.getNumberOfCreditNoteItems()).append(csvSeperator);
			// TODO	Complaint Reason
			// TODO Turnover
			sb.append("-"+ DoubleUtils.getRoundedAmount(iiib.getNetValue())).append(csvSeperator);
			// LOGTO Gross Value
			sb.append("-"+DoubleUtils.getRoundedAmount(iiib.getGrossValue())).append(csvSeperator);
			// LONTO Net Value/Customer Turnover
			sb.append("-"+DoubleUtils.getRoundedAmount(iiib.getNetValue())).append(csvSeperator);
			// Discount
			sb.append("-"+DoubleUtils.getRoundedAmount(iiib.getDiscount())).append(csvSeperator);
			
			// Price Increase Surcharge No logic, just correct format; if not available use LOGTO minus LONTO; If the Document is a Creditnote, put a minus in front of the Keyfigure
			sb.append(DoubleUtils.getRoundedAmount(iiib.getNetValue()-iiib.getGrossValue())).append(csvSeperator);
			// Basis Price
			sb.append(DoubleUtils.getRoundedAmount(iiib.getPrice())).append(csvSeperator);
			// TODO Freight Costs, is on header level, how to calculate it on line level.
			
			// Cost of Goods PFEP
			sb.append("-"+iiib.getCogspfep()).append(csvSeperator);
			// Cost of Goods GLEP
			sb.append("-"+iiib.getCogsglep()).append(csvSeperator);
			// Tax Amount
			sb.append("-"+iiib.getTaxAmount()).append(csvSeperator);
			// Netto Weight in Kilogramms
			sb.append("-"+FormatHelper.getWeightFormat().format(iiib.getWeight())).append(csvSeperator);
//			sb.append(
//					BIDateMapping.fillWS1OrderOrInvoiceNumber(iiib
//							.getOrderNumber())).append(csvSeperator);
//			sb.append(" ").append(csvSeperator);
//			
//			sb.append(" ").append(csvSeperator);
//			sb.append(" ").append(csvSeperator);
			sb.append(" ");

			out1.println(sb.toString());
			numberOfInvoiceItems++;
			System.out.println("\n Number of Invoice Item writed in txt 1: "
					+ numberOfInvoiceItems);
		}
		out1.flush();
	}
	
	

//	private void writeInvoiceItemInfoToTxt() {
//		for (int i = 0; i < invoiceItemInfoList.size(); i++) {
//			InvoiceItemInformationBean iiib = invoiceItemInfoList.get(i);
//			StringBuffer sb = new StringBuffer();
//			sb.append(iiib.getWs1SalesOrganisation()).append(csvSeperator);
//			sb.append(iiib.getInvoiceItem()).append(csvSeperator);
//			sb.append(iiib.getOrderItem()).append(csvSeperator);
//			sb.append(iiib.getDocumentType()).append(csvSeperator);
//			sb.append(" ").append(csvSeperator);
//			sb.append(" ").append(csvSeperator);
//			sb.append(
//					BIDateMapping.fillWS1OrderOrInvoiceNumber(iiib
//							.getInvoiceNumber())).append(csvSeperator);
//			
//			//
//			if (iiib.getOrderDate() != null) {
//				sb.append(BIDateMapping.dateFormat.format(iiib.getOrderDate()))
//						.append(csvSeperator);
//			} else {
//				sb.append(" ").append(csvSeperator);
//			}
//			if (iiib.getInvoiceDate() != null) {
//				sb.append(
//						BIDateMapping.dateFormat.format(iiib.getInvoiceDate()))
//						.append(csvSeperator);
//			} else {
//				sb.append(" ").append(csvSeperator);
//			}
//			sb.append(iiib.getOrderQuantity()).append(csvSeperator);
//			sb.append(DoubleUtils.getRoundedAmount(iiib.getPrice(),10)).append(csvSeperator);
//			sb.append("-"+df.format(iiib.getWeight())).append(csvSeperator);
//			sb.append("-"+iiib.getInvoiceQuantity()).append(csvSeperator);
//			sb.append("-"+DoubleUtils.getRoundedAmount(iiib.getGrossValue())).append(csvSeperator);
//			sb.append("-"+DoubleUtils.getRoundedAmount(iiib.getNetValue())).append(csvSeperator);
//			sb.append(DoubleUtils.getRoundedAmount(iiib.getGlep(),10)).append(csvSeperator);
//			sb.append(DoubleUtils.getRoundedAmount(iiib.getPfep(),10)).append(csvSeperator);
//			sb.append(" ").append(csvSeperator);
//			sb.append(" ").append(csvSeperator);
//			sb.append("-"+iiib.getCogsglep()).append(csvSeperator);
//			sb.append("-"+iiib.getCogspfep()).append(csvSeperator);
//			sb.append(iiib.getWs1CustomerNumber()).append(csvSeperator);
//			sb.append(iiib.getArticleNumber()).append(csvSeperator);
//			sb.append(iiib.getWs1RegisterNumber()).append(csvSeperator);
//			sb.append(BIDateMapping.getPriceUnitMapping(iiib.getPriceUnit()))
//					.append(csvSeperator);
//			sb.append(
//					BIDateMapping.fillWS1OrderOrInvoiceNumber(iiib
//							.getOrderNumber())).append(csvSeperator);
//			sb.append(" ").append(csvSeperator);
//			sb.append(iiib.getPlant()).append(csvSeperator);
//			sb.append(" ").append(csvSeperator);
//			sb.append(" ").append(csvSeperator);
//			sb.append(" ");
//
//			out1.println(sb.toString());
//			numberOfInvoiceItems++;
//			System.out
//					.println("\n Number of credit note items writed in txt 1: "
//							+ numberOfInvoiceItems);
//		}
//		out1.flush();
//	}
}
