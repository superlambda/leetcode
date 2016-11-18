package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.enums.SampleGoodsLineStatus;
import com.wuerth.phoenix.Phxbasic.models.Product;
import com.wuerth.phoenix.Phxbasic.models.Quant;
import com.wuerth.phoenix.Phxbasic.models.SampleGoodsLine;
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
 * StockValueExport
 * 
 * @author pcnsh197
 * 
 */
public class MAPLoadExport extends BatchRunner {
	private int								NUMBER_OF_BATCH_TO_FECTCH	= 100;

	private LinkedList<StockValueBean>		mapBeanList					= new LinkedList<StockValueBean>();

	private LinkedList<StockValueBean>		carbootMapBeanList			= new LinkedList<StockValueBean>();

	private LinkedList<StockValueBean>		qualityMapBeanList			= new LinkedList<StockValueBean>();

	private LinkedList<StockValueBean>		specialMapBeanList			= new LinkedList<StockValueBean>();
	
	private TreeMap<String,StockValueBean>  mainStockvalueMap= new TreeMap<String,StockValueBean>();
	private TreeMap<String,StockValueBean>  carbootStockvalueMap= new TreeMap<String,StockValueBean>();
	private TreeMap<String,StockValueBean>  qualityStockvalueMap= new TreeMap<String,StockValueBean>();
	private TreeMap<String,StockValueBean>  specialStockvalueMap= new TreeMap<String,StockValueBean>();

	private TreeMap<String, StockValueBean>	uniqueStockValueBeanMap		= new TreeMap<String, StockValueBean>();

	private String				dataSource	= "../../etc/exportSAP/MAP_load_template.xlsx";

	private String				target		= "../../var/exportSAP/MAP_load_";

	private double							totalStockValue				= 0.0d;

	private MathContext						mathContext					= new MathContext(
																				13);

	private HashSet<String>					gzWH						= new HashSet<String>();

	private HashSet<String>					szWH						= new HashSet<String>();

	private HashSet<String>					cqWH						= new HashSet<String>();

	private HashSet<String>					cdWH						= new HashSet<String>();

	private HashSet<String>					gzSpecialWH					= new HashSet<String>();

	private HashSet<String>					cqQuantityWH				= new HashSet<String>();

	private HashSet<String>					cdQuantityWH				= new HashSet<String>();

	private String							project;																		// GZ,SZ,CQ,CD

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
		gzWH.add("A");
//		gzWH.add("B");
		gzWH.add("C");
		gzWH.add("D");
		gzWH.add("A-S");
		gzWH.add("A-W");
		gzWH.add("CQ-A");

		gzSpecialWH.add("C");
		gzSpecialWH.add("A-W");

		szWH.add("SZ-A");
		szWH.add("SZ-B");

		cdWH.add("CD-A");
		cdWH.add("CD-B");
		cdWH.add("CD-GZ");
		cdWH.add("CD-D");

		cqWH.add("CQ-A");
		cqWH.add("CQ-D");
		cqWH.add("CQ-B");
		cqWH.add("CQ-GZ");

