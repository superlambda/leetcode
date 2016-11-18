package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.util.ArrayList;
import java.util.List;

import com.wuerth.phoenix.Phxbasic.models.BusinessRole;
import com.wuerth.phoenix.Phxbasic.models.Company;
import com.wuerth.phoenix.Phxbasic.models.CustomerAccount;
import com.wuerth.phoenix.Phxbasic.models.CustomerInvoice;
import com.wuerth.phoenix.Phxbasic.models.CustomerOrder;
import com.wuerth.phoenix.Phxbasic.models.DebitNote;
import com.wuerth.phoenix.Phxbasic.models.Debitor;
import com.wuerth.phoenix.Phxbasic.models.NormalInvoice;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;

/**
 * Get Debtor pays for more than one customers.
 * 
 * @author pcnsh197
 * 
 */
public class PayerInvoiceRoleExport extends BatchRunner {

	private int	NUMBER_OF_BATCH_TO_FECTCH	= 100;
	@Override
	protected void batchMethod() throws TimestampException, PUserException {
		searchCompany();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PayerInvoiceRoleExport().startBatch(args);
	}

	private void searchCompany() throws TimestampException, PUserException {
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(Company.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(Company.ID);
		Condition cond = qh.condition();
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		int batch = 1;
		while (penum.hasMoreElements()) {
			List<Company> list = new ArrayList<Company>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			for (Company company : list) {
				List<BusinessRole> businessRoleList = new ArrayList(
						company.getAllChildBusinessRole());
				int customerCount = 0;

				List<CustomerAccount> caList = new ArrayList<CustomerAccount>();
				List<Debitor> debitorList = new ArrayList<Debitor>();
				int debitorId = 0;
				for (BusinessRole businessRole : businessRoleList) {
					if (businessRole instanceof CustomerAccount) {
						customerCount++;
					}
					if (businessRole instanceof Debitor) {
						Debitor debitor = (Debitor) businessRole;
						debitorList.add(debitor);
					}
				}

				if (customerCount > 1) {
					for (Debitor debitor : debitorList) {
						caList = getAllCustomerPayedByTheDebitor(debitor);
						if (caList.size() > 1) {
							System.out
									.println("\n Company has more then one customers: "
											+ company.getId()
											+ " debitorId "
											+ debitorId);
						}

					}
				}
			}
		}
		// writeCustomerInfoToExcel(batch);
		penum.destroy();
	}

	private List<CustomerAccount> getAllCustomerPayedByTheDebitor(
			Debitor debitor) throws PUserException {

		List<CustomerAccount> caList = new ArrayList<CustomerAccount>();
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(CustomerInvoice.class);
		qh.setDeepSelect(true);

		QueryPredicate p = qh.attr(CustomerInvoice.ASS_DEBITOR_OPEN).eq()
				.val(debitor).predicate();
		QueryPredicate p1 = qh.attr(CustomerInvoice.ASS_DEBITOR_TOBOOKTO).eq()
				.val(debitor).predicate();
		Condition cond = qh.condition(p.or(p1));
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);

		while (penum.hasMoreElements()) {
			List<CustomerInvoice> list = new ArrayList<CustomerInvoice>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			for (CustomerInvoice customerInvoice : list) {
				if (customerInvoice instanceof NormalInvoice) {
					NormalInvoice invoice = (NormalInvoice) customerInvoice;
					List<CustomerOrder> allCustomerOrdersource = new ArrayList<CustomerOrder>(
							invoice.getAllCustomerOrdersource());
					for (CustomerOrder customerOrder : allCustomerOrdersource) {
						if (!caList
								.contains(customerOrder.getCustomerAccount())) {
							caList.add(customerOrder.getCustomerAccount());
						}
					}

				}
				if (customerInvoice instanceof DebitNote) {
					List<CustomerOrder> allCustomerOrderDebitOrder = new ArrayList<CustomerOrder>(
							((DebitNote) customerInvoice)
									.getAllCustomerOrderDebitOrder());
					for (CustomerOrder customerOrder : allCustomerOrderDebitOrder) {
						if (!caList
								.contains(customerOrder.getCustomerAccount())) {
							caList.add(customerOrder.getCustomerAccount());
						}
					}

				}
			}
		}
		penum.destroy();
		return caList;
	}
}
