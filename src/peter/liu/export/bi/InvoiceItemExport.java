package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.wuerth.phoenix.Phxbasic.enums.DocType;
import com.wuerth.phoenix.Phxbasic.models.CustomerInvoiceLine;
import com.wuerth.phoenix.Phxbasic.models.CustomerOrderLine;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerInvoiceLine;
import com.wuerth.phoenix.Phxbasic.models.DebitNoteNumber;
import com.wuerth.phoenix.Phxbasic.models.InvoiceNumber;
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
 * 
 * @author pcnsh197
 * 
 */
public class InvoiceItemExport extends BatchRunner {

	private int								NUMBER_OF_BATCH_TO_FECTCH	= 1000;
	LinkedList<InvoiceItemInformationBean>	invoiceItemInfoList			= new LinkedList<InvoiceItemInformationBean>();
	private String							targetTxt2015				= "../../var/exportSAP/MappingInvoiceItems-2015.csv";
	private String							targetTxt2016				= "../../var/exportSAP/MappingInvoiceItems-2016.csv";
	private String							targetTxt2017				= "../../var/exportSAP/MappingInvoiceItems-2017.csv";
	private PrintWriter						out1						= null;
	private String							csvSeperator				= ",";
	private int								numberOfInvoiceItems		= 0;
	private String							ownCompanyName;
	private boolean 						isTest 						= false;
	private boolean 						isAll 						= false;
	private boolean 						isFirstRound				= false;
	private boolean 						isFinalRound				= false;
	private int 							year						= 2015;

	@Override
	protected void batchMethod() throws TimestampException, PUserException,
			IOException {
		ownCompanyName = _controller.getSingletonOwnCompany().getName();

		if(isTest){
			searchInvoiceItem(year,targetTxt2015);
		}else if(isFirstRound){
			searchInvoiceItem(2015, targetTxt2015);
		}else if(isFinalRound){
			searchInvoiceItem(2017, targetTxt2017);
		}else if (isAll){
			searchInvoiceItem(2015, targetTxt2015);
			searchInvoiceItem(2016, targetTxt2016);
			searchInvoiceItem(2017, targetTxt2017);
		}else{
			searchInvoiceItem(year, targetTxt2015);
		}
		System.out.println("\n[OK]");
	}
	
	protected void usage() {
		System.out.println("-args test \n\ttest : Only output invoice lines in date 20151201-20151204");
		System.out.println("-args first \n\tfirst : Only output invoice lines in date 20151201-20151231");
		System.out.println("-args final \n\tfinal : Only output invoice lines in date 20170201-20170228");
		System.out.println("-args all \n\tall : Output invoice lines between year 2014 and 2016, each year has a output file");
		System.out.println("-args 2015, 2016 or 2017 \n\tall : Output invoice lines in 2015 or 2016 or 2017");
		System.out.println("output file are in path ../../var/exportSAP/ start with MappingInvoiceItems-");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new InvoiceItemExport().startBatch(args);
	}
	
	protected void processargs(String[] args) {
		for (String argstr : args) {
			if(argstr.equalsIgnoreCase("commit")){
				willcommit = true;
			}else if (argstr.equalsIgnoreCase("test")){
				isTest =  true;
				targetTxt2015 = "../../var/exportSAP/MappingInvoiceItems-test-2015.csv";
			}else if (argstr.equalsIgnoreCase("all")){
				isAll =  true;
			}else if (argstr.equalsIgnoreCase("first")){
				isFirstRound = true;
				targetTxt2015 = "../../var/exportSAP/MappingInvoiceItems-201512.csv";
			}else if (argstr.equalsIgnoreCase("final")){
				isFinalRound = true;
				targetTxt2017 = "../../var/exportSAP/MappingInvoiceItems-201702.csv";
				year = 2017;
			}else if(argstr.startsWith("201")){
				if(argstr=="2015"){
					year=2015;
				}else if(argstr=="2016"){
					year=2016;
				}else if(argstr=="2017"){
					year=2017;
				}
			}else{
				throw new RuntimeException("Arguments is not supported by the tool!");
			}
		}
	}

