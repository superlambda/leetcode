	package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.basic.etnax.common.utilsdir.DoubleUtils;

/**
 * BIDateMapping
 * 
 * @author pcnsh197
 * 
 */
public class BIDateMapping {
	
	public static String							csvSeperator				= ";";
	public static Map<String, String>	companyCodeMap	= new HashMap<String, String>();

	public static Map<String, String>	salesOrgMap		= new HashMap<String, String>();

	public static Map<String, String>	plantMap		= new HashMap<String, String>();

	public static Map<String, String>	warehouseMap	= new HashMap<String, String>();

	public static Map<String, String>	extStorageMap	= new HashMap<String, String>();

	public static Map<Integer, String>	newCustomerMap	= new HashMap<Integer, String>();
	
	public static Map<String, String>	productMap	= new HashMap<String, String>(8000);
	public static Set<String>	productGZMissingset	= new HashSet<String>(10);
	
	public static Map<String, String>	sapProductToSylProductMap	= new HashMap<String, String>(8000);
	public static Map<String, String>	duplicatedProductTwoWayMap	= new HashMap<String, String>(2000);
	
	public static Map<Integer, String>	customerOrgMap	= new HashMap<Integer, String>(2000);

	public static Set<String>			duplicatedProductSet		= new HashSet<String>(
																			2000);
	public static String				dummyCustomerNumber			= "0000999921";
	private static String				customerMappingdataSourceForBI		= "../../etc/exportSAP/Cust_chk_20170210.xlsx";
	
	private static String				productMappingdataSource		= "../../etc/exportSAP/MAPPING_MaterialMapping.xlsx";

	public static final DateFormat		dateFormat		= new SimpleDateFormat(
																"yyyyMMdd");
	
	public static Map<String, String>	customerMap	= new HashMap<String, String>(20000);
	
	public static Map<Integer, String>	sToSCustomerMap	= new HashMap<Integer, String>(20000);
	
	public static Map<Integer, String>	customerNoMappingMap	= new HashMap<Integer, String>(2000);

