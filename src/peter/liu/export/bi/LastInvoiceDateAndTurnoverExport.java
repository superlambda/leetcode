package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.derby.impl.store.access.btree.index.B2IUndo;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.models.CustomerAccount;
import com.wuerth.phoenix.Phxbasic.models.DWCustomer;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerInvoiceLine;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerStatisticDay;
import com.wuerth.phoenix.Phxbasic.models.DWDay;
import com.wuerth.phoenix.basic.etnax.common.utilsdir.DoubleUtils;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.IteratorFactory;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryParseException;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;
import com.wuerth.phoenix.internal.bc.server.query.QueryResultEntry;
import com.wuerth.phoenix.util.PDate;

/**
 * LastInvoiceDateAndTurnoverExport
 * 
 * @author pcnsh197
 * 
 */
public class LastInvoiceDateAndTurnoverExport extends BatchRunner {

	private int									NUMBER_OF_BATCH_TO_FECTCH	= 100;

	LinkedList<CustomerInvoiceInformationBean>	customerInvoiceInfoList		= new LinkedList<CustomerInvoiceInformationBean>();

	private String								dataSource					= "../../etc/exportSAP/BI_LastInvocieDateAndTurnoverBefore2012.xlsx";

	private String								target						= "../../var/exportSAP/BI_LastInvocieDateAndTurnoverBefore2012.xlsx";

	private CustomerInvoiceInformationBean		dummyCIIB					= new CustomerInvoiceInformationBean();

	private String								ownCompanyName;

	private Map<Long, Double>					turnoverMap					= new HashMap<Long, Double>(
																					4000);

	private Map<Long, PDate>					lastInvoiceDateMap			= new HashMap<Long, PDate>(
																					4000);

	@Override
	protected void batchMethod() throws TimestampException, PUserException {
		ownCompanyName = _controller.getSingletonOwnCompany().getName();
		getTotalTurnoverBefore2012();
		getLastInvoiceDate();
		searchCustomer();
		checkCustomerInTheExcelNotExist();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new LastInvoiceDateAndTurnoverExport().startBatch(args);
	}
	
	private void checkCustomerInTheExcelNotExist(){
		for(Integer customerNumber:BIDateMapping.customerOrgMap.keySet()){
			if(_controller.lookupCustomerAccount(customerNumber)==null){
				System.out.println("\n Customer in the excel not found: "
						+ customerNumber);
			}
		}
	}

