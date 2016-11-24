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
import com.wuerth.phoenix.Phxbasic.models.DWCustomerInvoiceLine;
import com.wuerth.phoenix.Phxbasic.models.DWFreightCostLine;
import com.wuerth.phoenix.Phxbasic.models.DWPriceLine;
import com.wuerth.phoenix.Phxbasic.models.DWRetoureLine;
import com.wuerth.phoenix.Phxbasic.models.DebitNoteNumber;
import com.wuerth.phoenix.Phxbasic.models.DeliveryLine;
import com.wuerth.phoenix.Phxbasic.models.InvoiceNumber;
import com.wuerth.phoenix.Phxbasic.models.OwnCompany;
import com.wuerth.phoenix.Phxbasic.models.Product;
import com.wuerth.phoenix.Phxbasic.models.SalesArea;
import com.wuerth.phoenix.Phxbasic.models.Salesman;
import com.wuerth.phoenix.basic.etnax.datawarehouse.DWConstants;
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

	private int NUMBER_OF_BATCH_TO_FECTCH = 1000;
	LinkedList<InvoiceItemInformationBean> invoiceItemInfoList = new LinkedList<InvoiceItemInformationBean>();
	private String targetTxt2015 = "../../var/exportSAP/MappingInvoiceItems-2015.csv";
	private String targetTxt2016 = "../../var/exportSAP/MappingInvoiceItems-2016.csv";
	private String targetTxt2017 = "../../var/exportSAP/MappingInvoiceItems-2017.csv";
	private PrintWriter out1 = null;
	private int numberOfInvoiceItems = 0;
	private String ownCompanyName;
	private boolean isTest = false;
	private boolean isAll = false;
	private boolean isFirstRound = false;
	private boolean isFinalRound = false;
	private int year = 2015;

	@Override
	protected void batchMethod() throws TimestampException, PUserException, IOException {
		ownCompanyName = _controller.getSingletonOwnCompany().getName();

		if (isTest) {
			searchInvoiceItem(year, targetTxt2015);
			searchCreditNoteItem(year, targetTxt2015);
			out1.close();
		} else if (isFirstRound) {
			searchInvoiceItem(2015, targetTxt2015);
			searchCreditNoteItem(2015, targetTxt2015);
			out1.close();
		} else if (isFinalRound) {
			searchInvoiceItem(2017, targetTxt2017);
			searchCreditNoteItem(2017, targetTxt2017);
		} else if (isAll) {
			searchInvoiceItem(2015, targetTxt2015);
			searchCreditNoteItem(2015, targetTxt2015);
			out1.close();
			searchInvoiceItem(2016, targetTxt2016);
			searchCreditNoteItem(2016, targetTxt2016);
			out1.close();
			searchInvoiceItem(2017, targetTxt2017);
			searchCreditNoteItem(2017, targetTxt2017);
			out1.close();
		} else {
			searchInvoiceItem(year, targetTxt2015);
			searchCreditNoteItem(year, targetTxt2015);
			out1.close();
		}

		System.out.println("\n[OK]");
	}

	protected void usage() {
		System.out.println("-args test \n\ttest : Only output invoice lines in date 20151201-20151204");
		System.out.println("-args first \n\tfirst : Only output invoice lines in date 20151201-20151231");
		System.out.println("-args final \n\tfinal : Only output invoice lines in date 20170201-20170228");
		System.out.println(
				"-args all \n\tall : Output invoice lines between year 2014 and 2016, each year has a output file");
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
			if (argstr.equalsIgnoreCase("commit")) {
				willcommit = true;
			} else if (argstr.equalsIgnoreCase("test")) {
				isTest = true;
				targetTxt2015 = "../../var/exportSAP/MappingInvoiceItems-test-2015.csv";
			} else if (argstr.equalsIgnoreCase("all")) {
				isAll = true;
			} else if (argstr.equalsIgnoreCase("first")) {
				isFirstRound = true;
				targetTxt2015 = "../../var/exportSAP/MappingInvoiceItems-201512.csv";
			} else if (argstr.equalsIgnoreCase("final")) {
				isFinalRound = true;
				targetTxt2017 = "../../var/exportSAP/MappingInvoiceItems-201702.csv";
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

	private void searchInvoiceItem(int year, String targetTxt) throws TimestampException, PUserException, IOException {
		FileWriter outFile = new FileWriter(targetTxt);
		out1 = new PrintWriter(outFile);
		BIDateMapping.writeInvoiceItemHeaderToCSV(out1);
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(DWCustomerInvoiceLine.class);

		QueryPredicate p1 = null;
		QueryPredicate p2 = null;
		if (isTest) {
			p1 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte().val(new PDate(year, 11, 1)).predicate();
			p2 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte().val(new PDate(year, 11, 4)).predicate();
		} else if (isFirstRound) {
			p1 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte().val(new PDate(year, 11, 1)).predicate();
			p2 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte().val(new PDate(year, 11, 31)).predicate();
		} else if (isFinalRound) {
			p1 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte().val(new PDate(year, 1, 1)).predicate();
			p2 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte().val(new PDate(year, 1, 28)).predicate();
		} else if (year == 2017 && !isFinalRound) {
			p1 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte().val(new PDate(year, 0, 1)).predicate();
			p2 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte().val(new PDate(year, 0, 31)).predicate();
		} else {
			p1 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte().val(new PDate(year, 0, 1)).predicate();
			p2 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte().val(new PDate(year, 11, 31)).predicate();
		}

		qh.setDeepSelect(true);
		qh.addAscendingOrdering(DWCustomerInvoiceLine.INVOICENUMBER);
		qh.addAscendingOrdering(DWCustomerInvoiceLine.LINENUMBER);
		Condition cond = null;
		cond = qh.condition(p1.and(p2));
		System.out.println("QueryString " + Query.getQueryString(cond));

		PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
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
				iiib.setPrice(invoiceLine.getPrice().getAmount() / invoiceLine.getPrice().getUnit());

				iiib.setInvoiceQuantity(invoiceLine.getQuantity().getAmount());
				iiib.setGrossValue(invoiceLine.getCcNetAmount().getAmount());

				iiib.setNetValue(invoiceLine.getCcNetAmount().getAmount());
				iiib.setTurnover(invoiceLine.getCcNetAmount().getAmount());
				if (invoiceLine.getCcRebateAmount().getAmount() != 0) {
					System.out.println("Rebate information Invoice: " + iiib.getInvoiceNumber() + " Line: "
							+ iiib.getInvoiceItem());
					iiib.setTurnover(iiib.getTurnover() - invoiceLine.getCcRebateAmount().getAmount());
				}
				iiib.setCogsglep(invoiceLine.getCcCostAmount().getAmount());
				iiib.setCogspfep(iiib.getCogsglep() / 1.1);

				iiib.setDiscount(invoiceLine.getCcDiscountAmount().getAmount());
				iiib.setTaxAmount(invoiceLine.getTaxAmount().getAmount());
				iiib.setCustomerNumber(invoiceLine.getDWCustomer().getAccountNumber());
				iiib.setName1(invoiceLine.getDWCustomer().getName1());
				iiib.setProductNumber(invoiceLine.getDWProduct().getProductNumber());
				iiib.setRegisterNumber(invoiceLine.getDWSalesman().getRegisterNumber());
				Product product = _controller.lookupProduct(invoiceLine.getDWProduct().getProductNumber());
				iiib.setPriceUnit(invoiceLine.getPrice().getUnit());
				iiib.setStorageLocation("1000");
				iiib.setNumberOfInvoiceDocumentItems(1);
				iiib.setOrderCreditNoteSign('A');
				if (invoiceLine.getLineNumber() == 1) {
					iiib.setFreightCost(invoiceLine.getDWCustomerInvoice().getCcFreightCosts().getAmount());
				}
				CustomerInvoiceLine cil = getCustomerInvoiceLine(invoiceLine.getDocumentType(), iiib.getInvoiceNumber(),
						iiib.getInvoiceItem());

				if (invoiceLine.getDocumentType().equals(DocType.NORMALINVOICE)) {
					CustomerOrderLine col = cil.getDeliveryLine().getCustomerOrderLineMain();
					iiib.setWarehouseNumber(col.getParentCustomerOrder().getWarehouseNumber());
					iiib.setGoodsRecipient(col.getParentCustomerOrder().getGoodsRecipient().getId());
					iiib.setDebtor(col.getParentCustomerOrder().getDebitor().getId());
					iiib.setGoodsRecipientName(col.getParentCustomerOrder().getGoodsRecipient().getName());
					iiib.setDebtorName(col.getParentCustomerOrder().getDebitor().getName());
					iiib.setOrderItem(col.getLineNumber());
					iiib.setOrderNumber(col.getParentCustomerOrder().getOrderNumber());
					iiib.setOrderDate(col.getParentCustomerOrder().getOrderDate());
					iiib.setOrderQuantity(col.getOrderquantity().getAmount());
					OwnCompany oc = _controller.getSingletonOwnCompany();
					Weight weight = WeightController.getNewWeight(0,
							oc.getDefaultWeightPerUnit().getWeightMeasureUnit());
					weight = weight.add(product.getOwnCompanyProductSalesUnit().getWeight().getNormalizedAmount()
							.multiply(cil.getDeliveryLine().getDeliveredQuantity().getAmount()));
					iiib.setWeight(weight.getAmount());
					// double glep =
					// cil.getDeliveryLine().getCostPrice().getAmount();
					// iiib.setGlep(glep);
					// iiib.setPfep(glep / 1.1);
					// iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
					// iiib.setCogspfep(iiib.getPfep() *
					// iiib.getInvoiceQuantity());
				} else {
					CustomerOrderLine col = cil.getCustomerOrderLineDebitOrder();
					iiib.setWarehouseNumber(col.getParentCustomerOrder().getWarehouseNumber());
					iiib.setGoodsRecipient(col.getParentCustomerOrder().getGoodsRecipient().getId());
					iiib.setDebtor(col.getParentCustomerOrder().getDebitor().getId());
					iiib.setGoodsRecipientName(col.getParentCustomerOrder().getGoodsRecipient().getName());
					iiib.setDebtorName(col.getParentCustomerOrder().getDebitor().getName());
					iiib.setOrderItem(col.getLineNumber());
					iiib.setOrderNumber(col.getParentCustomerOrder().getOrderNumber());
					iiib.setOrderDate(col.getParentCustomerOrder().getOrderDate());
					iiib.setOrderQuantity(col.getOrderquantity().getAmount());

					// double glep = col.getCostPrice().getAmount();
					// iiib.setGlep(glep);
					// iiib.setPfep(glep / 1.1);
					// iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
					// iiib.setCogspfep(iiib.getPfep() *
					// iiib.getInvoiceQuantity());
				}
				if (iiib.getPriceUnit() == 10000) {
					iiib.setPriceUnit(1000);
					iiib.setPrice(iiib.getPrice() / 10);
				}

				BIDateMapping.fillWS1Information(iiib);
				fillfillWS1InformationForInvoice(iiib);
				invoiceItemInfoList.addLast(iiib);
			}
			System.out.println("\n Number of invoice items loaded: " + invoiceItemInfoList.size());
			BIDateMapping.writeInvoiceItemInfoToTxt(invoiceItemInfoList, out1, numberOfInvoiceItems);
			_context.commit();
		}
		penum.destroy();
		// out1.close();
		System.out.println("\n(*) Write txt file  end.");

	}

	private void searchCreditNoteItem(int year, String targetTxt)
			throws TimestampException, PUserException, IOException {
		QueryHelper qh = Query.newQueryHelper();

		QueryPredicate p1 = null;
		QueryPredicate p2 = null;
		if (isTest) {
			p1 = qh.attr(DWCreditNoteLine.DATE).gte().val(new PDate(2015, 11, 1)).predicate();
			p2 = qh.attr(DWCreditNoteLine.DATE).lte().val(new PDate(2015, 11, 4)).predicate();
		} else if (isFirstRound) {
			p1 = qh.attr(DWCreditNoteLine.DATE).gte().val(new PDate(year, 11, 1)).predicate();
			p2 = qh.attr(DWCreditNoteLine.DATE).lte().val(new PDate(year, 11, 31)).predicate();
		} else if (isFinalRound) {
			p1 = qh.attr(DWCreditNoteLine.DATE).gte().val(new PDate(year, 1, 1)).predicate();
			p2 = qh.attr(DWCreditNoteLine.DATE).lte().val(new PDate(year, 1, 28)).predicate();
		} else if (year == 2017 && !isFinalRound) {
			p1 = qh.attr(DWCreditNoteLine.DATE).gte().val(new PDate(year, 0, 1)).predicate();
			p2 = qh.attr(DWCreditNoteLine.DATE).lte().val(new PDate(year, 0, 31)).predicate();
		} else {
			p1 = qh.attr(DWCreditNoteLine.DATE).gte().val(new PDate(year, 0, 1)).predicate();
			p2 = qh.attr(DWCreditNoteLine.DATE).lte().val(new PDate(year, 11, 31)).predicate();
		}

		qh.setClass(DWCreditNoteLine.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(DWCreditNoteLine.CREDITNOTENUMBER);
		qh.addAscendingOrdering(DWCreditNoteLine.LINENUMBER);

		Condition cond = qh.condition(p1.and(p2));

		PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<DWCreditNoteLine> list = new ArrayList<DWCreditNoteLine>(penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
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
				iiib.setDiscount(creditNoteLine.getCcDiscountCreditAmount().getAmount());
				iiib.setCustomerNumber(creditNoteLine.getDWCustomer().getAccountNumber());
				iiib.setName1(creditNoteLine.getDWCustomer().getName1());
				iiib.setGrossValue(creditNoteLine.getCcBrutCreditAmount().getAmount()
						+ creditNoteLine.getCcDiscountCreditAmount().getAmount());
				iiib.setNetValue(creditNoteLine.getCcBrutCreditAmount().getAmount()
						+ creditNoteLine.getCcDiscountCreditAmount().getAmount());
				iiib.setTurnover(iiib.getNetValue());

				if (creditNoteLine.getCcRebateAmount().getAmount() != 0) {
					System.out.println("Rebate information Credite: " + iiib.getInvoiceNumber() + " Line: "
							+ iiib.getInvoiceItem());
					iiib.setTurnover(iiib.getTurnover() - creditNoteLine.getCcRebateAmount().getAmount());
				}
				if (creditNoteLine.getFromComplaitLineType().equals(ComplaintLineType.RETOURELINE)) {
					DWRetoureLine retoureLine = (DWRetoureLine) creditNoteLine;
					iiib.setDocumentType("ZRE");
					iiib.setPrice(retoureLine.getCcPrice().getAmount() / retoureLine.getCcPrice().getUnit());
					iiib.setInvoiceQuantity(retoureLine.getQuantityReturned().getAmount());
					iiib.setProductNumber(retoureLine.getDWProduct().getProductNumber());
					iiib.setRegisterNumber(retoureLine.getDWSalesman().getRegisterNumber());
					Product product = _controller.lookupProduct(retoureLine.getDWProduct().getProductNumber());
					iiib.setPriceUnit(retoureLine.getCcPrice().getUnit());

					DeliveryLine deliveryLine = _controller.lookupDelivery(retoureLine.getDeliveryNumber())
							.lookupDeliveryLine(retoureLine.getDeliveryNumberLine());

					CustomerOrderLine col = deliveryLine.getCustomerOrderLineMain();
					iiib.setOrderItem(col.getLineNumber());
					iiib.setOrderNumber(col.getParentCustomerOrder().getOrderNumber());
					iiib.setOrderDate(col.getParentCustomerOrder().getOrderDate());
					iiib.setOrderQuantity(col.getOrderquantity().getAmount());
					iiib.setGoodsRecipient(col.getParentCustomerOrder().getGoodsRecipient().getId());
					iiib.setDebtor(col.getParentCustomerOrder().getDebitor().getId());
					iiib.setGoodsRecipientName(col.getParentCustomerOrder().getGoodsRecipient().getName());
					iiib.setDebtorName(col.getParentCustomerOrder().getDebitor().getName());
					OwnCompany oc = _controller.getSingletonOwnCompany();
					Weight weight = WeightController.getNewWeight(0,
							oc.getDefaultWeightPerUnit().getWeightMeasureUnit());
					weight = weight.add(product.getOwnCompanyProductSalesUnit().getWeight().getNormalizedAmount()
							.multiply(retoureLine.getQuantityReturned().getAmount()));
					iiib.setWeight(weight.getAmount());
					iiib.setWarehouseNumber(col.getParentCustomerOrder().getWarehouseNumber());

					// double glep = deliveryLine.getCostPrice().getAmount();
					// iiib.setGlep(glep);
					// iiib.setPfep(glep /1.1);
					// iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
					// iiib.setCogspfep(iiib.getPfep() *
					// iiib.getInvoiceQuantity());
					iiib.setCogsglep(retoureLine.getCcCostAmount().getAmount());
					iiib.setCogspfep(iiib.getCogsglep() / 1.1);
				} else if (creditNoteLine.getFromComplaitLineType().equals(ComplaintLineType.PRICEREDUCTIONLINE)) {
					DWPriceLine priceLine = (DWPriceLine) creditNoteLine;
					iiib.setDocumentType("ZG2");
					iiib.setPrice((priceLine.getOldPrice().getAmount() / priceLine.getOldPrice().getUnit()
							- priceLine.getNewPrice().getAmount() / priceLine.getNewPrice().getUnit())
							* priceLine.getNewPrice().getUnit());
					iiib.setInvoiceQuantity(priceLine.getNewQuantity().getAmount());
					iiib.setProductNumber(priceLine.getDWProduct().getProductNumber());
					iiib.setRegisterNumber(priceLine.getDWSalesman().getRegisterNumber());
					Product product = _controller.lookupProduct(priceLine.getDWProduct().getProductNumber());
					iiib.setPriceUnit(priceLine.getNewPrice().getUnit());

					CustomerInvoiceLine cil = getCustomerInvoiceLine(DocType.NORMALINVOICE,
							priceLine.getCustomerInvoiceNumber(), priceLine.getCustomerInvoiceLine());
					CustomerOrderLine col = null;
					if (cil != null) {
						col = cil.getDeliveryLine().getCustomerOrderLineMain();
					} else {
						cil = getCustomerInvoiceLine(DocType.DEBITNOTE, priceLine.getCustomerInvoiceNumber(),
								priceLine.getCustomerInvoiceLine());
						col = cil.getCustomerOrderLineDebitOrder();
					}
					iiib.setOrderItem(col.getLineNumber());
					iiib.setOrderNumber(col.getParentCustomerOrder().getOrderNumber());
					iiib.setOrderDate(col.getParentCustomerOrder().getOrderDate());
					iiib.setOrderQuantity(col.getOrderquantity().getAmount());
					iiib.setGoodsRecipient(col.getParentCustomerOrder().getGoodsRecipient().getId());
					iiib.setDebtor(col.getParentCustomerOrder().getDebitor().getId());
					iiib.setGoodsRecipientName(col.getParentCustomerOrder().getGoodsRecipient().getName());
					iiib.setDebtorName(col.getParentCustomerOrder().getDebitor().getName());
					iiib.setWarehouseNumber(col.getParentCustomerOrder().getWarehouseNumber());

					if (cil.getParentCustomerInvoice().getDocumentType().equals(DocType.NORMALINVOICE)) {
						OwnCompany oc = _controller.getSingletonOwnCompany();
						Weight weight = WeightController.getNewWeight(0,
								oc.getDefaultWeightPerUnit().getWeightMeasureUnit());
						weight = weight.add(product.getOwnCompanyProductSalesUnit().getWeight().getNormalizedAmount()
								.multiply(cil.getDeliveryLine().getDeliveredQuantity().getAmount()));
						iiib.setWeight(weight.getAmount());
						// Compared with CASA,Seems only return line needs to
						// count glep and pfep
						// double glep =
						// cil.getDeliveryLine().getCostPrice().getAmount();
						// iiib.setGlep(glep);
						// iiib.setPfep(glep /1.1);
						// iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
						// iiib.setCogspfep(iiib.getPfep() *
						// iiib.getInvoiceQuantity());
					} else {
						// double glep = col.getCostPrice().getAmount();
						// iiib.setGlep(glep);
						// iiib.setPfep(glep /1.1);
						// iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
						// iiib.setCogspfep(iiib.getPfep() *
						// iiib.getInvoiceQuantity());
					}
				} else {
					// DWManualLine manualLine = (DWManualLine) creditNoteLine;
					if (creditNoteLine.getFromComplaitLineType().equals(ComplaintLineType.FREIGHTLINE)) {
						DWFreightCostLine dwfcl = (DWFreightCostLine) creditNoteLine;
						iiib.setFreightCost(
								dwfcl.getNewFreightCosts().getAmount() - dwfcl.getOldFreightCosts().getAmount());
					}
					iiib.setDocumentType("ZG2");
					iiib.setPrice(creditNoteLine.getCcBrutCreditAmount().getAmount()
							+ creditNoteLine.getCcDiscountCreditAmount().getAmount());
					iiib.setInvoiceQuantity(1);
					// How to get material number for manual credit note line
					// Not sure I understand this one if it should have a
					// product number unless it’s a freight only credit then if
					// we don’t need this info it won’t need to be exported
					if (creditNoteLine.getProductSurrogateKey() != DWConstants.NO_SURROGATEKEY_SPECIFIED) {
						String productNumber = _controller.lookupDWProduct(creditNoteLine.getProductSurrogateKey())
								.getProductNumber();
						iiib.setProductNumber(productNumber);
					} else {
						System.out.println("Warning: ProductNumber not found for CreditNote: " + iiib.getInvoiceNumber()
								+ " Line: " + iiib.getInvoiceItem());
					}

					CustomerAccount customer = _controller.lookupCustomerAccount(iiib.getCustomerNumber());
					if (creditNoteLine.getDWSalesman() != null) {
						iiib.setRegisterNumber(creditNoteLine.getDWSalesman().getRegisterNumber());
					} else {

						if (customer != null) {
							SalesArea sa = customer.getSalesArea(iiib.getInvoiceDate());
							if (sa != null) {
								Salesman salesman = sa.getResponsibleSalesman(iiib.getInvoiceDate());
								if (salesman != null) {
									iiib.setRegisterNumber(salesman.getRegisterNumber());
								}
							}
						}
					}
					
					if (creditNoteLine.getDWDebitor() != null) {
						iiib.setDebtor(creditNoteLine.getDWDebitor().getAccountNumber());
						iiib.setDebtorName(creditNoteLine.getDWDebitor().getName1());
					} else if (customer != null && customer.getDefaultDebitor() != null) {
						iiib.setDebtor(customer.getDefaultDebitor().getId());
						iiib.setDebtorName(customer.getDefaultDebitor().getName());
					} else {
						System.out.println("Error: Debitor not found for CreditNote: " + iiib.getInvoiceNumber()
								+ " Line: " + iiib.getInvoiceItem());
					}

					if (customer != null && customer.getGoodsRecipientdefault() != null) {
						iiib.setGoodsRecipient(customer.getGoodsRecipientdefault().getId());
						iiib.setGoodsRecipientName(customer.getGoodsRecipientdefault().getName());
					} else {
						System.out.println("Error: GoodsRecipient not found for CreditNote: " + iiib.getInvoiceNumber()
								+ " Line: " + iiib.getInvoiceItem());
					}
					iiib.setPriceUnit(1);
					iiib.setWeight(0);

					iiib.setOrderItem(1);
					// How to get orderNumber for manual credit note line.
					// Just use the credit note Number
					iiib.setOrderNumber(creditNoteLine.getCreditNoteNumber());
					iiib.setOrderDate(creditNoteLine.getDate());
					iiib.setOrderQuantity(1);
					iiib.setGlep(0);
					iiib.setPfep(0);
					iiib.setCogsglep(0);
					iiib.setCogspfep(0);
					// 1: How to get warehouseNumber for manual credit note
					// line.
					// If you are not sure just use Cn00 it not that important
					iiib.setWarehouseNumber("1");
				}
				BIDateMapping.fillWS1Information(iiib);
				fillfillWS1InformationForCreditNote(iiib);
				invoiceItemInfoList.addLast(iiib);
			}
			System.out.println("\n Number of credit note items loaded: " + invoiceItemInfoList.size());
			BIDateMapping.writeInvoiceItemInfoToTxt(invoiceItemInfoList, out1, numberOfInvoiceItems);
			_context.commit();
		}
		penum.destroy();
		// out1.close();
		System.out.println("\n(*) Write txt file  end.");

	}

	private void fillfillWS1InformationForInvoice(InvoiceItemInformationBean iiib) {
		iiib.setOrderReason("001");
		iiib.setOrderCategory("1");
		iiib.setDocumentCategory("M");
		iiib.setSalesDocumentType("");
	}

	private void fillfillWS1InformationForCreditNote(InvoiceItemInformationBean iiib) {
		iiib.setOrderReason("001");
		iiib.setOrderCategory("1");
		iiib.setDocumentCategory("O");
		iiib.setSalesDocumentType("");
		if (iiib.getPriceUnit() == 10000) {
			iiib.setPriceUnit(1000);
			iiib.setPrice(iiib.getPrice() / 10);
		}
		iiib.setInvoiceQuantity(iiib.getInvoiceQuantity() * (-1));
		iiib.setNetValue(iiib.getNetValue() * (-1));
		iiib.setTurnover(iiib.getTurnover() * (-1));
		iiib.setGrossValue(iiib.getGrossValue() * (-1));
		iiib.setTaxAmount(iiib.getTaxAmount() * (-1));
		iiib.setCogsglep(iiib.getCogsglep() * (-1));
		iiib.setCogspfep(iiib.getCogspfep() * (-1));
		iiib.setWeight(iiib.getWeight() * (-1));
	}

	private CustomerInvoiceLine getCustomerInvoiceLine(DocType docType, int invoiceNumber, int invoiceLineNumber)
			throws PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p1 = null;
		if (docType.equals(DocType.NORMALINVOICE)) {
			p1 = qh.attr(InvoiceNumber.NUMBER).eq().val(invoiceNumber).predicate();
			qh.setClass(InvoiceNumber.class);
			qh.setDeepSelect(true);
			Condition cond = qh.condition(p1);
			PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
			InvoiceNumber invoiceNum = null;
			if (penum.hasMoreElements()) {
				invoiceNum = ((InvoiceNumber) penum.nextElement());
			}
			penum.destroy();
			if (invoiceNum != null) {
				return invoiceNum.getNormalInvoice().lookupCustomerInvoiceLine(invoiceLineNumber);
			} else {
				return null;
			}
		} else {
			p1 = qh.attr(DebitNoteNumber.NUMBER).eq().val(invoiceNumber).predicate();
			qh.setClass(DebitNoteNumber.class);
			qh.setDeepSelect(true);
			Condition cond = qh.condition(p1);
			PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
			DebitNoteNumber debitNoteNumber = null;
			if (penum.hasMoreElements()) {
				debitNoteNumber = ((DebitNoteNumber) penum.nextElement());
			}
			penum.destroy();
			if (debitNoteNumber != null) {
				return debitNoteNumber.getDebitNote().lookupCustomerInvoiceLine(invoiceLineNumber);
			} else {
				return null;
			}
		}

	}
}
