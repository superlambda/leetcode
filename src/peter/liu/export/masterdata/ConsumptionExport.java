package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.wuerth.phoenix.Phxbasic.enums.DemandType;
import com.wuerth.phoenix.Phxbasic.models.Demand;
import com.wuerth.phoenix.Phxbasic.models.WarehouseProductAllowance;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi.BIDateMapping;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;

/**
 * ConsumptionExport, First export to a txt file ../../var/exportSAP/Consumption_template.txt", then open the
 * file with LibreOffice or other office, make the comma as separator, and select the first column and third
 * column 's type as text, then save as office xlsx file.
 * 
 * Filter logic:
 * 
 * 1:Export demand of product has one--one relation between SAP and Sylvestrix 2:Export demand of product has
 * one--more relation between SAP and Sylvestrix 3:If the demand of product has zero--one relation between SAP
 * and Sylvestrix, does not export 4:Consumption<=0, does not export
 * 
 * @author pcnsh197
 * 
 */
public class ConsumptionExport extends BatchRunner {

	private int					NUMBER_OF_BATCH_TO_FECTCH				= 100;

//	LinkedList<ConsumptionBean>	consumptionBeanList						= new LinkedList<ConsumptionBean>();
//
//	LinkedList<ConsumptionBean>	consumptionBeanListOfDuplicatedProduct	= new LinkedList<ConsumptionBean>();
	private Map<String,ConsumptionBean> consumptionBeanMap =new LinkedHashMap<String,ConsumptionBean>();

//	private String				targetTxt								= "../../var/exportSAP/Consumption_template.txt";

	private int					consumptionWrited						= 0;

	// private String ownCompanyName;
	private HashSet<String>		gzWH									= new HashSet<String>();

	private HashSet<String>		szWH									= new HashSet<String>();

	private HashSet<String>		cqWH									= new HashSet<String>();

	private HashSet<String>		cdWH									= new HashSet<String>();

	private String				project;																					// GZ,SZ,CQ,CD

	@Override
	protected void processargs(String[] args) {
		for (String argstr : args) {
			if (argstr.equalsIgnoreCase("commit")) {
				willcommit = true;
			} else {
				project = argstr;
			}
		}

		System.out.println("###################project: " + project);
	}

	@Override
	protected void batchMethod() throws TimestampException, PUserException {
//		ownCompanyName = _controller.getSingletonOwnCompany().getName();

		gzWH.add("A");
//		gzWH.add("B");
		gzWH.add("C");
		gzWH.add("D");
		gzWH.add("A-S");
		gzWH.add("A-W");
		

		szWH.add("SZ-A");
		szWH.add("SZ-B");
		szWH.add("CQ-A");

		cdWH.add("CD-A");
		cdWH.add("CD-B");
		cdWH.add("CD-GZ");
		cdWH.add("CD-D");
		cqWH.add("CQ-A");
		cqWH.add("CQ-B");
		cqWH.add("CQ-GZ");
		cqWH.add("CQ-D");

		searchDemandOfUniqueProduct();
		writeConsumptionInfoToTxt();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ConsumptionExport().startBatch(args);
	}	

	private QueryPredicate getQP(QueryPredicate p, QueryHelper qh, String wh) {
		QueryPredicate temp = qh
				.attr(Demand.PARENT_WAREHOUSEPRODUCTALLOWANCE_REF
						+ WarehouseProductAllowance.WAREHOUSENUMBER).eq()
				.val(wh).predicate();
		return p = p == null ? temp : p.or(temp);
	}

	private void searchDemandOfUniqueProduct() throws TimestampException,
			PUserException {
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(Demand.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(Demand.PARENT_WAREHOUSEPRODUCTALLOWANCE_REF
				+ WarehouseProductAllowance.PRODUCTNUMBER);
		qh.addAscendingOrdering(Demand.DEMANDPERIOD);
		QueryPredicate qp = qh.attr(Demand.DEMANDTYPE).eq()
				.val(DemandType.SALES).predicate();
		//TODO only one year before will be counted, will be changed each time.
		
		qp.and(qh.attr(Demand.DEMANDPERIOD).gte()
				.val(201310).predicate());
		QueryPredicate p = null;
		if (project.equals("GZ")) {
			for (String wh : gzWH) {
				p = getQP(p, qh, wh);
			}
		}
		if (project.equals("SZ")) {
			for (String wh : szWH) {
				p = getQP(p, qh, wh);
			}
		}
		if (project.equals("CQ")) {
			for (String wh : cqWH) {
				p = getQP(p, qh, wh);
			}
		}
		if (project.equals("CD")) {
			for (String wh : cdWH) {
				p = getQP(p, qh, wh);
			}
		}
		Condition cond = qh.condition(qp.and(p));
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<Demand> list = new ArrayList<Demand>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			for (Demand demand : list) {
				String productNumber = demand
						.getParentWarehouseProductAllowance()
						.getProductNumber();
				// Product without mapping will not be exported.
				if (!BIDateMapping.productMap.containsKey(productNumber)) {
					continue;
				}
				if (demand.getDemandQuantity().getAmount() <= 0.0D) {
					continue;
				}
				ConsumptionBean cb = new ConsumptionBean();
				cb.setProductNumber(BIDateMapping.productMap.get(productNumber));
//				cb.setProductNumber(productNumber);
				cb.setPeriod(String.valueOf(demand.getDemandPeriod()));
				
				if (project.equals("CD")) {
					cb.setPlant("BN00");
				} else if (project.equals("CQ")) {
					cb.setPlant("BP00");
				} else if (project.equals("GZ")) {
					cb.setPlant("BL00");
				} else if (project.equals("SZ")) {
					cb.setPlant("BL10");
				}
				cb.setConsumption((int)demand.getDemandQuantity().getAmount());
				String key=cb.getProductNumber()+cb.getPlant()+cb.getPeriod();
				if(!consumptionBeanMap.containsKey(key)){
					consumptionBeanMap.put(key, cb);
				}else{
					ConsumptionBean cbInMap=consumptionBeanMap.get(key);
					cbInMap.setConsumption(cbInMap.getConsumption()+cb.getConsumption());
				}
				System.out.println("\n Number of demands loaded: "
						+ consumptionBeanMap.size());
			}
		}
		penum.destroy();
	}

	private void writeConsumptionInfoToTxt() {
		System.out.println("\n(!) Write txt file  start.\n");
		try {
			String targetTxt="../../var/exportSAP/Consumption_"+project+".txt";
			FileWriter outFile = new FileWriter(targetTxt);
			PrintWriter out1 = new PrintWriter(outFile);
			StringBuffer title = new StringBuffer();
			title.append("Material").append("	").append("Plant").append("	")
					.append("Date (MMYYYY)").append("	").append(" ").append("	").append("consumption");
			out1.println(title);
			for (ConsumptionBean cb: consumptionBeanMap.values()) {
				StringBuffer sb = new StringBuffer();
				sb.append(cb.getProductNumber())
						.append("	")
						.append(cb.getPlant())
						.append("	")
						.append(cb.getPeriod()+"01")
						.append("	")
						.append(" ")
						.append("	")
						.append(cb.getConsumption());

				out1.println(sb.toString());
				consumptionWrited++;
				System.out.println("\n Number of consumptions writed in txt: "
						+ consumptionWrited);
			}
			out1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n(*) Write txt file  end.");
	}
}