	private void searchCustomer() throws TimestampException, PUserException {
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(DWCustomer.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(CustomerAccount.ACCOUNTNUMBER);
		Condition cond = qh.condition();
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		int batch = 1;
		while (penum.hasMoreElements()) {
			List<DWCustomer> list = new ArrayList<DWCustomer>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			for (DWCustomer customer : list) {
				CustomerInvoiceInformationBean ciib = new CustomerInvoiceInformationBean();
				ciib.setCustomerNumber(customer.getAccountNumber());
				if (lastInvoiceDateMap.containsKey(customer.getSurrogateKey())) {
					ciib.setLastInvoiceDate(lastInvoiceDateMap.get(customer
							.getSurrogateKey()));
				}

				if (turnoverMap.containsKey(customer.getSurrogateKey())) {
					ciib.setTurnoverBefore2011(turnoverMap.get(customer
							.getSurrogateKey()));
				}
				fillWS1Information(ciib);
				customerInvoiceInfoList.addLast(ciib);
				System.out.println("\n Number of customers loaded: "
						+ customerInvoiceInfoList.size());
			}
			_context.commit();
		}

		List<CustomerInvoiceInformationBean> listBeforeFilter = customerInvoiceInfoList;
		customerInvoiceInfoList = new LinkedList<CustomerInvoiceInformationBean>();
		for (CustomerInvoiceInformationBean ciib : listBeforeFilter) {
			if (!ciib.getWs1CustomerNumber().equals("0000999921")) {
				customerInvoiceInfoList.add(ciib);
			} else {
				dummyCIIB.setWs1CustomerNumber(ciib.getWs1CustomerNumber());
				dummyCIIB.setWs1SalesOrganisation(ciib
						.getWs1SalesOrganisation());
				dummyCIIB
						.setTurnoverBefore2011(dummyCIIB
								.getTurnoverBefore2011()
								+ ciib.getTurnoverBefore2011());
			}
		}
		listBeforeFilter = null;
		writeCustomerInfoToExcel(batch);
		penum.destroy();
	}

	private void getLastInvoiceDate() throws QueryParseException,
			PUserException {
		QueryHelper qh = Query.newQueryHelper();
		// TODO qp2 should be removed finally
//		 QueryPredicate qp2 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte()
//		 .val(new PDate(2014, 7, 31)).predicate();
		qh.addResultAttribute(DWCustomerInvoiceLine.CUSTOMERSURROGATEKEY);
		qh.addResultAttributeAsMax(DWCustomerInvoiceLine.INVOICEDATE,
				"lastInvoiceDate");
		qh.setGrouping(DWCustomerInvoiceLine.CUSTOMERSURROGATEKEY);
		qh.setDeepSelect(true);
		qh.setClass(DWCustomerInvoiceLine.class);
		Condition cond = qh.condition();
		IteratorFactory fac = (IteratorFactory) _controller
				.createIteratorFactory();
		PEnumeration penum = fac.getCursor(cond);
		while (penum.hasMoreElements()) {
			QueryResultEntry resultEntry = (QueryResultEntry) penum
					.nextElement();
			Long customerSurrogateKey = (Long) resultEntry.get(0);
			if (resultEntry.get(1) != null) {
				String dateAsString = ((Long) resultEntry.get(1)).toString();
				PDate lastInvoiceDate = new PDate(Integer.valueOf(dateAsString
						.substring(0, 4)), Integer.valueOf(dateAsString
						.substring(4, 6)) - 1, Integer.valueOf(dateAsString
						.substring(6, 8)));
				System.out.println("\n Customer surrogatekey: "
						+ customerSurrogateKey + " last invoice date "
						+ dateAsString + " " + lastInvoiceDate);
				lastInvoiceDateMap.put(customerSurrogateKey, lastInvoiceDate);

			}
		}
		penum.destroy();
	}

	private void getTotalTurnoverBefore2012() throws QueryParseException,
			PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate qp = qh
				.attr(DWCustomerStatisticDay.ASS_DWDAY_REF + DWDay.DATE).lt()
				.val(new PDate(2012, 0, 1)).predicate();
		qh.addResultAttribute(DWCustomerStatisticDay.CUSTOMERSURROGATEKEY);
		qh.addResultAttributeAsSum(DWCustomerStatisticDay.CCTURNOVERAMOUNT,
				"totalTurnoverBefore2011");
		qh.setGrouping(DWCustomerStatisticDay.CUSTOMERSURROGATEKEY);
		qh.setClass(DWCustomerStatisticDay.class);
		IteratorFactory fac = (IteratorFactory) _controller
				.createIteratorFactory();
		PEnumeration penum = fac.getCursor(qh.condition(qp));
		while (penum.hasMoreElements()) {
			QueryResultEntry resultEntry = (QueryResultEntry) penum
					.nextElement();
			Long customerSurrogateKey = (Long) resultEntry.get(0);
			if (resultEntry.get(1) != null) {
				double totalTurnoverBefore2011 = DoubleUtils
						.getRoundedAmount((Double) resultEntry.get(1));
				turnoverMap.put(customerSurrogateKey, totalTurnoverBefore2011);
			} else {
				turnoverMap.put(customerSurrogateKey, 0.0D);
			}
		}
		penum.destroy();
	}

	
	private void fillWS1Information(CustomerInvoiceInformationBean ciib) {
		String customerOrg = BIDateMapping.customerOrgMap.get(ciib
				.getCustomerNumber());
		if (customerOrg != null) {
			String[] customerOrgArray = customerOrg.split(",");
			ciib.setWs1SalesOrganisation(customerOrgArray[1]);
			ciib.setWs1CustomerNumber(customerOrgArray[0]);
		} else {
			if (ownCompanyName.equals("伍尔特（重庆）五金工具有限公司")) {
				ciib.setWs1CustomerNumber("0000999921");
				ciib.setWs1SalesOrganisation("8807");
			} else {
				ciib.setWs1CustomerNumber("0000999921");
				ciib.setWs1SalesOrganisation("8805");
			}
			System.out.println("\n Customer mapping can not find : "
					+ ciib.getCustomerNumber());
		}
	}

