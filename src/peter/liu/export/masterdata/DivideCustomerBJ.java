package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.models.BusinessRole;
import com.wuerth.phoenix.Phxbasic.models.Company;
import com.wuerth.phoenix.Phxbasic.models.CustomerAccount;
import com.wuerth.phoenix.Phxbasic.models.Debitor;
import com.wuerth.phoenix.Phxbasic.models.PhxbasicController;
import com.wuerth.phoenix.Phxbasic.models.SalesArea;
import com.wuerth.phoenix.Phxbasic.models.Salesman;
import com.wuerth.phoenix.Phxbasic.models.SalesmanAssignment;
import com.wuerth.phoenix.basic.etnax.sap.webservice.service.SylvestrixHelper;
import com.wuerth.phoenix.bcutil.HookException;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;
import com.wuerth.phoenix.util.PDate;

public class DivideCustomerBJ {
	private PhxbasicController _controller;
	private String dataSource1 = "../../etc/exportSAP/bjsp.xlsx";
	private String dataSource2 = "../../etc/exportSAP/bjdebtors.xlsx";
	
	private String target = "../../var/exportSAP/deleteCustomer_out.xlsx";
	private String rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static void main(String[] args) {
		DivideCustomerBJ batch = new DivideCustomerBJ();
		batch.init();
//		List<CustomerAccount> list;
//		try {
//			list = batch.getBJSP();
//			for (CustomerAccount customerAccount : list) {
//				System.out.println("customer Account<-------------->" + customerAccount.getAccountNumber());
//			}
//		} catch (HookException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (PUserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			batch.fillterCustomer();
		} catch (HookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		batch.getName2NotNull();
		batch.commit();
	}
	
	private void init() {
		_controller = SylvestrixHelper.get().getController();
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

		return 26 * (rows.indexOf(s.substring(0, 1)) + 1)
				+ column(s.substring(1, s.length()));
	}
	
	private void fillterCustomer() throws HookException, PUserException {
		List<CustomerAccount> custDebtorList = getRelatedCustomer();
		List<CustomerAccount> bjspList = getBJSP();
		for (CustomerAccount bjspCust : bjspList) {
			boolean existed = false;
			for (CustomerAccount custDebtor : custDebtorList) {
				if(bjspCust.equals(custDebtor)) {
					existed = true;
					break;
				}
			}
			if(!existed) {
				System.out.println("############Cust " + bjspCust.getAccountNumber());
				bjspCust.setTextOnOffer("BJ");
			}
		}
	}
	
	private void getName2NotNull() {
		List<Company> companyList = _controller.getAllRootsCompany();
		for (Company company : companyList) {
			List<BusinessRole> brList = company.getAllChildBusinessRole();
			for (BusinessRole businessRole : brList) {
				if(businessRole instanceof CustomerAccount) {
					CustomerAccount ca = (CustomerAccount) businessRole;
					if(ca.getTextOnOffer() != null && !ca.getTextOnOffer().equals("")) {
						System.out.println(ca.getAccountNumber() + "  " + ca.getTextOnOffer());
					}
				}
			}
		}
	}
	
