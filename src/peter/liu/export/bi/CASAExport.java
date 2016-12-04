package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wuerth.phoenix.Phxbasic.enums.CustomerAccountStatus;
import com.wuerth.phoenix.Phxbasic.models.CompanyPeriod;
import com.wuerth.phoenix.Phxbasic.models.CompanyYear;
import com.wuerth.phoenix.Phxbasic.models.CustomerAccount;
import com.wuerth.phoenix.Phxbasic.models.CustomerClassification;
import com.wuerth.phoenix.Phxbasic.models.CustomerTurnoverRange;
import com.wuerth.phoenix.Phxbasic.models.DWCustomer;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerInvoice;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerInvoiceLine;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerStatisticDay;
import com.wuerth.phoenix.Phxbasic.models.DWDay;
import com.wuerth.phoenix.Phxbasic.models.DWDebitor;
import com.wuerth.phoenix.Phxbasic.models.Debitor;
import com.wuerth.phoenix.Phxbasic.models.PhxbasicController;
import com.wuerth.phoenix.Phxbasic.models.SalesArea;
import com.wuerth.phoenix.Phxbasic.models.Salesman;
import com.wuerth.phoenix.basic.etnax.common.utilsdir.CalendarUtils;
import com.wuerth.phoenix.basic.etnax.common.utilsdir.DoubleUtils;
import com.wuerth.phoenix.basic.etnax.datawarehouse.DWUtil;
import com.wuerth.phoenix.basic.etnax.exception.ProcessException;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.HookException;
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
import com.wuerth.phoenix.util.money.Money;
import com.wuerth.phoenix.util.money.MoneyController;

/**
 * CASAExport Receive year as parameter, only support four years 2010-2014
 * 
 * @author pcnsh197
 * 
 */
public class CASAExport extends BatchRunner {

	private int NUMBER_OF_BATCH_TO_FECTCH = 1000;

	LinkedList<CASABean> casaList = new LinkedList<CASABean>();
	LinkedList<CASABean> dummyCustomerCasaList = new LinkedList<CASABean>();

	private String targetTxt2015 = "../../var/exportSAP/CASA-2015.csv";

	private String targetTxt2016 = "../../var/exportSAP/CASA-2016.csv";

	private String targetTxt2017 = "../../var/exportSAP/CASA-201701.csv";

	private PrintWriter out1 = null;

	private int numberOfCASAsWritten = 0;

	private Map<Integer, CASAPeriodBean> periodMap = new HashMap<Integer, CASAPeriodBean>();

	private String ownCompanyName;
	private double averagePotentialOfEmployee;

	private int year = 2015;
	private boolean isAll = false;
	private boolean isFirstRound = false;
	private boolean isFinalRound = false;

	private double turnoverOfActiveCustomer;

//	private DecimalFormat df = new DecimalFormat();

	private Map<Long, double[]> turnover_glep_cm_map = new HashMap<Long, double[]>();
	private Map<Long, double[]> turnover_glep_r12cy_map = new HashMap<Long, double[]>();

	private Map<Long, double[]> turnover_glep_r12ly_map = new HashMap<Long, double[]>();

	private Map<Long, double[]> turnover_glep_bf_cm_map = new HashMap<Long, double[]>();

	private Map<Long, double[]> turnover_glep_bf_r12cy_map = new HashMap<Long, double[]>();

	private Map<Long, double[]> turnover_glep_12pe_bf_cm_map = new HashMap<Long, double[]>();
	private Map<Long, double[]> turnover_glep_bf_12pe_map = new HashMap<Long, double[]>();
	private Map<Long, double[]> turnover_glep_bf_23pe_map = new HashMap<Long, double[]>();
	private Map<Long, Double> freight_cost_cm_map = new HashMap<Long, Double>();
	
	private Set<Long> validCustomerSet = new HashSet<Long>(2000);

	private List<CustomerTurnoverRange> turnoverRangeList = null;
	List<CustomerClassification> classificationList = null;

	private int[] periodArray2015First = { 201512 };
	private int[] periodArray2015All = { 201501, 201502, 201503, 201504, 201505, 201506, 201507, 201508, 201509, 201510,
			201511, 201512 };
	private int[] periodArray2016 = { 201601, 201602, 201603, 201604, 201605, 201606, 201607, 201608, 201609, 201610,
			201611, 201612 };
	private int[] periodArray2017All = { 201701 };
	private int[] periodArray2017Final = { 201702 };