	static {
		readCustomerMappingFromExcel();
//		readProductMappingFromExcelForBI();
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
					XSSFCell cellCustomerNumber = row.getCell(1);
					XSSFCell cellWS1CustomerNumber = row.getCell(0);
					if (cellCustomerNumber == null&& cellWS1CustomerNumber == null) {
						break;
					}
					
					String customerNumber = "";
					try {
						customerNumber = cellCustomerNumber
								.getRichStringCellValue().getString();
					} catch (Exception e) {
						startRow++;
						row = sheet.getRow(startRow);
						continue;
					}			
					
					if(customerNumber==null || customerNumber.equals("")){
						startRow++;
						row = sheet.getRow(startRow);
						continue;
					}
					
					String wspCustomerNumber = cellWS1CustomerNumber
							.getRichStringCellValue().getString();
					if (wspCustomerNumber == null
							|| "".equals(wspCustomerNumber.trim())) {
						break;
					}
					
					wspCustomerNumber = wspCustomerNumber.startsWith("0") ? wspCustomerNumber : 0 + wspCustomerNumber;
					customerNumber = customerNumber.startsWith("0") ? customerNumber : 0 + customerNumber;
					
					if (!customerMap.containsKey(customerNumber)) {
						customerMap.put(customerNumber,wspCustomerNumber);
					} else {
						System.out.println("\n(!) WARING duplicated mapping customer loaded: " + customerNumber);
					}
					
					customerMappingLoaded++;
					startRow++;
					row = sheet.getRow(startRow);
				}
				System.out.println("\n(!) WS1 customers loaded: " + customerMappingLoaded);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("\n(*) Read excel file  end.");

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
			return "CN00";
		} else if (warehouseNumber.equals("2")) {
			return "CN20";
		} else{
			return "CN00";
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
	
	public static void readProductMappingFromExcelForBI() {
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
				startRow++;
				row = sheet.getRow(startRow);
			}
			System.out.println("\n(!) " + sheet.getSheetName()
			+ " products loaded: " + productMappingLoaded);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n(*) Read product mapping file  end.");

	}
	
	
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
		
		if (sToSCustomerMap.containsKey(accountNumber)) {
			return sToSCustomerMap.get(accountNumber);
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
		}else if (name1!=null&&name1.contains("Staff")){
			ws1CustomerNumber = "9"+String.valueOf(accountNumber).substring(1);
		}else if (name1!=null&&name1.contains("Wurth")){
			ws1CustomerNumber = "9"+String.valueOf(accountNumber).substring(1);
		}else if (name1!=null&&name1.contains("EDL")){
			ws1CustomerNumber = "9"+String.valueOf(accountNumber).substring(1);
		}else if(accountNumber<10000){//accountNumber is salesman
			ws1CustomerNumber = "920544";//accountNumber is salesman
		}else{
			ws1CustomerNumber=""+accountNumber;
		}
		ws1CustomerNumber = "0120" + ws1CustomerNumber;
		//TODO Check this line later, temporaily set to empty if customer is not in the mapping file
		if(!customerMap.containsKey(ws1CustomerNumber)){
			if(!customerNoMappingMap.containsKey(accountNumber)){
				customerNoMappingMap.put(accountNumber, ws1CustomerNumber);
				System.out.println("\n(!) WARING no mappping sylvestrix customer: " + accountNumber +" calculated customer: " +ws1CustomerNumber);
			}
			ws1CustomerNumber= "";
		}else{
			ws1CustomerNumber = customerMap.get(ws1CustomerNumber);
		}
		sToSCustomerMap.put(accountNumber, ws1CustomerNumber);
		return ws1CustomerNumber;
	}
	
	
	public static void fillWS1Information(InvoiceItemInformationBean iiib) {
		iiib.setWs1SalesOrganisation("3120");
		iiib.setWs1CustomerNumber(BIDateMapping.getWS1CustomerNumber(iiib.getCustomerNumber(),iiib.getName1()));
		iiib.setPayer(BIDateMapping.getWS1CustomerNumber(iiib.getDebtor(),iiib.getDebtorName()));
		String shipToCustomer=BIDateMapping.getWS1CustomerNumber(iiib.getGoodsRecipient(),iiib.getGoodsRecipientName());
		if(shipToCustomer.length()<6){
			shipToCustomer=iiib.getWs1CustomerNumber();
		}
		iiib.setShipToCustomer(shipToCustomer);
		if(iiib.getRegisterNumber()!=0){
			iiib.setWs1RegisterNumber("0000"+iiib.getRegisterNumber());
		}else{
			iiib.setWs1RegisterNumber("");
		}
		
		iiib.setPlant(BIDateMapping.getPlantBasedOnWarehouse(iiib.getWarehouseNumber()));
		iiib.setDeliveryPlant(BIDateMapping.getDeliveryPlantBasedOnWarehouse(iiib.getWarehouseNumber()));
		String eeeeProductNumber = BIDateMapping.productMap.get(iiib.getProductNumber());
		if (eeeeProductNumber != null && !eeeeProductNumber.trim().equals("")) {
			iiib.setArticleNumber(eeeeProductNumber);
		} else {
			iiib.setArticleNumber("");
//			System.out.println("Error: ArticalNumber not found for Invoice: " + iiib.getInvoiceNumber() + " Line: "
//					+ iiib.getInvoiceItem());
		}
	}
	
	public static void writeInvoiceItemHeaderToCSV(PrintWriter out1) {
		StringBuffer sb = new StringBuffer();
		String[] header1 = { "CSALESORG", "CTERREP", "CBOD", "CPLT", "CCUST", "CCBILLTO", "CCSHIPTO", "CCPAYER",
				"DORDENTRY", "CORDNO", "CORDREAS", "CORDCATS", "CDOCTYPE", "CDOCCAT", "CMAT", "CORDITM", "QIXQUOR",
				"CPRKEY", "DBILL", "CBILLNO", "CBILLTYPE", "CBILLITM", "QIXQUSU", "NIXINIT", "CORDCRED", "NIXCNIT",
				"CREACOMP", "LIITO", "LIGTO", "LINTO", "LINDC", "LIN_PS", "LIXPRB", "LIN_FR", "LINPP", "LINMP",
				"LIXXTAX", "QIWTNTKG" };
		for (int i = 0; i < header1.length; i++) {
			sb.append(header1[i]).append(csvSeperator);
		}
		out1.println(sb.toString());
		
		
//		sb = new StringBuffer();
//		String[] header2 = { "Sales Organization", "Sales Rep WS1", "Branch Office Did the Deal WS1",
//				"Delivery Plant WS1", "Customer Number (Sold-to-Party) WS1", "Customer Number (Bill-to-Party) WS1",
//				"Customer Number (Ship-to-Party) WS1", "Customer Number (Payer) WS1", "Order Document Entry Date",
//				"Order Document", "Order Reason", "Order Category (Statistic)", "Sales Document Type",
//				"Sales Document Category", "Article Number WS1", "Order Document Item", "Order Quantity", "Price Key",
//				"Billing Date", "Billing Document Number", "Billing Type", "Billing Item", "Billing Quantity",
//				"Number of Invoice Document Items", "Order/Credit Note", "Number of Credit Note Items",
//				"Complaint Reason", "Turnover", "Gross Value", "Net Value/Customer Turnover", "Discount",
//				"Price Increase Surcharge", "Basis Price", "Freight Costs", "Cost of Goods PFEP", "Cost of Goods GLEP",
//				"Tax Amount", "Netto Weight in Kilogramms" };
//		for (int i = 0; i < header2.length; i++) {
//			sb.append(header2[i]).append(csvSeperator);
//		}
//		out1.println(sb.toString());
		out1.flush();
	}

	public static void writeInvoiceItemInfoToTxt(List<InvoiceItemInformationBean> invoiceItemInfoList, PrintWriter out1,
			int numberOfInvoiceItems) {
		for (int i = 0; i < invoiceItemInfoList.size(); i++) {
			InvoiceItemInformationBean iiib = invoiceItemInfoList.get(i);
			StringBuffer sb = new StringBuffer();
			// Sales Organization
			sb.append(iiib.getWs1SalesOrganisation()).append(BIDateMapping.csvSeperator);
			// Sales Rep WS1
			sb.append(iiib.getWs1RegisterNumber()).append(BIDateMapping.csvSeperator);
			// Branch Office Did the Deal WS1
			sb.append(iiib.getPlant()).append(BIDateMapping.csvSeperator);
			// Delivery Plant WS1
			sb.append(iiib.getDeliveryPlant()).append(BIDateMapping.csvSeperator);
			// Customer Number (Sold-to-Party) WS1
			sb.append(iiib.getWs1CustomerNumber()).append(BIDateMapping.csvSeperator);
			// TODO Customer Number (Bill-to-Party) WS1
			sb.append(iiib.getWs1CustomerNumber()).append(BIDateMapping.csvSeperator);
			// Customer Number (Ship-to-Party) WS1
			sb.append(iiib.getShipToCustomer()).append(BIDateMapping.csvSeperator);
			// Customer Number (Payer) WS1
			sb.append(iiib.getPayer()).append(BIDateMapping.csvSeperator);
			// Order Document Entry Date
			if (iiib.getOrderDate() != null) {
				sb.append(BIDateMapping.dateFormat.format(iiib.getOrderDate())).append(BIDateMapping.csvSeperator);
			} else {
				sb.append(" ").append(BIDateMapping.csvSeperator);
			}

			// Order Document
			sb.append(iiib.getOrderNumber()).append(BIDateMapping.csvSeperator);
			// Order Reason
			sb.append(iiib.getOrderReason()).append(BIDateMapping.csvSeperator);
			// TODO Order Category (Statistic)
			sb.append(iiib.getOrderCategory()).append(BIDateMapping.csvSeperator);
			// TODO Sales Document Type
			sb.append(iiib.getSalesDocumentType()).append(BIDateMapping.csvSeperator);
			// TODO Sales Document Category
			sb.append(iiib.getDocumentCategory()).append(BIDateMapping.csvSeperator);

			// Article Number WS1
			sb.append(iiib.getArticleNumber()).append(BIDateMapping.csvSeperator);

			// Order Document Item
			sb.append(iiib.getOrderItem()).append(BIDateMapping.csvSeperator);
			// Order Quantity
			sb.append(iiib.getOrderQuantity()).append(BIDateMapping.csvSeperator);
			// Price Key
			sb.append(BIDateMapping.getPriceUnitMapping(iiib.getPriceUnit())).append(BIDateMapping.csvSeperator);

			// Billing Date
			if (iiib.getInvoiceDate() != null) {
				sb.append(BIDateMapping.dateFormat.format(iiib.getInvoiceDate())).append(BIDateMapping.csvSeperator);
			} else {
				sb.append(" ").append(BIDateMapping.csvSeperator);
			}
			// Billing Document Number
			sb.append(BIDateMapping.fillWS1OrderOrInvoiceNumber(iiib.getInvoiceNumber()))
					.append(BIDateMapping.csvSeperator);
			// Billing Type
			sb.append(iiib.getDocumentType()).append(BIDateMapping.csvSeperator);
			// Billing Item
			sb.append(iiib.getInvoiceItem()).append(BIDateMapping.csvSeperator);
			// Billing Quantity
			sb.append(iiib.getInvoiceQuantity()).append(BIDateMapping.csvSeperator);
			// Number of Invoice Document Items
			sb.append(iiib.getNumberOfInvoiceDocumentItems()).append(BIDateMapping.csvSeperator);
			// Order/Credit Note, If it is an Order then 'A', if Credit Note
			// then 'G'
			sb.append(iiib.getOrderCreditNoteSign()).append(BIDateMapping.csvSeperator);
			// Number of Credit Note Items, set what in this column?
			sb.append(iiib.getNumberOfCreditNoteItems()).append(BIDateMapping.csvSeperator);
			// Complaint Reason
			sb.append(" ").append(BIDateMapping.csvSeperator);
			// Turnover
			sb.append(convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(iiib.getTurnover()))))
					.append(BIDateMapping.csvSeperator);
			// LOGTO Gross Value
			sb.append(convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(iiib.getGrossValue()))))
					.append(BIDateMapping.csvSeperator);
			// LONTO Net Value/Customer Turnover
			sb.append(convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(iiib.getNetValue()))))
					.append(BIDateMapping.csvSeperator);
			// Discount
			sb.append(convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(iiib.getDiscount()))))
					.append(BIDateMapping.csvSeperator);

			// Price Increase Surcharge No logic, just correct format; if not
			// available use LOGTO minus LONTO; If the Document is a Creditnote,
			// put a minus in front of the Keyfigure
			if(iiib.getSurcharge()!=0.0D){
				sb.append(convertDotToComma(
						FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(iiib.getSurcharge()))))
						.append(BIDateMapping.csvSeperator);
			}else{
				sb.append("0,00").append(BIDateMapping.csvSeperator);
			}
		
			// Basis Price
			sb.append(convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(iiib.getPrice()))))
					.append(BIDateMapping.csvSeperator);
			// TODO Freight Costs, is on header level, how to calculate it on
			// line level.
			sb.append(convertDotToComma(FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(iiib.getFreightCost()))))
					.append(BIDateMapping.csvSeperator);
			// Cost of Goods PFEP
			sb.append(convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(iiib.getCogspfep()))))
					.append(BIDateMapping.csvSeperator);
			// Cost of Goods GLEP
			sb.append(convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(iiib.getCogsglep()))))
					.append(BIDateMapping.csvSeperator);
			// Tax Amount
			sb.append(convertDotToComma(
					FormatHelper.getPriceFormat().format(DoubleUtils.getRoundedAmount(iiib.getTaxAmount()))))
					.append(BIDateMapping.csvSeperator);
			// Netto Weight in Kilogramms
			sb.append(convertDotToComma(FormatHelper.getWeightFormat().format(iiib.getWeight())))
					.append(BIDateMapping.csvSeperator);
			out1.println(sb.toString());
			numberOfInvoiceItems++;
		}
		System.out.println("\n Number of Invoice Item writed in txt: " + numberOfInvoiceItems);
		out1.flush();
	}
		
		public static String convertDotToComma(String s){
			return s.replace(".", ",");
		}
}