	private List<Debitor> getBJCustomerAccount() {
//		List<CustomerAccount> custList = new ArrayList<CustomerAccount>();
		List<Debitor> debitorList = new ArrayList<Debitor>();
		try {
//			FileOutputStream fos = new FileOutputStream(target);
			Workbook twb = new XSSFWorkbook(new FileInputStream(dataSource2));
			Sheet sheet = twb.getSheet("Sheet1");
			int lastRowNum = sheet.getLastRowNum() + 1;
			for (int i = 0; i < lastRowNum; i++) {
				Row row = sheet.getRow(i);
				Cell debtor_cell = row.getCell(column("A"));
				debtor_cell.setCellType(Cell.CELL_TYPE_STRING);
				String debtor_S = debtor_cell.getStringCellValue().trim();
				int debtor_Id = Integer.valueOf(debtor_S);
				Debitor debitor = _controller.lookupDebitor(debtor_Id);

				if(debitor == null) {
					System.out.println("Debtor " + debtor_Id + " not found");
					continue;
				}
				debitorList.add(debitor);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return debitorList;
	}
	
	private List<CustomerAccount> getRelatedCustomer() {
		List<Debitor> debitorList = getBJCustomerAccount();
		List<CustomerAccount> custList = new ArrayList<CustomerAccount>();
		List<Company> companyList = _controller.getAllRootsCompany();
		for (Debitor debitor : debitorList) {
			boolean existed = false;
			for (Company company : companyList) {
				Debitor defaultDebitor = company.getDebitordefault();
				if(defaultDebitor == null) {
					continue;
				}
				if(defaultDebitor.equals(debitor)) {
					CustomerAccount ca = company.getCustomerAccountDefault();
					if(ca == null) {
						ca = _controller.lookupCustomerAccount(debitor.getId());
						if(ca != null) {
							custList.add(ca);
						} else {
							System.out.println("Debitor Cust " + debitor.getId() +" not found");
						}
					} else {
						custList.add(ca);
					}
					existed = true;
				}
			}
			if(!existed) {
				System.out.println("Debitor Cust " + debitor.getId() +" not found");
			}
		}
		return custList;
	}
	
	private List<CustomerAccount> getBJSP() throws HookException, PUserException {
		List<CustomerAccount> custList = new ArrayList<CustomerAccount>();
		List<Salesman> salesmanList = new ArrayList<Salesman>();
		try {
//			FileOutputStream fos = new FileOutputStream(target);
			Workbook twb = new XSSFWorkbook(new FileInputStream(dataSource1));
			Sheet sheet = twb.getSheet("Sheet1");
			int lastRowNum = sheet.getLastRowNum() + 1;
			for (int i = 0; i < lastRowNum; i++) {
				Row row = sheet.getRow(i);
				Cell salesman_cell = row.getCell(column("A"));
				salesman_cell.setCellType(Cell.CELL_TYPE_STRING);
				String salesman_S = salesman_cell.getStringCellValue();
				int salesmanId = Integer.valueOf(salesman_S);
				List<CustomerAccount> salesCustList = getCustomers(_controller, salesmanId);
				for (CustomerAccount customerAccount : salesCustList) {
					if(!custList.contains(customerAccount)) {
						custList.add(customerAccount);
					}
				} 
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return custList;
	}
	
	
	public static List<CustomerAccount> getCustomers(PhxbasicController controller, int salesmanId) throws HookException, PUserException {
		PDate currentDate = new PDate();
		List<CustomerAccount> caList = new ArrayList<CustomerAccount>();
		List<SalesArea> salesAreaList = getAllSalesArea(controller, salesmanId, currentDate);
		for (SalesArea sa : salesAreaList) {
			List<CustomerAccount> list = new ArrayList<CustomerAccount>(sa.getAllCustomerAccount(currentDate));
			if(list != null && list.size() !=0) {
				caList.addAll(list);
			}
		}

		return caList;
	}
	
	private static List<SalesArea> getAllSalesArea(PhxbasicController controller, int salesmanId, PDate currentDate) throws PUserException {
		List<SalesArea> list = new ArrayList<SalesArea>();
		QueryHelper saQH = Query.newQueryHelper().setClass(SalesArea.class);
		QueryPredicate qp2 = saQH.attr(SalesArea.AGG_SALESMANASSIGNMENT_REF + SalesmanAssignment.ASS_SALESMAN_REF + Salesman.REGISTERNUMBER).eq()
				.val(salesmanId).predicate();
		QueryPredicate qp3 = saQH.attr(SalesArea.AGG_SALESMANASSIGNMENT_REF + SalesmanAssignment.DATEFROM).lte().val(currentDate).predicate();
		QueryPredicate qp4 = saQH.attr(SalesArea.AGG_SALESMANASSIGNMENT_REF + SalesmanAssignment.DATETO).gte().val(currentDate).or()
				.attr(SalesArea.AGG_SALESMANASSIGNMENT_REF + SalesmanAssignment.DATETO).eq().val((PDate) null).predicate();
		PEnumeration saPEnum = controller.createIteratorFactory().getCursor(saQH.condition(qp2.and(qp3).and(qp4)));
		while (saPEnum.hasMoreElements()) {
			SalesArea sa = (SalesArea) saPEnum.nextElement();
			list.add(sa);
		}
		return list;
	}
	
}
