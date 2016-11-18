package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * MasterDataMappingSouth
 * 
 * @author pcnsh197
 * 
 */
public class MasterDataMappingSouth {

	public static Map<String, String>	cdLocationMap				= new HashMap<String, String>(
																			80);

	public static Map<String, String>	cdStorageBinMap				= new HashMap<String, String>(
																			80);

	public static Map<String, String>	cdStorageLocationMap		= new HashMap<String, String>(
																			80);

	public static Map<String, String>	cdStorageTypeMap				= new HashMap<String, String>(
			80);
	
	public static Map<String, String>	cdWarehouseMap				= new HashMap<String, String>(
			10);
	
	public static Map<String, String>	cdStorageUnitTypeMap		= new HashMap<String, String>(
																			200);

	public static Map<String, String>	cqLocationMap				= new HashMap<String, String>(
																			1000);

	public static Map<String, String>	cqStorageBinMap				= new HashMap<String, String>(
																			1000);

	public static Map<String, String>	cqStorageLocationMap		= new HashMap<String, String>(
																			1000);
	
	public static Map<String, String>	cqStorageTypeMap				= new HashMap<String, String>(
			1000);
	public static Map<String, String>	cqWarehouseMap				= new HashMap<String, String>(
			10);

	public static Map<String, String>	cqStorageUnitTypeMap		= new HashMap<String, String>(
																			200);

	public static Map<String, String>	gzLocationMap				= new HashMap<String, String>(
																			3000);

	public static Map<String, String>	gzStorageBinMap				= new HashMap<String, String>(
																			3000);

	public static Map<String, String>	gzStorageLocationMap		= new HashMap<String, String>(
																			3000);
	public static Map<String, String>	gzStorageTypeMap		    = new HashMap<String, String>(
			3000);
	public static Map<String, String>	gzWarehouseMap				= new HashMap<String, String>(
			10);

//	public static Map<String, String>	gzCarbootLocationMap		= new HashMap<String, String>(
//																			200);

	public static Map<String, String>	szLocationMap				= new HashMap<String, String>(
																			500);

	public static Map<String, String>	szStorageBinMap				= new HashMap<String, String>(
																			500);

	public static Map<String, String>	szStorageLocationMap		= new HashMap<String, String>(
																			500);
	public static Map<String, String>	szStorageTypeMap			= new HashMap<String, String>(
			500);
	public static Map<String, String>	szWarehouseMap				= new HashMap<String, String>(
			10);
//	public static Map<String, String>	szCarbootLocationMap		= new HashMap<String, String>(
//																			200);

	public static Set<Integer>			tjExistingVender			= new HashSet<Integer>();

	public static Set<Integer>			syExistingVender			= new HashSet<Integer>();

	public static List<String>			cityList					= new LinkedList<String>();

	private static String				locationMappingdataSource	= "../../etc/exportSAP/MAPPING_CNN_Bins_ALL.xlsx";

	private static String				existingvenderdataSource	= "../../etc/exportSAP/ExistingVendors.xlsx";

	public static final DateFormat		dateFormat					= new SimpleDateFormat(
																			"yyyyMMdd");

	static {
		readLocationMappingFromExcel();
		// readExistingVendorFromExcel();
	}

