/**
 * 
 */
package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.models.PhxbasicController;
import com.wuerth.phoenix.basic.etnax.sap.webservice.service.SylvestrixHelper;



/**
 * @author pcnsh222
 * 
 */
public class ImportCustomerDataBatch {
	private PhxbasicController _controller;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private Properties propertiesForMapping;
	private String dataSource = "../../etc/exportSAP/ImportCustomerData.xlsx";
	private String target = "../../etc/exportSAP/ImportCustomerData_out.xlsx";
	private String rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String url = "jdbc:mysql://10.62.120.131:3306/wurthtj?user=web&password=web&useUnicode=true&characterEncoding=UTF-8";

	private static String dataSourceBJ = "../../var/exportSAP/CustomerNumber_BJ.xlsx";
	private static String dataSourceTJ = "../../var/exportSAP/CustomerNumber_TJ.xlsx";
	private static String dataSourceQD = "../../var/exportSAP/CustomerNumber_QD.xlsx";
	
	public static void main(String args[]) {
		ImportCustomerDataBatch batch = new ImportCustomerDataBatch();
		batch.importNumber(dataSourceBJ, "BJ");
		batch.importNumber(dataSourceTJ, "TJ");
		batch.importNumber(dataSourceQD, "QD");
		batch.updateCityAndProvince();
	}

	private void init() {
		_controller = SylvestrixHelper.get().getController();
		propertiesForMapping = MappingParam.getInstance().getMappingProperties();
		System.out.println("inited");
	}

	private void commit() {
		SylvestrixHelper.get().commit();
		System.out.println("commited");
	}

	public int column(String s) {

		if (s.length() == 1) {
			return rows.indexOf(s);
		}

		return 26 * (rows.indexOf(s.substring(0, 1)) + 1) + column(s.substring(1, s.length()));
	}
	
