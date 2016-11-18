package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.wuerth.phoenix.Phxbasic.enums.ComplaintLineType;
import com.wuerth.phoenix.Phxbasic.models.DWCreditNote;
import com.wuerth.phoenix.Phxbasic.models.DWCreditNoteLine;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerInvoice;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerInvoiceLine;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;
import com.wuerth.phoenix.util.PDate;

/**
 * InvoiceHeaderExport
 * 
 * @author pcnsh197
 * 
 */
public class InvoiceHeaderExport extends BatchRunner {

	private int									NUMBER_OF_BATCH_TO_FECTCH	= 1000;

	LinkedList<InvoiceHeaderInformationBean>	invoiceHeaderInfoList		= new LinkedList<InvoiceHeaderInformationBean>();

	private static final DateFormat				dateFormat					= new SimpleDateFormat(
																					"yyyyMMdd");

	private String								targetTxt					= "../../var/exportSAP/MappingInvoiceHeader.txt";

	private PrintWriter							out1						= null;

	private int									numberOfInvoicesWritten		= 0;

	private String								csvSeperator				= ",";

	private String								ownCompanyName;

	@Override
	protected void batchMethod() throws TimestampException, PUserException,
			IOException {
		searchInvoice();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new InvoiceHeaderExport().startBatch(args);
	}

	private void searchInvoice() throws TimestampException, PUserException,
			IOException {
		FileWriter outFile = new FileWriter(targetTxt);
		out1 = new PrintWriter(outFile);
		System.out.println("\n(!) Write txt file  start.\n");
		ownCompanyName = _controller.getSingletonOwnCompany().getName();
		searchNormalInvoice();
		searchCreditNote();
		out1.close();
		System.out.println("\n(*) Write txt file  end.");
	}

	private void searchNormalInvoice() throws TimestampException,
			PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p = qh.attr(DWCustomerInvoice.INVOICEDATE).gte()
				.val(new PDate(2012, 0, 1)).predicate();
		QueryPredicate p1 = qh.attr(DWCustomerInvoice.INVOICEDATE).lte()
				.val(new PDate(2014, 8, 30)).predicate();
		qh.setClass(DWCustomerInvoice.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(DWCustomerInvoice.INVOICENUMBER);
		Condition cond = qh.condition(p.and(p1));
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);

		while (penum.hasMoreElements()) {
			List<DWCustomerInvoice> list = new ArrayList<DWCustomerInvoice>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			invoiceHeaderInfoList = new LinkedList<InvoiceHeaderInformationBean>();
			for (DWCustomerInvoice invoice : list) {
				InvoiceHeaderInformationBean ihib = new InvoiceHeaderInformationBean();
				// ihib.setDocumentType("ZF1");
				ihib.setInvoiceNumber(invoice.getInvoiceNumber());
				ihib.setInvoiceDate(invoice.getInvoiceDate());
				List<DWCustomerInvoiceLine> invoiceLineList = new ArrayList<DWCustomerInvoiceLine>(
						invoice.getAllDWCustomerInvoiceLine());
				if (invoiceLineList.size() > 0) {
					DWCustomerInvoiceLine line = invoiceLineList.get(0);
					ihib.setRegisterNumber(line.getDWSalesman()
							.getRegisterNumber());
					ihib.setCustomerNumber(line.getDWCustomer()
							.getAccountNumber());
					
				}
				fillWS1Information(ihib);
				invoiceHeaderInfoList.addLast(ihib);
			}
			writeInvoiceInfoToTxt();
			_context.commit();
		}

