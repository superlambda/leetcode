package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.wuerth.phoenix.Phxbasic.enums.CustomerAccountStatus;
import com.wuerth.phoenix.Phxbasic.models.CompanyPeriod;
import com.wuerth.phoenix.Phxbasic.models.CompanyYear;
import com.wuerth.phoenix.Phxbasic.models.CustomerAccount;
import com.wuerth.phoenix.Phxbasic.models.CustomerTurnoverRange;
import com.wuerth.phoenix.Phxbasic.models.DWCustomer;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerStatisticDay;
import com.wuerth.phoenix.Phxbasic.models.DWDay;
import com.wuerth.phoenix.Phxbasic.models.PhxbasicController;
import com.wuerth.phoenix.Phxbasic.models.SalesArea;
import com.wuerth.phoenix.Phxbasic.models.Salesman;
import com.wuerth.phoenix.Phxbasic.models.WarehouseProductAllowance;
import com.wuerth.phoenix.basic.etnax.common.utilsdir.CalendarUtils;
import com.wuerth.phoenix.basic.etnax.common.utilsdir.DoubleUtils;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.HookException;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;
import com.wuerth.phoenix.internal.bc.server.query.QueryResultEntry;
import com.wuerth.phoenix.util.PDate;

/**
 * CASAExport Receive year as parameter, only support four years 2010-2014 
 * 
 * @author pcnsh197
 * 
 */
public class CASAExport extends BatchRunner {

	private int								NUMBER_OF_BATCH_TO_FECTCH	= 1000;

	LinkedList<CASABean>					casaList					= new LinkedList<CASABean>();
	LinkedList<CASABean>                    dummyCustomerCasaList       = new LinkedList<CASABean>();
	

	private String							targetTxt					= "../../var/exportSAP/CASA.txt";

	private String							targetTxt2012					= "../../var/exportSAP/CASA-2012.txt";

	private String							targetTxt2013					= "../../var/exportSAP/CASA-2013.txt";

	private String							targetTxt2014					= "../../var/exportSAP/CASA-2014.txt";

	private PrintWriter						out1						= null;

	private int								numberOfCASAsWritten		= 0;

	private String							csvSeperator				= ",";

	private Map<Integer, CASAPeriodBean>	periodMap					= new HashMap<Integer, CASAPeriodBean>();

	private String							ownCompanyName;

	private int								year;

	private double							turnoverOfActiveCustomer;

	private DecimalFormat					df							= new DecimalFormat();

	
	
	private Map<Long,double[]> turnover_glep_cm_map = new HashMap<Long,double[]>();
	private Map<Long,double[]> turnover_glep_r12cy_map = new HashMap<Long,double[]>();
	
	private Map<Long,double[]> turnover_glep_r12ly_map = new HashMap<Long,double[]>();
	
	private Map<Long,double[]> turnover_glep_bf_cm_map = new HashMap<Long,double[]>();
	
	private Map<Long,double[]> turnover_glep_bf_r12cy_map = new HashMap<Long,double[]>();
	
	private Map<Long,double[]> turnover_glep_12pe_bf_cm_map = new HashMap<Long,double[]>();
	private Map<Long,double[]> turnover_glep_bf_12pe_map = new HashMap<Long,double[]>();
	
	
	private List<CustomerTurnoverRange> turnoverRangeList=null; 
	@Override
	protected void batchMethod() throws TimestampException, PUserException,
			IOException {
		
		switch (year) {
			case 2012:
				int[] periodArray2012 = { 201201, 201202, 201203, 201204,
						201205, 201206, 201207, 201208, 201209, 201210, 201211,
						201212 };
				searchInvoice(periodArray2012);
				break;
			case 2013:
				int[] periodArray2013 = { 201301, 201302, 201303, 201304,
						201305, 201306, 201307, 201308, 201309, 201310, 201311,
						201312};
				searchInvoice(periodArray2013);
				break;
			case 2014:
				int[] periodArray2014 = { 201401, 201402, 201403, 201404,
						201405, 201406, 201407, 201408, 201409};
				searchInvoice(periodArray2014);
				break;
			default:
				System.out.println("\nThe year parameter specified is not correct: "+year);
		}
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CASAExport().startBatch(args);
	}
	
