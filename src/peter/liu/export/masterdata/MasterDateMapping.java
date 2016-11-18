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
 * BIDateMapping
 * 
 * @author pcnsh197
 * 
 */
public class MasterDateMapping {

	public static Map<String, String>	tjqdLocationMap				= new HashMap<String, String>(
																			8000);

	public static Map<String, String>	tjqdLocationBinMap				= new HashMap<String, String>(
			8000);
	
	
	public static Map<String, String>	tjqdMS00LocationMap			= new HashMap<String, String>(
																			200);

	public static Map<String, String>	tjqdConsignmentLocationMap	= new HashMap<String, String>(
																			50);

	public static Map<String, String>	bjLocationMap				= new HashMap<String, String>(
																			3000);
	
	public static Map<String, String>	bjLocationBinMap				= new HashMap<String, String>(
			3000);

	public static Map<String, String>	bjConsignmentLocationMap	= new HashMap<String, String>(
																			50);

	public static Map<String, String>	tjCarbootLocationMap		= new HashMap<String, String>(
																			500);

	public static Map<String, String>	syLocationMap				= new HashMap<String, String>(
																			800);
	
	public static Map<String, String>	syLocationBinMap				= new HashMap<String, String>(
			800);

	public static Map<String, String>	syCarbootLocationMap		= new HashMap<String, String>(
																			200);
	

	public static Set<Integer>			tjExistingVender			= new HashSet<Integer>();

	public static Set<Integer>			syExistingVender			= new HashSet<Integer>();
	
	
	public static Map<String, String>	postCodeMap					= new HashMap<String, String>(
																			3000);
	public static Map<String, String>	provinceMap					= new HashMap<String, String>(
			3000);
	
	public static List<String>			cityList					= new LinkedList<String>();
	
	private static String				locationMappingdataSource	= "../../etc/exportSAP/BinStructure_Mappings.xlsx";
	private static String				existingvenderdataSource	= "../../etc/exportSAP/ExistingVendors.xlsx";
	private static String				postCodedataSource	= "../../etc/exportSAP/PostCode.xlsx";
	public static final DateFormat		dateFormat					= new SimpleDateFormat(
																			"yyyyMMdd");
	
	public static String[]				cqTopTenProduct				= {
			"00890 108 7", "00893 557 15", "00897 110 402-4", "00893 558 15",
			"00893 557", "00892 009 100", "00893 075", "00893 559 15",
			"00893 110 5", "00893 560 15"							};

	public static String[]				gzTopTenProduct				= {
			"00890 108 7", "00892 009 100", "00893 075", "00893 558",
			"00897 110 402-4", "00893 110 5", "00893 557 15", "00893 221",
			"00893 106", "00897 105 402-4"							};
	

	static {
//		readLocationMappingFromExcel();
//		readExistingVendorFromExcel();
		readPostCodeFromExcel();
	}

	private static void readLocationMappingFromExcel() {
		System.out.println("\n(!) Read excel file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					locationMappingdataSource));
			for (int i = 0; i < 8; i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				int startRow = 1;
				XSSFRow row = sheet.getRow(startRow);
				if (row == null) {
					continue;
				}
				int locationMappingLoaded = 0;
				while (row != null) {
					XSSFCell cellLocation = row.getCell(0);
					XSSFCell cellWS1Location = row.getCell(1);
					if (cellLocation == null) {
						break;
					}
					String location = cellLocation.getRichStringCellValue()
							.getString().trim();
					String ws1Location =null;
					try {
						ws1Location = cellWS1Location.getRichStringCellValue()
								.getString();
					} catch (Exception e) {
						if(cellWS1Location!=null){
							ws1Location = String.valueOf(cellWS1Location
									.getNumericCellValue());
						}
					}
//					if(ws1Location==null){
//						ws1Location=location;
//					}
						
//					if (ws1Location == null || "".equals(ws1Location.trim())) {
//						break;
//					}
					if (i == 0) {
						if (!tjqdLocationMap.containsKey(location)) {
							tjqdLocationMap.put(location, ws1Location);
							tjqdLocationBinMap.put(location, getBinType(row));
							
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map tjqdLocationMap : "
											+ location);
						}
					}

					if (i == 1) {
						if (!tjqdMS00LocationMap.containsKey(location)) {
							tjqdMS00LocationMap.put(location, ws1Location);
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map tjqdMS00LocationMap: "
											+ location);
						}
					}
					if (i == 2) {
						if (!tjqdConsignmentLocationMap.containsKey(location)) {
							tjqdConsignmentLocationMap.put(location,
									ws1Location);
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map tjqdConsignmentLocationMap: "
											+ location);
						}
					}
					if (i == 3) {
						if (!bjLocationMap.containsKey(location)) {
							bjLocationMap.put(location, ws1Location);
							bjLocationBinMap.put(location, getBinType(row));
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map bjLocationMap: "
											+ location);
						}
					}