	private void searchInvoiceItem(int year,String targetTxt)
			throws TimestampException, PUserException, IOException {
		FileWriter outFile = new FileWriter(targetTxt);
		out1 = new PrintWriter(outFile);
		writeHeaderToCSV(); 
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(DWCustomerInvoiceLine.class);
		
		QueryPredicate p1 = null;
		QueryPredicate p2 = null;
		if (isTest){
			p1 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte()
					.val(new PDate(year, 11, 1)).predicate();
			p2 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte()
					.val(new PDate(year, 11, 4)).predicate();
		}else if(isFirstRound){
			p1 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte()
					.val(new PDate(year, 11, 1)).predicate();
			p2 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte()
					.val(new PDate(year, 11, 31)).predicate();
		}else if(isFinalRound){
			p1 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte()
					.val(new PDate(year, 1, 1)).predicate();
			p2 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte()
					.val(new PDate(year, 1, 28)).predicate();
		}else if(year==2017 && !isFinalRound){
			p1 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte()
					.val(new PDate(year, 0, 1)).predicate();
			p2 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte()
					.val(new PDate(year, 0, 31)).predicate();
		}else{
			p1 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte()
					.val(new PDate(year, 0, 1)).predicate();
			 p2 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte()
					.val(new PDate(year, 11, 31)).predicate();
		}
		 
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(DWCustomerInvoiceLine.INVOICENUMBER);
		qh.addAscendingOrdering(DWCustomerInvoiceLine.LINENUMBER);
		Condition cond = null;
		cond = qh.condition(p1.and(p2));
		System.out.println("QueryString "+Query.getQueryString(cond));
		System.out.println("isTest: "+isTest);
		
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<DWCustomerInvoiceLine> list = new ArrayList<DWCustomerInvoiceLine>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			invoiceItemInfoList = new LinkedList<InvoiceItemInformationBean>();
			System.out.println("\n(!) Write txt file  start.\n");
			for (DWCustomerInvoiceLine invoiceLine : list) {
				InvoiceItemInformationBean iiib = new InvoiceItemInformationBean();
				iiib.setInvoiceItem(invoiceLine.getLineNumber());
				iiib.setDocumentType("ZF1");
				iiib.setInvoiceNumber(invoiceLine.getInvoiceNumber());
				iiib.setInvoiceDate(invoiceLine.getInvoiceDate());
				iiib.setPrice(invoiceLine.getPrice().getAmount()
						/ invoiceLine.getPrice().getUnit());

				iiib.setInvoiceQuantity(invoiceLine.getQuantity().getAmount());
				iiib.setGrossValue(invoiceLine.getCcNetAmount().getAmount()
						+ invoiceLine.getCcTaxAmount().getAmount());
				iiib.setNetValue(invoiceLine.getCcNetAmount().getAmount());
				iiib.setTurnover(invoiceLine.getCcNetAmount().getAmount());
				iiib.setDiscount(invoiceLine.getDiscount());
				iiib.setTaxAmount(invoiceLine.getTaxAmount().getAmount());
				iiib.setCustomerNumber(invoiceLine.getDWCustomer()
						.getAccountNumber());
				iiib.setName1(invoiceLine.getDWCustomer().getName1());
				String productNumber=invoiceLine.getDWProduct().getProductNumber();
				String eeeeProductNumber=BIDateMapping.productMap.get(productNumber);
				if(eeeeProductNumber!=null&&!eeeeProductNumber.trim().equals("")){
					iiib.setArticleNumber(eeeeProductNumber);
				}else{
					iiib.setArticleNumber(BIDateMapping.dummyMaterialNumber);
				}
				iiib.setRegisterNumber(invoiceLine.getDWSalesman()
						.getRegisterNumber());
				Product product = _controller.lookupProduct(invoiceLine
						.getDWProduct().getProductNumber());
				iiib.setPriceUnit(invoiceLine.getPrice().getUnit());
				iiib.setStorageLocation("1000");
				iiib.setNumberOfInvoiceDocumentItems(1);
				iiib.setOrderCreditNoteSign('A');
				if(invoiceLine.getLineNumber()==1){
					iiib.setFreightCost(invoiceLine.getDWCustomerInvoice().getCcFreightCosts().getAmount());
				}
				CustomerInvoiceLine cil = getCustomerInvoiceLine(
						invoiceLine.getDocumentType(), iiib.getInvoiceNumber(),
						iiib.getInvoiceItem());

				if (invoiceLine.getDocumentType().equals(DocType.NORMALINVOICE)) {
					CustomerOrderLine col = cil.getDeliveryLine()
							.getCustomerOrderLineMain();
					iiib.setWarehouseNumber(col.getParentCustomerOrder().getWarehouseNumber());
					iiib.setGoodsRecipient(col.getParentCustomerOrder().getGoodsRecipient().getId());
					iiib.setDebtor(col.getParentCustomerOrder().getDebitor().getId());
					iiib.setOrderItem(col.getLineNumber());
					iiib.setOrderNumber(col.getParentCustomerOrder()
							.getOrderNumber());
					iiib.setOrderDate(col.getParentCustomerOrder()
							.getOrderDate());
					iiib.setOrderQuantity(col.getOrderquantity().getAmount());
					OwnCompany oc = _controller.getSingletonOwnCompany();
					Weight weight = WeightController.getNewWeight(0, oc
							.getDefaultWeightPerUnit().getWeightMeasureUnit());
					weight = weight
							.add(product
									.getOwnCompanyProductSalesUnit()
									.getWeight()
									.getNormalizedAmount()
									.multiply(
											cil.getDeliveryLine()
													.getDeliveredQuantity()
													.getAmount()));
					iiib.setWeight(weight.getAmount());
					double glep = cil.getDeliveryLine().getCostPrice()
							.getAmount();
					iiib.setGlep(glep);
					iiib.setPfep(glep / 1.1);
					iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
					iiib.setCogspfep(iiib.getPfep() * iiib.getInvoiceQuantity());
				} else {
					CustomerOrderLine col = cil
							.getCustomerOrderLineDebitOrder();
					iiib.setWarehouseNumber(col.getParentCustomerOrder().getWarehouseNumber());
					iiib.setGoodsRecipient(col.getParentCustomerOrder().getGoodsRecipient().getId());
					iiib.setDebtor(col.getParentCustomerOrder().getDebitor().getId());
					iiib.setOrderItem(col.getLineNumber());
					iiib.setOrderNumber(col.getParentCustomerOrder()
							.getOrderNumber());
					iiib.setOrderDate(col.getParentCustomerOrder()
							.getOrderDate());
					iiib.setOrderQuantity(col.getOrderquantity().getAmount());

					double glep = col.getCostPrice().getAmount();
					iiib.setGlep(glep);
					iiib.setPfep(glep / 1.1);
					iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
					iiib.setCogspfep(iiib.getPfep() * iiib.getInvoiceQuantity());
				}
				if(iiib.getPriceUnit()==10000){
					iiib.setPriceUnit(1000);
					iiib.setPrice(iiib.getPrice()/10);
				}
				
				BIDateMapping.fillWS1Information(iiib);
				fillfillWS1InformationForInvoice(iiib);
				invoiceItemInfoList.addLast(iiib);
			}
			System.out.println("\n Number of invoice items loaded: "
					+ invoiceItemInfoList.size());
			writeInvoiceItemInfoToTxt();
			_context.commit();
		}
		penum.destroy();
		out1.close();
		System.out.println("\n(*) Write txt file  end.");

	}
	private void fillfillWS1InformationForInvoice(InvoiceItemInformationBean iiib){
		iiib.setOrderReason("001");
		iiib.setOrderCategory("1");
		iiib.setDocumentCategory("M");
		iiib.setSalesDocumentType("");
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
			//Delivery Plant WS1
			sb.append(iiib.getDeliveryPlant()).append(csvSeperator);
            //Customer Number (Sold-to-Party) WS1
			sb.append(iiib.getWs1CustomerNumber()).append(csvSeperator);
			// TODO Customer Number (Bill-to-Party) WS1
			sb.append(iiib.getCustomerNumber()).append(csvSeperator);
			// TODO Customer Number (Ship-to-Party) WS1
			sb.append(iiib.getGoodsRecipient()).append(csvSeperator);
			// TODO Customer Number (Payer) WS1
			sb.append(iiib.getDebtor()).append(csvSeperator);
			// Order Document Entry Date
			if (iiib.getOrderDate() != null) {
				sb.append(BIDateMapping.dateFormat.format(iiib.getOrderDate()))
						.append(csvSeperator);
			} else {
				sb.append(" ").append(csvSeperator);
			}
			
            // Order Document
			sb.append(iiib.getOrderNumber()).append(csvSeperator);
			// Order Reason
			sb.append(iiib.getOrderReason()).append(csvSeperator);
			// TODO Order Category (Statistic)
			sb.append(iiib.getOrderCategory()).append(csvSeperator);
			// TODO Sales Document Type
			sb.append(iiib.getSalesDocumentType()).append(csvSeperator);
			// TODO Sales Document Category
			sb.append(iiib.getDocumentCategory()).append(csvSeperator);
			
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
			sb.append(iiib.getInvoiceQuantity()).append(csvSeperator);
			// Number of Invoice Document Items
			sb.append(iiib.getNumberOfInvoiceDocumentItems()).append(csvSeperator);
			// Order/Credit Note, If it is an Order then 'A', if Credit Note then 'G'
			sb.append(iiib.getOrderCreditNoteSign()).append(csvSeperator);
			// Number of Credit Note Items, set what in this column?
			sb.append(iiib.getNumberOfCreditNoteItems()).append(csvSeperator);
			// Complaint Reason
			sb.append(" ").append(csvSeperator);
			// Turnover
			sb.append(DoubleUtils.getRoundedAmount(iiib.getTurnover())).append(csvSeperator);
			// LOGTO Gross Value
			sb.append(DoubleUtils.getRoundedAmount(iiib.getGrossValue())).append(csvSeperator);
			// LONTO Net Value/Customer Turnover
			sb.append(DoubleUtils.getRoundedAmount(iiib.getNetValue())).append(csvSeperator);
			// Discount
			sb.append(DoubleUtils.getRoundedAmount(iiib.getDiscount())).append(csvSeperator);
			
			// Price Increase Surcharge No logic, just correct format; if not available use LOGTO minus LONTO; If the Document is a Creditnote, put a minus in front of the Keyfigure
			sb.append("0.00").append(csvSeperator);
			// Basis Price
			sb.append(DoubleUtils.getRoundedAmount(iiib.getPrice())).append(csvSeperator);
			// TODO Freight Costs, is on header level, how to calculate it on line level.
			sb.append(DoubleUtils.getRoundedAmount(iiib.getFreightCost())).append(csvSeperator);
			// Cost of Goods PFEP
			sb.append(DoubleUtils.getRoundedAmount(iiib.getCogspfep())).append(csvSeperator);
			// Cost of Goods GLEP
			sb.append(DoubleUtils.getRoundedAmount(iiib.getCogsglep())).append(csvSeperator);
			// Tax Amount
			sb.append(DoubleUtils.getRoundedAmount(iiib.getTaxAmount())).append(csvSeperator);
			// Netto Weight in Kilogramms
			sb.append(FormatHelper.getWeightFormat().format(iiib.getWeight())).append(csvSeperator);
			sb.append(" ");

			out1.println(sb.toString());
			numberOfInvoiceItems++;
		}
		System.out.println("\n Number of Invoice Item writed in txt: "
				+ numberOfInvoiceItems);
		out1.flush();
	}
	
	
	
	private void writeHeaderToCSV() {
		StringBuffer sb = new StringBuffer();
		String[] header1 = { "CSALESORG", "CTERREP", "CBOD", "CPLT", "CCUST", "CCBILLTO", "CCSHIPTO", "CCPAYER",
				"DORDENTRY", "CORDNO", "CORDREAS", "CORDCATS", "CDOCTYPE", "CDOCCAT", "CMAT", "CORDITM", "QIXQUOR",
				"CPRKEY", "DBILL", "CBILLNO", "CBILLTYPE", "CBILLITM", "QIXQUSU", "NIXINIT", "CORDCRED", "NIXCNIT",
				"CREACOMP", "LIITO", "LIGTO", "LINTO", "LINDC", "LIN_PS", "LIXPRB", "LIN_FR", "LINPP", "LINMP",
				"LIXXTAX", "QIWTNTKG" };
		for (int i = 0; i < header1.length; i++) {
			sb.append(header1[i]).append(csvSeperator);
		}
		out1.println(sb.toString());
		
		
		sb = new StringBuffer();
		String[] header2 = { "Sales Organization", "Sales Rep WS1", "Branch Office Did the Deal WS1",
				"Delivery Plant WS1", "Customer Number (Sold-to-Party) WS1", "Customer Number (Bill-to-Party) WS1",
				"Customer Number (Ship-to-Party) WS1", "Customer Number (Payer) WS1", "Order Document Entry Date",
				"Order Document", "Order Reason", "Order Category (Statistic)", "Sales Document Type",
				"Sales Document Category", "Article Number WS1", "Order Document Item", "Order Quantity", "Price Key",
				"Billing Date", "Billing Document Number", "Billing Type", "Billing Item", "Billing Quantity",
				"Number of Invoice Document Items", "Order/Credit Note", "Number of Credit Note Items",
				"Complaint Reason", "Turnover", "Gross Value", "Net Value/Customer Turnover", "Discount",
				"Price Increase Surcharge", "Basis Price", "Freight Costs", "Cost of Goods PFEP", "Cost of Goods GLEP",
				"Tax Amount", "Netto Weight in Kilogramms" };
		for (int i = 0; i < header2.length; i++) {
			sb.append(header2[i]).append(csvSeperator);
		}
		out1.println(sb.toString());
		out1.flush();
	}
}
