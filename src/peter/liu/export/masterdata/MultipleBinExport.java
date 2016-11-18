package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.models.Product;
import com.wuerth.phoenix.Phxbasic.models.Quant;
import com.wuerth.phoenix.Phxbasic.models.WarehouseProductAllowance;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;

/**
 * MultipleBinExport
 * 
 * @author pcnsh197
 * 
 */
public class MultipleBinExport extends BatchRunner {

	private int							NUMBER_OF_BATCH_TO_FECTCH	= 100;

	private List<WarehouseStockBean>	warehouseStockBeanList		= null;

	private String						dataSource					= "../../etc/exportSAP/multiplebin.xlsx";

	private String						target						= "../../var/exportSAP/multiplebin.xlsx";

	private Map<String, String>			locationMap					= new HashMap<String, String>(
																			5000);

	private HashSet<String>				gzWH						= new HashSet<String>();

	private HashSet<String>				szWH						= new HashSet<String>();

	private HashSet<String>				cqWH						= new HashSet<String>();

	private HashSet<String>				cdWH						= new HashSet<String>();

	private String						project;																// GZ,SZ,CQ,CD

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
		gzWH.add("B");
		gzWH.add("C");
		gzWH.add("D");
		gzWH.add("A-S");
		gzWH.add("A-W");

		szWH.add("SZ-A");
		szWH.add("SZ-B");

		cdWH.add("CD-A");
		cdWH.add("CD-B");
		cdWH.add("CD-GZ");
		cdWH.add("CD-D");

		cqWH.add("CQ-A");
		cqWH.add("CQ-B");
		cqWH.add("CQ-GZ");
		cqWH.add("CQ-D");

		searchWarehouseStockBean();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MultipleBinExport().startBatch(args);
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
		checkMultiple();
		writeStockInfoToExcel();
	}

	private void checkMultiple() {
		for (WarehouseStockBean wsb : warehouseStockBeanList) {
			
			if (project.equals("GZ")) {
				if (MasterDataMappingSouth.gzLocationMap.containsKey(wsb
						.getWarehouseLocation())) {
					String storageType = MasterDataMappingSouth.gzStorageTypeMap
							.get(wsb.getWarehouseLocation());
					// GZ: 只需要导出在BL00 Main+Quality D列StorageType为120,121,160的物料
					if (storageType.equals("120") || storageType.equals("121")
							|| storageType.equals("160")) {
						String key = wsb.getProductNumber() + ","
								+ wsb.getWarehouseNumber();
						if (locationMap.containsKey(key)) {
							locationMap.put(key, locationMap.get(key) + ","
									+ wsb.getWarehouseLocation());
						} else {
							locationMap.put(key, wsb.getWarehouseLocation());
						}
					}

				}
			} else if (project.equals("SZ")) {
				if (MasterDataMappingSouth.szLocationMap.containsKey(wsb
						.getWarehouseLocation())) {
					String storageType = MasterDataMappingSouth.szStorageTypeMap
							.get(wsb.getWarehouseLocation());
					// SZ: 只需要导出在BL10 Main D列StorageType为120,160的物料
					if (storageType.equals("120") || storageType.equals("160")) {
						String key = wsb.getProductNumber() + ","
								+ wsb.getWarehouseNumber();
						if (locationMap.containsKey(key)) {
							locationMap.put(key, locationMap.get(key) + ","
									+ wsb.getWarehouseLocation());
						} else {
							locationMap.put(key, wsb.getWarehouseLocation());
						}
					}

				}
			}else {
				String key = wsb.getProductNumber() + ","
						+ wsb.getWarehouseNumber();
				if (locationMap.containsKey(key)) {
					locationMap.put(
							key,
							locationMap.get(key) + ","
									+ wsb.getWarehouseLocation());
				} else {
					locationMap.put(key, wsb.getWarehouseLocation());
				}
			}

		}
	}

	private void writeStockInfoToExcel() throws PUserException {
		System.out.println("\n(!) Read excel file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					dataSource));
			XSSFSheet sheet = workbook.getSheetAt(0);
			writeMainStockInfoToSheet(sheet);

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
		for (String key : locationMap.keySet()) {
			XSSFRow row = sheet.getRow(startRow);
			if (row == null) {
				row = sheet.createRow(startRow);
			}

			XSSFCell cellProductNumber = row.getCell(0);
			if (cellProductNumber == null) {
				cellProductNumber = row.createCell(0);
			}

			XSSFCell cellWarehouseNumber = row.getCell(1);
			if (cellWarehouseNumber == null) {
				cellWarehouseNumber = row.createCell(1);
			}

			XSSFCell cellLocation = row.getCell(2);
			if (cellLocation == null) {
				cellLocation = row.createCell(2);
			}

			XSSFCell cellMultiple = row.getCell(3);
			if (cellMultiple == null) {
				cellMultiple = row.createCell(3);
			}
			String[] productWarehouse = key.split(",");

			cellProductNumber.setCellValue(productWarehouse[0]);
			cellWarehouseNumber.setCellValue(productWarehouse[1]);
			String location=locationMap.get(key);
			cellLocation.setCellValue(location);
			if(location.indexOf(",")>0){
				cellMultiple.setCellValue("X");
			}
			startRow++;
			System.out.println("\n Number of locations writed in sheet 1: "
					+ (startRow - 1));
		}

	}

}
