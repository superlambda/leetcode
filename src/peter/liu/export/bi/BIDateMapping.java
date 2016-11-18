	package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
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
public class BIDateMapping {
	public static Map<String, String>	companyCodeMap	= new HashMap<String, String>();

	public static Map<String, String>	salesOrgMap		= new HashMap<String, String>();

	public static Map<String, String>	plantMap		= new HashMap<String, String>();

	public static Map<String, String>	warehouseMap	= new HashMap<String, String>();

	public static Map<String, String>	extStorageMap	= new HashMap<String, String>();

	public static Map<Integer, String>	newCustomerMap	= new HashMap<Integer, String>();
	
	public static Map<String, String>	productMap	= new HashMap<String, String>(8000);
	public static Set<String>	productGZMissingset	= new HashSet<String>(1000);
	
	public static Map<String, String>	sapProductToSylProductMap	= new HashMap<String, String>(8000);
	public static Map<String, String>	duplicatedProductTwoWayMap	= new HashMap<String, String>(2000);
	
	public static Map<Integer, String>	customerOrgMap	= new HashMap<Integer, String>(2000);
	
	public static Map<String, String>	gzSalesmanMap	= new HashMap<String, String>(100);
	public static Map<String, String>	cqSalesmanMap	= new HashMap<String, String>(100);
	public static Map<String, String>	cdSalesmanMap	= new HashMap<String, String>(100);
	

	public static Set<String>			duplicatedProductSet		= new HashSet<String>(
																			2000);
	public static String				dummyMaterialNumber			= "NB10000000990   1";
	public static String				dummyCustomerNumber			= "0000999921";
	private static String				customerMappingdataSourceForBI		= "../../etc/exportSAP/MAPPING_Customers_ALL.xlsx";
	
	private static String				productMappingdataSource		= "../../etc/exportSAP/MAPPING_MaterialMapping.xlsx";
	
//	private static String				productMissingSource		= "../../etc/exportSAP/MAPPING_MaterialMapping_Missing.xlsx";
	private static String				salesmanMappingdataSource		= "../../etc/exportSAP/MAPPING_Salesrep_ALL.xlsx";

	public static final DateFormat		dateFormat		= new SimpleDateFormat(
																"yyyyMMdd");
	
	public static Map<Integer, String>	customerMap	= new HashMap<Integer, String>(2000);
	public static Set<String>			newCustomer2011Set		= new HashSet<String>(
			2000);
	public static Set<String>			newCustomer2012Set		= new HashSet<String>(
			2000);
	public static Set<String>			newCustomer2013Set		= new HashSet<String>(
			2000);
	

	static {
//		readCustomerMappingFromExcel();
//		readSalesmanMappingFromExcel();
		readProductMappingFromExcelForBI();
//		readProductGZMissing();
//		readCustomerMappingFromExcelForBICheckNewCustomer();
	}

	
	
