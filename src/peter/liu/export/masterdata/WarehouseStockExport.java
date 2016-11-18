package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.wuerth.phoenix.util.PDate;

/**
 * WarehouseStockExport
 * 
 * @author pcnsh197
 * 
 */
public class WarehouseStockExport extends BatchRunner {

	private int									NUMBER_OF_BATCH_TO_FECTCH		= 100;

	private List<WarehouseStockBean>			warehouseStockBeanList			= null;

	private List<WarehouseStockBean>			carbootwarehouseStockBeanList	= new ArrayList<WarehouseStockBean>();

	private List<WarehouseStockBean>			specialwarehouseStockBeanList	= new ArrayList<WarehouseStockBean>();

	private List<WarehouseStockBean>			qualitywarehouseStockBeanList	= new ArrayList<WarehouseStockBean>();

	private TreeMap<String, WarehouseStockBean>	mainStockMap					= new TreeMap<String, WarehouseStockBean>();

	private TreeMap<String, WarehouseStockBean>	carbootStockMap					= new TreeMap<String, WarehouseStockBean>();

	private TreeMap<String, WarehouseStockBean>	specialStockMap					= new TreeMap<String, WarehouseStockBean>();

	private TreeMap<String, WarehouseStockBean>	qualityStockMap					= new TreeMap<String, WarehouseStockBean>();

	private String								dataSource						= "../../etc/exportSAP/WM_Templates_CN_South.xlsx";

//	private String								target							= "../../var/exportSAP/WM_Templates_CN_South.xlsx";

	private HashSet<String>						gzWH							= new HashSet<String>();

	private HashSet<String>						gzSpecialWH						= new HashSet<String>();

	private HashSet<String>						szWH							= new HashSet<String>();

	private HashSet<String>						cqWH							= new HashSet<String>();

	private HashSet<String>						cqQuantityWH					= new HashSet<String>();

	private HashSet<String>						cdWH							= new HashSet<String>();

	private HashSet<String>						cdQuantityWH					= new HashSet<String>();

	private DateFormat							df								= new SimpleDateFormat(
																						"yyyyMMdd");

	private String								project;																			// GZ,SZ,CQ,CD

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
		cdQuantityWH.add("CD-D");

		cqWH.add("CQ-A");
		cqWH.add("CQ-B");
		cqWH.add("CQ-GZ");
		cqWH.add("CQ-D");
		cqQuantityWH.add("CQ-D");