		cdQuantityWH.add("CD-D");
		cqQuantityWH.add("CQ-D");
		searchWPA();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MAPLoadExport().startBatch(args);
	}

	private QueryPredicate getQP(QueryPredicate p, QueryHelper qh, String wh) {
		QueryPredicate temp = qh
				.attr(WarehouseProductAllowance.WAREHOUSENUMBER).eq().val(wh)
				.predicate();
		return p = p == null ? temp : p.or(temp);
	}

	private void searchWPA() throws TimestampException, PUserException {
		QueryHelper qh = Query.newQueryHelper();
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

		qh.setClass(WarehouseProductAllowance.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(WarehouseProductAllowance.WAREHOUSENUMBER);
		qh.addAscendingOrdering(WarehouseProductAllowance.PRODUCTNUMBER);
		Condition cond = qh.condition(p);
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<WarehouseProductAllowance> list = new ArrayList<WarehouseProductAllowance>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			for (WarehouseProductAllowance wpa : list) {
				if (wpa.getPhysicalStock().getQuantityUnit().getAmount() > 0
						&& wpa.getPhysicalStock().getQuantityUnit().getAmount() < 2147483647) {
					List<Quant> quantList = new ArrayList<Quant>(
							wpa.getAllChildQuant());
					for (Quant quant : quantList) {
						String location = quant.getWarehouseLocation()
								.getFullLocationName();
						StockValueBean mb = new StockValueBean();
						if (cdWH.contains(wpa.getWarehousenumber())) {
							mb.setPlant("BN00");
						} else if (cqWH.contains(wpa.getWarehousenumber())) {
							mb.setPlant("BP00");
						} else if (gzWH.contains(wpa.getWarehousenumber())) {
							mb.setPlant("BL00");
						} else if (szWH.contains(wpa.getWarehousenumber())) {
							mb.setPlant("BL10");
						}

						mb.setWarehouseNumber(wpa.getWarehousenumber());
						String productNumber = wpa.getProductNumber();
						mb.setProductNumber(productNumber);
						mb.setSapProductNumber(BIDateMapping.productMap.get(mb
								.getProductNumber()));
						mb.setLocation(location);
						mb.setValidKey("D-QU");
						mb.setTotalStock(quant.getPhysicalStock().getAmount());
						mb.setMap(wpa.getAverageCostPrice().getAmount());
						mb.setPriceUnit(1);
						mb.setTotalStockValue(mb.getMap() * mb.getTotalStock());
						totalStockValue += mb.getTotalStockValue();
						if (gzSpecialWH.contains(mb.getWarehouseNumber())) {
							specialMapBeanList.addLast(mb);
						} else if (cdQuantityWH.contains(mb
								.getWarehouseNumber())
								|| cqQuantityWH.contains(mb
										.getWarehouseNumber())
								|| (mb.getWarehouseNumber().equals("SZ-A") && (mb
										.getLocation().equals("SD2012") || mb
										.getLocation().equals("SD2013")))) {
							qualityMapBeanList.addLast(mb);
						} else {
							String sylvestrixUniqueKey = getSylvestrixUniquekey(mb);
							if (!uniqueStockValueBeanMap
									.containsKey(sylvestrixUniqueKey)) {
								uniqueStockValueBeanMap.put(
										sylvestrixUniqueKey, mb);
							} else {
								putBeanToMapBasedOnLocation(
										uniqueStockValueBeanMap, mb);
							}
						}
						System.out
								.println("\n(*) Number of StockValueBean Loaded:"
										+ uniqueStockValueBeanMap.size());
					}

				}

			}
			_context.commit();
		}
		penum.destroy();

		// Add bean to List
		mapBeanList.addAll(uniqueStockValueBeanMap.values());
		fetchCarbootStock();
		System.out.println("\n(!) Warning totalStockValue " + totalStockValue);

		if (project.equals("CQ")) {
			moveMainStockToCarbootStock();
		}
		mergeDuplicatedLineBasedOnSapProductNumber(mapBeanList,mainStockvalueMap);
		if(!project.equals("SZ")){
			mergeDuplicatedLineBasedOnSapProductNumber(carbootMapBeanList,carbootStockvalueMap);
		}
		if(project.equals("GZ")){
			mergeDuplicatedLineBasedOnSapProductNumber(fetchSZCarbootStock(),carbootStockvalueMap);
		}
		
		mergeDuplicatedLineBasedOnSapProductNumber(specialMapBeanList,specialStockvalueMap);
		mergeDuplicatedLineBasedOnSapProductNumber(qualityMapBeanList,qualityStockvalueMap);
		calculateAverageMap();
		getFinalMap();
		
		
		writeMapInfoToExcel();
	}
	
	private void calculateAverageMap(){
		
		TreeMap<String,StockValueBean>  averageMap= new TreeMap<String,StockValueBean>();
		for(StockValueBean svb :mainStockvalueMap.values()){
			StockValueBean newSVB =new StockValueBean();
			newSVB.setProductNumber(svb.getProductNumber());
			newSVB.setSapProductNumber(svb.getSapProductNumber());
			newSVB.setMap(svb.getMap());
			newSVB.setPlant(svb.getPlant());
			newSVB.setTotalStock(svb.getTotalStock());
			newSVB.setTotalStockValue(svb.getTotalStockValue());
			averageMap.put(getUniqueKeyBasedOnSapProductNumber(newSVB), newSVB);
		}
		
		for (StockValueBean svb : specialStockvalueMap.values()) {
			if (averageMap
					.containsKey(getUniqueKeyBasedOnSapProductNumber(svb))) {
				StockValueBean svbInMap = averageMap
						.get(getUniqueKeyBasedOnSapProductNumber(svb));

				svbInMap.setTotalStockValue(svbInMap.getMap()
						* svbInMap.getTotalStock() + svb.getMap()
						* svb.getTotalStock());
				svbInMap.setTotalStock(svbInMap.getTotalStock()
						+ svb.getTotalStock());
				svbInMap.setMap(new BigDecimal(svbInMap.getTotalStockValue()
						/ svbInMap.getTotalStock(), mathContext).doubleValue());
				averageMap.put(getUniqueKeyBasedOnSapProductNumber(svbInMap),
						svbInMap);
			}else{
				StockValueBean newSVB =new StockValueBean();
				newSVB.setProductNumber(svb.getProductNumber());
				newSVB.setSapProductNumber(svb.getSapProductNumber());
				newSVB.setMap(svb.getMap());
				newSVB.setPlant(svb.getPlant());
				newSVB.setTotalStock(svb.getTotalStock());
				newSVB.setTotalStockValue(svb.getTotalStockValue());
				averageMap.put(getUniqueKeyBasedOnSapProductNumber(newSVB), newSVB);
			}
		}
		
		for (StockValueBean svb : qualityStockvalueMap.values()) {
			if (averageMap
					.containsKey(getUniqueKeyBasedOnSapProductNumber(svb))) {
				StockValueBean svbInMap = averageMap
						.get(getUniqueKeyBasedOnSapProductNumber(svb));

				svbInMap.setTotalStockValue(svbInMap.getMap()
						* svbInMap.getTotalStock() + svb.getMap()
						* svb.getTotalStock());
				svbInMap.setTotalStock(svbInMap.getTotalStock()
						+ svb.getTotalStock());
				svbInMap.setMap(new BigDecimal(svbInMap.getTotalStockValue()
						/ svbInMap.getTotalStock(), mathContext).doubleValue());
				averageMap.put(getUniqueKeyBasedOnSapProductNumber(svbInMap),
						svbInMap);
			}else{
				StockValueBean newSVB =new StockValueBean();
				newSVB.setProductNumber(svb.getProductNumber());
				newSVB.setSapProductNumber(svb.getSapProductNumber());
				newSVB.setMap(svb.getMap());
				newSVB.setPlant(svb.getPlant());
				newSVB.setTotalStock(svb.getTotalStock());
				newSVB.setTotalStockValue(svb.getTotalStockValue());
				averageMap.put(getUniqueKeyBasedOnSapProductNumber(newSVB), newSVB);
			}
		}
		
		
		for (StockValueBean svb : mainStockvalueMap.values()) {
			if (averageMap
					.containsKey(getUniqueKeyBasedOnSapProductNumber(svb))) {
				StockValueBean svbInMap = averageMap
						.get(getUniqueKeyBasedOnSapProductNumber(svb));

				svb.setMap(svbInMap.getMap());
				svb.setTotalStockValue(svb.getMap() * svb.getTotalStock());
				mainStockvalueMap.put(getUniqueKeyBasedOnSapProductNumber(svb),
						svb);
			}
		}
		
		for (StockValueBean svb : specialStockvalueMap.values()) {
			if (averageMap
					.containsKey(getUniqueKeyBasedOnSapProductNumber(svb))) {
				StockValueBean svbInMap = averageMap
						.get(getUniqueKeyBasedOnSapProductNumber(svb));

				svb.setMap(svbInMap.getMap());
				svb.setTotalStockValue(svb.getMap() * svb.getTotalStock());
				specialStockvalueMap.put(getUniqueKeyBasedOnSapProductNumber(svb),
						svb);
			}
		}
		
		for (StockValueBean svb : qualityStockvalueMap.values()) {
			if (averageMap
					.containsKey(getUniqueKeyBasedOnSapProductNumber(svb))) {
				StockValueBean svbInMap = averageMap
						.get(getUniqueKeyBasedOnSapProductNumber(svb));

				svb.setMap(svbInMap.getMap());
				svb.setTotalStockValue(svb.getMap() * svb.getTotalStock());
				qualityStockvalueMap.put(getUniqueKeyBasedOnSapProductNumber(svb),
						svb);
			}
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	

	private String getSylvestrixUniquekey(StockValueBean svb) {
		return svb.getWarehouseNumber() + svb.getProductNumber()
				+ svb.getLocation();
	}

	private void putBeanToMapBasedOnLocation(
			TreeMap<String, StockValueBean> uniqueStockValueBeanMap,
			StockValueBean svb) {
		String uniqueKey = getSylvestrixUniquekey(svb);
		StockValueBean beanFound = uniqueStockValueBeanMap.get(uniqueKey);
		beanFound.setTotalStockValue(beanFound.getMap()
				* beanFound.getTotalStock() + svb.getMap()
				* svb.getTotalStock());
		beanFound.setTotalStock(beanFound.getTotalStock()
				+ svb.getTotalStock());
		uniqueStockValueBeanMap.put(uniqueKey, beanFound);
	}

	private void moveMainStockToCarbootStock() {
		LinkedList<StockValueBean> tempList = mapBeanList;
		mapBeanList = new LinkedList<StockValueBean>();
		for (StockValueBean mb : tempList) {
			if (mb.getWarehouseNumber().equals("CQ-B")
					|| mb.getWarehouseNumber().equals("CQ-GZ")) {
				mb.setPlant("BP90");
				carbootMapBeanList.add(mb);
			} else {
				mapBeanList.add(mb);
			}
		}
	}

	private QueryPredicate getSampleGoodsQP(QueryPredicate p, QueryHelper qh,
			String wh) {
		QueryPredicate temp = qh.attr(SampleGoodsLine.WAREHOUSENUMBER).eq()
				.val(wh).predicate();
		return p = p == null ? temp : p.or(temp);
	}

	private void fetchCarbootStock() throws PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p = qh.attr(SampleGoodsLine.STATUS).ne()
				.val(SampleGoodsLineStatus.CLOSED).predicate();
		QueryPredicate p1 = null;
		if (project.equals("GZ")) {
			for (String wh : gzWH) {
				p1 = getSampleGoodsQP(p1, qh, wh);
			}
			
		}
		if (project.equals("SZ")) {
			for (String wh : szWH) {
				p1 = getSampleGoodsQP(p1, qh, wh);
			}
		}

		if (project.equals("CQ")) {
			for (String wh : cqWH) {
				p1 = getSampleGoodsQP(p1, qh, wh);
			}
		}
		if (project.equals("CD")) {
			for (String wh : cdWH) {
				p1 = getSampleGoodsQP(p1, qh, wh);
			}
		}
		qh.setClass(SampleGoodsLine.class);
		qh.setDeepSelect(true);
		qh.addDescendingOrdering(SampleGoodsLine.CREATEDATE);
		Condition cond = qh.condition(p.and(p1));
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);

		while (penum.hasMoreElements()) {
			SampleGoodsLine sgl = (SampleGoodsLine) penum.nextElement();

			StockValueBean mb = new StockValueBean();
			if (cdWH.contains(sgl.getWarehouseNumber())) {
				mb.setPlant("BN90");
			} else if (cqWH.contains(sgl.getWarehouseNumber())) {
				mb.setPlant("BP90");
			} else if (gzWH.contains(sgl.getWarehouseNumber())) {
				mb.setPlant("BL90");
			} else if (szWH.contains(sgl.getWarehouseNumber())) {
				mb.setPlant("BM90");
			}

			mb.setWarehouseNumber(sgl.getWarehouseNumber());
			String productNumber = sgl.getProductNumber();
			mb.setProductNumber(productNumber);
			mb.setSapProductNumber(BIDateMapping.productMap.get(mb
					.getProductNumber()));
			mb.setLocation(sgl.getWarehouseLocation().getFullLocationName());
			mb.setValidKey("D-QU");
			mb.setTotalStock(sgl.getQuantityAssigned()
					.subtract(sgl.getQuantityReturned())
					.subtract(sgl.getQuantityConsumed()).getAmount());
			mb.setMap(_controller.lookupProduct(sgl.getProductNumber())
					.lookupWarehouseProductAllowance(sgl.getWarehouseNumber())
					.getAverageCostPrice().getAmount());
			mb.setPriceUnit(1);
			mb.setTotalStockValue(mb.getMap() * mb.getTotalStock());

			mergeCarbootStock(mb);
			substractCarbootStock(mb);
		}
		penum.destroy();

	}
	
	private LinkedList<StockValueBean> fetchSZCarbootStock() throws PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p = qh.attr(SampleGoodsLine.STATUS).ne()
				.val(SampleGoodsLineStatus.CLOSED).predicate();
		QueryPredicate p1 = null;
		for (String wh : szWH) {
			p1 = getSampleGoodsQP(p1, qh, wh);
		}
		
		qh.setClass(SampleGoodsLine.class);
		qh.setDeepSelect(true);
		qh.addDescendingOrdering(SampleGoodsLine.CREATEDATE);
		Condition cond = qh.condition(p.and(p1));
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		LinkedList<StockValueBean> szCarbootList=new LinkedList<StockValueBean>();
		while (penum.hasMoreElements()) {
			SampleGoodsLine sgl = (SampleGoodsLine) penum.nextElement();
			StockValueBean mb = new StockValueBean();
			mb.setPlant("BL90");
			mb.setWarehouseNumber(sgl.getWarehouseNumber());
			String productNumber = sgl.getProductNumber();
			mb.setProductNumber(productNumber);
			mb.setSapProductNumber(BIDateMapping.productMap.get(mb
					.getProductNumber()));
			mb.setLocation(sgl.getWarehouseLocation().getFullLocationName());
			mb.setValidKey("D-QU");
			mb.setTotalStock(sgl.getQuantityAssigned()
					.subtract(sgl.getQuantityReturned())
					.subtract(sgl.getQuantityConsumed()).getAmount());
			mb.setMap(_controller.lookupProduct(sgl.getProductNumber())
					.lookupWarehouseProductAllowance(sgl.getWarehouseNumber())
					.getAverageCostPrice().getAmount());
			szCarbootList.add(mb);
		}
		penum.destroy();
		return szCarbootList;

	}
	
	
	

	private void mergeCarbootStock(StockValueBean mapb) {
		boolean foundExistingMB = false;
		for (StockValueBean mb : carbootMapBeanList) {
			if (getSylvestrixUniquekey(mb).equals(getSylvestrixUniquekey(mapb))) {
				mb.setTotalStock(mb.getTotalStock() + mapb.getTotalStock());
				mb.setTotalStockValue(mb.getMap() * mb.getTotalStock());
				foundExistingMB = true;
				break;
			}
		}
		if (!foundExistingMB) {
			carbootMapBeanList.add(mapb);
		}
	}

	private void substractCarbootStock(StockValueBean mapb) {
		for (StockValueBean mb : mapBeanList) {
			if (getSylvestrixUniquekey(mb).equals(getSylvestrixUniquekey(mapb))) {
				mb.setTotalStock(mb.getTotalStock() - mapb.getTotalStock());
				mb.setTotalStockValue(mb.getMap() * mb.getTotalStock());
				if (mb.getTotalStock() == 0.0D) {
					mapBeanList.remove(mb);
				}
				break;
			}
		}
		// CQ has quality stock in carboot.
		if(project.equals("CQ")){
			for (StockValueBean mb : qualityMapBeanList) {
				if (getSylvestrixUniquekey(mb).equals(getSylvestrixUniquekey(mapb))) {
					mb.setTotalStock(mb.getTotalStock() - mapb.getTotalStock());
					mb.setTotalStockValue(mb.getMap() * mb.getTotalStock());
					if (mb.getTotalStock() == 0.0D) {
						qualityMapBeanList.remove(mb);
					}

					break;
				}
			}
		}
	}
	
	private void mergeDuplicatedLineBasedOnSapProductNumber(
			LinkedList<StockValueBean> mapBeanList,
			TreeMap<String, StockValueBean> stockvalueMap) {
		for (StockValueBean tempSVB : mapBeanList) {
			String uniqueKey = getUniqueKeyBasedOnSapProductNumber(tempSVB);
			if (stockvalueMap.containsKey(uniqueKey)) {
				StockValueBean svbInMap = stockvalueMap.get(uniqueKey);
				svbInMap.setTotalStockValue(svbInMap.getMap()
						* svbInMap.getTotalStock() + tempSVB.getMap()
						* tempSVB.getTotalStock());
				svbInMap.setTotalStock(svbInMap.getTotalStock()
						+ tempSVB.getTotalStock());
				if (svbInMap.getTotalStock() == 0.0D) {
					stockvalueMap.remove(uniqueKey);
				} else {
					svbInMap.setMap(new BigDecimal(svbInMap
							.getTotalStockValue() / svbInMap.getTotalStock(),
							mathContext).doubleValue());
				}
				stockvalueMap.put(uniqueKey, svbInMap);
				System.out.println("\n(!) Stock value merged: "
						+ tempSVB.getProductNumber() + "+"
						+ svbInMap.getProductNumber() + "----->"
						+ svbInMap.getSapProductNumber() + " stock value:"
						+ +svbInMap.getTotalStockValue());
			} else {
				stockvalueMap.put(uniqueKey, tempSVB);
			}
		}
	}
	
	private String getUniqueKeyBasedOnSapProductNumber(StockValueBean svb) {
		if (!"".equals(svb.getSapProductNumber())) {
			return svb.getPlant() + svb.getSapProductNumber();
		} else {
			return svb.getPlant() + svb.getProductNumber();
		}
	}

	private void writeMapInfoToExcel() {
		System.out.println("\n(!) Read excel file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					dataSource));
			XSSFSheet sheet = workbook.getSheetAt(0);
			System.out.println("sheet: " + sheet);
			writeToSheet(sheet, mainStockvalueMap);

			sheet = workbook.getSheetAt(1);
			System.out.println("sheet: " + sheet);
			writeToSheet(sheet, carbootStockvalueMap);
			target = target + project + ".xlsx";
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
		System.out.println("\n(!) Read excel file  end.\n");

	}
	
	private void writeToSheet(XSSFSheet sheet,
			TreeMap<String, StockValueBean> map) {
		int startRow = 1;
		for (StockValueBean mb : map.values()) {
			mb.roundMAP();
			XSSFRow row = sheet.getRow(startRow);
			if (row == null) {
				row = sheet.createRow(startRow);
			}

			XSSFCell cellSapProductNumber = row.getCell(0);
			if (cellSapProductNumber == null) {
				cellSapProductNumber = row.createCell(0);
			}
			XSSFCell cellSapPlantNumber = row.getCell(1);
			if (cellSapPlantNumber == null) {
				cellSapPlantNumber = row.createCell(1);
			}

			XSSFCell cellMap = row.getCell(2);
			if (cellMap == null) {
				cellMap = row.createCell(2);
			}

			XSSFCell cellPriceUnit = row.getCell(3);
			if (cellPriceUnit == null) {
				cellPriceUnit = row.createCell(3);
			}

			XSSFCell cellProductNumber = row.getCell(4);
			if (cellProductNumber == null) {
				cellProductNumber = row.createCell(4);
			}

			XSSFCell cellOriginalMap = row.getCell(5);
			if (cellOriginalMap == null) {
				cellOriginalMap = row.createCell(5);
			}
			cellSapPlantNumber.setCellValue(mb.getPlant());

			cellSapProductNumber.setCellValue(mb.getSapProductNumber());
			cellProductNumber.setCellValue(mb.getProductNumber());
			cellMap.setCellValue(mb.getMap());
			cellPriceUnit.setCellValue(mb.getPriceUnit());
			startRow++;
			System.out.println("\n Number of maps writed in sheet : "
					+ (startRow));
		}
	}
	

	private void getFinalMap() {
		String plant="";
		if (project.equals("CD")) {
			plant="BN00";
		} else if (project.equals("CQ")) {
			plant="BP00";
		} else if (project.equals("GZ")) {
			plant="BL00";
		} else if (project.equals("SZ")) {
			plant="BL10";
		}
		for (String productNumber : BIDateMapping.productMap.keySet()) {
			Product product = _controller.lookupProduct(productNumber);
			if (product == null) {
				System.out
						.println("\n sylvetrix product number in mapping file not found :_"
								+ productNumber + "_");
				continue;
			}
			String sapProductNumber = BIDateMapping.productMap
					.get(productNumber);
			String key = plant + sapProductNumber;
			StockValueBean svb = mainStockvalueMap.get(key);
			if (svb == null) {
				svb = specialStockvalueMap.get(key);
				if(svb!=null){
					mainStockvalueMap.put(key, svb);
				}
			}
			if (svb == null) {
				svb = qualityStockvalueMap.get(key);
				if(svb!=null){
					mainStockvalueMap.put(key, svb);
				}
			}

			StockValueBean newSVB = new StockValueBean();
			newSVB.setProductNumber(productNumber);
			newSVB.setSapProductNumber(sapProductNumber);
			newSVB.setPlant(plant);
			newSVB.setPriceUnit(1);
			newSVB.setTotalStock(0);
			newSVB.setTotalStockValue(0);
			newSVB.setMap(product.getStandardCostPrice().getAmount());
			if (svb == null) {
				mainStockvalueMap.put(key, newSVB);
			} else {
				if (svb.getTotalStock() == 0.0D) {
					svb.setMap(new BigDecimal(
							(svb.getMap() + newSVB.getMap()) / 2, mathContext)
							.doubleValue());
					mainStockvalueMap.put(key, svb);
				}
			}

		}
	}

	
}