	protected void processargs(String[] args) {
		for (String argstr : args) {
			if(argstr.equalsIgnoreCase("commit")){
				willcommit = true;
			}else{
				year=Integer.parseInt(argstr);
			}
			
		}
	}

	private void searchInvoice(int[] periodArray) throws TimestampException, PUserException,
			IOException {
		FileWriter outFile = null;
		if (year == 2012) {
			outFile = new FileWriter(targetTxt2012);
		} else if (year == 2013) {
			outFile = new FileWriter(targetTxt2013);
		} else if (year == 2014) {
			outFile = new FileWriter(targetTxt2014);
		} else {
			outFile = new FileWriter(targetTxt);
		}
		
		out1 = new PrintWriter(outFile);
		df.setMaximumFractionDigits(2);
		df.setGroupingUsed(false);
		initializeDate(periodArray);
		System.out.println("\n(!) Write txt file  start.\n");
		searchDWCustomer(periodArray);
		
		out1.close();
		System.out.println("\n(*) Write txt file  end.");
	}
	
	
	
	private void searchDWCustomer(int[] periodArray) throws TimestampException, PUserException {
		ownCompanyName = _controller.getSingletonOwnCompany().getName();
		turnoverOfActiveCustomer = _controller
				.getSingletonStatisticParameters().getAmountForActiveCustomer()
				.getAmount();
		System.out.println("\n(*) turnoverOfActiveCustomer "+turnoverOfActiveCustomer);
		fillCustomerStatisticByPeriod(periodArray);
		writeCASAInfoToTxt(dummyCustomerCasaList);
	}

	/**
	 * First store the period value in a map
	 * 
	 * @throws HookException
	 * @throws PUserException
	 */
	private void initializeDate(int[] periodArray) throws HookException, PUserException {
		for (int i = 0; i < periodArray.length; i++) {
			int yearAndPeriod = periodArray[i];
			short year = (short) (yearAndPeriod / 100);
			int period = yearAndPeriod % 100;
			CompanyYear currentYear = _controller.lookupCompanyYear(year);
			CompanyPeriod currentPeriod = currentYear
					.lookupCompanyPeriod(period);

			PDate currentPeriodFromDate = currentPeriod.getFrom();
			PDate currentPeriodToDate = currentPeriod.getTo();
			CompanyYear previousYear = _controller
					.lookupCompanyYear((short) (year - 1));
			CompanyPeriod currentPeriodPreviousYear = previousYear
					.lookupCompanyPeriod(period);
			PDate currentPeriodPreviousYearFromDate = currentPeriodPreviousYear
					.getFrom();
			PDate currentPeriodPreviousYearToDate = currentPeriodPreviousYear
					.getTo();

			// Turnover 12 periods rolling
			
			CompanyPeriod onPeriodAgo = CalendarUtils.addPeriod(
					_controller, currentPeriod, -1);
			PDate onPeriodAgoToDate=onPeriodAgo.getTo();
			
			CompanyPeriod elevenPeriodAgo = CalendarUtils.addPeriod(
					_controller, currentPeriod, -11);
			PDate elevenPeriodAgoFromDate = elevenPeriodAgo.getFrom();

			

			// Turnover 12 periods rolling before current period
			CompanyPeriod twelvePeriodAgo = CalendarUtils.addPeriod(
					_controller, currentPeriod, -12);
			PDate twelvePeriodAgoToDate = twelvePeriodAgo.getTo();
			
			
			CompanyPeriod thirteenPeriodAgo = CalendarUtils.addPeriod(
					_controller, currentPeriod, -13);
			PDate thirteenPeriodAgoToDate = thirteenPeriodAgo.getTo();
			
			CompanyPeriod twentythreePeriodAgo = CalendarUtils.addPeriod(
					_controller, currentPeriod, -23);
			PDate twentythreePeriodAgoFromDate = twentythreePeriodAgo.getFrom();
			

			CASAPeriodBean casaPeriodBean = new CASAPeriodBean();
			casaPeriodBean.setYearAndPeriod(yearAndPeriod);
			casaPeriodBean.setCurrentPeriodFromDate(currentPeriodFromDate);
			casaPeriodBean.setCurrentPeriodToDate(currentPeriodToDate);
			casaPeriodBean.setOnPeriodAgoToDate(onPeriodAgoToDate);
			casaPeriodBean
					.setCurrentPeriodPreviousYearFromDate(currentPeriodPreviousYearFromDate);
			casaPeriodBean
					.setCurrentPeriodPreviousYearToDate(currentPeriodPreviousYearToDate);
			casaPeriodBean.setElevenPeriodAgoFromDate(elevenPeriodAgoFromDate);
			casaPeriodBean.setTwelvePeriodAgoToDate(twelvePeriodAgoToDate);
			casaPeriodBean.setThirteenPeriodAgoToDate(thirteenPeriodAgoToDate);
			casaPeriodBean
					.setTwentythreePeriodAgoFromDate(twentythreePeriodAgoFromDate);

			System.out.println("currentPeriodFromDate " + currentPeriodFromDate
					+ " currentPeriodToDate " + currentPeriodToDate
					+ " onPeriodAgoToDate  " + onPeriodAgoToDate
					+ " elevenPeriodAgoFromDate " + elevenPeriodAgoFromDate
					+ " twelvePeriodAgoToDate " + twelvePeriodAgoToDate
					+ " thirteenPeriodAgoToDate " + thirteenPeriodAgoToDate
					+ " twentythreePeriodAgoFromDate "
					+ twentythreePeriodAgoFromDate);
		
			
			periodMap.put(yearAndPeriod, casaPeriodBean);
		}
		turnoverRangeList = new ArrayList<CustomerTurnoverRange>(_controller
				.getSingletonStatisticParameters()
				.getAllChildCustomerTurnoverRange());
	}
	