		penum.destroy();
	}

	
	
	private void fillWS1Information(InvoiceHeaderInformationBean ihib) {
		String customerOrg = BIDateMapping.customerOrgMap.get(ihib
				.getCustomerNumber());
		String defaultWS1RegisterNumber=null;
		if (customerOrg != null) {
			String[] customerOrgArray = customerOrg.split(",");
			ihib.setWs1SalesOrganisation(customerOrgArray[1]);
			ihib.setWs1CustomerNumber(customerOrgArray[0]);
			defaultWS1RegisterNumber=customerOrgArray[2];
		} else {
			if (ownCompanyName.equals("伍尔特（重庆）五金工具有限公司")) {
				ihib.setWs1CustomerNumber("0000999921");
				ihib.setWs1SalesOrganisation("8807");
			} else {
				ihib.setWs1CustomerNumber("0000999921");
				ihib.setWs1SalesOrganisation("8805");
			}
			System.out.println("\n Customer mapping can not find : "
					+ ihib.getCustomerNumber());
		}
		
		String ws1RegisterNumber = BIDateMapping.getWS1RegisterNumber(
				ownCompanyName, ihib.getRegisterNumber());
		if (BIDateMapping.isWS1RUDUmmyRU(ws1RegisterNumber)
				&& defaultWS1RegisterNumber != null) {
			ws1RegisterNumber = defaultWS1RegisterNumber;
		}
		if (ihib.getWs1CustomerNumber().equals("0000999921")) {
			ws1RegisterNumber = BIDateMapping.getDummyWS1RegisterNumber();
		}
		ihib.setWs1RegisterNumber(ws1RegisterNumber);
	}

	private void searchCreditNote() throws TimestampException, PUserException {
		QueryHelper qh = Query.newQueryHelper();
		
		QueryPredicate p = qh.attr(DWCreditNote.CREDITNOTEDATE).gte()
				.val(new PDate(2012, 0, 1)).predicate();
		QueryPredicate p1 = qh.attr(DWCreditNote.CREDITNOTEDATE).lte()
				.val(new PDate(2014, 8, 30)).predicate();
		qh.setClass(DWCreditNote.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(DWCreditNote.CREDITNOTENUMBER);
		Condition cond = qh.condition(p.and(p1));
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<DWCreditNote> list = new ArrayList<DWCreditNote>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			invoiceHeaderInfoList = new LinkedList<InvoiceHeaderInformationBean>();
			for (DWCreditNote creditNote : list) {
				InvoiceHeaderInformationBean ihib = new InvoiceHeaderInformationBean();
				ihib.setCredit(true);
				ihib.setInvoiceNumber(creditNote.getCreditNoteNumber());
				ihib.setInvoiceDate(creditNote.getCreditNoteDate());
				List<DWCreditNoteLine> creditNoteLineList = new ArrayList<DWCreditNoteLine>(
						creditNote.getAllDWCreditNoteLine());
				boolean hasReturnLine = false;
				for (DWCreditNoteLine line : creditNoteLineList) {
					if (line.getDWSalesman() != null) {
						ihib.setRegisterNumber(line.getDWSalesman()
								.getRegisterNumber());
					}
					if (line.getDWCustomer() != null) {
						ihib.setCustomerNumber(line.getDWCustomer()
								.getAccountNumber());
					}
					if (line.getFromComplaitLineType().equals(
							ComplaintLineType.RETOURELINE)) {
						hasReturnLine = true;
					}
				}
				if (hasReturnLine) {
					ihib.setDocumentType("ZRE");
				}
				if (ihib.getDocumentType() == null) {
					ihib.setDocumentType("ZG2");
				}
				fillWS1Information(ihib);
				
				invoiceHeaderInfoList.addLast(ihib);
			}
			writeInvoiceInfoToTxt();
			_context.commit();
		}
		penum.destroy();
	}

	private void writeInvoiceInfoToTxt() {
		for (int i = 0; i < invoiceHeaderInfoList.size(); i++) {
			InvoiceHeaderInformationBean ihib = invoiceHeaderInfoList.get(i);
			StringBuffer sb = new StringBuffer();
			sb.append(ihib.getWs1SalesOrganisation()).append(csvSeperator);
			if (ihib.isCredit()) {
				sb.append(ihib.getDocumentType()).append(csvSeperator);
			} else {
				sb.append("ZF1").append(csvSeperator);
				
			}
			sb.append(
					BIDateMapping.fillWS1OrderOrInvoiceNumber(ihib
							.getInvoiceNumber())).append(csvSeperator);

			if (ihib.getInvoiceDate() != null) {
				sb.append(dateFormat.format(ihib.getInvoiceDate())).append(
						csvSeperator);
			} else {
				sb.append(" ").append(csvSeperator);
			}
			sb.append(ihib.getWs1CustomerNumber()).append(csvSeperator);
			sb.append(ihib.getWs1RegisterNumber()).append(csvSeperator);

			out1.println(sb.toString());
			numberOfInvoicesWritten++;
			System.out.println("\n Number of invoices writed in txt 1: "
					+ numberOfInvoicesWritten);
		}
	}

}