	private static void readLocationMappingFromExcel() {
		System.out.println("\n(!) Read excel file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					locationMappingdataSource));
			for (int i = 0; i < 7; i++) {
				if (i == 1 || i == 3 || i == 5 || i == 7) {
					continue;
				}
				XSSFSheet sheet = workbook.getSheetAt(i);
				int startRow = 1;
				XSSFRow row = sheet.getRow(startRow);
				if (row == null) {
					continue;
				}
				int locationMappingLoaded = 0;
				while (row != null) {
					XSSFCell cellwarehouseNumber = null;
					XSSFCell cellLocation = null;
					XSSFCell cellWS1Location = null;

					String warehouseNumber = "";
					String location = "";
					String ws1Location = "";
					String ws1WarehouseNumber="";

					cellwarehouseNumber = row.getCell(0);
					cellLocation = row.getCell(1);
					cellWS1Location = row.getCell(2);
					if (cellwarehouseNumber != null) {
						warehouseNumber = cellwarehouseNumber
								.getRichStringCellValue().getString();
					}

					if (cellLocation == null) {
						break;
					}

					try {
						location = cellLocation.getRichStringCellValue()
								.getString();
					} catch (Exception e) {
						if (cellLocation != null) {
							location = String.valueOf(cellLocation
									.getNumericCellValue());
						}
					}

					try {
						ws1Location = cellWS1Location
								.getRichStringCellValue().getString();
					} catch (Exception e) {
						if (cellWS1Location != null) {
							ws1Location = String.valueOf((int) cellWS1Location
									.getNumericCellValue());
						}
					}

					if (i == 0) {
						if (!cdLocationMap.containsKey(location)) {
							cdLocationMap.put(location, ws1Location);
							cdStorageLocationMap.put(location, getStorageLocation(row));
							cdStorageBinMap.put(location, getStorageBin(row));
							cdStorageTypeMap.put(location, getStorageType(row));
							cdWarehouseMap.put(warehouseNumber, getWS1WarehouseNumber(row));
							cdStorageUnitTypeMap.put(location, getStorageUnitType(row));
							
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map cdLocationBinMap : "
											+ location);
						}
					}

//					if (i == 2) {
//						if (!cdCarbootLocationMap.containsKey(warehouseNumber
//								+ location)) {
//							cdCarbootLocationMap.put(
//									warehouseNumber + location, ws1Location);
//						} else {
//							System.out
//									.println("\n(*) WARNING! location already put in the map cdCarbootLocationMap: "
//											+ warehouseNumber + location);
//						}
//					}

					if (i == 2) {
						if (!cqLocationMap.containsKey(location)) {
							cqLocationMap.put(location, ws1Location);
							cqStorageLocationMap.put(location, getStorageLocation(row));
							cqStorageBinMap.put(location, getStorageBin(row));
							cqStorageTypeMap.put(location, getStorageType(row));
							cqWarehouseMap.put(warehouseNumber, getWS1WarehouseNumber(row));
							cqStorageUnitTypeMap.put(location,
									getStorageUnitType(row));
							System.out
									.println("\n(*) CQ location unit type mapping: _"
											+ location
											+ "_"
											+ getStorageUnitType(row) + "_");
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map cqLocationMap: "
											+ location);
						}
					}

//					if (i == 5) {
//						if (!cqCarbootLocationMap.containsKey(warehouseNumber
//								+ location)) {
//							cqCarbootLocationMap.put(
//									warehouseNumber + location, ws1Location);
//						} else {
//							System.out
//									.println("\n(*) WARNING! location already put in the map cqCarbootLocationMap: "
//											+ warehouseNumber + location);
//						}
//					}

					if (i == 4) {
						if (!gzLocationMap.containsKey(location)) {
							gzLocationMap.put(location, ws1Location);
							gzStorageLocationMap.put(location, getStorageLocation(row));
							gzStorageBinMap.put(location, getStorageBin(row));
							gzStorageTypeMap.put(location, getStorageType(row));
							gzWarehouseMap.put(warehouseNumber, getWS1WarehouseNumber(row));
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map gzLocationMap: "
											+ location);
						}
					}

//					if (i == 8) {
//						if (!gzCarbootLocationMap.containsKey(warehouseNumber
//								+ location)) {
//							gzCarbootLocationMap.put(
//									warehouseNumber + location, ws1Location);
//						} else {
//							System.out
//									.println("\n(*) WARNING! location already put in the map gzCarbootLocationMap: "
//											+ warehouseNumber + location);
//						}
//					}

					if (i == 6) {
						if (!szLocationMap.containsKey(location)) {
							szLocationMap.put(location, ws1Location);
							szStorageLocationMap.put(location, getStorageLocation(row));
							szStorageBinMap.put(location, getStorageBin(row));
							szStorageTypeMap.put(location, getStorageType(row));
							szWarehouseMap.put(warehouseNumber, getWS1WarehouseNumber(row));
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map szLocationMap: "
											+ location);
						}
					}

//					if (i == 11) {
//						if (!szCarbootLocationMap.containsKey(warehouseNumber
//								+ location)) {
//							szCarbootLocationMap.put(
//									warehouseNumber + location, ws1Location);
//						} else {
//							System.out
//									.println("\n(*) WARNING! location already put in the map szCarbootLocationMap: "
//											+ warehouseNumber + location);
//						}
//					}