	private void fillCustomerStatisticByPeriod(int[] periodArray)
			throws HookException, PUserException {
		
		double turnoverCM=0.0D;
		for (int i = 0; i < periodArray.length; i++) {
			CASAPeriodBean casaPeriodBean = periodMap.get(periodArray[i]);
			turnover_glep_cm_map = new HashMap<Long, double[]>();
			turnover_glep_r12cy_map = new HashMap<Long, double[]>();
			turnover_glep_r12ly_map = new HashMap<Long, double[]>();
			turnover_glep_bf_cm_map = new HashMap<Long, double[]>();
			turnover_glep_bf_r12cy_map = new HashMap<Long, double[]>();
			turnover_glep_12pe_bf_cm_map = new HashMap<Long, double[]>();
			turnover_glep_bf_12pe_map = new HashMap<Long, double[]>();

			turnover_glep_cm_map=getCustomerTurnover(turnover_glep_cm_map,
					casaPeriodBean.getCurrentPeriodFromDate(),
					casaPeriodBean.getCurrentPeriodToDate());
			Iterator<double[]> cmArrayIterator = turnover_glep_cm_map.values()
					.iterator();
			while (cmArrayIterator.hasNext()) {
				turnoverCM += cmArrayIterator.next()[0];
			}
			turnover_glep_r12cy_map=getCustomerTurnover(turnover_glep_r12cy_map,
					casaPeriodBean.getElevenPeriodAgoFromDate(),
					casaPeriodBean.getCurrentPeriodToDate());

			turnover_glep_r12ly_map=getCustomerTurnover(turnover_glep_r12ly_map,
					casaPeriodBean.getTwentythreePeriodAgoFromDate(),
					casaPeriodBean.getCurrentPeriodPreviousYearToDate());
			turnover_glep_bf_cm_map=getCustomerTurnover(turnover_glep_bf_cm_map, null,
					casaPeriodBean.getOnPeriodAgoToDate());

			turnover_glep_12pe_bf_cm_map=getCustomerTurnover(turnover_glep_12pe_bf_cm_map,
					casaPeriodBean.getTwelvePeriodAgoFromDate(),
					casaPeriodBean.getOnPeriodAgoToDate());

			turnover_glep_bf_12pe_map=getCustomerTurnover(turnover_glep_bf_12pe_map, null,
					casaPeriodBean.getThirteenPeriodAgoToDate());

			// turnover before current 12 periods
			turnover_glep_bf_r12cy_map=getCustomerTurnover(turnover_glep_bf_r12cy_map, null,
					casaPeriodBean.getTwelvePeriodAgoToDate());
			int year=periodArray[i]/100;
			int period=periodArray[i]%100;
			
			QueryHelper qh = Query.newQueryHelper();
			qh.setClass(DWCustomer.class);
			//TODO remove it
//			QueryPredicate temp = qh
//					.attr(DWCustomer.ACCOUNTNUMBER).eq().val(10000642).predicate();
			//TODO end
			
			qh.setDeepSelect(true);
			qh.addAscendingOrdering(DWCustomer.ACCOUNTNUMBER);
			Condition cond = qh.condition();
			PEnumeration penum = _controller.createIteratorFactory()
					.getCursorFetch(cond);
			while (penum.hasMoreElements()) {
				List<DWCustomer> list = new ArrayList<DWCustomer>(
						penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
				casaList = new LinkedList<CASABean>();
				for (DWCustomer dwCustomer : list) {
					CustomerAccount customer = _controller
							.lookupCustomerAccount(dwCustomer
									.getAccountNumber());
					PDate customerCreationDate = customer.getCreationDate();
					if (customerCreationDate != null) {
						if (customerCreationDate.getYear() > year
								|| (customerCreationDate.getYear() == year && customerCreationDate
										.getMonth() + 1 > period)) {
							System.out
									.println("\n Customer creation date later than period : "
											+ customer.getAccountNumber()
											+ " creationdate "
											+ customerCreationDate
											+ " period "
											+ periodArray[i]);
							continue;
						}
					}
								
					CASABean casa = new CASABean();
					casa.setPeriod(periodArray[i]);
					casa.setCustomerNumber(dwCustomer.getAccountNumber());
					
					
					if (customer != null) {
						SalesArea sa = customer.getSalesArea(casaPeriodBean
								.getCurrentPeriodToDate());
						if (sa != null) {
							Salesman salesman = sa
							.getResponsibleSalesman(casaPeriodBean
									.getCurrentPeriodToDate());
							if (salesman != null) {
								casa.setRegisterNumber(salesman.getRegisterNumber());
							}
						}
					}
					
					double[] turnover_glep_cm=turnover_glep_cm_map.get(dwCustomer.getSurrogateKey());
					if(turnover_glep_cm!=null){
						casa.setTurnover_cm(turnover_glep_cm[0]);
						casa.setCogsglep_cm(turnover_glep_cm[1]);
						casa.setCogspfep_cm(turnover_glep_cm[1] * 1.085);
					}
					
					double[] turnover_glep_r12cy=turnover_glep_r12cy_map.get(dwCustomer.getSurrogateKey());
					if(turnover_glep_r12cy!=null){
						casa.setTurnover_r12cy(turnover_glep_r12cy[0]);
						casa.setCogsglep_r12cy(turnover_glep_r12cy[1]);
						casa.setCogspfep_r12cy(turnover_glep_r12cy[1] * 1.085);
					}

					String smlClassification = getSML(_controller,
							casa.getTurnover_r12cy());
					casa.setSml_r12cy(smlClassification);
					if (casa.getTurnover_r12cy() >= 5000D) {
						casa.setBuyingcustomer500_r12cy(true);
					}
					
					double[] turnover_glep_r12ly = turnover_glep_r12ly_map
							.get(dwCustomer.getSurrogateKey());

					if (turnover_glep_r12ly != null) {
						casa.setTurnover_r12ly(turnover_glep_r12ly[0]);
						casa.setCogsglep_r12ly(turnover_glep_r12ly[1]);
						casa.setCogspfep_r12ly(turnover_glep_r12ly[1] * 1.085);
					}
					
					smlClassification = getSML(_controller, casa.getTurnover_r12ly());
					casa.setSml_r12ly(smlClassification);
					
					// New Customer
					double[] turnover_glep_bf_cm = turnover_glep_bf_cm_map
							.get(dwCustomer.getSurrogateKey());
//					if ((turnover_glep_bf_cm == null || turnover_glep_bf_cm[0] < turnoverOfActiveCustomer)
					if ((turnover_glep_bf_cm == null)
							&& casa.getTurnover_cm() >= turnoverOfActiveCustomer) {
						casa.setNewcustomer_cm(true);
					}
					// Zero Customer
					if (casa.getTurnover_r12cy() < turnoverOfActiveCustomer) {
						double[] turnover_glep_bf_r12cy = turnover_glep_bf_r12cy_map
								.get(dwCustomer.getSurrogateKey());
						// Has turnover before current 12 periods
						if (turnover_glep_bf_r12cy != null
								&& turnover_glep_bf_r12cy[0] >= turnoverOfActiveCustomer) {
							casa.setZerocustomer_r12cy(true);
						}
					}

					// Reactive Customer
					if (casa.getTurnover_cm() > turnoverOfActiveCustomer) {
						// turnover 12 periods before current period
						double[] turnover_glep_12pe_bf_cm = turnover_glep_12pe_bf_cm_map
								.get(dwCustomer.getSurrogateKey());
						if (turnover_glep_12pe_bf_cm == null
								|| turnover_glep_12pe_bf_cm[0] < turnoverOfActiveCustomer) {
							double[] turnover_glep_bf_12pe = turnover_glep_bf_12pe_map
									.get(dwCustomer.getSurrogateKey());
							if (turnover_glep_bf_12pe!=null&&turnover_glep_bf_12pe[0] > turnoverOfActiveCustomer) {
								casa.setReactivatedcustomer_cm(true);
							}
						}
					}
					
					if (customer.getStatus().equals(CustomerAccountStatus.DELETED)) {
						casa.setWs1CustomerStatus(9);
					} else if (customer.getStatus().equals(
							CustomerAccountStatus.PROSPECTIVE)) {
						casa.setWs1CustomerStatus(2);
					} else {
						casa.setWs1CustomerStatus(4);
					}
					fillWS1Information(casa);
					if (casa.getWs1CustomerNumber().equals(
							BIDateMapping.dummyCustomerNumber)) {
						boolean found = false;
						for (CASABean casaBean : dummyCustomerCasaList) {
							if (casaBean.getPeriod() == casa.getPeriod()) {
								found = true;
								casaBean.setTurnover_cm(casaBean.getTurnover_cm()+casa.getTurnover_cm());
								casaBean.setTurnover_bf_cm(casaBean.getTurnover_bf_cm()+casa.getTurnover_bf_cm());
								casaBean.setTurnover_r12cy(casaBean.getTurnover_r12cy()+casa.getTurnover_r12cy());
								casaBean.setTurnover_r12ly(casaBean.getTurnover_r12ly()+casa.getTurnover_r12ly());
								casaBean.setCogspfep_r12cy(casaBean.getCogspfep_r12cy()+casa.getCogspfep_r12cy());
								casaBean.setCogspfep_r12ly(casaBean.getCogspfep_r12ly()+casa.getCogspfep_r12ly());
								casaBean.setCogspfep_cm(casaBean.getCogspfep_cm()+casa.getCogspfep_cm());
								casaBean.setCogsglep_r12cy(casaBean.getCogsglep_r12cy()+casa.getCogsglep_r12cy());
								casaBean.setCogsglep_r12ly(casaBean.getCogsglep_r12ly()+casa.getCogsglep_r12ly());
								casaBean.setCogsglep_cm(casaBean.getCogsglep_cm()+casa.getCogsglep_cm());
								break;
							}
						}
						if (!found) {
							dummyCustomerCasaList.add(casa);
						}
					} else {
						casaList.addLast(casa);
					}
					
				}
				writeCASAInfoToTxt(casaList);
				_context.commit();
			}
			penum.destroy();
		}
		System.out.println("turnoverCM " + turnoverCM);
		

	}
	
	
	public String getSML(PhxbasicController controller,double turnoverAmount){
		for (CustomerTurnoverRange tr : turnoverRangeList) {
			if (tr.getMinAmount().getAmount() <= turnoverAmount
					&& (tr.getMaxAmount() == null || tr.getMaxAmount().getAmount() >= turnoverAmount)) {
				return tr.getId();
			}
		}
		return null;
	}
	
	private void fillWS1Information(CASABean casa) {
		String customerOrg = BIDateMapping.customerOrgMap.get(casa
				.getCustomerNumber());
		String defaultWS1RegisterNumber=null;
		if (customerOrg != null) {
			String[] customerOrgArray = customerOrg.split(",");
			casa.setWs1SalesOrganisation(customerOrgArray[1]);
			casa.setWs1CustomerNumber(customerOrgArray[0]);
			defaultWS1RegisterNumber=customerOrgArray[2];
		} else {
			if (ownCompanyName.equals("伍尔特（重庆）五金工具有限公司")) {
				casa.setWs1CustomerNumber("0000999921");
				casa.setWs1SalesOrganisation("8807");
			} else {
				casa.setWs1CustomerNumber("0000999921");
				casa.setWs1SalesOrganisation("8805");
			}

			System.out.println("\n Customer mapping can not find : "
					+ casa.getCustomerNumber());
		}

		String ws1RegisterNumber = BIDateMapping.getWS1RegisterNumber(
				ownCompanyName, casa.getRegisterNumber());
		if (BIDateMapping.isWS1RUDUmmyRU(ws1RegisterNumber)
				&& defaultWS1RegisterNumber != null) {
			ws1RegisterNumber = defaultWS1RegisterNumber;
		}
		if (casa.getWs1CustomerNumber().equals("0000999921")) {
			ws1RegisterNumber = BIDateMapping.getDummyWS1RegisterNumber();
		}
		casa.setWs1RegisterNumber(ws1RegisterNumber);
	}

	private Map<Long,double[]> getCustomerTurnover(Map<Long,double[]> turnoverMap,
			PDate fromDate, PDate toDate) throws PUserException {
		
		
		PDate creditNoteDate=new PDate(2012,10,22);
		PDate fromCheckDate=fromDate!=null? fromDate:new PDate(2012,10,21);
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(DWCustomerStatisticDay.class);
		
		QueryPredicate qp = null;
		if (fromDate != null) {
			qp = qh.attr(DWCustomerStatisticDay.ASS_DWDAY_REF + DWDay.DATE)
					.gte().val(fromDate).predicate();
		}
		if (toDate != null) {
			QueryPredicate qp1 = qh
					.attr(DWCustomerStatisticDay.ASS_DWDAY_REF + DWDay.DATE)
					.lte().val(toDate).predicate();
			if (qp != null) {
				qp = qp.and(qp1);
			} else {
				qp = qp1;
			}

		}
		qh.addResultAttribute(DWCustomerStatisticDay.CUSTOMERSURROGATEKEY);
		qh.addResultAttributeAsSum(DWCustomerStatisticDay.CCTURNOVERAMOUNT,
				"totalTurnover");
		qh.addResultAttributeAsSum(
				DWCustomerStatisticDay.CCCOSTOFGOODSTURNOVERAMOUNT,
				"costAmount");
		
		qh.addResultAttributeAsSum(DWCustomerStatisticDay.CCAMOUNTCREDITNOTESFPAMOUNT,
				"ccAmountCreditNotesFP");
		qh.addAscendingOrdering(DWCustomerStatisticDay.CUSTOMERSURROGATEKEY);
		qh.setGrouping(DWCustomerStatisticDay.CUSTOMERSURROGATEKEY);
		Condition cond=qh.condition(qp);
		System.out.println("QueryString "+Query.getQueryString(cond));
		PEnumeration cusStatPEnum = _controller.createIteratorFactory()
				.getSelect(cond);
		int i=0;
		
		double turnoverAmount=0.0d;
		while (cusStatPEnum.hasMoreElements()) {
			QueryResultEntry resultEntry = (QueryResultEntry) cusStatPEnum
					.nextElement();
			double[] returnArray = new double[2];
			double totalTurnoverAmount = 0.0D;
			double totalCostAmount = 0.0D;
			Long customerSurrogateKey=(Long) resultEntry.get(0);
			if (resultEntry.get(1) != null) {
				totalTurnoverAmount = DoubleUtils
						.getRoundedAmount((Double) resultEntry.get(1));
			}
			if (resultEntry.get(2) != null) {
				totalCostAmount = DoubleUtils
						.getRoundedAmount((Double) resultEntry.get(2));
			}
			
			if (resultEntry.get(3) != null) {
				totalTurnoverAmount = totalTurnoverAmount
						- DoubleUtils.getRoundedAmount((Double) resultEntry
								.get(3));
				if (customerSurrogateKey == 2441273
						&& creditNoteDate.after(fromCheckDate)
						&& creditNoteDate.before(toDate)) {
					totalTurnoverAmount = totalTurnoverAmount + 118347011;
				}
			}
			
			returnArray[0] = totalTurnoverAmount;
			turnoverAmount+=totalTurnoverAmount;
			returnArray[1] = totalCostAmount;
			turnoverMap.put(customerSurrogateKey, returnArray);
			System.out.println("customerSurrogateKey " + customerSurrogateKey
					+ " " + returnArray[0] + " " + returnArray[1]);
			i++;
		}
		System.out.println("turnoverMap size " + turnoverMap.size()+" total searched out: "+i);
		System.out.println("turnoverAmount " + turnoverAmount);
		cusStatPEnum.destroy();
		return turnoverMap;
	}
	


	private void writeCASAInfoToTxt(LinkedList<CASABean> casaList) {
		for (int i = 0; i < casaList.size(); i++) {
			CASABean casab = casaList.get(i);
			StringBuffer sb = new StringBuffer();
			sb.append(casab.getWs1SalesOrganisation()).append(csvSeperator);
			sb.append(casab.getPeriod()).append(csvSeperator);
			sb.append(casab.getWs1CustomerNumber()).append(csvSeperator);
			sb.append(casab.getWs1RegisterNumber()).append(csvSeperator);
			sb.append(" ").append(csvSeperator);
			sb.append(casab.getWs1CustomerStatus()).append(csvSeperator);

			sb.append(df.format(DoubleUtils.getRoundedAmount(casab.getTurnover_cm()))).append(csvSeperator);
			sb.append(df.format(DoubleUtils.getRoundedAmount(casab.getTurnover_r12cy()))).append(csvSeperator);
			sb.append(df.format(DoubleUtils.getRoundedAmount(casab.getTurnover_r12ly()))).append(csvSeperator);
			
			sb.append(df.format(DoubleUtils.getRoundedAmount(casab.getCogspfep_r12cy()))).append(csvSeperator);
			sb.append(df.format(DoubleUtils.getRoundedAmount(casab.getCogspfep_r12ly()))).append(csvSeperator);
			
			sb.append(df.format(DoubleUtils.getRoundedAmount(casab.getCogsglep_r12cy()))).append(csvSeperator);
			sb.append(df.format(DoubleUtils.getRoundedAmount(casab.getCogsglep_r12ly()))).append(csvSeperator);
			
			sb.append(df.format(DoubleUtils.getRoundedAmount(casab.getCogspfep_cm()))).append(csvSeperator);
			sb.append(df.format(DoubleUtils.getRoundedAmount(casab.getCogsglep_cm()))).append(csvSeperator);
			
			if (casab.getSml_r12cy() != null) {
				sb.append(casab.getSml_r12cy()).append(csvSeperator);
			} else {
				sb.append("").append(csvSeperator);
			}
			if (casab.getSml_r12ly() != null) {
				sb.append(casab.getSml_r12ly()).append(csvSeperator);
			} else {
				sb.append("").append(csvSeperator);
			}

			if (casab.isNewcustomer_cm()) {
				sb.append("x").append(csvSeperator);
			} else {
				sb.append("").append(csvSeperator);
			}
			if (casab.isZerocustomer_r12cy()) {
				sb.append("x").append(csvSeperator);
			} else {
				sb.append("").append(csvSeperator);
			}

			if (casab.isReactivatedcustomer_cm()) {
				sb.append("x").append(csvSeperator);
			} else {
				sb.append("").append(csvSeperator);
			}

			if (casab.isBuyingcustomer500_r12cy()) {
				sb.append("x").append(csvSeperator);
			} else {
				sb.append("").append(csvSeperator);
			}
			
			out1.println(sb.toString());
			numberOfCASAsWritten++;
			System.out.println("\n Number of casas writed in txt 1: "
					+ numberOfCASAsWritten);
		}
		out1.flush();
	}
}