					if (i == 4) {
						if (!bjConsignmentLocationMap.containsKey(location)) {
							bjConsignmentLocationMap.put(location, ws1Location);
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map bjConsignmentLocationMap: "
											+ location);
						}
					}

					if (i == 5) {
						if (!tjCarbootLocationMap.containsKey(location)) {
							tjCarbootLocationMap.put(location, ws1Location);
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map tjCarbootLocationMap: "
											+ location);
						}
					}
					if (i == 6) {
						if (!syLocationMap.containsKey(location)) {
							syLocationMap.put(location, ws1Location);
							syLocationBinMap.put(location, getBinType(row));
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map syLocationMap: "
											+ location);
						}
					}

					if (i == 7) {
						if (!syCarbootLocationMap.containsKey(location)) {
							syCarbootLocationMap.put(location, ws1Location);
						} else {
							System.out
									.println("\n(*) WARNING! location already put in the map syCarbootLocationMap: "
											+ location);
						}
					}
					System.out.println("\n(!) "
							+ " syCarbootLocationMap.size() "
							+ syCarbootLocationMap.size());
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
		System.out.println("\n(*) Read excel file  end.");

	}
	
	private static String getBinType(XSSFRow row) {
		XSSFCell cellBinType = row.getCell(2);
		String binType = null;
		try {
			binType = cellBinType.getRichStringCellValue().getString();
		} catch (Exception e) {
			if (cellBinType != null) {
				int productNumberLong = (int) cellBinType.getNumericCellValue();
				binType = String.valueOf(productNumberLong);
			}
		}
		return binType;
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
					System.out.println("\n(*) cellVendor.getCellType() :"+cellVendor.getCellType());
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
	
	
	private static void readPostCodeFromExcel() {
		System.out.println("\n(!) Read excel file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					postCodedataSource));
			XSSFSheet sheet = workbook.getSheetAt(0);
			int startRow = 1;
			XSSFRow row = sheet.getRow(startRow);
			int postCodeLoaded = 0;
			while (row != null) {
				XSSFCell cellCity = row.getCell(1);
				if (cellCity == null) {
					break;
				}
				String city = cellCity.getRichStringCellValue().getString();
				if (city == null) {
					break;
				}
				
				
				XSSFCell cellPostCode = row.getCell(2);
				String postCode = null;
				if (cellPostCode.getCellType() == 0) {
					postCode = String.valueOf((int) cellPostCode
							.getNumericCellValue());
				} else {
					postCode = cellPostCode.getRichStringCellValue()
							.getString();
				}
				
				XSSFCell cellProvince = row.getCell(0);
				String province = cellProvince.getRichStringCellValue()
						.getString();

				if(postCode.length()==5){
					postCode="0"+postCode;
				}
				postCodeMap.put(city, postCode);
				cityList.add(city);
				provinceMap.put(city, province);
				
				postCodeLoaded++;
				System.out.println("\n(!) " + sheet.getSheetName()
						+ " postCode loaded: '" + city + "' '" + postCodeLoaded
						+ "'");
				startRow++;
				row = sheet.getRow(startRow);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n(*) Read excel file  end.");

	}
}