					locationMappingLoaded++;
					System.out.println("\n(!) " + sheet.getSheetName()
							+ " locations loaded: " + locationMappingLoaded);
					startRow++;
					row = sheet.getRow(startRow);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("\n(*) cqStorageUnitTypeMap.size(): "+cqStorageUnitTypeMap.size());

		System.out.println("\n(*) Read excel file  end.");

	}
	
	private static String getStorageBin(XSSFRow row) {
		XSSFCell cellStorageBin= row.getCell(2);
		String storageBin = null;
		try {
			storageBin = cellStorageBin.getRichStringCellValue().getString();
		} catch (Exception e) {
			if (cellStorageBin != null) {
				int productNumberLong = (int) cellStorageBin.getNumericCellValue();
				storageBin = String.valueOf(productNumberLong);
			}
		}
		return storageBin;
	}
	

	private static String getStorageType(XSSFRow row) {
		XSSFCell cellStorageType= row.getCell(3);
		String storageType = null;
		try {
			storageType = cellStorageType.getRichStringCellValue().getString();
		} catch (Exception e) {
			if (cellStorageType != null) {
				int productNumberLong = (int) cellStorageType.getNumericCellValue();
				storageType = String.valueOf(productNumberLong);
			}
		}
		return storageType;
	}
	
	private static String getStorageLocation(XSSFRow row) {
		XSSFCell cellStorageLocation= row.getCell(4);
		String storageLocation = null;
		try {
			storageLocation = cellStorageLocation.getRichStringCellValue().getString();
		} catch (Exception e) {
			if (cellStorageLocation != null) {
				int productNumberLong = (int) cellStorageLocation.getNumericCellValue();
				storageLocation = String.valueOf(productNumberLong);
			}
		}
		return storageLocation;
	}
	
	
	private static String getStorageUnitType(XSSFRow row) {
		XSSFCell cellStorageUnitType= row.getCell(7);
		String storageUnitType = null;
		try {
			storageUnitType = cellStorageUnitType.getRichStringCellValue().getString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return storageUnitType;
	}
	
	
	
	private static String getWS1WarehouseNumber(XSSFRow row) {
		XSSFCell cellWS1WarehouseNumber= row.getCell(5);
		String ws1WarehouseNumber  = cellWS1WarehouseNumber.getRichStringCellValue().getString();
		return ws1WarehouseNumber;
	}

	private static void readExistingVendorFromExcel() {
		System.out.println("\n(!) Read excel file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					existingvenderdataSource));
			for (int i = 0; i < 2; i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				int startRow = 1;
				XSSFRow row = sheet.getRow(startRow);

				if (row == null) {
					continue;
				}
				int existingVendorLoaded = 0;
				while (row != null) {
					XSSFCell cellVendor = row.getCell(0);
					if (cellVendor == null) {
						break;
					}
					Integer vender = null;
					System.out.println("\n(*) cellVendor.getCellType() :"
							+ cellVendor.getCellType());
					if (cellVendor.getCellType() == 0) {
						vender = Integer.valueOf((int) cellVendor
								.getNumericCellValue());
					} else {
						vender = Integer.valueOf(cellVendor
								.getRichStringCellValue().getString());
					}
					if (vender == null) {
						break;
					}
					if (i == 0) {
						syExistingVender.add(vender);
					}
					if (i == 1) {
						tjExistingVender.add(vender);
					}
					existingVendorLoaded++;
					System.out.println("\n(!) " + sheet.getSheetName()
							+ " existing vendor loaded: "
							+ existingVendorLoaded);
					startRow++;
					row = sheet.getRow(startRow);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n(*) Read excel file  end.");

	}
}