	@Override
	protected void batchMethod() throws TimestampException, PUserException, IOException {

		if (isFirstRound) {
			searchInvoice(periodArray2015First, targetTxt2015);
		} else if (isFinalRound) {
			searchInvoice(periodArray2017Final, targetTxt2017);
		} else if (isAll) {
			searchInvoice(periodArray2015All, targetTxt2015);
			searchInvoice(periodArray2016, targetTxt2016);
			searchInvoice(periodArray2017All, targetTxt2017);
		} else {
			if (year == 2015) {
				searchInvoice(periodArray2015All, targetTxt2015);
			} else if (year == 2016) {
				searchInvoice(periodArray2016, targetTxt2016);
			} else if (year == 2017) {
				searchInvoice(periodArray2017All, targetTxt2017);
			}
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
			if (argstr.equalsIgnoreCase("commit")) {
				willcommit = true;
			} else if (argstr.equalsIgnoreCase("all")) {
				isAll = true;
			} else if (argstr.equalsIgnoreCase("first")) {
				isFirstRound = true;
				targetTxt2015 = "../../var/exportSAP/CASA-201512.csv";
				year = 2015;
			} else if (argstr.equalsIgnoreCase("final")) {
				isFinalRound = true;
				targetTxt2017 = "../../var/exportSAP/CASA-201702.csv";
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
		System.out.println("-args first \n\tfirst : Only output CASA in period 201512");
		System.out.println("-args final \n\tfinal : Only output CASA in date 201702");
		System.out.println("-args all \n\tall : Output CASA from year 2015 to 2017, each year has a output file");
		System.out.println("-args 2015, 2016 or 2017 \n\tall : Output CASA in 2015 or 2016 or 2017");
		System.out.println("output file are in path ../../var/exportSAP/ start with CASA-");
	}

	private void searchInvoice(int[] periodArray, String targetFile)
			throws TimestampException, PUserException, IOException {
		FileWriter outFile = new FileWriter(targetFile);
		out1 = new PrintWriter(outFile);
		writeInvoiceItemHeaderToCSV(out1);
		initializeDate(periodArray);
		System.out.println("\n(!) Write txt file  start.\n");
		searchDWCustomer(periodArray);

		out1.close();
		System.out.println("\n(*) Write txt file  end.");
	}

	private void searchDWCustomer(int[] periodArray) throws TimestampException, PUserException {
		ownCompanyName = _controller.getSingletonOwnCompany().getName();
		averagePotentialOfEmployee = _controller.getSingletonOwnCompany().getAveragePotentialOfEmployee().getAmount();
		turnoverOfActiveCustomer = _controller.getSingletonStatisticParameters().getAmountForActiveCustomer()
				.getAmount();
		System.out.println("\n(*) turnoverOfActiveCustomer " + turnoverOfActiveCustomer);
		getCustomerSet();
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
			CompanyPeriod currentPeriod = currentYear.lookupCompanyPeriod(period);

			PDate currentPeriodFromDate = currentPeriod.getFrom();
			PDate currentPeriodToDate = currentPeriod.getTo();
			CompanyYear previousYear = _controller.lookupCompanyYear((short) (year - 1));
			CompanyPeriod currentPeriodPreviousYear = previousYear.lookupCompanyPeriod(period);
			PDate currentPeriodPreviousYearFromDate = currentPeriodPreviousYear.getFrom();
			PDate currentPeriodPreviousYearToDate = currentPeriodPreviousYear.getTo();

			// Turnover 12 periods rolling

			CompanyPeriod onPeriodAgo = CalendarUtils.addPeriod(_controller, currentPeriod, -1);
			PDate onPeriodAgoToDate = onPeriodAgo.getTo();

			CompanyPeriod elevenPeriodAgo = CalendarUtils.addPeriod(_controller, currentPeriod, -11);
			PDate elevenPeriodAgoFromDate = elevenPeriodAgo.getFrom();

			// Turnover 12 periods rolling before current period
			CompanyPeriod twelvePeriodAgo = CalendarUtils.addPeriod(_controller, currentPeriod, -12);
			PDate twelvePeriodAgoToDate = twelvePeriodAgo.getTo();

			CompanyPeriod thirteenPeriodAgo = CalendarUtils.addPeriod(_controller, currentPeriod, -13);
			PDate thirteenPeriodAgoToDate = thirteenPeriodAgo.getTo();

			CompanyPeriod twentythreePeriodAgo = CalendarUtils.addPeriod(_controller, currentPeriod, -23);
			PDate twentythreePeriodAgoFromDate = twentythreePeriodAgo.getFrom();

			CompanyPeriod twentyfourPeriodAgo = CalendarUtils.addPeriod(_controller, currentPeriod, -24);
			PDate twentyfourPeriodAgoToDate = twentyfourPeriodAgo.getTo();

			CASAPeriodBean casaPeriodBean = new CASAPeriodBean();
			casaPeriodBean.setYearAndPeriod(yearAndPeriod);
			casaPeriodBean.setCurrentPeriodFromDate(currentPeriodFromDate);
			casaPeriodBean.setCurrentPeriodToDate(currentPeriodToDate);
			casaPeriodBean.setOnPeriodAgoToDate(onPeriodAgoToDate);
			casaPeriodBean.setCurrentPeriodPreviousYearFromDate(currentPeriodPreviousYearFromDate);
			casaPeriodBean.setCurrentPeriodPreviousYearToDate(currentPeriodPreviousYearToDate);
			casaPeriodBean.setElevenPeriodAgoFromDate(elevenPeriodAgoFromDate);
			casaPeriodBean.setTwelvePeriodAgoToDate(twelvePeriodAgoToDate);
			casaPeriodBean.setThirteenPeriodAgoToDate(thirteenPeriodAgoToDate);
			casaPeriodBean.setTwentythreePeriodAgoFromDate(twentythreePeriodAgoFromDate);
			casaPeriodBean.setTwentyfourPeriodAgoToDate(twentyfourPeriodAgoToDate);

			System.out.println("currentPeriodFromDate " + currentPeriodFromDate + " currentPeriodToDate "
					+ currentPeriodToDate + " onPeriodAgoToDate  " + onPeriodAgoToDate + " elevenPeriodAgoFromDate "
					+ elevenPeriodAgoFromDate + " twelvePeriodAgoToDate " + twelvePeriodAgoToDate
					+ " thirteenPeriodAgoToDate " + thirteenPeriodAgoToDate + " twentythreePeriodAgoFromDate "
					+ twentythreePeriodAgoFromDate +" twentyfourPeriodAgoToDate " +twentyfourPeriodAgoToDate);

			periodMap.put(yearAndPeriod, casaPeriodBean);
		}
		turnoverRangeList = new ArrayList<CustomerTurnoverRange>(
				_controller.getSingletonStatisticParameters().getAllChildCustomerTurnoverRange());
		classificationList = new ArrayList<CustomerClassification>(_controller.getAllRootsCustomerClassification());
	}

	private void fillCustomerStatisticByPeriod(int[] periodArray) throws HookException, PUserException {

		double turnoverCM = 0.0D;
		for (int i = 0; i < periodArray.length; i++) {
			CASAPeriodBean casaPeriodBean = periodMap.get(periodArray[i]);
			turnover_glep_cm_map = new HashMap<Long, double[]>();
			turnover_glep_r12cy_map = new HashMap<Long, double[]>();
			turnover_glep_r12ly_map = new HashMap<Long, double[]>();
			turnover_glep_bf_cm_map = new HashMap<Long, double[]>();
			turnover_glep_bf_r12cy_map = new HashMap<Long, double[]>();
			turnover_glep_12pe_bf_cm_map = new HashMap<Long, double[]>();
			turnover_glep_bf_12pe_map = new HashMap<Long, double[]>();
			turnover_glep_bf_23pe_map = new HashMap<Long, double[]>();
			freight_cost_cm_map = new HashMap<Long, Double>();
			turnover_glep_cm_map = getCustomerTurnover(turnover_glep_cm_map, casaPeriodBean.getCurrentPeriodFromDate(),
					casaPeriodBean.getCurrentPeriodToDate());
			Iterator<double[]> cmArrayIterator = turnover_glep_cm_map.values().iterator();
			while (cmArrayIterator.hasNext()) {
				turnoverCM += cmArrayIterator.next()[0];
			}
			turnover_glep_r12cy_map = getCustomerTurnover(turnover_glep_r12cy_map,
					casaPeriodBean.getElevenPeriodAgoFromDate(), casaPeriodBean.getCurrentPeriodToDate());

			turnover_glep_r12ly_map = getCustomerTurnover(turnover_glep_r12ly_map,
					casaPeriodBean.getTwentythreePeriodAgoFromDate(),
					casaPeriodBean.getCurrentPeriodPreviousYearToDate());
			turnover_glep_bf_cm_map = getCustomerTurnover(turnover_glep_bf_cm_map, null,
					casaPeriodBean.getOnPeriodAgoToDate());

			turnover_glep_12pe_bf_cm_map = getCustomerTurnover(turnover_glep_12pe_bf_cm_map,
					casaPeriodBean.getTwelvePeriodAgoFromDate(), casaPeriodBean.getOnPeriodAgoToDate());

			turnover_glep_bf_12pe_map = getCustomerTurnover(turnover_glep_bf_12pe_map, null,
					casaPeriodBean.getThirteenPeriodAgoToDate());

			// turnover before current 12 periods
			turnover_glep_bf_r12cy_map = getCustomerTurnover(turnover_glep_bf_r12cy_map, null,
					casaPeriodBean.getTwelvePeriodAgoToDate());

			turnover_glep_bf_23pe_map = getCustomerTurnover(turnover_glep_bf_23pe_map, null,
					casaPeriodBean.getTwentyfourPeriodAgoToDate());
			
			getFreightCost(freight_cost_cm_map, casaPeriodBean.getCurrentPeriodFromDate(),
					casaPeriodBean.getCurrentPeriodToDate());
			
			int year = periodArray[i] / 100;
			int period = periodArray[i] % 100;
			Money buying500Eur=MoneyController.getNewMoney(500, "EUR");
			double buying500NZD=DWUtil.convertCurrencyInDate(_controller,buying500Eur,casaPeriodBean.getCurrentPeriodToDate()).getAmount();
			System.out.print("Buying 500 in NZD is: "+buying500NZD);
			QueryHelper qh = Query.newQueryHelper();
			qh.setClass(DWCustomer.class);
			qh.setDeepSelect(true);
			qh.addAscendingOrdering(DWCustomer.ACCOUNTNUMBER);
			Condition cond = qh.condition();
			PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
			while (penum.hasMoreElements()) {
				List<DWCustomer> list = new ArrayList<DWCustomer>(penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
				casaList = new LinkedList<CASABean>();
				for (DWCustomer dwCustomer : list) {
					if (!validCustomerSet.contains(dwCustomer.getSurrogateKey())) {
						System.out.println("\n Customer did not purchase since 20120101 : " + dwCustomer.getAccountNumber());
						continue;
					}
					CustomerAccount customer = _controller.lookupCustomerAccount(dwCustomer.getAccountNumber());
					PDate customerCreationDate = customer.getCreationDate();
					if (customerCreationDate != null) {
						if (customerCreationDate.getYear() > year || (customerCreationDate.getYear() == year
								&& customerCreationDate.getMonth() + 1 > period)) {
							System.out.println(
									"\n Customer creation date later than period : " + customer.getAccountNumber()
											+ " creationdate " + customerCreationDate + " period " + periodArray[i]);
							continue;
						}
					}

					CASABean casa = new CASABean();
					casa.setPeriod(periodArray[i]);
					casa.setCustomerNumber(dwCustomer.getAccountNumber());
					casa.setName1(dwCustomer.getName1());

					if (customer != null) {
						SalesArea sa = customer.getSalesArea(casaPeriodBean.getCurrentPeriodToDate());
						if (sa != null) {
							Salesman salesman = sa.getResponsibleSalesman(casaPeriodBean.getCurrentPeriodToDate());
							if (salesman != null) {
								casa.setRegisterNumber(salesman.getRegisterNumber());
							}else{
								System.out.println("\n Customer has no salesman assigned : "
										+ customer.getAccountNumber() + " in period " + periodArray[i]);
								continue;
							}
						}else{
							System.out.println("\n Customer has no salesman assigned : "
									+ customer.getAccountNumber() + " in period " + periodArray[i]);
							continue;
						}
						
						if (customer.getIsOrsyCustomerActivated() && customer.getOrsyRegisterDate() != null
								&& customer.getOrsyRegisterDate().before(casaPeriodBean.getCurrentPeriodToDate())) {
							casa.setOrsy(true);
						} else {
							casa.setOrsy(false);
						}
						casa.setName1(customer.getName());
						casa.setPotential(averagePotentialOfEmployee * customer.getNumberOfEmployees());
						casa.setSml_potential_r12cy(getSML(casa.getPotential()));
						casa.setSml_n_potential_r12cy(getCustomerPotentialSML(casa.getPotential()));
						casa.setSml_n_potential_r12ly(casa.getSml_n_potential_r12cy());
					}

					double[] turnover_glep_cm = turnover_glep_cm_map.get(dwCustomer.getSurrogateKey());
					if (turnover_glep_cm != null) {
						casa.setTurnover_cm(turnover_glep_cm[0]);
						casa.setCogsglep_cm(turnover_glep_cm[1]);
						casa.setCogspfep_cm(turnover_glep_cm[1] / 1.1);
						casa.setFreightcost_cm(casa.getFreightcost_cm()-turnover_glep_cm[2]);
						casa.setNum_of_ord_cm(turnover_glep_cm[3]);
						casa.setNum_of_cn_cm(turnover_glep_cm[4]);
					}
					if (freight_cost_cm_map.get(dwCustomer.getSurrogateKey()) != null) {
						casa.setFreightcost_cm(
								casa.getFreightcost_cm() + freight_cost_cm_map.get(dwCustomer.getSurrogateKey()));
					}

					double[] turnover_glep_r12cy = turnover_glep_r12cy_map.get(dwCustomer.getSurrogateKey());
					if (turnover_glep_r12cy != null) {
						casa.setTurnover_r12cy(turnover_glep_r12cy[0]);
						casa.setCogsglep_r12cy(turnover_glep_r12cy[1]);
						casa.setCogspfep_r12cy(turnover_glep_r12cy[1] / 1.1);
						casa.setNum_of_ord_r12cy(turnover_glep_r12cy[3]);
						casa.setNum_of_cn_r12cy(turnover_glep_r12cy[4]);
					}

					String smlClassification = getSML(casa.getTurnover_r12cy());
					casa.setSml_r12cy(smlClassification);
					if (casa.getTurnover_r12cy() >= buying500NZD) {
						casa.setBuyingcustomer500_r12cy(true);
					}

					double[] turnover_glep_r12ly = turnover_glep_r12ly_map.get(dwCustomer.getSurrogateKey());

					if (turnover_glep_r12ly != null) {
						casa.setTurnover_r12ly(turnover_glep_r12ly[0]);
						casa.setCogsglep_r12ly(turnover_glep_r12ly[1]);
						casa.setCogspfep_r12ly(turnover_glep_r12ly[1] / 1.1);
						casa.setNum_of_ord_r12ly(turnover_glep_r12ly[3]);
						casa.setNum_of_cn_r12ly(turnover_glep_r12ly[4]);
					}

					smlClassification = getSML(casa.getTurnover_r12ly());
					casa.setSml_r12ly(smlClassification);

					// New Customer
					double[] turnover_glep_bf_cm = turnover_glep_bf_cm_map.get(dwCustomer.getSurrogateKey());
					if ((turnover_glep_bf_cm == null) && casa.getTurnover_cm() >= turnoverOfActiveCustomer) {
						casa.setNewcustomer_cm(true);
					}
					// New Customer Flag Rolling 12 current Year
					if (casa.getTurnover_r12cy() >= turnoverOfActiveCustomer) {
						double[] turnover_glep_bf_r12cy = turnover_glep_bf_r12cy_map.get(dwCustomer.getSurrogateKey());
						// Has no turnover before current 12 periods
						if (turnover_glep_bf_r12cy == null || turnover_glep_bf_r12cy[0] < turnoverOfActiveCustomer) {
							casa.setNewcustomer_r12cy(true);
						}
					}

					// Zero Customer
					if (casa.getTurnover_r12cy() < turnoverOfActiveCustomer) {
						double[] turnover_glep_bf_r12cy = turnover_glep_bf_r12cy_map.get(dwCustomer.getSurrogateKey());
						// Has turnover before current 12 periods
						if (turnover_glep_bf_r12cy != null && turnover_glep_bf_r12cy[0] >= turnoverOfActiveCustomer) {
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
							if (turnover_glep_bf_12pe != null && turnover_glep_bf_12pe[0] > turnoverOfActiveCustomer) {
								casa.setReactivatedcustomer_cm(true);
							}
						}
					}
					// Reactivated Customer Flag Rolling 12 current Year
					if (casa.getTurnover_r12cy() > turnoverOfActiveCustomer) {
						// turnover 12 periods before current year
						if (turnover_glep_r12ly == null || turnover_glep_r12ly[0] < turnoverOfActiveCustomer) {
							double[] turnover_glep_bf_23pe = turnover_glep_bf_23pe_map
									.get(dwCustomer.getSurrogateKey());
							if (turnover_glep_bf_23pe != null && turnover_glep_bf_23pe[0] > turnoverOfActiveCustomer) {
								casa.setReactivatedcustomer_r12cy(true);
							}
						}
					}

					if (customer.getStatus().equals(CustomerAccountStatus.DELETED)) {
						casa.setWs1CustomerStatus(9);
					} else if (customer.getStatus().equals(CustomerAccountStatus.PROSPECTIVE)) {
						casa.setWs1CustomerStatus(2);
					} else {
						casa.setWs1CustomerStatus(4);
					}

					if (casa.getWs1CustomerStatus() == 4) {
						// Lost Customer Flag Current Month
						if (casa.getTurnover_cm() < turnoverOfActiveCustomer) {
							if (turnover_glep_bf_cm != null && turnover_glep_bf_cm[0] >= turnoverOfActiveCustomer) {
								casa.setLostcustomer_cm(true);
							}
						}

						// Lost Customer Flag rolling 12 current Year
						if (casa.getTurnover_r12cy() < turnoverOfActiveCustomer) {
							double[] turnover_glep_bf_r12cy = turnover_glep_bf_r12cy_map
									.get(dwCustomer.getSurrogateKey());
							// Has turnover before current 12 periods
							if (turnover_glep_bf_r12cy != null
									&& turnover_glep_bf_r12cy[0] >= turnoverOfActiveCustomer) {
								casa.setLostcustomer_r12cy(true);
							}
						}
					}
					
					

					fillWS1Information(casa);
					if (casa.getWs1CustomerNumber().equals(BIDateMapping.dummyCustomerNumber)) {
						boolean found = false;
						for (CASABean casaBean : dummyCustomerCasaList) {
							if (casaBean.getPeriod() == casa.getPeriod()) {
								found = true;
								casaBean.setTurnover_cm(casaBean.getTurnover_cm() + casa.getTurnover_cm());
								casaBean.setTurnover_bf_cm(casaBean.getTurnover_bf_cm() + casa.getTurnover_bf_cm());
								casaBean.setTurnover_r12cy(casaBean.getTurnover_r12cy() + casa.getTurnover_r12cy());
								casaBean.setTurnover_r12ly(casaBean.getTurnover_r12ly() + casa.getTurnover_r12ly());
								casaBean.setCogspfep_r12cy(casaBean.getCogspfep_r12cy() + casa.getCogspfep_r12cy());
								casaBean.setCogspfep_r12ly(casaBean.getCogspfep_r12ly() + casa.getCogspfep_r12ly());
								casaBean.setCogspfep_cm(casaBean.getCogspfep_cm() + casa.getCogspfep_cm());
								casaBean.setCogsglep_r12cy(casaBean.getCogsglep_r12cy() + casa.getCogsglep_r12cy());
								casaBean.setCogsglep_r12ly(casaBean.getCogsglep_r12ly() + casa.getCogsglep_r12ly());
								casaBean.setCogsglep_cm(casaBean.getCogsglep_cm() + casa.getCogsglep_cm());
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

	public String getSML(double turnoverAmount) {
		for (CustomerTurnoverRange tr : turnoverRangeList) {
			if (tr.getMinAmount().getAmount() <= turnoverAmount
					&& (tr.getMaxAmount() == null || tr.getMaxAmount().getAmount() >= turnoverAmount)) {
				if(tr.getId().equals("Z12")){
					return "ZE";
				}else{
					return tr.getId();
				}
			}
		}
		return null;
	}
	
	public String getCustomerPotentialSML(double turnoverAmount) {
		String sml="";
		for (CustomerClassification cc : classificationList) {
			if (cc.getFromTurnover().getAmount() <= turnoverAmount
					&& (cc.getToTurnover() == null || cc.getToTurnover().getAmount() >= turnoverAmount)) {
				sml=cc.getName().substring(1);
				if(sml.equals("PZ12")){
					sml="ZE";
				}
				return sml;
			}
		}
		return null;
	}
	

	private void fillWS1Information(CASABean casa) {
		casa.setWs1SalesOrganisation("3120");
		casa.setWs1CustomerNumber(BIDateMapping.getWS1CustomerNumber(casa.getCustomerNumber(), casa.getName1()));
		casa.setWs1RegisterNumber("0000" + casa.getRegisterNumber());
	}

	private void getFreightCost(Map<Long, Double> freightCostMap,PDate fromDate, PDate toDate)
			throws TimestampException, PUserException {
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(DWCustomerInvoice.class);

		QueryPredicate p1 = qh.attr(DWCustomerInvoice.INVOICEDATE).gte().val(fromDate).predicate();
		QueryPredicate p2 = qh.attr(DWCustomerInvoice.INVOICEDATE).lte().val(toDate).predicate();

		qh.setDeepSelect(true);
		qh.addResultAttribute(DWCustomerInvoice.DEBITORSURROGATEKEY);
		qh.addResultAttributeAsSum(DWCustomerInvoice.CCFREIGHTCOSTSAMOUNT, "freightCost");
		qh.setGrouping(DWCustomerInvoice.DEBITORSURROGATEKEY);
		Condition cond = null;
		cond = qh.condition(p1.and(p2));
		System.out.println("searchFreightCost QueryString " + Query.getQueryString(cond));

		PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			QueryResultEntry resultEntry = (QueryResultEntry) penum.nextElement();
			double freightCost = 0.0D;
			Long debitorSurrogateKey = (Long) resultEntry.get(0);
			if (resultEntry.get(1) != null) {
				freightCost = DoubleUtils.getRoundedAmount((Double) resultEntry.get(1));
			}
			freightCostMap.put(getCustomerSurrogatekey(debitorSurrogateKey), freightCost);
		}
		penum.destroy();
	}
	
	public long getCustomerSurrogatekey(long debitorSurrogateKey) {
		DWDebitor debitor = null;
		try {
			QueryHelper qh = Query.newQueryHelper();
			QueryPredicate qp = qh.attr(DWDebitor.SURROGATEKEY).eq().val(debitorSurrogateKey).predicate();
			if (qp != null) {
				qh.setClass(DWDebitor.class);
				Condition cond = qh.condition(qp);
				IteratorFactory fac = (IteratorFactory) _controller.createIteratorFactory();
				PEnumeration penum = fac.getCursor(cond);
				if (penum.hasMoreElements()) {
					debitor = (DWDebitor) penum.nextElement();
				}
				penum.destroy();
			}
		} catch (PUserException pe) {
			ProcessException.getIntance().process("DWUtil.getDWDebitor()", pe);
		}

		Debitor deb = _controller.lookupDebitor(debitor.getAccountNumber());
		return DWUtil.getDWCustomer(_controller, deb.getParentCompany().getCustomerAccountDefault().getAccountNumber())
				.getSurrogateKey();
	}
	
	

	private Map<Long, double[]> getCustomerTurnover(Map<Long, double[]> turnoverMap, PDate fromDate, PDate toDate)
			throws PUserException {

		// PDate creditNoteDate = new PDate(2012, 10, 22);
		// PDate fromCheckDate = fromDate != null ? fromDate : new PDate(2012,
		// 10, 21);
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(DWCustomerStatisticDay.class);

		QueryPredicate qp = null;
		if (fromDate != null) {
			qp = qh.attr(DWCustomerStatisticDay.ASS_DWDAY_REF + DWDay.DATE).gte().val(fromDate).predicate();
		}
		if (toDate != null) {
			QueryPredicate qp1 = qh.attr(DWCustomerStatisticDay.ASS_DWDAY_REF + DWDay.DATE).lte().val(toDate)
					.predicate();
			if (qp != null) {
				qp = qp.and(qp1);
			} else {
				qp = qp1;
			}
		}
		qh.addResultAttribute(DWCustomerStatisticDay.CUSTOMERSURROGATEKEY);
		qh.addResultAttributeAsSum(DWCustomerStatisticDay.CCTURNOVERAMOUNT, "totalTurnover");
		qh.addResultAttributeAsSum(DWCustomerStatisticDay.CCCOSTOFGOODSTURNOVERAMOUNT, "costAmount");
		qh.addResultAttributeAsSum(DWCustomerStatisticDay.CCAMOUNTCREDITNOTESFPAMOUNT, "ccAmountCreditNotesFP");
		qh.addResultAttributeAsSum(DWCustomerStatisticDay.NUMBERORDERS, "numberOrders");
		qh.addResultAttributeAsSum(DWCustomerStatisticDay.NUMBERCREDITNOTESWITHCUSTOMERLINE, "numberCreditNotes");
		
		qh.addAscendingOrdering(DWCustomerStatisticDay.CUSTOMERSURROGATEKEY);
		qh.setGrouping(DWCustomerStatisticDay.CUSTOMERSURROGATEKEY);
		Condition cond = qh.condition(qp);
		System.out.println("QueryString " + Query.getQueryString(cond));
		PEnumeration cusStatPEnum = _controller.createIteratorFactory().getSelect(cond);
		int i = 0;

		double turnoverAmount = 0.0d;
		while (cusStatPEnum.hasMoreElements()) {
			QueryResultEntry resultEntry = (QueryResultEntry) cusStatPEnum.nextElement();
			double[] returnArray = new double[5];
			double totalTurnoverAmount = 0.0D;
			double totalCostAmount = 0.0D;
			double totalCreditNotesFP = 0.0D;
			long totalNumberOrders = 0;
			long totalNumberCreditNotes = 0;
			
			Long customerSurrogateKey = (Long) resultEntry.get(0);
			if (resultEntry.get(1) != null) {
				totalTurnoverAmount = DoubleUtils.getRoundedAmount((Double) resultEntry.get(1));
			}
			if (resultEntry.get(2) != null) {
				totalCostAmount = DoubleUtils.getRoundedAmount((Double) resultEntry.get(2));
			}

			if (resultEntry.get(3) != null) {
				totalTurnoverAmount = totalTurnoverAmount - DoubleUtils.getRoundedAmount((Double) resultEntry.get(3));
				totalCreditNotesFP= DoubleUtils.getRoundedAmount((Double) resultEntry.get(3));
			}
			
			if (resultEntry.get(4) != null) {
				totalNumberOrders = (Long) resultEntry.get(4);
			}
			if (resultEntry.get(5) != null) {
				totalNumberCreditNotes = (Long) resultEntry.get(5);
			}

			returnArray[0] = totalTurnoverAmount;
			turnoverAmount += totalTurnoverAmount;
			returnArray[1] = totalCostAmount;
			returnArray[2] = totalCreditNotesFP;
			returnArray[3] = totalNumberOrders;
			returnArray[4] = totalNumberCreditNotes;
			
			turnoverMap.put(customerSurrogateKey, returnArray);
//			System.out.println(
//					"customerSurrogateKey " + customerSurrogateKey + " " + returnArray[0] + " " + returnArray[1]+ " " + returnArray[2]);
			i++;
		}
		System.out.println("turnoverMap size " + turnoverMap.size() + " total searched out: " + i);
		System.out.println("turnoverAmount " + turnoverAmount);
		cusStatPEnum.destroy();
		return turnoverMap;
	}
	
	private void getCustomerSet() throws QueryParseException, PUserException {
		QueryHelper qh = Query.newQueryHelper();
		qh.addResultAttribute(DWCustomerInvoiceLine.CUSTOMERSURROGATEKEY);
		QueryPredicate p = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte().val(new PDate(2013,0,1)).predicate();
		qh.setClass(DWCustomerInvoiceLine.class);
		qh.setDistinct(true);
		Condition condition = qh.condition(p);
		PEnumeration enumeration = _controller.createIteratorFactory().getSelect(condition);
		while (enumeration.hasMoreElements()) {
		   QueryResultEntry resultEntry = (QueryResultEntry) enumeration.nextElement();
		   validCustomerSet.add((Long)resultEntry.get(0));
		}
		System.out.println("Valid customer total: "+validCustomerSet.size());
		enumeration.destroy();
	}

	private void writeCASAInfoToTxt(LinkedList<CASABean> casaList) {
		for (int i = 0; i < casaList.size(); i++) {
			CASABean casab = casaList.get(i);
			StringBuffer sb = new StringBuffer();

			// Sales Organization
			sb.append(casab.getWs1SalesOrganisation()).append(BIDateMapping.csvSeperator);
			// Calendar Year/Month
			sb.append(casab.getPeriod()).append(BIDateMapping.csvSeperator);
			// Sales Rep WS1
			sb.append(casab.getWs1RegisterNumber()).append(BIDateMapping.csvSeperator);
			// Customer Number WS1
			sb.append(casab.getWs1CustomerNumber()).append(BIDateMapping.csvSeperator);
			// Customer Status
			sb.append(casab.getWs1CustomerStatus()).append(BIDateMapping.csvSeperator);
			// Orsy Flag
			if (casab.isOrsy()) {
				sb.append('X').append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// Active Customer Flag rolling 12 current Year
			if (casab.getTurnover_r12cy() > 0.0d) {
				sb.append('X').append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// Buying Customer Flag rolling 12 current Year
			if (casab.isBuyingcustomer500_r12cy()) {
				sb.append("X").append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// Zero Customer Flag rolling 12 current Year
			if (casab.isZerocustomer_r12cy()) {
				sb.append("X").append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// New Customer Flag
			if (casab.isNewcustomer_cm()) {
				sb.append("X").append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// New Customer Flag Rolling 12 current Year
			if (casab.isNewcustomer_r12cy()) {
				sb.append("X").append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// Reactivated Customer Flag
			if (casab.isReactivatedcustomer_cm()) {
				sb.append("X").append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// Reactivated Customer Flag Rolling 12 current Year
			if (casab.isReactivatedcustomer_r12cy()) {
				sb.append("X").append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// Lost Customer Flag Current Month
			if (casab.isLostcustomer_cm()) {
				sb.append("X").append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// Lost Customer Flag rolling 12 current Year
			if (casab.isLostcustomer_r12cy()) {
				sb.append("X").append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// SML Classification rolling 12 last Year
			if (casab.getSml_r12ly() != null) {
				sb.append(casab.getSml_r12ly()).append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// SML Classification rolling 12 current Year
			if (casab.getSml_r12cy() != null) {
				sb.append(casab.getSml_r12cy()).append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// TODO SML Classification depending on Potential rolling 12 current
			// Year
			// The classification of the customers for the current month into
			// the dedicated SML-Ranges based on on the customer Potential of
			// the rolling 12 months current year: SML for 201208 based on
			// Potential 201109 - 201208
			if (casab.getSml_potential_r12cy() != null) {
				sb.append(casab.getSml_potential_r12cy()).append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// SEA-N Classification rolling 12 last Year
			if (casab.getSml_n_potential_r12ly() != null) {
				sb.append(casab.getSml_n_potential_r12ly()).append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// SEA-N Classification rolling 12 current Year
			if (casab.getSml_n_potential_r12cy() != null) {
				sb.append(casab.getSml_n_potential_r12cy()).append(BIDateMapping.csvSeperator);
			} else {
				sb.append("").append(BIDateMapping.csvSeperator);
			}
			// Potential
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getPotential()))))
					.append(BIDateMapping.csvSeperator);
			// Turnover rolling 12 Months last Year
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getTurnover_r12ly()))))
					.append(BIDateMapping.csvSeperator);
			// Turnover current Month
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getTurnover_cm()))))
					.append(BIDateMapping.csvSeperator);
			// Turnover rolling 12 Months current Year
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getTurnover_r12cy()))))
					.append(BIDateMapping.csvSeperator);
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getFreightcost_cm()))))
					.append(BIDateMapping.csvSeperator);
			// Cost of Goods PFEP rolling 12 Months last Year
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getCogspfep_r12ly()))))
					.append(BIDateMapping.csvSeperator);
			// Cost of Goods PFEP current Month
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getCogspfep_cm()))))
					.append(BIDateMapping.csvSeperator);
			// Cost of Goods PFEP rolling 12 Months current Year
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getCogspfep_r12cy()))))
					.append(BIDateMapping.csvSeperator);
			// Cost of Goods GLEP rolling 12 Months last Year
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getCogsglep_r12ly()))))
					.append(BIDateMapping.csvSeperator);
			// Cost of Goods GLEP current Month
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getCogsglep_cm()))))
					.append(BIDateMapping.csvSeperator);
			// Cost of Goods GLEP rolling 12 Months current Year
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getCogsglep_r12cy()))))
					.append(BIDateMapping.csvSeperator);
			// Number of Credit Notes last Year
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getNum_of_cn_r12ly()))))
					.append(BIDateMapping.csvSeperator);
			// Number of Credit Notes current Month
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getNum_of_cn_cm()))))
					.append(BIDateMapping.csvSeperator);
			// Number of Credit Notes current Year
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getNum_of_cn_r12cy()))))
					.append(BIDateMapping.csvSeperator);
			// Number of Orders last Year
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getNum_of_ord_r12ly()))))
					.append(BIDateMapping.csvSeperator);
			// Number of Orders current Month
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getNum_of_ord_cm()))))
					.append(BIDateMapping.csvSeperator);
			// Number of Orders current Year
			sb.append(BIDateMapping.convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(casab.getNum_of_ord_r12cy()))))
					.append(BIDateMapping.csvSeperator);
			out1.println(sb.toString());
			numberOfCASAsWritten++;
		}
		System.out.println("\n Number of casas writed in txt 1: " + numberOfCASAsWritten);
		out1.flush();
	}

	public static void writeInvoiceItemHeaderToCSV(PrintWriter out1) {
		StringBuffer sb = new StringBuffer();
		String[] header1 = { "CSALESORG", "CALMONTH", "CTERREP", "CCUST", "CCCASASTA", "CCKATR5", "CCACTCR", "CCBUYCR",
				"CCZEROCR", "CCNEWCM", "CCNEWCR", "CCREACM", "CCREACR", "CCLOSTCM", "CCLOSTCR", "CCSMLLR", "CCSMLCR",
				"CCSMLPOT", "CCSEANLR", "CCSEANCR", "LCCPO", "LCITOALLR", "LCITOALCM", "LCITOALCR", "LCNACFRCM",
				"LCIPPALLR", "LCIPPALCM", "LCIPPALCR", "LCIMPALLR", "LCIMPALCM", "LCIMPALCR", "NCXCHALLY", "NCXCHALCM",
				"NCXCHALCC", "NCXSHALLY", "NCXSHALCM", "NCXSHALCY " };
		for (int i = 0; i < header1.length; i++) {
			sb.append(header1[i]).append(BIDateMapping.csvSeperator);
		}
		out1.println(sb.toString());

		sb = new StringBuffer();
		String[] header2 = { "Sales Organization", "Calendar Year/Month", "Sales Rep WS1", "Customer Number WS1",
				"Customer Status", "Orsy Flag", "Active Customer Flag rolling 12 current Year",
				"Buying Customer Flag rolling 12 current Year", "Zero Customer Flag rolling 12 current Year",
				"New Customer Flag", "New Customer Flag Rolling 12 current Year", "Reactivated Customer Flag",
				"Reactivated Customer Flag Rolling 12 current Year", "Lost Customer Flag Current Month",
				"Lost Customer Flag rolling 12 current Year", "SML Classification rolling 12 last Year",
				"SML Classification rolling 12 current Year",
				"SML Classification depending on Potential rolling 12 current Year",
				"SEA-N Classification rolling 12 last Year", "SEA-N Classification rolling 12 current Year",
				"Potential", "Turnover rolling 12 Months last Year", "Turnover current Month",
				"Turnover rolling 12 Months current Year", "Freight Costs Current Month",
				"Cost of Goods PFEP rolling 12 Months last Year", "Cost of Goods PFEP current Month",
				"Cost of Goods PFEP rolling 12 Months current Year", "Cost of Goods GLEP rolling 12 Months last Year",
				"Cost of Goods GLEP current Month", "Cost of Goods GLEP rolling 12 Months current Year",
				"Number of Credit Notes last Year", "Number of Credit Notes current Month",
				"Number of Credit Notes current Year", "Number of Orders last Year", "Number of Orders current Month",
				"Number of Orders current Year" };
		for (int i = 0; i < header2.length; i++) {
			sb.append(header2[i]).append(BIDateMapping.csvSeperator);
		}
		out1.println(sb.toString());
		out1.flush();
	}
}
