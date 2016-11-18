package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.wuerth.phoenix.Phxbasic.enums.CustomerOrderStatus;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerOrder;
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
 * OrderHeaderExport
 * 
 * @author pcnsh197
 * 
 */
public class OrderHeaderExport extends BatchRunner {

	private int								NUMBER_OF_BATCH_TO_FECTCH	= 1000;

	LinkedList<OrderHeaderInformationBean>	orderHeaderInfoList			= new LinkedList<OrderHeaderInformationBean>();

	private String							targetTxt					= "../../var/exportSAP/MappingOrderHeader.txt";

	private PrintWriter						out1						= null;

	private int								numberOfOrdersWritten		= 0;

	private String							csvSeperator				= ",";

	private String							ownCompanyName;

	@Override
	protected void batchMethod() throws TimestampException, PUserException,
			IOException {
		exportOrder();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new OrderHeaderExport().startBatch(args);
	}

	private void exportOrder() throws TimestampException, PUserException,
			IOException {
		FileWriter outFile = new FileWriter(targetTxt);
		out1 = new PrintWriter(outFile);
		System.out.println("\n(!) Write txt file  start.\n");
		searchCustomerOrder();
		out1.close();
		System.out.println("\n(*) Write txt file  end.");
	}

	private void searchCustomerOrder() throws TimestampException,
			PUserException {
		ownCompanyName = _controller.getSingletonOwnCompany().getName();
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p1 = qh.attr(DWCustomerOrder.CUSTOMERORDERDATE).gte()
				.val(new PDate(2012, 0, 1)).predicate();
		QueryPredicate p2 = qh.attr(DWCustomerOrder.ORDERSTATUS).ne()
				.val(CustomerOrderStatus.DELETED).predicate();

		QueryPredicate p3 = qh.attr(DWCustomerOrder.CUSTOMERORDERDATE).lte()
				.val(new PDate(2014, 8, 30)).predicate();
		
		qh.setClass(DWCustomerOrder.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(DWCustomerOrder.CUSTOMERORDERNUMBER);
		Condition cond = qh.condition(p1.and(p2).and(p3));
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<DWCustomerOrder> list = new ArrayList<DWCustomerOrder>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			orderHeaderInfoList = new LinkedList<OrderHeaderInformationBean>();
			for (DWCustomerOrder order : list) {
				OrderHeaderInformationBean ohib = new OrderHeaderInformationBean();
				ohib.setOrderNumber(order.getCustomerOrderNumber());
				ohib.setOrderDate(order.getCustomerOrderDate());

				ohib.setRegisterNumber(order.getDWSalesman()
						.getRegisterNumber());
				ohib.setCustomerNumber(order.getDWCustomer().getAccountNumber());
				fillWS1Information(ohib);
				orderHeaderInfoList.addLast(ohib);
			}
			writeOrderInfoToTxt();
			_context.commit();
//			System.out.println("\n Number of orders loaded: "+ orderHeaderInfoList.size());
		}
		penum.destroy();
	}
	

	private void fillWS1Information(OrderHeaderInformationBean ohib) {
		String customerOrg = BIDateMapping.customerOrgMap.get(ohib
				.getCustomerNumber());
		String defaultWS1RegisterNumber=null;
		if (customerOrg != null) {
			String[] customerOrgArray = customerOrg.split(",");
			ohib.setWs1SalesOrganisation(customerOrgArray[1]);
			ohib.setWs1CustomerNumber(customerOrgArray[0]);
			defaultWS1RegisterNumber=customerOrgArray[2];
		} else {
			if (ownCompanyName.equals("伍尔特（重庆）五金工具有限公司")) {
				ohib.setWs1CustomerNumber("0000999921");
				ohib.setWs1SalesOrganisation("8807");
			} else {
				ohib.setWs1CustomerNumber("0000999921");
				ohib.setWs1SalesOrganisation("8805");
			}
			System.out.println("\n Customer mapping can not find : "
					+ ohib.getCustomerNumber());
		}
		
		String ws1RegisterNumber = BIDateMapping.getWS1RegisterNumber(
				ownCompanyName, ohib.getRegisterNumber());
		if (BIDateMapping.isWS1RUDUmmyRU(ws1RegisterNumber)
				&& defaultWS1RegisterNumber != null) {
			ws1RegisterNumber = defaultWS1RegisterNumber;
		}
		if (ohib.getWs1CustomerNumber().equals("0000999921")) {
			ws1RegisterNumber = BIDateMapping.getDummyWS1RegisterNumber();
		}
		ohib.setWs1RegisterNumber(ws1RegisterNumber);
	}

	private void writeOrderInfoToTxt() {
		for (int i = 0; i < orderHeaderInfoList.size(); i++) {
			OrderHeaderInformationBean ohib = orderHeaderInfoList.get(i);
			StringBuffer sb = new StringBuffer();
			sb.append(ohib.getWs1SalesOrganisation()).append(csvSeperator);
			sb.append("ZTA").append(csvSeperator);
			sb.append(
					BIDateMapping.fillWS1OrderOrInvoiceNumber(ohib
							.getOrderNumber())).append(csvSeperator);
			sb.append(BIDateMapping.dateFormat.format(ohib.getOrderDate()))
					.append(csvSeperator);
			sb.append(ohib.getWs1CustomerNumber()).append(csvSeperator);
			sb.append(ohib.getWs1RegisterNumber()).append(csvSeperator);
			out1.println(sb.toString());
			numberOfOrdersWritten++;
			System.out.println("\n Number of orders writed in txt 1: "
					+ numberOfOrdersWritten);
		}
	}
}