	private static void readCustomerMappingFromExcel() {
		System.out.println("\n(!) Read excel file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					customerMappingdataSourceForBI));
			for (int i = 0; i < 1; i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				int startRow = 1;
				XSSFRow row = sheet.getRow(startRow);
				if (row == null) {
					continue;
				}
				int customerMappingLoaded = 0;
				while (row != null) {
					XSSFCell cellCustomerNumber = row.getCell(0);
					XSSFCell cellWS1CustomerNumber = row.getCell(1);
					XSSFCell cellOrganization = row.getCell(2);
					XSSFCell cellWS1RegisterNumber = row.getCell(3);
					if (cellCustomerNumber == null
							&& cellWS1CustomerNumber == null) {
						break;
					}
					int customerNumber = 0;
					try {
						customerNumber = Integer.valueOf(cellCustomerNumber
								.getRichStringCellValue().getString());
					} catch (Exception e) {
						customerNumber = (int) cellCustomerNumber
								.getNumericCellValue();
					}
							
					String wspCustomerNumber = cellWS1CustomerNumber
							.getRichStringCellValue().getString();
					if (wspCustomerNumber == null
							|| "".equals(wspCustomerNumber.trim())) {
						break;
					}
					String salesOrganization=null;
					
					if (cellOrganization.getCellType() == 0) {
						salesOrganization = Integer.valueOf(
								(int) cellOrganization.getNumericCellValue())
								.toString();
					} else {
						salesOrganization = cellOrganization
								.getRichStringCellValue().getString();
					}
					
					String ws1RegisterNumber = cellWS1RegisterNumber
							.getRichStringCellValue().getString();
					String value = wspCustomerNumber + "," + salesOrganization
							+ "," + ws1RegisterNumber;
					if (!customerOrgMap.containsKey(customerNumber)) {
						customerOrgMap.put(customerNumber, value);
					} else {
						System.out
								.println("\n(!) WARING duplicated customer loaded: "
										+ customerNumber);
					}
					
					customerMappingLoaded++;
					
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
	
	private static void readSalesmanMappingFromExcel() {
		System.out.println("\n(!) Read excel file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					salesmanMappingdataSource));
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				int startRow = 1;
				XSSFRow row = sheet.getRow(startRow);
				if (row == null) {
					continue;
				}
				int registerMappingLoaded = 0;
				while (row != null) {
					XSSFCell cellRegisterNumber = row.getCell(0);
					XSSFCell cellWS1RegisterNumber = row.getCell(1);
//					XSSFCell cellCarbootStorageLocation = row.getCell(4);

					if (cellRegisterNumber == null) {
						startRow++;
						row = sheet.getRow(startRow);
						continue;
					}
					String registerNumber = "";
					try {
						registerNumber = cellRegisterNumber
								.getRichStringCellValue().getString();
					} catch (Exception e) {
						try {
							registerNumber = String.valueOf((int)cellRegisterNumber
									.getNumericCellValue());
						} catch (Exception e1) {
							e.printStackTrace();
						}
					}
					if (registerNumber.trim().equals("")) {
						startRow++;
						row = sheet.getRow(startRow);
						continue;
					}

					String ws1RegisterNumber = cellWS1RegisterNumber
							.getRichStringCellValue().getString();
					if (ws1RegisterNumber == null
							|| "".equals(ws1RegisterNumber.trim())) {
						break;
					}

//					String carbootStorageLocation = cellCarbootStorageLocation
//							.getRichStringCellValue().getString();
					String value = ws1RegisterNumber + ","
							+ "storagelocation";
					if (sheet.getSheetName().equals("BL90")) {
						gzSalesmanMap.put(String.valueOf(registerNumber), value);
					} else if (sheet.getSheetName().equals("BN90")) {
						cdSalesmanMap.put(String.valueOf(registerNumber), value);
					} else if (sheet.getSheetName().equals("BP90")){
						cqSalesmanMap.put(String.valueOf(registerNumber), value);
					}else{
						break;
					}

					registerMappingLoaded++;
					System.out.println("\n(!) " + sheet.getSheetName() + " "
							+ registerNumber + "-->" + value
							+ " salesmen loaded: " + registerMappingLoaded);
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
	
	public static String getWS1RegisterNumber(String ownCompanyName,
			int registerNumber) {
		String registerNumberPlant = null;
		if (ownCompanyName.equals("伍尔特（重庆）五金工具有限公司")) {
			registerNumberPlant = cdSalesmanMap.get(String
					.valueOf(registerNumber));
			if (registerNumberPlant == null) {
				registerNumberPlant = cqSalesmanMap.get(String
						.valueOf(registerNumber));
			}
			System.out.println("\n(*) get ws1 registernumber for CQ: "
					+ registerNumber);

		} else {
			System.out.println("\n(*) get ws1 registernumber for GZ: "
					+ registerNumber);
			registerNumberPlant = gzSalesmanMap.get(String
					.valueOf(registerNumber));
		}
		String ws1RegisterNumber = "";
		if (registerNumberPlant == null) {
			ws1RegisterNumber = getDummyWS1RegisterNumber();
			System.out.println("\n Salesman mapping can not find : "
					+ registerNumber);
		} else {
			String[] registerNumberArray = registerNumberPlant.split(",");
			ws1RegisterNumber = registerNumberArray[0];
		}
		return ws1RegisterNumber;
	}

	public static String getDummyWS1RegisterNumber() {
		return "00009999";

	}

	public static boolean isWS1RUDUmmyRU(String ws1RegisterNumber) {
		if (ws1RegisterNumber.equals("00009999")) {
			return true;
		}
		return false;
	}
	
	public static String getPlantBasedOnWarehouse(String warehouseNumber){
		if (warehouseNumber.equals("1")) {
			return "CN0";
		} else if (warehouseNumber.equals("2")) {
			return "CN2";
		} else{
			return "CN0";
		}
	}
	
	public static String getDeliveryPlantBasedOnWarehouse(String warehouseNumber){
		if (warehouseNumber.equals("1")) {
			return "CN00";
		} else if (warehouseNumber.equals("2")) {
			return "CN20";
		}else{
			return "CN00";
		}
	}
	
	
	public static String fillWS1OrderOrInvoiceNumber(int orderNumber) {
		String rn = String.valueOf(orderNumber);
		int numberOfZeroTofill = 10 - rn.length();
		StringBuffer ws1OrderNumber = new StringBuffer();
		int i = 0;
		while (i < numberOfZeroTofill) {
			ws1OrderNumber.append("0");
			i++;
		}
		ws1OrderNumber.append(rn);
		return ws1OrderNumber.toString();
	}
	
	private static void readProductMappingFromExcelForBI() {
		System.out.println("\n(!) Read product mapping file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					productMappingdataSource));
			XSSFSheet sheet = workbook.getSheetAt(0);
			int startRow = 1;
			XSSFRow row = sheet.getRow(startRow);
			int productMappingLoaded = 0;
			while (row != null) {
				XSSFCell cellProductNumber = row.getCell(0);
				XSSFCell cellEEEEProductNumber = row.getCell(1);
				if (cellProductNumber == null) {
					break;
				}
				String productNumber = null;
				try {
					productNumber = cellProductNumber.getRichStringCellValue()
							.getString().trim();
				} catch (Exception e) {
					if (cellProductNumber != null) {
						long productNumberLong = (long) cellProductNumber
								.getNumericCellValue();
						productNumber = String.valueOf(productNumberLong)
								.trim();
					}
				}				

				String eeeeProductNumber = null;
				try {
					eeeeProductNumber = cellEEEEProductNumber
							.getRichStringCellValue().getString().trim();
				} catch (Exception e) {
					if (cellEEEEProductNumber != null) {
						long productNumberLong = (long) cellEEEEProductNumber
								.getNumericCellValue();
						eeeeProductNumber = String.valueOf((productNumberLong))
								.trim();
					}
				}
				
				if (productNumber == null || "".equals(productNumber.trim())) {
					break;
				}

				if(productMap.containsKey(productNumber)){
					System.out.println("\n(!) "
							+ " duplicated sylvestrix products found in excel: "+ productNumber );
				}
				productMap.put(productNumber, eeeeProductNumber);
				if (sapProductToSylProductMap.containsKey(eeeeProductNumber)) {
					System.out.println("\n(!) "
							+ "sylvestrix product *---->1 SAP product: "
							+ productNumber + ", "
							+ sapProductToSylProductMap.get(eeeeProductNumber)
							+ "---->" + eeeeProductNumber);
				} else {
					sapProductToSylProductMap.put(eeeeProductNumber,
							productNumber);

				}

				productMappingLoaded++;
				System.out.println("\n(!) " + sheet.getSheetName()
						+ " products loaded: " + productMappingLoaded);
				startRow++;
				row = sheet.getRow(startRow);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n(*) Read product mapping file  end.");

	}
	
//	private static void readProductGZMissing() {
//		System.out.println("\n(!) Read product missing mapping file  start.\n");
//		try {
//			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
//					productMissingSource));
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			int startRow = 0;
//			XSSFRow row = sheet.getRow(startRow);
//			int productMappingLoaded = 0;
//			while (row != null) {
//				XSSFCell cellProductNumber = row.getCell(0);
//				
//				if (cellProductNumber == null) {
//					break;
//				}
//				String productNumber = null;
//				try {
//					productNumber = cellProductNumber.getRichStringCellValue()
//							.getString().trim();
//				} catch (Exception e) {
//					if (cellProductNumber != null) {
//						long productNumberLong = (long) cellProductNumber
//								.getNumericCellValue();
//						productNumber = String.valueOf(productNumberLong)
//								.trim();
//					}
//				}				
//
//				if (productNumber == null || "".equals(productNumber.trim())) {
//					break;
//				}
//				
//				productGZMissingset.add(productNumber);
//				
//				productMappingLoaded++;
//				System.out.println("\n(!) " + sheet.getSheetName()
//						+ " products loaded: " + productMappingLoaded);
//				startRow++;
//				row = sheet.getRow(startRow);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("\n(*) Read product mapping file  end.");
//
//	}
	
	
	public static String fillWS1RegisterNumber(int registerNumber) {
		String rn = String.valueOf(registerNumber);
		int numberOfZeroTofill = 8 - rn.length();
		StringBuffer ws1RegisterNumber = new StringBuffer();
		int i = 0;
		while (i < numberOfZeroTofill) {
			ws1RegisterNumber.append("0");
			i++;
		}
		ws1RegisterNumber.append(rn);
		return ws1RegisterNumber.toString();
	}
	
	
	public static int getPriceUnitMapping(int sylPriceUnit) {
		int ws1PriceUnit = 0;
		switch (sylPriceUnit) {
			case 1:
				ws1PriceUnit = 1;
				break;
			case 100:
				ws1PriceUnit = 2;
				break;
			case 1000:
				ws1PriceUnit = 3;
				break;
			default:
				System.out.println("WRING: price unit mapping not found");
				break;
		}
		return ws1PriceUnit;
	}
	
	public static String getWS1CustomerNumber(int accountNumber,String name1){
		
		if (customerMap.containsKey(accountNumber)) {
			return customerMap.get(accountNumber);
		}
		String ws1CustomerNumber = "";

		if (accountNumber == 1) {
			ws1CustomerNumber = "900006";
		} else if (accountNumber == 100001) {
			ws1CustomerNumber = "910001";
		} else if (accountNumber == 100002) {
			ws1CustomerNumber = "111111111";
		} else if (accountNumber == 600000) {
			ws1CustomerNumber = "900009";
		} else if (accountNumber == 700001) {
			ws1CustomerNumber = "902010";
		} else if (accountNumber == 700002) {
			ws1CustomerNumber = "902020";
		} else if (accountNumber == 700003) {
			ws1CustomerNumber = "902030";
		} else if (accountNumber == 700004) {
			ws1CustomerNumber = "902040";
		} else if (accountNumber == 700005) {
			ws1CustomerNumber = "902050";
		} else if (accountNumber == 700006) {
			ws1CustomerNumber = "902060";
		} else if (accountNumber == 700007) {
			ws1CustomerNumber = "902070";
		} else if (accountNumber == 700008) {
			ws1CustomerNumber = "903010";
		} else if (accountNumber == 700009) {
			ws1CustomerNumber = "903020";
		} else if (accountNumber == 700010) {
			ws1CustomerNumber = "903030";
		} else if (accountNumber == 700011) {
			ws1CustomerNumber = "904010";
		} else if (accountNumber == 700012) {
			ws1CustomerNumber = "904020";
		} else if (accountNumber == 700013) {
			ws1CustomerNumber = "904030";
		} else if (accountNumber == 700014) {
			ws1CustomerNumber = "905010";
		} else if (accountNumber == 700015) {
			ws1CustomerNumber = "905000";
		} else if (accountNumber == 700016) {
			ws1CustomerNumber = "905030";
		} else if (accountNumber == 700017) {
			ws1CustomerNumber = "902007";
		} else if (accountNumber == 700018) {
			ws1CustomerNumber = "902000";
		} else if (accountNumber == 700019) {
			ws1CustomerNumber = "903000";
		}else if (name1 .contains("Staff")){
			ws1CustomerNumber = "9"+String.valueOf(accountNumber).substring(1);
		}else if (name1.contains("Wurth")){
			ws1CustomerNumber = "9"+String.valueOf(accountNumber).substring(1);
		}else if (name1.contains("EDL")){
			ws1CustomerNumber = "9"+String.valueOf(accountNumber).substring(1);
		}else{
			ws1CustomerNumber=""+accountNumber;
		}
		customerMap.put(accountNumber, ws1CustomerNumber);
		return ws1CustomerNumber;
	}
	
	public static void fillWS1Information(InvoiceItemInformationBean iiib) {
		iiib.setWs1SalesOrganisation("3120");
		iiib.setWs1CustomerNumber(BIDateMapping.getWS1CustomerNumber(iiib.getCustomerNumber(),iiib.getName1()));
		iiib.setWs1RegisterNumber("0000"+iiib.getRegisterNumber());
		iiib.setPlant(BIDateMapping.getPlantBasedOnWarehouse(iiib.getWarehouseNumber()));
		iiib.setDeliveryPlant(BIDateMapping.getDeliveryPlantBasedOnWarehouse(iiib.getWarehouseNumber()));
	}
}