	private void writeCustomerInfoToExcel(int batch) {
		System.out.println("\n(!) Read excel file  start.\n");
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					dataSource));
			XSSFSheet sheet = workbook.getSheetAt(0);
			System.out.println("sheet1: " + sheet);
			int startRow = 1;
			for (int i = 0; i < customerInvoiceInfoList.size(); i++) {
				CustomerInvoiceInformationBean ciib = customerInvoiceInfoList
						.get(i);
				XSSFRow row = sheet.getRow(startRow);
				if (row == null) {
					row = sheet.createRow(startRow);
				}

				XSSFCell cellLegacySystemID = row.getCell(0);
				if (cellLegacySystemID == null) {
					cellLegacySystemID = row.createCell(0);
				}
				XSSFCell cellSalesOrganization = row.getCell(1);
				if (cellSalesOrganization == null) {
					cellSalesOrganization = row.createCell(1);
				}

				XSSFCell cellCustomerNumber = row.getCell(2);
				if (cellCustomerNumber == null) {
					cellCustomerNumber = row.createCell(2);
				}

				XSSFCell cellLastInvoiceDate = row.getCell(3);
				if (cellLastInvoiceDate == null) {
					cellLastInvoiceDate = row.createCell(3);
				}

				cellLegacySystemID.setCellValue("/");
				cellSalesOrganization.setCellValue(ciib
						.getWs1SalesOrganisation());
				cellCustomerNumber.setCellValue(ciib.getWs1CustomerNumber());
				if (ciib.getLastInvoiceDate() != null) {
					cellLastInvoiceDate.setCellValue(new XSSFRichTextString(
							BIDateMapping.dateFormat.format(ciib
									.getLastInvoiceDate())));
				}

				startRow++;
				System.out.println("\n Number of customers writed in sheet 1: "
						+ (i + 1));
			}

			// ---------------sheet 2--------------------------------
			XSSFSheet sheet1 = workbook.getSheetAt(1);
			System.out.println("sheet2: " + sheet1);
			startRow = 1;
			for (int i = 0; i < customerInvoiceInfoList.size(); i++) {
				CustomerInvoiceInformationBean ciib = customerInvoiceInfoList
						.get(i);
				writeTurnover(ciib, startRow, sheet1);
				startRow++;
				System.out.println("\n Number of customers writed in sheet 2: "
						+ (i + 1));
			}
			writeTurnover(dummyCIIB, startRow, sheet1);
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

	private void writeTurnover(CustomerInvoiceInformationBean ciib,
			int startRow, XSSFSheet sheet1) {
		XSSFRow row = sheet1.getRow(startRow);
		if (row == null) {
			row = sheet1.createRow(startRow);
		}

		XSSFCell cellLegacySystemID = row.getCell(0);
		if (cellLegacySystemID == null) {
			cellLegacySystemID = row.createCell(0);
		}
		XSSFCell cellSalesOrganization = row.getCell(1);
		if (cellSalesOrganization == null) {
			cellSalesOrganization = row.createCell(1);
		}

		XSSFCell cellCustomerNumber = row.getCell(2);
		if (cellCustomerNumber == null) {
			cellCustomerNumber = row.createCell(2);
		}

		XSSFCell cellTurnoverBefore2011 = row.getCell(3);
		if (cellTurnoverBefore2011 == null) {
			cellTurnoverBefore2011 = row.createCell(3);
		}
		cellLegacySystemID.setCellValue("/");
		cellSalesOrganization.setCellValue(ciib.getWs1SalesOrganisation());
		cellCustomerNumber.setCellValue(ciib.getWs1CustomerNumber());
		cellTurnoverBefore2011.setCellValue(ciib.getTurnoverBefore2011());
	}

}