		searchWarehouseStockBean();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new WarehouseStockExport().startBatch(args);
	}

	private QueryPredicate getQP(QueryPredicate p, QueryHelper qh, String wh) {
		QueryPredicate temp = qh
				.attr(Quant.PARENT_WAREHOUSEPRODUCTALLOWANCE_REF
						+ WarehouseProductAllowance.WAREHOUSENUMBER).eq()
				.val(wh).predicate();
		return p = p == null ? temp : p.or(temp);
	}

	private void searchWarehouseStockBean() throws PUserException {
		List<Quant> quantList = new ArrayList<Quant>();
		QueryHelper qh = Query.newQueryHelper();
		qh.addAscendingOrdering(Quant.PARENT_WAREHOUSEPRODUCTALLOWANCE_REF
				+ WarehouseProductAllowance.PARENT_PRODUCT_REF
				+ Product.PRODUCTNUMBER);
		qh.setClass(Quant.class);

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

		Condition condition = qh.condition(p);
		PEnumeration penum = _controller.createIteratorFactory().getCursor(
				condition);
		while (penum.hasMoreElements()) {
			quantList.addAll(penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			System.out.println("\n Number of quants loaded: "
					+ quantList.size());
		}

		warehouseStockBeanList = WarehouseStockUtils
				.getSortedWarehouseStockBeanList(quantList);
		penum.destroy();

		double totalStock = 0.0D;
		for (WarehouseStockBean wsb : warehouseStockBeanList) {
			totalStock += wsb.getStock();
		}
		System.out.println("\n totalStock: " + totalStock);
		fetchCarbootStock();

		filterWarehouseStock();
		mergeMainStock();
		mergeCarbootStock();
		mergeSpecialStock();
		mergeQualityStock();
		writeStockInfoToExcel();

	}

	private void filterWarehouseStock() {
		for (int i = 0; i < warehouseStockBeanList.size(); i++) {
			WarehouseStockBean wsb = warehouseStockBeanList.get(i);
			wsb.setValuationType("D-QU");
			wsb.setSapProductNumber(BIDateMapping.productMap.get(wsb
					.getProductNumber()));
			if (wsb.getSapProductNumber() == null
					|| wsb.getSapProductNumber().trim().equals("")) {
				System.out.println("\n(*)sap product not found for product: "
						+ wsb.getProductNumber());
			}

			// wsb.setStorageLocation("1000");
			if (project.equals("GZ")) {
				// main warehouse
				if (!gzSpecialWH.contains(wsb.getWarehouseNumber())) {
					String ws1Location = MasterDataMappingSouth.gzLocationMap
							.get(wsb.getWarehouseLocation());
					if (ws1Location == null) {
						System.out
								.println("\n(*) WARNING! GZ main location mapping not exists:"
										+ wsb.getWarehouseNumber()
										+ " "
										+ wsb.getWarehouseLocation());
						ws1Location = wsb.getWarehouseLocation().replace("-",
								"");
					}

					wsb.setWs1WarehouseLocation(ws1Location);
					wsb.setSapWarehouseNumber(MasterDataMappingSouth.gzWarehouseMap
							.get(wsb.getWarehouseNumber()));
					if (wsb.getSapWarehouseNumber() != null) {
						wsb.setPlant(wsb.getSapWarehouseNumber() + "0");
					}
					wsb.setWs1WarehouseLocation(ws1Location);
					wsb.setStorageLocation(MasterDataMappingSouth.gzStorageLocationMap
							.get(wsb.getWarehouseLocation()));
					wsb.setStorageType(MasterDataMappingSouth.gzStorageTypeMap
							.get(wsb.getWarehouseLocation()));
					continue;

				} else {
					wsb.setWs1WarehouseLocation(wsb.getWarehouseLocation());
					specialwarehouseStockBeanList.add(wsb);
					wsb.setConsignment(true);
					wsb.setPlant("BL00");
					wsb.setSapWarehouseNumber("BL0");
					if (wsb.getWarehouseNumber().equals("C")) {
						wsb.setCustomerNumber(30000094);
					} else {
						wsb.setCustomerNumber(30001097);
					}
					continue;
				}
			} else if (project.equals("SZ")) {
				String ws1Location = MasterDataMappingSouth.szLocationMap
						.get(wsb.getWarehouseLocation());

				if (ws1Location == null
						&& !wsb.getWarehouseLocation().equals("SD2012")
						&& !wsb.getWarehouseLocation().equals("SD2013")) {
					System.out
							.println("\n(*) WARNING! SZ main location mapping not exists:"
									+ wsb.getWarehouseNumber()
									+ " "
									+ wsb.getWarehouseLocation());
					ws1Location = wsb.getWarehouseLocation().replace("-", "");
				}
				wsb.setWs1WarehouseLocation(ws1Location);
				wsb.setSapWarehouseNumber(MasterDataMappingSouth.szWarehouseMap
						.get(wsb.getWarehouseNumber()));
				if (wsb.getSapWarehouseNumber() != null) {
					wsb.setPlant(wsb.getSapWarehouseNumber() + "0");
				}
				wsb.setWs1WarehouseLocation(ws1Location);
				wsb.setStorageLocation(MasterDataMappingSouth.szStorageLocationMap
						.get(wsb.getWarehouseLocation()));
				wsb.setStorageType(MasterDataMappingSouth.szStorageTypeMap
						.get(wsb.getWarehouseLocation()));
				if (wsb.getWarehouseLocation().equals("SD2012")
						|| wsb.getWarehouseLocation().equals("SD2013")) {
					wsb.setHasQualityProblem(true);
					qualitywarehouseStockBeanList.add(wsb);
				}
				continue;

			} else if (project.equals("CQ")) {
				String ws1Location = MasterDataMappingSouth.cqLocationMap
						.get(wsb.getWarehouseLocation());

				if (ws1Location == null) {
					System.out
							.println("\n(*) WARNING! CQ main location mapping not exists:"
									+ wsb.getWarehouseNumber()
									+ " "
									+ wsb.getWarehouseLocation());
					ws1Location = wsb.getWarehouseLocation().replace("-", "");
				}

				wsb.setWs1WarehouseLocation(ws1Location);
				wsb.setSapWarehouseNumber(MasterDataMappingSouth.cqWarehouseMap
						.get(wsb.getWarehouseNumber()));
				
				System.out
						.println("\n(*) WARNING! CQ sap warehouse number get:"
								+ wsb.getSapWarehouseNumber());
				
				
				if (wsb.getSapWarehouseNumber() != null) {
					wsb.setPlant(wsb.getSapWarehouseNumber() + "0");
				}
				wsb.setStorageLocation(MasterDataMappingSouth.cqStorageLocationMap
						.get(wsb.getWarehouseLocation()));
				wsb.setStorageType(MasterDataMappingSouth.cqStorageTypeMap
						.get(wsb.getWarehouseLocation()));

				// wsb.setStorageUnitType(MasterDataMappingSouth.cqStorageUnitTypeMap
				// .get(wsb.getWarehouseLocation()));
				wsb.setWs1WarehouseLocation(ws1Location);
				if (cqQuantityWH.contains(wsb.getWarehouseNumber())) {
					wsb.setPlant("BP00");
					wsb.setHasQualityProblem(true);
					wsb.setStorageLocation("1099");
					qualitywarehouseStockBeanList.add(wsb);
				}
				if (wsb.getWarehouseNumber().equals("CQ-B")
						|| wsb.getWarehouseNumber().equals("CQ-GZ")) {
					wsb.setPlant("BP00");
					wsb.setCarboot(true);
					if (wsb.getWarehouseNumber().equals("CQ-B")) {
						wsb.setSalesmanNumber("SHWC");
					} else {
						wsb.setSalesmanNumber("GZWC");
					}

					carbootwarehouseStockBeanList.add(wsb);
				}
				continue;

			} else if (project.equals("CD")) {
				String ws1Location = MasterDataMappingSouth.cdLocationMap
						.get(wsb.getWarehouseLocation());
				if (ws1Location == null) {
					System.out
							.println("\n(*) WARNING! CD main location mapping not exists: "
									+ wsb.getWarehouseNumber()
									+ " "
									+ wsb.getWarehouseLocation());
					ws1Location = wsb.getWarehouseLocation().replace("-", "");
				}

				wsb.setWs1WarehouseLocation(ws1Location);
				wsb.setSapWarehouseNumber(MasterDataMappingSouth.cdWarehouseMap
						.get(wsb.getWarehouseNumber()));
				if (wsb.getSapWarehouseNumber() != null) {
					wsb.setPlant(wsb.getSapWarehouseNumber() + "0");
				}
				wsb.setWs1WarehouseLocation(ws1Location);
				wsb.setStorageLocation(MasterDataMappingSouth.cdStorageLocationMap
						.get(wsb.getWarehouseLocation()));
				wsb.setStorageType(MasterDataMappingSouth.cdStorageTypeMap
						.get(wsb.getWarehouseLocation()));
				wsb.setStorageUnitType(MasterDataMappingSouth.cdStorageUnitTypeMap
						.get(wsb.getWarehouseLocation()));
				if (cdQuantityWH.contains(wsb.getWarehouseNumber())) {
					wsb.setPlant("BN00");
					wsb.setHasQualityProblem(true);
					wsb.setStorageLocation("1099");
					qualitywarehouseStockBeanList.add(wsb);
				}
				continue;
			}
		}
	}

	private void mergeMainStock() {
		for (WarehouseStockBean wsb : warehouseStockBeanList) {
			if (!wsb.isCarboot() && !wsb.isConsignment()
					&& !wsb.isHasQualityProblem()) {
				String key = getMainStockKey(wsb);
				if (!mainStockMap.containsKey(key)) {
					mainStockMap.put(key, wsb);
				} else {
					WarehouseStockBean wsbInMap = mainStockMap.get(key);
					System.out.println("\n(*) WARNING! Merge stock main: "
							+ wsbInMap.getProductNumber() + " "
							+ wsb.getProductNumber() + " from "
							+ wsbInMap.getStock() + " to "
							+ (wsbInMap.getStock() + wsb.getStock()));
					wsbInMap.setStock(wsbInMap.getStock() + wsb.getStock());
					mainStockMap.put(key, wsbInMap);
				}
			}

		}
	}

	private String getMainStockKey(WarehouseStockBean wsb) {
		if ("".equals(wsb.getSapProductNumber())) {
			return wsb.getPlant() + wsb.getSapProductNumber()
					+ wsb.getWs1WarehouseLocation();
		} else {
			return wsb.getPlant() + wsb.getProductNumber()
					+ wsb.getWs1WarehouseLocation();
		}

	}

	private void mergeCarbootStock() {
		for (WarehouseStockBean wsb : carbootwarehouseStockBeanList) {
			String sapSalesmanAndStorageLocation = null;
			wsb.setSapProductNumber(BIDateMapping.productMap.get(wsb
					.getProductNumber()));
			System.out.println("\n(*)sap product found for product: "
					+ wsb.getProductNumber() + "----->"
					+ wsb.getSapProductNumber());
			if (wsb.getSapProductNumber() == null
					|| wsb.getSapProductNumber().trim().equals("")) {
				System.out.println("\n(*)sap product not found for product: "
						+ wsb.getProductNumber());
			}
			if (project.equals("GZ") || project.equals("SZ")) {
				sapSalesmanAndStorageLocation = BIDateMapping.gzSalesmanMap
						.get(wsb.getSalesmanNumber());
			} else if (project.equals("CD")) {
				sapSalesmanAndStorageLocation = BIDateMapping.cdSalesmanMap
						.get(wsb.getSalesmanNumber());
			} else {
				sapSalesmanAndStorageLocation = BIDateMapping.cqSalesmanMap
						.get(wsb.getSalesmanNumber());
			}
			if (sapSalesmanAndStorageLocation != null) {
				String[] salesmanAndSLArray = sapSalesmanAndStorageLocation
						.split(",");
				wsb.setSalesmanNumber(salesmanAndSLArray[0]);
				wsb.setStorageLocation(salesmanAndSLArray[1]);
			}

			String key = getCarbootStockKey(wsb);
			if (!carbootStockMap.containsKey(key)) {
				carbootStockMap.put(key, wsb);
			} else {
				WarehouseStockBean wsbInMap = carbootStockMap.get(key);
				System.out.println("\n(*) WARNING! Merge stock carboot: "
						+ wsbInMap.getProductNumber() + " "
						+ wsb.getProductNumber() + " from "
						+ wsbInMap.getStock() + " to "
						+ (wsbInMap.getStock() + wsb.getStock()));
				wsbInMap.setStock(wsbInMap.getStock() + wsb.getStock());
				carbootStockMap.put(key, wsbInMap);
			}
		}
	}

	private String getCarbootStockKey(WarehouseStockBean wsb) {
		if (!"".equals(wsb.getSapProductNumber())) {
			return wsb.getSalesmanNumber() + wsb.getSapProductNumber();
		} else {
			return wsb.getSalesmanNumber() + wsb.getProductNumber();
		}
	}

	private void mergeSpecialStock() {
		for (WarehouseStockBean wsb : specialwarehouseStockBeanList) {
			String key = getSpecialStockKey(wsb);
			if (!specialStockMap.containsKey(key)) {
				specialStockMap.put(key, wsb);
			} else {
				WarehouseStockBean wsbInMap = specialStockMap.get(key);
				System.out.println("\n(*) WARNING! Merge stock special: "
						+ wsbInMap.getProductNumber() + " "
						+ wsb.getProductNumber() + " from "
						+ wsbInMap.getStock() + " to "
						+ (wsbInMap.getStock() + wsb.getStock()));
				wsbInMap.setStock(wsbInMap.getStock() + wsb.getStock());
				specialStockMap.put(key, wsbInMap);
			}

		}
	}

	private String getSpecialStockKey(WarehouseStockBean wsb) {
		if (!"".equals(wsb.getSapProductNumber())) {
			return wsb.getSapProductNumber() + wsb.getCustomerNumber();
		} else {
			return wsb.getProductNumber() + wsb.getCustomerNumber();
		}

	}

	private void mergeQualityStock() {
		for (WarehouseStockBean wsb : qualitywarehouseStockBeanList) {
			String key = getQualityStockKey(wsb);
			if (!qualityStockMap.containsKey(key)) {
				qualityStockMap.put(key, wsb);
			} else {
				WarehouseStockBean wsbInMap = qualityStockMap.get(key);
				System.out.println("\n(*) WARNING! Merge stock quality: "
						+ wsbInMap.getProductNumber() + " "
						+ wsb.getProductNumber() + " from "
						+ wsbInMap.getStock() + " to "
						+ (wsbInMap.getStock() + wsb.getStock()));
				wsbInMap.setStock(wsbInMap.getStock() + wsb.getStock());
				qualityStockMap.put(key, wsbInMap);
			}
		}
	}

	private String getQualityStockKey(WarehouseStockBean wsb) {
		if (!"".equals(wsb.getSapProductNumber())) {
			return wsb.getSapProductNumber();
		} else {
			return wsb.getProductNumber();
		}
	}

	private void writeStockInfoToExcel() throws PUserException {
		System.out.println("\n(!) Read excel file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					dataSource));
			XSSFSheet sheet = workbook.getSheetAt(0);
			writeMainStockInfoToSheet(sheet);

			sheet = workbook.getSheetAt(1);
			writeCarbootStockInfoToSheet(sheet);

			sheet = workbook.getSheetAt(2);
			writeSpecialInfoToSheet(sheet);

			sheet = workbook.getSheetAt(3);
			writeQualityInfoToSheet(sheet);

			String target="../../var/exportSAP/WM_Templates_CN_South_"+project+"_Stock.xlsx";
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

	private void writeMainStockInfoToSheet(XSSFSheet sheet)
			throws PUserException {
		System.out.println("\n(!) Read excel file  start.\n");
		int startRow = 1;
		for (WarehouseStockBean wsb : mainStockMap.values()) {
			XSSFRow row = sheet.getRow(startRow);
			if (row == null) {
				row = sheet.createRow(startRow);
			}

			XSSFCell cellWarehouseNumber = row.getCell(0);
			if (cellWarehouseNumber == null) {
				cellWarehouseNumber = row.createCell(0);
			}

			XSSFCell cellmovementType = row.getCell(4);
			if (cellmovementType == null) {
				cellmovementType = row.createCell(4);
			}

			XSSFCell cellSAPProductNumber = row.getCell(5);
			if (cellSAPProductNumber == null) {
				cellSAPProductNumber = row.createCell(5);
			}

			XSSFCell cellProductNumber = row.getCell(6);
			if (cellProductNumber == null) {
				cellProductNumber = row.createCell(6);
			}

			XSSFCell cellSapPlantNumber = row.getCell(7);
			if (cellSapPlantNumber == null) {
				cellSapPlantNumber = row.createCell(7);
			}

			XSSFCell cellStorageLocation = row.getCell(8);
			if (cellStorageLocation == null) {
				cellStorageLocation = row.createCell(8);
			}

			XSSFCell cellBatchNumber = row.getCell(9);
			if (cellBatchNumber == null) {
				cellBatchNumber = row.createCell(9);
			}

			XSSFCell cellAlternativeUnitOfMeasure = row.getCell(13);
			if (cellAlternativeUnitOfMeasure == null) {
				cellAlternativeUnitOfMeasure = row.createCell(13);
			}

			XSSFCell cellStorageUnitType = row.getCell(14);
			if (cellStorageUnitType == null) {
				cellStorageUnitType = row.createCell(14);
			}

			XSSFCell cellStorageType = row.getCell(16);
			if (cellStorageType == null) {
				cellStorageType = row.createCell(16);
			}

			XSSFCell cellStorageBin = row.getCell(18);
			if (cellStorageBin == null) {
				cellStorageBin = row.createCell(18);
			}

			XSSFCell cellStock = row.getCell(19);
			if (cellStock == null) {
				cellStock = row.createCell(19);
			}

			XSSFCell cellLastGoodsEntry = row.getCell(22);
			if (cellLastGoodsEntry == null) {
				cellLastGoodsEntry = row.createCell(22);
			}

			cellWarehouseNumber.setCellValue(wsb.getSapWarehouseNumber());
			cellmovementType.setCellValue("561");
			cellSAPProductNumber.setCellValue(wsb.getSapProductNumber());
			cellProductNumber.setCellValue(wsb.getProductNumber());
			cellSapPlantNumber.setCellValue(wsb.getPlant());
			cellStorageLocation.setCellValue(wsb.getStorageLocation());
			cellBatchNumber.setCellValue("D-QU");
			cellAlternativeUnitOfMeasure.setCellValue("PC");
			if (project.equals("CD")) {
				cellStorageUnitType.setCellValue(wsb.getStorageUnitType());
			} else if (project.equals("CQ")) {

				System.out.println("\n(*) WARNING! CQ storageunittype set:"
						+ wsb.getWarehouseLocation()
						+ "_"
						+ MasterDataMappingSouth.cqStorageUnitTypeMap.get(wsb
								.getWarehouseLocation()));
				cellStorageUnitType
						.setCellValue(MasterDataMappingSouth.cqStorageUnitTypeMap
								.get(wsb.getWarehouseLocation()));
			}
			cellStorageType.setCellValue(wsb.getStorageType());
			cellStorageBin.setCellValue(wsb.getWs1WarehouseLocation());
			cellStock.setCellValue(wsb.getStock());
			cellLastGoodsEntry.setCellValue(df.format(new PDate()));
			startRow++;
			System.out.println("\n Number of locations writed in sheet 1: "
					+ (startRow - 1));
		}
	}

	private void writeCarbootStockInfoToSheet(XSSFSheet sheet)
			throws PUserException {
		System.out.println("\n(!) Read excel file  start.\n");
		int startRow = 1;
		for (WarehouseStockBean wsb : carbootStockMap.values()) {
			XSSFRow row = sheet.getRow(startRow);
			if (row == null) {
				row = sheet.createRow(startRow);
			}

			XSSFCell cellSapPlantNumber = row.getCell(0);
			if (cellSapPlantNumber == null) {
				cellSapPlantNumber = row.createCell(0);
			}

			XSSFCell cellWarehouseNumber = row.getCell(1);
			if (cellWarehouseNumber == null) {
				cellWarehouseNumber = row.createCell(1);
			}

			XSSFCell cellStorageLocation = row.getCell(2);
			if (cellStorageLocation == null) {
				cellStorageLocation = row.createCell(2);
			}

			XSSFCell cellSalesmanNumber = row.getCell(3);
			if (cellSalesmanNumber == null) {
				cellSalesmanNumber = row.createCell(3);
			}

			XSSFCell cellSAPProductNumber = row.getCell(4);
			if (cellSAPProductNumber == null) {
				cellSAPProductNumber = row.createCell(4);
			}

			XSSFCell cellProductNumber = row.getCell(5);
			if (cellProductNumber == null) {
				cellProductNumber = row.createCell(5);
			}

			XSSFCell cellStock = row.getCell(6);
			if (cellStock == null) {
				cellStock = row.createCell(6);
			}
			XSSFCell cellmovementType = row.getCell(7);
			if (cellmovementType == null) {
				cellmovementType = row.createCell(7);
			}

			XSSFCell cellDocumentHeader = row.getCell(8);
			if (cellDocumentHeader == null) {
				cellDocumentHeader = row.createCell(8);
			}

			XSSFCell cellPostingDate = row.getCell(9);
			if (cellPostingDate == null) {
				cellPostingDate = row.createCell(9);
			}

			cellSapPlantNumber.setCellValue(wsb.getPlant());
			cellWarehouseNumber.setCellValue(wsb.getWarehouseNumber());

			cellStorageLocation.setCellValue(wsb.getStorageLocation());
			cellSalesmanNumber.setCellValue(wsb.getSalesmanNumber());

			cellSAPProductNumber.setCellValue(wsb.getSapProductNumber());

			cellProductNumber.setCellValue(wsb.getProductNumber());
			cellStock.setCellValue(wsb.getStock());
			cellmovementType.setCellValue("561");
			cellDocumentHeader.setCellValue("Load from Sylvestrix");
			cellPostingDate.setCellValue(df.format(new PDate()));
			startRow++;
			System.out
					.println("\n Number of carboot locations writed in sheet 2: "
							+ (startRow - 1));
		}
	}

	private void writeSpecialInfoToSheet(XSSFSheet sheet) throws PUserException {
		System.out.println("\n(!) Read excel file  start.\n");
		int startRow = 1;
		for (WarehouseStockBean wsb:specialStockMap.values()) {
			XSSFRow row = sheet.getRow(startRow);
			if (row == null) {
				row = sheet.createRow(startRow);
			}

			XSSFCell cellSapPlantNumber = row.getCell(0);
			if (cellSapPlantNumber == null) {
				cellSapPlantNumber = row.createCell(0);
			}

			XSSFCell cellWarehouseNumber = row.getCell(1);
			if (cellWarehouseNumber == null) {
				cellWarehouseNumber = row.createCell(1);
			}

			XSSFCell cellSAPProductNumber = row.getCell(3);
			if (cellSAPProductNumber == null) {
				cellSAPProductNumber = row.createCell(3);
			}

			XSSFCell cellProductNumber = row.getCell(4);
			if (cellProductNumber == null) {
				cellProductNumber = row.createCell(4);
			}

			XSSFCell cellStock = row.getCell(5);
			if (cellStock == null) {
				cellStock = row.createCell(5);
			}

			XSSFCell cellBatchNumber = row.getCell(6);
			if (cellBatchNumber == null) {
				cellBatchNumber = row.createCell(6);
			}
			XSSFCell cellSpecialStockIndicator = row.getCell(7);
			if (cellSpecialStockIndicator == null) {
				cellSpecialStockIndicator = row.createCell(7);
			}

			XSSFCell cellCustomerNumber = row.getCell(8);
			if (cellCustomerNumber == null) {
				cellCustomerNumber = row.createCell(8);
			}
			XSSFCell cellmovementType = row.getCell(9);
			if (cellmovementType == null) {
				cellmovementType = row.createCell(9);
			}
			XSSFCell cellDocumentHeader = row.getCell(10);
			if (cellDocumentHeader == null) {
				cellDocumentHeader = row.createCell(10);
			}

			XSSFCell cellPostingDate = row.getCell(11);
			if (cellPostingDate == null) {
				cellPostingDate = row.createCell(11);
			}

			cellSapPlantNumber.setCellValue(wsb.getPlant());
			cellWarehouseNumber.setCellValue(wsb.getWarehouseNumber());
			cellSAPProductNumber.setCellValue(wsb.getSapProductNumber());
			cellProductNumber.setCellValue(wsb.getProductNumber());
			cellStock.setCellValue(wsb.getStock());
			cellBatchNumber.setCellValue("D-QU");
			cellSpecialStockIndicator.setCellValue("W");
			cellCustomerNumber.setCellValue(wsb.getCustomerNumber());
			cellmovementType.setCellValue("561");
			cellDocumentHeader.setCellValue("Load from Sylvestrix");
			cellPostingDate.setCellValue(df.format(new PDate()));
			startRow++;
			System.out
					.println("\n Number of special locations writed in sheet 3: "
							+ (startRow - 1));
		}
	}

	private void writeQualityInfoToSheet(XSSFSheet sheet)
			throws PUserException {
		System.out.println("\n(!) Read excel file  start.\n");
		int startRow = 1;
		for (WarehouseStockBean wsb:qualityStockMap.values()) {
			XSSFRow row = sheet.getRow(startRow);
			if (row == null) {
				row = sheet.createRow(startRow);
			}

			XSSFCell cellSapPlantNumber = row.getCell(0);
			if (cellSapPlantNumber == null) {
				cellSapPlantNumber = row.createCell(0);
			}

			XSSFCell cellWarehouseNumber = row.getCell(1);
			if (cellWarehouseNumber == null) {
				cellWarehouseNumber = row.createCell(1);
			}

			XSSFCell cellStorageBin = row.getCell(2);
			if (cellStorageBin == null) {
				cellStorageBin = row.createCell(2);
			}

			XSSFCell cellSAPProductNumber = row.getCell(3);
			if (cellSAPProductNumber == null) {
				cellSAPProductNumber = row.createCell(3);
			}

			XSSFCell cellProductNumber = row.getCell(4);
			if (cellProductNumber == null) {
				cellProductNumber = row.createCell(4);
			}

			XSSFCell cellStock = row.getCell(5);
			if (cellStock == null) {
				cellStock = row.createCell(5);
			}

			XSSFCell cellBatchNumber = row.getCell(6);
			if (cellBatchNumber == null) {
				cellBatchNumber = row.createCell(6);
			}

			XSSFCell cellmovementType = row.getCell(7);
			if (cellmovementType == null) {
				cellmovementType = row.createCell(7);
			}

			XSSFCell cellDocumentHeader = row.getCell(8);
			if (cellDocumentHeader == null) {
				cellDocumentHeader = row.createCell(8);
			}

			XSSFCell cellPostingDate = row.getCell(9);
			if (cellPostingDate == null) {
				cellPostingDate = row.createCell(9);
			}
			cellSapPlantNumber.setCellValue(wsb.getPlant());
			cellWarehouseNumber.setCellValue(wsb.getWarehouseNumber());
			cellSAPProductNumber.setCellValue(wsb.getSapProductNumber());
			cellProductNumber.setCellValue(wsb.getProductNumber());
			cellStorageBin.setCellValue("1099");
			cellStock.setCellValue(wsb.getStock());
			cellBatchNumber.setCellValue("D-QU");
			cellmovementType.setCellValue("561");
			cellDocumentHeader.setCellValue("Load from Sylvestrix");
			cellPostingDate.setCellValue(df.format(new PDate()));
			startRow++;
			System.out
					.println("\n Number of quantity locations writed in sheet 4: "
							+ (startRow - 1));
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
			WarehouseStockBean wsb = new WarehouseStockBean();
			if (project.equals("GZ")) {
				wsb.setPlant("BL90");
			} else if (project.equals("CQ")) {
				wsb.setPlant("BP90");
			} else if (project.equals("CD")) {
				wsb.setPlant("BN90");
			} else if (project.equals("SZ")) {
				wsb.setPlant("BM90");
			}

			wsb.setWarehouseNumber(sgl.getWarehouseNumber());
			wsb.setWarehouseLocation(sgl.getWarehouseLocation().getId());
			wsb.setSalesmanNumber(sgl.getSalesman().getRegisterNumber() + "");
			wsb.setCarboot(true);
			wsb.setProductNumber(sgl.getProductNumber());
			wsb.setStock(sgl.getQuantityAssigned()
					.subtract(sgl.getQuantityReturned())
					.subtract(sgl.getQuantityConsumed()).getAmount());
			mergeCarbootStock(wsb);
			substractCarbootStock(wsb);
		}
		penum.destroy();

	}

	/**
	 * merge carboot should based on plant productnumber and salesman
	 * 
	 * @param wsb
	 */
	private void mergeCarbootStock(WarehouseStockBean wsb) {
		boolean foundExistingWSB = false;
		for (WarehouseStockBean warehouseStockBean : carbootwarehouseStockBeanList) {
			if (warehouseStockBean.getPlant().equals(wsb.getPlant())
					&& warehouseStockBean.getProductNumber().equals(
							wsb.getProductNumber())
					&& warehouseStockBean.getSalesmanNumber().equals(
							wsb.getSalesmanNumber())) {
				warehouseStockBean.setStock(warehouseStockBean.getStock()
						+ wsb.getStock());
				foundExistingWSB = true;
				break;
			}
		}
		if (!foundExistingWSB) {
			carbootwarehouseStockBeanList.add(wsb);
		}
	}

	private void substractCarbootStock(WarehouseStockBean wsb) {
		for (WarehouseStockBean warehouseStockBean : warehouseStockBeanList) {
			if (warehouseStockBean.getWarehouseNumber().equals(
					wsb.getWarehouseNumber())
					&& warehouseStockBean.getWarehouseLocation().equals(
							wsb.getWarehouseLocation())
					&& warehouseStockBean.getProductNumber().equals(
							wsb.getProductNumber())) {
				warehouseStockBean.setStock(warehouseStockBean.getStock()
						- wsb.getStock());
				if (warehouseStockBean.getStock() == 0.0D) {
					warehouseStockBeanList.remove(warehouseStockBean);
				}
				if (warehouseStockBean.getStock() < 0) {
					System.out.println("\n(!) Stock is not valid: "
							+ warehouseStockBean.getWarehouseNumber() + " "
							+ warehouseStockBean.getProductNumber());
				}
				break;
			}
		}
	}

}