	private void importNumber(String source, String company) {
		XSSFWorkbook workbook = null;
		Connection con = null;
		Statement stmt = null;
//		FileOutputStream fos = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url);
			stmt = con.createStatement();
			// fos = new FileOutputStream(outputPath);
			workbook = new XSSFWorkbook(new FileInputStream(source));
			XSSFSheet sheet = workbook.getSheet("工作表1");
			int lastRowNum = sheet.getLastRowNum() + 1;
			System.out.println(lastRowNum + " row number");
			for (int i = 4; i < lastRowNum; i++) {
				Row row = sheet.getRow(i);
				Cell cell_CustNo = row.getCell(0);
				cell_CustNo.setCellType(Cell.CELL_TYPE_STRING);
				String custNumber = cell_CustNo.getStringCellValue();
				String insertCustNumberSql = "insert into customer(customernumber,company) values ('" + custNumber + "','" + company + "')";
				stmt.execute(insertCustNumberSql);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getTelphoneStartIndex(String value) {
		int length = value.length();
		for (int i = length; i > 0 ; i--) {
			String currentValue = value.substring(i - 1,i);
//			System.out.println("value = " + currentValue);
			if(!isNumeric(currentValue) && !currentValue.equals("-")) {
				return i;
			}
		}
		return 0;
	}
	
	public int getBankStartIndex(String value) {
		int length = value.length();
		for (int i = length; i > 0 ; i--) {
			String currentValue = value.substring(i - 1,i);
//			System.out.println("value = " + currentValue);
			if(!isNumeric(currentValue) && !currentValue.equals("-")) {
				return i;
			}
		}
		return 0;
	}
	
	public  boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 

	private void importData() {
		Connection con = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url);
			stmt = con.createStatement();
			// FileOutputStream fos = new FileOutputStream(target);
			Workbook twb = new XSSFWorkbook(new FileInputStream(dataSource));
			Sheet sheet = twb.getSheet("Sheet1");
			int lastRowNum = sheet.getLastRowNum() + 1;
			for (int i = 1; i < lastRowNum; i++) {
				Row row = sheet.getRow(i);
				Cell custNumber_cell = row.getCell(column("D"));
				custNumber_cell.setCellType(Cell.CELL_TYPE_STRING);
				String custNumber = custNumber_cell.getStringCellValue();
				
				String countSql = "select count(*) from customer where customernumber='"+ custNumber +"'";
				ResultSet rsCount = stmt.executeQuery(countSql);
				
				rsCount.next();
				int count = rsCount.getInt(1);
				if(count == 1) {
					
					String companySql = "select company from customer where customernumber='"+ custNumber +"'";
					ResultSet rsCompany= stmt.executeQuery(companySql);
					rsCompany.next();
					String company = rsCompany.getString(1);
					System.out.println(custNumber + " " + company);
					String address = "";
					String tel = "";
					String taxcode = "";
					String bankbranch = "";
					String bankcard = "";
					String name="";
					Cell address_cell = null;
					Cell taxcode_cell = null;
					Cell bank_cell = null;
					Cell name_cell = null;
					name_cell = row.getCell(column("E"));
					if(name_cell != null) {
						name= name_cell.getStringCellValue();
					}
					if(company.equals("TJ")) {
						address_cell =  row.getCell(column("H"));
						taxcode_cell = row.getCell(column("G"));
						bank_cell = row.getCell(column("I"));
						if(address_cell != null) {
							String address_tel = address_cell.getStringCellValue().trim();
//							System.out.println(address_tel);
							int startTel = getTelphoneStartIndex(address_tel);
							address = address_tel.substring(0, startTel);
							tel = address_tel.substring(startTel, address_tel.length());
							System.out.println(address_tel);
							System.out.println(address + " #######" + tel);
//							System.out.println(address_tel.substring(0, startTel));
//							System.out.println(address_tel.substring(startTel, address_tel.length()));
						}
						if(taxcode_cell != null) {
							taxcode_cell.setCellType(Cell.CELL_TYPE_STRING);
							taxcode = taxcode_cell.getStringCellValue();
						}
						if(bank_cell != null) {
							bank_cell.setCellType(Cell.CELL_TYPE_STRING);
							String branch_bankcard = bank_cell.getStringCellValue();
							int startCard = getBankStartIndex(branch_bankcard);
							bankbranch = branch_bankcard.substring(0, startCard);
							bankcard = branch_bankcard.substring(startCard, branch_bankcard.length());
							System.out.println(branch_bankcard);
							System.out.println(bankbranch + "######" + bankcard);
						}
					} else if(company.equals("BJ")) {
						address_cell =  row.getCell(column("P"));
						taxcode_cell = row.getCell(column("O"));
						bank_cell = row.getCell(column("Q"));
						if(address_cell != null) {
							String address_tel = address_cell.getStringCellValue().trim();
//							System.out.println(address_tel);
							int startTel = getTelphoneStartIndex(address_tel);
							address = address_tel.substring(0, startTel);
							tel = address_tel.substring(startTel, address_tel.length());
							System.out.println(address_tel);
							System.out.println(address + " #######" + tel);
//							System.out.println(address_tel.substring(0, startTel));
//							System.out.println(address_tel.substring(startTel, address_tel.length()));
						}
						if(taxcode_cell != null) {
							taxcode_cell.setCellType(Cell.CELL_TYPE_STRING);
							taxcode = taxcode_cell.getStringCellValue();
						}
						if(bank_cell != null) {
							bank_cell.setCellType(Cell.CELL_TYPE_STRING);
							String branch_bankcard = bank_cell.getStringCellValue();
							int startCard = getBankStartIndex(branch_bankcard);
							bankbranch = branch_bankcard.substring(0, startCard);
							bankcard = branch_bankcard.substring(startCard, branch_bankcard.length());
							System.out.println(branch_bankcard);
							System.out.println(bankbranch + "######" + bankcard);
						}
					} else if(company.equals("QD")) {
						address_cell =  row.getCell(column("T"));
						taxcode_cell = row.getCell(column("S"));
						bank_cell = row.getCell(column("U"));
						if(address_cell != null) {
							String address_tel = address_cell.getStringCellValue().trim();
//							System.out.println(address_tel);
							int startTel = getTelphoneStartIndex(address_tel);
							address = address_tel.substring(0, startTel);
							tel = address_tel.substring(startTel, address_tel.length());
							System.out.println(address_tel);
							System.out.println(address + " #######" + tel);
//							System.out.println(address_tel.substring(0, startTel));
//							System.out.println(address_tel.substring(startTel, address_tel.length()));
						}
						if(taxcode_cell != null) {
							taxcode_cell.setCellType(Cell.CELL_TYPE_STRING);
							taxcode = taxcode_cell.getStringCellValue();
						}
						if(bank_cell != null) {
							bank_cell.setCellType(Cell.CELL_TYPE_STRING);
							String branch_bankcard = bank_cell.getStringCellValue();
							int startCard = getBankStartIndex(branch_bankcard);
							bankbranch = branch_bankcard.substring(0, startCard);
							bankcard = branch_bankcard.substring(startCard, branch_bankcard.length());
							System.out.println(branch_bankcard);
							System.out.println(bankbranch + "######" + bankcard);
						}
					} else if(company.equals("BJ")) {
						address_cell =  row.getCell(column("P"));
						taxcode_cell = row.getCell(column("O"));
						bank_cell = row.getCell(column("Q"));
						if(address_cell != null) {
							String address_tel = address_cell.getStringCellValue().trim();
//							System.out.println(address_tel);
							int startTel = getTelphoneStartIndex(address_tel);
							address = address_tel.substring(0, startTel);
							tel = address_tel.substring(startTel, address_tel.length());
							System.out.println(address_tel);
							System.out.println(address + " #######" + tel);
//							System.out.println(address_tel.substring(0, startTel));
//							System.out.println(address_tel.substring(startTel, address_tel.length()));
						}
						if(taxcode_cell != null) {
							taxcode_cell.setCellType(Cell.CELL_TYPE_STRING);
							taxcode = taxcode_cell.getStringCellValue();
						}
						if(bank_cell != null) {
							bank_cell.setCellType(Cell.CELL_TYPE_STRING);
							String branch_bankcard = bank_cell.getStringCellValue();
							int startCard = getBankStartIndex(branch_bankcard);
							bankbranch = branch_bankcard.substring(0, startCard);
							bankcard = branch_bankcard.substring(startCard, branch_bankcard.length());
							System.out.println(branch_bankcard);
							System.out.println(bankbranch + "######" + bankcard);
						}
					}
					String updateSql = "update customer set bankcard='"+ bankcard+ "',taxcode='" + taxcode+ "',address='"+ address + "',tel='"+ tel +"', bankbranch='" + bankbranch + "',name='"+ name + "'"
							+ " where customernumber='" + custNumber +"'";
					stmt.executeUpdate(updateSql);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateCityAndProvince() {
		Connection con = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url);
			stmt = con.createStatement();
			stmt2 = con.createStatement();
			List<String> citylist = new ArrayList<String>();
			List<String> provincelist = new ArrayList<String>();
			Map<String,String> map = new HashMap<String, String>();
			String sel_city = "select city_name,province_name from province";
			ResultSet rs_city = stmt.executeQuery(sel_city);
			while (rs_city.next()) {
				String actualcity = rs_city.getString(1);
				String actualprovince = rs_city.getString(2);
				citylist.add(actualcity);
				if(map.get(actualcity) == null) {
					map.put(actualcity, actualprovince);
				}
			}

			String sel_province = "select province_name from province";
			ResultSet rs_province = stmt.executeQuery(sel_province);
			while (rs_province.next()) {
				String actualprovince = rs_province.getString(1);
				provincelist.add(actualprovince);
			}
			
			String selcustomer = "select address,name,id from customer";
			ResultSet rs_Customer = stmt.executeQuery(selcustomer);
			while (rs_Customer.next()) {
				String address = rs_Customer.getString(1);
				String name = rs_Customer.getString(2);
				
				int id = rs_Customer.getInt(3);
//				System.out.println(name + "      " + address);
				String sourceValue = "";
				if(address!= null && !address.equals("")) {
					sourceValue = address;
				} else {
					sourceValue = name;
				}
				for (String city : citylist) {
					// System.out.println(city);
					if (sourceValue.replace("市", "").indexOf(city) != -1) {
						String province = map.get(city);
						String updateCityAndProvince = "update customer set city='"+ city + "',province='"+ province + "' where id = " +id;
						stmt2.executeUpdate(updateCityAndProvince);
					}
				}
				for (String province : provincelist) {
					if (sourceValue.replace("市", "").indexOf(province) != -1) {
						String updateCityAndProvince = "update customer set province='"+ province + "' where id = " +id + " and province is null";
						stmt2.executeUpdate(updateCityAndProvince);
					}
				}
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
