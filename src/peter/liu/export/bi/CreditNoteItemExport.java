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

	private int NUMBER_OF_BATCH_TO_FECTCH = 1000;
	LinkedList<InvoiceItemInformationBean> invoiceItemInfoList = new LinkedList<InvoiceItemInformationBean>();

	private String targetTxt2015 = "../../var/exportSAP/MappingCreditNoteItems-2015.csv";
	private String targetTxt2016 = "../../var/exportSAP/MappingCreditNoteItems-2016.csv";
	private String targetTxt2017 = "../../var/exportSAP/MappingCreditNoteItems-2017.csv";
	private PrintWriter out1 = null;
	private int numberOfInvoiceItems = 0;
	private String ownCompanyName;
	private int year = 2015;
	private boolean isAll = false;
	private boolean isFirstRound = false;
	private boolean isFinalRound = false;
	private boolean isTest = false;

	@Override
	protected void batchMethod() throws TimestampException, PUserException, IOException {

		ownCompanyName = _controller.getSingletonOwnCompany().getName();
		if (isTest) {
			searchCreditNoteItem(year, targetTxt2015);
		} else if (isFirstRound) {
			searchCreditNoteItem(2015, targetTxt2015);
		} else if (isFinalRound) {
			searchCreditNoteItem(2017, targetTxt2017);
		} else if (isAll) {
			searchCreditNoteItem(2015, targetTxt2015);
			searchCreditNoteItem(2016, targetTxt2016);
			searchCreditNoteItem(2017, targetTxt2017);
		} else {
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
			if (argstr.equalsIgnoreCase("commit")) {
				willcommit = true;
			} else if (argstr.equalsIgnoreCase("test")) {
				isTest = true;
				targetTxt2015 = "../../var/exportSAP/MappingCreditNoteItems-test-2015.csv";
			} else if (argstr.equalsIgnoreCase("all")) {
				isAll = true;
			} else if (argstr.equalsIgnoreCase("first")) {
				isFirstRound = true;
				targetTxt2015 = "../../var/exportSAP/MappingCreditNoteItems-201512.csv";
			} else if (argstr.equalsIgnoreCase("final")) {
				isFinalRound = true;
				targetTxt2017 = "../../var/exportSAP/MappingCreditNoteItems-201702.csv";
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
		System.out.println("-args test \n\ttest : Output credit note lines in date 20150101-20150131");
		System.out.println("-args first \n\tfirst : Only output credit note lines in date 20151201-20151231");
		System.out.println("-args final \n\tfinal : Only output credit note lines in date 20170201-20170228");
		System.out.println("-args all \n\tall : Output credit note lines between year 2014 and 2016");
		System.out.println("-args 2014 or 2015 or 2016 n\201x : Output invoice lines in 2014 or 2015 or 2016");
		System.out.println("Output file are in path ../../var/exportSAP/ start with MappingCreditNoteItems-");
	}

	private void searchCreditNoteItem(int year, String targetTxt)
			throws TimestampException, PUserException, IOException {
		FileWriter outFile = new FileWriter(targetTxt);
		out1 = new PrintWriter(outFile);
		BIDateMapping.writeInvoiceItemHeaderToCSV(out1);
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
						+ creditNoteLine.getCcDiscountCreditAmount().getAmount()
						+ creditNoteLine.getCcRebateAmount().getAmount());
				iiib.setNetValue(creditNoteLine.getCcBrutCreditAmount().getAmount()
						+ creditNoteLine.getCcDiscountCreditAmount().getAmount());
				if (creditNoteLine.getCcRebateAmount().getAmount() != 0) {
					System.out.println("Rebate information Credite: " + iiib.getInvoiceNumber() + " Line: "
							+ iiib.getInvoiceItem());
				}
				if (creditNoteLine.getFromComplaitLineType().equals(ComplaintLineType.RETOURELINE)) {
					DWRetoureLine retoureLine = (DWRetoureLine) creditNoteLine;
					iiib.setDocumentType("ZRE");
					iiib.setPrice(retoureLine.getCcPrice().getAmount() / retoureLine.getCcPrice().getUnit());
					iiib.setInvoiceQuantity(retoureLine.getQuantityReturned().getAmount());
					
					String productNumber = retoureLine.getDWProduct().getProductNumber();
					String eeeeProductNumber = BIDateMapping.productMap.get(productNumber);
					if (eeeeProductNumber != null && !eeeeProductNumber.trim().equals("")) {
						iiib.setArticleNumber(eeeeProductNumber);
					} else {
						iiib.setArticleNumber(BIDateMapping.dummyMaterialNumber);
					}

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
					OwnCompany oc = _controller.getSingletonOwnCompany();
					Weight weight = WeightController.getNewWeight(0,
							oc.getDefaultWeightPerUnit().getWeightMeasureUnit());
					weight = weight.add(product.getOwnCompanyProductSalesUnit().getWeight().getNormalizedAmount()
							.multiply(retoureLine.getQuantityReturned().getAmount()));
					iiib.setWeight(weight.getAmount());
					iiib.setWarehouseNumber(col.getParentCustomerOrder().getWarehouseNumber());

					double glep = deliveryLine.getCostPrice().getAmount();
					iiib.setGlep(glep);
					iiib.setPfep(glep /1.1);
			
					iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
					iiib.setCogspfep(iiib.getPfep() * iiib.getInvoiceQuantity());
				} else if (creditNoteLine.getFromComplaitLineType().equals(ComplaintLineType.PRICEREDUCTIONLINE)) {
					DWPriceLine priceLine = (DWPriceLine) creditNoteLine;
					iiib.setDocumentType("ZG2");
					iiib.setPrice((priceLine.getOldPrice().getAmount() / priceLine.getOldPrice().getUnit()
							- priceLine.getNewPrice().getAmount() / priceLine.getNewPrice().getUnit())
							* priceLine.getNewPrice().getUnit());
					iiib.setInvoiceQuantity(priceLine.getNewQuantity().getAmount());
					String productNumber = priceLine.getDWProduct().getProductNumber();
					String eeeeProductNumber = BIDateMapping.productMap.get(productNumber);
					if (eeeeProductNumber != null && !eeeeProductNumber.trim().equals("")) {
						iiib.setArticleNumber(eeeeProductNumber);
					} else {
						iiib.setArticleNumber(BIDateMapping.dummyMaterialNumber);
					}

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
					iiib.setWarehouseNumber(col.getParentCustomerOrder().getWarehouseNumber());
					
					if (cil.getParentCustomerInvoice().getDocumentType().equals(DocType.NORMALINVOICE)) {
						OwnCompany oc = _controller.getSingletonOwnCompany();
						Weight weight = WeightController.getNewWeight(0,
								oc.getDefaultWeightPerUnit().getWeightMeasureUnit());
						weight = weight.add(product.getOwnCompanyProductSalesUnit().getWeight().getNormalizedAmount()
								.multiply(cil.getDeliveryLine().getDeliveredQuantity().getAmount()));
						iiib.setWeight(weight.getAmount());
						
						double glep = cil.getDeliveryLine().getCostPrice().getAmount();
						iiib.setGlep(glep);
						iiib.setPfep(glep /1.1);
						iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
						iiib.setCogspfep(iiib.getPfep() * iiib.getInvoiceQuantity());
					} else {
						double glep = col.getCostPrice().getAmount();
						iiib.setGlep(glep);
						iiib.setPfep(glep /1.1);
						iiib.setCogsglep(glep * iiib.getInvoiceQuantity());
						iiib.setCogspfep(iiib.getPfep() * iiib.getInvoiceQuantity());
					}
				} else {
					// DWManualLine manualLine = (DWManualLine) creditNoteLine;
					if(creditNoteLine.getFromComplaitLineType().equals(ComplaintLineType.FREIGHTLINE)){
						DWFreightCostLine dwfcl = (DWFreightCostLine)creditNoteLine;
						iiib.setFreightCost(dwfcl.getNewFreightCosts().getAmount()-dwfcl.getOldFreightCosts().getAmount());
					}
					iiib.setDocumentType("ZG2");
					iiib.setPrice(creditNoteLine.getCcBrutCreditAmount().getAmount()
							+ creditNoteLine.getCcDiscountCreditAmount().getAmount());
					iiib.setInvoiceQuantity(1);
					iiib.setArticleNumber(BIDateMapping.dummyMaterialNumber);
					if (creditNoteLine.getDWSalesman() != null) {
						iiib.setRegisterNumber(creditNoteLine.getDWSalesman().getRegisterNumber());
					} else {
						CustomerAccount customer = _controller.lookupCustomerAccount(iiib.getCustomerNumber());
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

					iiib.setPriceUnit(1);
					iiib.setWeight(0);

					iiib.setOrderItem(1);
					//TODO
					iiib.setOrderNumber(999990000);
					iiib.setOrderDate(creditNoteLine.getDate());
					iiib.setOrderQuantity(1);
					iiib.setGlep(0);
					iiib.setPfep(0);
					iiib.setCogsglep(0);
					iiib.setCogspfep(0);
					// TODO How to get warehouseNumber for manual credit note
					// line.
					iiib.setWarehouseNumber("1");
				}
				BIDateMapping.fillWS1Information(iiib);
				fillfillWS1InformationForCreditNote(iiib);
				invoiceItemInfoList.addLast(iiib);
			}
			System.out.println("\n Number of credit note items loaded: " + invoiceItemInfoList.size());
			BIDateMapping.writeInvoiceItemInfoToTxt(invoiceItemInfoList, out1,numberOfInvoiceItems);
			_context.commit();
		}
		penum.destroy();
		out1.close();
		System.out.println("\n(*) Write txt file  end.");

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
		iiib.setTurnover(iiib.getNetValue());
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
