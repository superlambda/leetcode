package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.enums.CustomerAccountStatus;
import com.wuerth.phoenix.Phxbasic.enums.DebitorStatus;
import com.wuerth.phoenix.Phxbasic.enums.GoodsRecipientStatus;
import com.wuerth.phoenix.Phxbasic.enums.OrderProcessingParameter;
import com.wuerth.phoenix.Phxbasic.enums.OrderType;
import com.wuerth.phoenix.Phxbasic.models.AddressStruct;
import com.wuerth.phoenix.Phxbasic.models.BankAccount;
import com.wuerth.phoenix.Phxbasic.models.BankBranch;
import com.wuerth.phoenix.Phxbasic.models.BusinessAddress;
import com.wuerth.phoenix.Phxbasic.models.BusinessRole;
import com.wuerth.phoenix.Phxbasic.models.CommunicationStruct;
import com.wuerth.phoenix.Phxbasic.models.Company;
import com.wuerth.phoenix.Phxbasic.models.Contact;
import com.wuerth.phoenix.Phxbasic.models.CustomerAccount;
import com.wuerth.phoenix.Phxbasic.models.Debitor;
import com.wuerth.phoenix.Phxbasic.models.GoodsRecipient;
import com.wuerth.phoenix.Phxbasic.models.PaymentTerm;
import com.wuerth.phoenix.Phxbasic.models.PhxbasicController;
import com.wuerth.phoenix.Phxbasic.models.SalesArea;
import com.wuerth.phoenix.Phxbasic.models.Salesman;
import com.wuerth.phoenix.Phxbasic.models.StringValue;
import com.wuerth.phoenix.basic.etnax.sap.webservice.service.SylvestrixHelper;
import com.wuerth.phoenix.bcserver.base.DbPeer;
import com.wuerth.phoenix.bcutil.IteratorFactory;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;
import com.wuerth.phoenix.util.PDate;

public class AbstractCustomerExport {

	protected PhxbasicController _controller;
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	protected Properties propertiesForMapping;
	protected String dataSource = "../../etc/exportSAP/Customer_master_Template.xlsx";
	protected String tempSKNA1 = "../../var/exportSAP/Customer_master_SKNA1.xlsx";
	protected String tempSKNKK = "../../var/exportSAP/Customer_master_SKNKK.xlsx";
	protected String tempSKNVK = "../../var/exportSAP/Customer_master_SKNVK.xlsx";
	protected String tempSKNBK = "../../var/exportSAP/Customer_master_SKNBK.xlsx";
	protected String tempSKNB1 = "../../var/exportSAP/Customer_master_SKNB1.xlsx";
	protected String tempSKNB5 = "../../var/exportSAP/Customer_master_SKNB5.xlsx";
	protected String tempSKNVV_PARTA = "../../var/exportSAP/Customer_master_SKNVV_PARTA.xlsx";
	protected String tempSKNVV_PARTB = "../../var/exportSAP/Customer_master_SKNVV_PARTB.xlsx";
	protected String tempSKNVP = "../../var/exportSAP/Customer_master_SKNVP.xlsx";
	protected String target = "../../var/exportSAP/Customer_master_out.xlsx";
	protected int totalLineCount = 0;


	
	protected String rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	protected List<CustomerAccountExportBean> debtorList = new ArrayList<CustomerAccountExportBean>();
	protected List<CustomerAccountExportBean> goodsReceiptList = new ArrayList<CustomerAccountExportBean>();
	protected List<CustomerAccountExportBean> custList = new ArrayList<CustomerAccountExportBean>();

	
	protected void init() {
		_controller = SylvestrixHelper.get().getController();
		propertiesForMapping = MappingParam.getInstance().getMappingProperties();
		System.out.println("inited");
	}
	
	protected void commit() {
		SylvestrixHelper.get().commit();
		System.out.println("commited");
	}
	
	protected int column(String s) {

		if (s.length() == 1) {
			return rows.indexOf(s);
		}

		return 26 * (rows.indexOf(s.substring(0, 1)) + 1)
				+ column(s.substring(1, s.length()));
	}
	
	protected void export(String branchFilter) {

		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p = qh.attr(CustomerAccount.STATUS).ne().val(CustomerAccountStatus.DELETED).predicate();
		qh.setClass(CustomerAccount.class);
		qh.addAscendingOrdering(CustomerAccount.ACCOUNTNUMBER);
		PEnumeration penum = null;
        Condition condition = qh.condition(p);
        IteratorFactory fac = (IteratorFactory) _controller.createIteratorFactory();
        try {
			penum = fac.getCursor(condition);
			while (penum.hasMoreElements()) {
				CustomerAccount ca = (CustomerAccount) penum.nextElement();
				if(branchFilter==null){
					if(ca.getTextOnOffer().equals("BJ") || ca.getTextOnOffer().equals("QD")) {
						continue;
					}
				}else{
					if(!ca.getTextOnOffer().equals(branchFilter)) {
						continue;
					}
				}
				
				CustomerAccountExportBean caeb = new CustomerAccountExportBean();
				caeb.setCustomerNumber(ca.getAccountNumber() + "");
				caeb.setCustomerAccountGroup("IALL");
	            if (ca.getParentCompany().getEnterpriseAccount() != null
	                    && !ca.getParentCompany().getEnterpriseAccount().equals("")) {
	            	caeb.setControllingFlagforAccountGroup("3");
	            } else {
	            	caeb.setControllingFlagforAccountGroup("1" );
	            }
				String countryKey = getCountryKey(ca.getLanguage().getDescription());

				caeb.setCountryKey(countryKey);
				caeb.setName1(ca.getName());
				caeb.setName2(getNotFilled(ca.getName2()));
				caeb.setSortField(ca.getShortName());
				if(ca.getCreationDate() != null) {
					caeb.setCreationDate(sdf.format(ca.getCreationDate()));
				} else {
					DbPeer dpBR = (DbPeer)ca;
					caeb.setCreationDate(sdf.format(dpBR.getCreateDate()));
				}
				
				caeb.setLanguageKey(propertiesForMapping.getProperty("language." + ca.getLanguage().getDescription()));
				BusinessAddress ba =  ca.getBusinessAddressdefault();
				if(ba != null) {
					AddressStruct as =  ba.getAddress();
					if(as != null) {
						if(as.getCountryCode() != null && !as.getCountryCode().equals("")) {
							caeb.setCountryKey(as.getCountryCode());
						}
						caeb.setCity(getNotFilled(as.getCity()));
						caeb.setProvince(getNotFilled(as.getCounty()));
						caeb.setPoBox(getEmpty(as.getPostBox()));
						caeb.setPostalCode(getNotFilled(as.getZip()));
						
						String houserAndStreet = getNotFilled(ba
								.getId())
								+ ""
								+ getEmpty(as.getStreet())
								+ ""
								+ getEmpty(as.getStreetNumber());
						caeb.setHouseNumberAndStreet(houserAndStreet);
						//Find the city!!!!!, try each way you can get
						if (as.getZip() != null
								&& !as.getZip().trim().equals("")) {
							caeb.setPostalCode(getNotFilled(as.getZip()));
						} else {
							if (as.getCity() != null
									&& !as.getCity().trim().equals("")) {
								String postCode = MasterDateMapping.postCodeMap
										.get(as.getCity());
								if (postCode != null) {
									caeb.setPostalCode(postCode);
								} else {
									caeb.setPostalCode(getNotFilled(as.getZip()));
								}
							} else {
								caeb.setPostalCode(getNotFilled(as.getZip()));
							}
						}
						if (caeb.getPostalCode().equals("/")
								|| caeb.getCity().equals("/")
								|| caeb.getProvince().equals("/")) {
							if (houserAndStreet != null
									&& !"/".equals(houserAndStreet)) {
								int numberOfCityFound=0;
								for (String city : MasterDateMapping.cityList) {
									if (houserAndStreet.contains(city)) {
										String postCode = MasterDateMapping.postCodeMap
												.get(city);
										if (postCode != null) {
											if("/".equals(caeb.getPostalCode())){
												caeb.setPostalCode(postCode);
											}
											if("/".equals(caeb.getCity())){
												caeb.setCity(city);
											}
											
										} else {
											System.out
													.println("\n(*) WARNING! post code of city : "
															+ as.getCity()
															+ " not found.");
										}
										
										if (caeb.getProvince() == null
												|| caeb.getProvince().trim()
														.equals("")
												|| "/".equals(caeb.getProvince()
														.trim())) {
											String province = MasterDateMapping.provinceMap
													.get(city);
											if (province != null) {
												caeb.setProvince(province);
											}
										}
										
										numberOfCityFound++;
//										break;
									}
								}
								if (numberOfCityFound > 1) {
									System.out
											.println("\n(*) WARNING! more than 1 city found! "
													+ caeb.getCustomerNumber()
													+ "  " + houserAndStreet);
								}
							}
						}
					}
				}
				SalesArea sa = ca.getSalesArea(new PDate());
				if(sa != null) {
					Salesman salesman = sa.getResponsibleSalesman(new PDate());
					if(salesman != null) {
						int salesmanId = salesman.getRegisterNumber();
						caeb.setSalesRepNo(salesmanId + "");
					}
				}
				Contact contact = ca.getContactdefault();
				if(contact != null) {
					CommunicationStruct cs =  contact.getCommunication();
					if(cs != null) {
						caeb.setFirstTelephoneNumber(getEmpty(cs.getPhone()));
						caeb.setFaxNumber(getNotFilled((cs.getFax())));
						caeb.setContactPersonName(contact.getName());
						caeb.setTelphone(getNotFilled(cs.getPhone()));
						caeb.setEmail(getNotFilled(cs.getEmail()));
						caeb.setMobilephone(getNotFilled(cs.getMobilPhone()));
					}
				}
				caeb.setCustomerStatus(propertiesForMapping.getProperty("cust.customerStatus." + ca.getStatus().getShortValue() + ""));
				Debitor debitor = ca.getDefaultDebitor();
				if(debitor != null) {
					StringValue paymentTermValue = (StringValue) debitor.lookupOrderProcessingParameterValue(OrderProcessingParameter.PAYMENTTERM, OrderType.NULL);
					if (paymentTermValue != null) {
						PaymentTerm paymentTerm = _controller.lookupPaymentTerm(paymentTermValue.getValue());
						if(paymentTerm != null) {
							String paymentTermMapping = propertiesForMapping.getProperty("cust.paymentTerm." + paymentTerm.getName());
							caeb.setTermsOfPaymentKey(paymentTerm.getName()); 
						}
					}
					if(debitor.getCreditLimit() != null) {
						caeb.setCreditlimit(debitor.getCreditLimit().getAmount());
					}
					BankAccount bankAccount = debitor.getBankAccountDefault();
					if(bankAccount != null) {
						caeb.setBankAccountNumber(getNotFilled(bankAccount.getAccountId()));
						BankBranch bb = bankAccount.getBankBranch();
						if(bb != null) {
							caeb.setReferenceSpecificationsForBankDetails(getNotFilled(bb.getName()));
						}
					}
				}
				if(ca.getNumberOfEmployees() != 0) {
					caeb.setFrozenYearlyNumberofEmployeesForTargetFigure(ca.getNumberOfEmployees());
				} else {
					caeb.setFrozenYearlyNumberofEmployeesForTargetFigure(3);
				}
				caeb.setMainPartner("/");
				caeb.setDunningProcedure("CN01");
				caeb.setVATRegistrationNumber("");
				caeb.setCreditControlArea("");
				caeb.setPartnerFunctionSKNVK("99");
				caeb.setDistributionChannel("00");
				caeb.setDivision("00");
				caeb.setDeliveryPlant("");
				caeb.setPriceList("");
				caeb.setIndustrykey("/");
				caeb.setBankCountryKey("CN");
				caeb.setBankKeysBankIdentificationCode("/");
				caeb.setAccountingClerk("/");
				caeb.setReconciliationAccountInGeneralLedger("/");
				caeb.setPartialDeliveryAtItemLevel("D");
				caeb.setAddInvoiceToParcel("0");
				caeb.setSalesOrganisation("");
				caeb.setCompanyCode("");
				caeb.setPartnerFunctionSKNVP("BP");
				caeb.setParnterCounter(0);
				caeb.setCustomerNumberOfPartner(caeb.getCustomerNumber());
				Company company = ca.getParentCompany();
				List<BusinessRole> businessRoles = company.getAllChildBusinessRole();
				int debitorpartnerCount = 0;
				int goodsRecipientCount = 0;
				for (BusinessRole businessRole : businessRoles) {
					if(businessRole instanceof Debitor) {
						Debitor sameCPDebitor = (Debitor) businessRole;
						if(sameCPDebitor.getStatus().equals(DebitorStatus.DELETED)) {
							continue;
						}
						CustomerAccountExportBean cpDebtor = new CustomerAccountExportBean();
						if(debitor != null && debitor.getId() != sameCPDebitor.getId()) {
							if(debitor.getId() != sameCPDebitor.getId()) {
								cpDebtor.setCustomerNumber(ca.getAccountNumber() + "");
								cpDebtor.setCompanyCode("");
								cpDebtor.setDistributionChannel("00");
								cpDebtor.setDivision("00");
								cpDebtor.setPartnerFunctionSKNVP("PY");
								debitorpartnerCount += 1;
								cpDebtor.setParnterCounter(debitorpartnerCount);
								cpDebtor.setCustomerNumberOfPartner(sameCPDebitor.getId() + "");
							} else {
								cpDebtor.setCustomerNumber(ca.getAccountNumber() + "");
								cpDebtor.setCompanyCode("");
								cpDebtor.setDistributionChannel("00");
								cpDebtor.setDivision("00");
								cpDebtor.setPartnerFunctionSKNVP("PY");
								cpDebtor.setParnterCounter(0);
								cpDebtor.setCustomerNumberOfPartner(ca.getAccountNumber() + "");
							}
						} else {
							if(ca.getAccountNumber() == sameCPDebitor.getId()) {
								cpDebtor.setCustomerNumber(ca.getAccountNumber() + "");
								cpDebtor.setCompanyCode("");
								cpDebtor.setDistributionChannel("00");
								cpDebtor.setDivision("00");
								cpDebtor.setPartnerFunctionSKNVP("PY");
								cpDebtor.setParnterCounter(0);
								cpDebtor.setCustomerNumberOfPartner(ca.getAccountNumber() + "");
							} else {
								cpDebtor.setCustomerNumber(ca.getAccountNumber() + "");
								cpDebtor.setCompanyCode("");
								cpDebtor.setDistributionChannel("00");
								cpDebtor.setDivision("00");
								cpDebtor.setPartnerFunctionSKNVP("PY");
								debitorpartnerCount += 1;
								cpDebtor.setParnterCounter(debitorpartnerCount);
								cpDebtor.setCustomerNumberOfPartner(sameCPDebitor.getId() + "");
							}

						}
						debtorList.add(cpDebtor);
						System.out.println("Debitor :" + sameCPDebitor.getId() + " Customer :" + ca.getAccountNumber());
					} else if(businessRole instanceof GoodsRecipient) {
						GoodsRecipient sameCPGoodsRecipient = (GoodsRecipient) businessRole;
						if(sameCPGoodsRecipient.getStatus().equals(GoodsRecipientStatus.DELETED)) {
							continue;
						}
						CustomerAccountExportBean cpGoodsRecipient = new CustomerAccountExportBean();
						if(ca.getGoodsRecipientdefault() != null) {
							if(ca.getGoodsRecipientdefault().getId() != sameCPGoodsRecipient.getId()) {
								cpGoodsRecipient.setCustomerNumber(ca.getAccountNumber() + "");
								cpGoodsRecipient.setCompanyCode("");
								cpGoodsRecipient.setDistributionChannel("00");
								cpGoodsRecipient.setDivision("00");
								cpGoodsRecipient.setPartnerFunctionSKNVP("SH");
								goodsRecipientCount += 1;
								cpGoodsRecipient.setParnterCounter(goodsRecipientCount);
								cpGoodsRecipient.setCustomerNumberOfPartner(sameCPGoodsRecipient.getId() + "");
							} else {
								cpGoodsRecipient.setCustomerNumber(ca.getAccountNumber() + "");
								cpGoodsRecipient.setCompanyCode("");
								cpGoodsRecipient.setDistributionChannel("00");
								cpGoodsRecipient.setDivision("00");
								cpGoodsRecipient.setPartnerFunctionSKNVP("SH");
								cpGoodsRecipient.setParnterCounter(0);
								cpGoodsRecipient.setCustomerNumberOfPartner(ca.getAccountNumber() + "");
							}
						} else {
							if(ca.getAccountNumber() == sameCPGoodsRecipient.getId()) {
								cpGoodsRecipient.setCustomerNumber(ca.getAccountNumber() + "");
								cpGoodsRecipient.setCompanyCode("");
								cpGoodsRecipient.setDistributionChannel("00");
								cpGoodsRecipient.setDivision("00");
								cpGoodsRecipient.setPartnerFunctionSKNVP("SH");
								cpGoodsRecipient.setParnterCounter(0);
								cpGoodsRecipient.setCustomerNumberOfPartner(ca.getAccountNumber() + "");
							} else {
								cpGoodsRecipient.setCustomerNumber(ca.getAccountNumber() + "");
								cpGoodsRecipient.setCompanyCode("");
								cpGoodsRecipient.setDistributionChannel("00");
								cpGoodsRecipient.setDivision("00");
								cpGoodsRecipient.setPartnerFunctionSKNVP("SH");
								goodsRecipientCount += 1;
								cpGoodsRecipient.setParnterCounter(goodsRecipientCount);
								cpGoodsRecipient.setCustomerNumberOfPartner(sameCPGoodsRecipient.getId() + "");
							}

						}
						

						goodsReceiptList.add(cpGoodsRecipient);
						System.out.println("GoodsRecipient :" + sameCPGoodsRecipient.getId() + " Customer :" + ca.getAccountNumber());
					} 
				}
				custList.add(caeb);
			}

		} catch (PUserException e) {
			e.printStackTrace();
		} finally {
			penum.destroy();
		}
	}
	
	protected String getCountryKey(String language) {
		if(language.equals("Chinese")) {
			return "CN";
		}
		return "";
	}
	
	protected String getEmpty(String value) {
		if(value == null) return "";
		else return value;
	}
	
	protected String getNotFilled(String value){
		if(value == null){
			return "/";
		}else if(value.equals("")){
			return "/";
		}
		else return value;
	}
	
	private XSSFRow getXSSFRow(XSSFSheet sheet, int i) {
		XSSFRow row = sheet.getRow(i);
		if(row == null) {
			row = sheet.createRow(i);
		}
		return row;
	}
	
	private XSSFCell getXSSFCell(XSSFRow row, int i) {
		XSSFCell cell = row.getCell(i);
		if(cell == null) {
			cell = row.createCell(i);
		}
		return cell;
	}
	
	
	protected void insertSKNA1() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tempSKNA1);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(dataSource));
			int rowNum = 5;
			XSSFSheet sheetSKNA1 = twb.getSheet("SKNA1");
			for (CustomerAccountExportBean caeb : custList) {
				
				//Sheet SKNA1
				
				XSSFRow rowSKNA1 = getXSSFRow(sheetSKNA1, rowNum);
				XSSFCell cellA_SKNA1 = getXSSFCell(rowSKNA1, column("A"));
				cellA_SKNA1.setCellValue(caeb.getCustomerNumber());
				
				XSSFCell cellB_SKNA1 = getXSSFCell(rowSKNA1, column("B"));
				cellB_SKNA1.setCellValue("/");
				XSSFCell cellC_SKNA1 = getXSSFCell(rowSKNA1, column("C"));
				cellC_SKNA1.setCellValue("/");
				
				XSSFCell cellD_SKNA1 = getXSSFCell(rowSKNA1, column("D"));
				cellD_SKNA1.setCellValue(caeb.getIndustrykey());
				XSSFCell cellE_SKNA1 = getXSSFCell(rowSKNA1, column("E"));
				cellE_SKNA1.setCellValue(caeb.getCustomerAccountGroup());
				XSSFCell cellF_SKNA1 = getXSSFCell(rowSKNA1, column("F"));
				cellF_SKNA1.setCellValue(caeb.getControllingFlagforAccountGroup());
				XSSFCell cellG_SKNA1 = getXSSFCell(rowSKNA1, column("G"));
				cellG_SKNA1.setCellValue(caeb.getCountryKey());
				XSSFCell cellH_SKNA1 = getXSSFCell(rowSKNA1, column("H"));
				cellH_SKNA1.setCellValue("/");
				XSSFCell cellI_SKNA1 = getXSSFCell(rowSKNA1, column("I"));
				cellI_SKNA1.setCellValue(caeb.getName1());
				XSSFCell cellJ_SKNA1 = getXSSFCell(rowSKNA1, column("J"));
				cellJ_SKNA1.setCellValue(caeb.getName2());
				XSSFCell cellK_SKNA1 = getXSSFCell(rowSKNA1, column("K"));
				cellK_SKNA1.setCellValue("/");
				XSSFCell cellL_SKNA1 = getXSSFCell(rowSKNA1, column("L"));
				cellL_SKNA1.setCellValue("/");
				XSSFCell cellM_SKNA1 = getXSSFCell(rowSKNA1, column("M"));
				cellM_SKNA1.setCellValue("/");
				XSSFCell cellN_SKNA1 = getXSSFCell(rowSKNA1, column("N"));
				cellN_SKNA1.setCellValue(caeb.getCity());
				XSSFCell cellO_SKNA1 = getXSSFCell(rowSKNA1, column("O"));
				cellO_SKNA1.setCellValue(caeb.getCity());
				XSSFCell cellP_SKNA1 = getXSSFCell(rowSKNA1, column("P"));
				cellP_SKNA1.setCellValue(caeb.getPoBox());
				XSSFCell cellQ_SKNA1 = getXSSFCell(rowSKNA1, column("Q"));
				cellQ_SKNA1.setCellValue(caeb.getPostalCode());
				XSSFCell cellR_SKNA1 = getXSSFCell(rowSKNA1, column("R"));
				cellR_SKNA1.setCellValue("/");
				XSSFCell cellS_SKNA1 = getXSSFCell(rowSKNA1, column("S"));
				cellS_SKNA1.setCellValue(caeb.getSortField());
				
				XSSFCell cellT_SKNA1 = getXSSFCell(rowSKNA1, column("T"));
				cellT_SKNA1.setCellValue(caeb.getLanguageKey());
				XSSFCell cellU_SKNA1 = getXSSFCell(rowSKNA1, column("U"));
				cellU_SKNA1.setCellValue(caeb.getHouseNumberAndStreet());
				XSSFCell cellV_SKNA1 = getXSSFCell(rowSKNA1, column("V"));
				cellV_SKNA1.setCellValue(caeb.getFirstTelephoneNumber());
				XSSFCell cellW_SKNA1 = getXSSFCell(rowSKNA1, column("W"));
				cellW_SKNA1.setCellValue("/");
				XSSFCell cellX_SKNA1 = getXSSFCell(rowSKNA1, column("X"));
				cellX_SKNA1.setCellValue(caeb.getFaxNumber());
				
				XSSFCell cellY_SKNA1 = getXSSFCell(rowSKNA1, column("Y"));
				cellY_SKNA1.setCellValue("/");
				XSSFCell cellZ_SKNA1 = getXSSFCell(rowSKNA1, column("Z"));
				cellZ_SKNA1.setCellValue("/");
				XSSFCell cellAA_SKNA1 = getXSSFCell(rowSKNA1, column("AA"));
				cellAA_SKNA1.setCellValue("/");
				XSSFCell cellAB_SKNA1 = getXSSFCell(rowSKNA1, column("AB"));
				cellAB_SKNA1.setCellValue("/");
				XSSFCell cellAC_SKNA1 = getXSSFCell(rowSKNA1, column("AC"));
				cellAC_SKNA1.setCellValue("/");
				
				XSSFCell cellAD_SKNA1 = getXSSFCell(rowSKNA1, column("AD"));
				cellAD_SKNA1.setCellValue(caeb.getCustomerStatus());
				XSSFCell cellAE_SKNA1 = getXSSFCell(rowSKNA1, column("AE"));
				cellAE_SKNA1.setCellValue("/");
				XSSFCell cellAF_SKNA1 = getXSSFCell(rowSKNA1, column("AF"));
				cellAF_SKNA1.setCellValue(caeb.getCompanyCode());
				XSSFCell cellAG_SKNA1 = getXSSFCell(rowSKNA1, column("AG"));
				
				XSSFCell cellAH_SKNA1 = getXSSFCell(rowSKNA1, column("AH"));
				cellAH_SKNA1.setCellValue("/");
				XSSFCell cellAI_SKNA1 = getXSSFCell(rowSKNA1, column("AI"));
				cellAI_SKNA1.setCellValue("/");
				
				XSSFCell cellAJ_SKNA1 = getXSSFCell(rowSKNA1, column("AJ"));
				cellAJ_SKNA1.setCellValue(caeb.getVATRegistrationNumber());
				XSSFCell cellAK_SKNA1 = getXSSFCell(rowSKNA1, column("AK"));
				cellAK_SKNA1.setCellValue("/");
				XSSFCell cellAL_SKNA1 = getXSSFCell(rowSKNA1, column("AL"));
				cellAL_SKNA1.setCellValue("/");
				
				cellAG_SKNA1.setCellValue(caeb.getSalesRepNo());
				XSSFCell cellAM_SKNA1 = getXSSFCell(rowSKNA1, column("AM"));
				cellAM_SKNA1.setCellValue(caeb.getProvince());
				XSSFCell cellAN_SKNA1 = getXSSFCell(rowSKNA1, column("AN"));
				cellAN_SKNA1.setCellValue(caeb.getFrozenYearlyNumberofEmployeesForTargetFigure());
				
				XSSFCell cellAO_SKNA1 = getXSSFCell(rowSKNA1, column("AO"));
				cellAO_SKNA1.setCellValue("/");
				XSSFCell cellAP_SKNA1 = getXSSFCell(rowSKNA1, column("AP"));
				cellAP_SKNA1.setCellValue("/");
				XSSFCell cellAQ_SKNA1 = getXSSFCell(rowSKNA1, column("AQ"));
				cellAQ_SKNA1.setCellValue("/");
				XSSFCell cellAR_SKNA1 = getXSSFCell(rowSKNA1, column("AR"));
				cellAR_SKNA1.setCellValue("/");
				XSSFCell cellAS_SKNA1 = getXSSFCell(rowSKNA1, column("AS"));
				cellAS_SKNA1.setCellValue("/");
				XSSFCell cellAT_SKNA1 = getXSSFCell(rowSKNA1, column("AT"));
				cellAT_SKNA1.setCellValue(caeb.getCreationDate());
				rowNum ++;
				System.out.println("SKNA1 Line --->" + rowNum + " inserted-------------------------------------<" );
			}
			twb.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
	protected void insertSKNKK() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tempSKNKK);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(tempSKNA1));
			int rowNum = 5;
			XSSFSheet sheetSKNKK = twb.getSheet("SKNKK");
			for (CustomerAccountExportBean caeb : custList) {
				// sheet SKNKK

				XSSFRow rowSKNKK = getXSSFRow(sheetSKNKK, rowNum);
				XSSFCell cellA_SKNKK = getXSSFCell(rowSKNKK, column("A"));
				cellA_SKNKK.setCellValue(caeb.getCustomerNumber());
				XSSFCell cellB_SKNKK = getXSSFCell(rowSKNKK, column("B"));
				cellB_SKNKK.setCellValue(caeb.getCreditControlArea());
				XSSFCell cellC_SKNKK = getXSSFCell(rowSKNKK, column("C"));
				cellC_SKNKK.setCellValue(caeb.getCreditlimit());
				XSSFCell cellD_SKNKK = getXSSFCell(rowSKNKK, column("D"));
				cellD_SKNKK.setCellValue(caeb.getCustomerNumber());
				XSSFCell cellE_SKNKK = getXSSFCell(rowSKNKK, column("E"));
				cellE_SKNKK.setCellValue("/");
				rowNum++;
				System.out.println("SKNKK Line --->" + rowNum + " inserted-------------------------------------<");
			}
			twb.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}
	
	protected void insertSKNVK() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tempSKNVK);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(tempSKNKK));
			int rowNum = 5;
			XSSFSheet sheetSKNVK = twb.getSheet("SKNVK");
			for (CustomerAccountExportBean caeb : custList) {
				//sheet SKNVK
				
				XSSFRow rowSKNVK = getXSSFRow(sheetSKNVK, rowNum);
				XSSFCell cellA_SKNVK = getXSSFCell(rowSKNVK, column("A"));
				cellA_SKNVK.setCellValue(caeb.getCustomerNumber());
				XSSFCell cellB_SKNVK = getXSSFCell(rowSKNVK, column("B"));
				cellB_SKNVK.setCellValue("/");
				XSSFCell cellC_SKNVK = getXSSFCell(rowSKNVK, column("C"));
				cellC_SKNVK.setCellValue(caeb.getLanguageKey());
				XSSFCell cellD_SKNVK = getXSSFCell(rowSKNVK, column("D"));
				cellD_SKNVK.setCellValue("/");
				XSSFCell cellE_SKNVK = getXSSFCell(rowSKNVK, column("E"));
				cellE_SKNVK.setCellValue("/");
				XSSFCell cellF_SKNVK = getXSSFCell(rowSKNVK, column("F"));
				cellF_SKNVK.setCellValue(caeb.getContactPersonName());
				XSSFCell cellG__SKNVK = getXSSFCell(rowSKNVK, column("G"));
				cellG__SKNVK.setCellValue(caeb.getMainPartner());
				XSSFCell cellH__SKNVK = getXSSFCell(rowSKNVK, column("H"));
				cellH__SKNVK.setCellValue(caeb.getPartnerFunctionSKNVK());
				
				XSSFCell cellI__SKNVK = getXSSFCell(rowSKNVK, column("I"));
				cellI__SKNVK.setCellValue(caeb.getTelphone());
				XSSFCell cellJ__SKNVK = getXSSFCell(rowSKNVK, column("J"));
				cellJ__SKNVK.setCellValue(caeb.getMobilephone());
				XSSFCell cellK__SKNVK = getXSSFCell(rowSKNVK, column("K"));
				cellK__SKNVK.setCellValue(caeb.getFaxNumber());
				XSSFCell cellL__SKNVK = getXSSFCell(rowSKNVK, column("L"));
				cellL__SKNVK.setCellValue(caeb.getEmail());
				XSSFCell cellM_SKNVK = getXSSFCell(rowSKNVK, column("M"));
				cellM_SKNVK.setCellValue("/");
				XSSFCell cellN_SKNVK = getXSSFCell(rowSKNVK, column("N"));
				cellN_SKNVK.setCellValue("/");
				XSSFCell cellO_SKNVK = getXSSFCell(rowSKNVK, column("O"));
				cellO_SKNVK.setCellValue("/");
				XSSFCell cellP_SKNVK = getXSSFCell(rowSKNVK, column("P"));
				cellP_SKNVK.setCellValue("/");
				XSSFCell cellQ_SKNVK = getXSSFCell(rowSKNVK, column("Q"));
				cellQ_SKNVK.setCellValue("/");
				rowNum ++;
				System.out.println("SKNVK Line --->" + rowNum + " inserted-------------------------------------<" );
			}
			twb.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
	protected void insertSKNBK() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tempSKNBK);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(tempSKNVK));
			int rowNum = 5;
			XSSFSheet sheetSKNBK = twb.getSheet("SKNBK");
			for (CustomerAccountExportBean caeb : custList) {
				//sheet SKNBK
				
				XSSFRow rowSKNBK = getXSSFRow(sheetSKNBK, rowNum);
				XSSFCell cellA_SKNBK = getXSSFCell(rowSKNBK, column("A"));
				cellA_SKNBK.setCellValue(caeb.getCustomerNumber());
				XSSFCell cellB_SKNBK = getXSSFCell(rowSKNBK, column("B"));
				cellB_SKNBK.setCellValue(caeb.getCompanyCode());
				XSSFCell cellC_SKNBK = getXSSFCell(rowSKNBK, column("C"));
				cellC_SKNBK.setCellValue(caeb.getBankCountryKey());
				XSSFCell cellD_SKNBK = getXSSFCell(rowSKNBK, column("D"));
				cellD_SKNBK.setCellValue(caeb.getBankKeysBankIdentificationCode());
				XSSFCell cellE_SKNBK = getXSSFCell(rowSKNBK, column("E"));
				cellE_SKNBK.setCellValue(caeb.getBankAccountNumber());
				XSSFCell cellF_SKNBK = getXSSFCell(rowSKNBK, column("F"));
				cellF_SKNBK.setCellValue(caeb.getBankAccountNumber());
				XSSFCell cellG_SKNBK = getXSSFCell(rowSKNBK, column("G"));
				cellG_SKNBK.setCellValue(caeb.getBankAccountNumber());
				XSSFCell cellH_SKNBK = getXSSFCell(rowSKNBK, column("H"));
				cellH_SKNBK.setCellValue(caeb.getBankAccountNumber());
				XSSFCell cellI_SKNBK = getXSSFCell(rowSKNBK, column("I"));
				cellI_SKNBK.setCellValue(caeb.getReferenceSpecificationsForBankDetails());
				

				rowNum ++;
				System.out.println("SKNBK Line --->" + rowNum + " inserted-------------------------------------<" );
			}
			twb.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
	protected void insertSKNB1() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tempSKNB1);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(tempSKNBK));
			int rowNum = 5;
			XSSFSheet sheetSKNB1 = twb.getSheet("SKNB1");
			for (CustomerAccountExportBean caeb : custList) {
				//sheet SKNB1
				
				XSSFRow rowSKNB1 = getXSSFRow(sheetSKNB1, rowNum);
				XSSFCell cellA_SKNB1 = getXSSFCell(rowSKNB1, column("A"));
				cellA_SKNB1.setCellValue(caeb.getCustomerNumber());
				XSSFCell cellB_SKNB1 = getXSSFCell(rowSKNB1, column("B"));
				cellB_SKNB1.setCellValue(caeb.getCompanyCode());
				XSSFCell cellC_SKNB1 = getXSSFCell(rowSKNB1, column("C"));
				cellC_SKNB1.setCellValue(caeb.getAccountingClerk());
				
				XSSFCell cellD_SKNB1 = getXSSFCell(rowSKNB1, column("D"));
				cellD_SKNB1.setCellValue(caeb.getReconciliationAccountInGeneralLedger());
				
				XSSFCell cellE_SKNB1 = getXSSFCell(rowSKNB1, column("E"));
				cellE_SKNB1.setCellValue("/");
				XSSFCell cellF_SKNB1 = getXSSFCell(rowSKNB1, column("F"));
				cellF_SKNB1.setCellValue("/");
				XSSFCell cellG_SKNB1 = getXSSFCell(rowSKNB1, column("G"));
				cellG_SKNB1.setCellValue(caeb.getTermsOfPaymentKey());
				XSSFCell cellH_SKNB1 = getXSSFCell(rowSKNB1, column("H"));
				cellH_SKNB1.setCellValue("/");
				XSSFCell cellI_SKNB1 = getXSSFCell(rowSKNB1, column("I"));
				cellI_SKNB1.setCellValue("/");
				XSSFCell cellJ_SKNB1 = getXSSFCell(rowSKNB1, column("J"));
				cellJ_SKNB1.setCellValue("/");
				

				rowNum ++;
				System.out.println("SKNB1 Line --->" + rowNum + " inserted-------------------------------------<" );
			}
			twb.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
	protected void insertSKNB5() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tempSKNB5);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(tempSKNB1));
			int rowNum = 5;
			XSSFSheet sheetSKNB5 = twb.getSheet("SKNB5");
			for (CustomerAccountExportBean caeb : custList) {
				//sheet SKNB5
				
				XSSFRow rowSKNB5 = getXSSFRow(sheetSKNB5, rowNum);
				XSSFCell cellA_SKNB5 = getXSSFCell(rowSKNB5, column("A"));
				cellA_SKNB5.setCellValue(caeb.getCustomerNumber());
				XSSFCell cellB_SKNB5 = getXSSFCell(rowSKNB5, column("B"));
				cellB_SKNB5.setCellValue(caeb.getCompanyCode());
				XSSFCell cellC_SKNB5 = getXSSFCell(rowSKNB5, column("C"));
				cellC_SKNB5.setCellValue(caeb.getDunningProcedure());
				XSSFCell cellD_SKNB5 = getXSSFCell(rowSKNB5, column("D"));
				cellD_SKNB5.setCellValue(caeb.getAccountingClerk());
				

				rowNum ++;
				System.out.println("SKNB5 Line --->" + rowNum + " inserted-------------------------------------<" );
			}
			twb.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
	protected void insertSKNVV_PartA() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tempSKNVV_PARTA);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(tempSKNB5));
			int rowNum = 5;
			XSSFSheet sheetSKNVV = twb.getSheet("SKNVV");
			for (CustomerAccountExportBean caeb : custList) {
				//sheet SKNVV
				
				XSSFRow rowSKNVV = getXSSFRow(sheetSKNVV, rowNum);
				XSSFCell cellA_SKNVV = getXSSFCell(rowSKNVV, column("A"));
				cellA_SKNVV.setCellValue(caeb.getCustomerNumber());
				XSSFCell cellB_SKNVV = getXSSFCell(rowSKNVV, column("B"));
				cellB_SKNVV.setCellValue(caeb.getSalesOrganisation());
				XSSFCell cellC_SKNVV = getXSSFCell(rowSKNVV, column("C"));
				cellC_SKNVV.setCellValue(caeb.getDistributionChannel());
				XSSFCell cellD_SKNVV = getXSSFCell(rowSKNVV, column("D"));
				cellD_SKNVV.setCellValue(caeb.getDivision());
				XSSFCell cellE_SKNVV = getXSSFCell(rowSKNVV, column("E"));
				cellE_SKNVV.setCellValue("/");
				XSSFCell cellF_SKNVV = getXSSFCell(rowSKNVV, column("F"));
				cellF_SKNVV.setCellValue(caeb.getPartialDeliveryAtItemLevel());
				
				XSSFCell cellG_SKNVV = getXSSFCell(rowSKNVV, column("G"));
				cellG_SKNVV.setCellValue("/");
				XSSFCell cellH_SKNVV = getXSSFCell(rowSKNVV, column("H"));
				cellH_SKNVV.setCellValue("/");
				XSSFCell cellI_SKNVV = getXSSFCell(rowSKNVV, column("I"));
				cellI_SKNVV.setCellValue("/");
				XSSFCell cellJ_SKNVV = getXSSFCell(rowSKNVV, column("J"));
				cellJ_SKNVV.setCellValue("/");
				XSSFCell cellK_SKNVV = getXSSFCell(rowSKNVV, column("K"));
				cellK_SKNVV.setCellValue("/");
				XSSFCell cellL_SKNVV = getXSSFCell(rowSKNVV, column("L"));
				cellL_SKNVV.setCellValue("/");
				XSSFCell cellM_SKNVV = getXSSFCell(rowSKNVV, column("M"));
				cellM_SKNVV.setCellValue(caeb.getTermsOfPaymentKey());
				XSSFCell cellN_SKNVV = getXSSFCell(rowSKNVV, column("N"));
				cellN_SKNVV.setCellValue("/");
				XSSFCell cellO_SKNVV = getXSSFCell(rowSKNVV, column("O"));
				cellO_SKNVV.setCellValue("/");
				XSSFCell cellP_SKNVV = getXSSFCell(rowSKNVV, column("P"));
				cellP_SKNVV.setCellValue("/");

				XSSFCell cellQ_SKNVV = getXSSFCell(rowSKNVV, column("Q"));
				cellQ_SKNVV.setCellValue("/");
				XSSFCell cellR_SKNVV = getXSSFCell(rowSKNVV, column("R"));
				cellR_SKNVV.setCellValue("/");
				XSSFCell cellS_SKNVV = getXSSFCell(rowSKNVV, column("S"));
				cellS_SKNVV.setCellValue("/");
				XSSFCell cellT_SKNVV = getXSSFCell(rowSKNVV, column("T"));
				cellT_SKNVV.setCellValue("/");
				XSSFCell cellU_SKNVV = getXSSFCell(rowSKNVV, column("U"));
				cellU_SKNVV.setCellValue("/");
				XSSFCell cellV_SKNVV = getXSSFCell(rowSKNVV, column("V"));
				cellV_SKNVV.setCellValue("/");
				XSSFCell cellW_SKNVV = getXSSFCell(rowSKNVV, column("W"));
				cellW_SKNVV.setCellValue("/");
				XSSFCell cellX_SKNVV = getXSSFCell(rowSKNVV, column("X"));
				cellX_SKNVV.setCellValue("/");
				XSSFCell cellY_SKNVV = getXSSFCell(rowSKNVV, column("Y"));
				cellY_SKNVV.setCellValue("/");
				XSSFCell cellZ_SKNVV = getXSSFCell(rowSKNVV, column("Z"));
				cellZ_SKNVV.setCellValue("/");
				
			

				rowNum ++;
				System.out.println("SKNVV_PARTA Line --->" + rowNum + " inserted-------------------------------------<" );
			}
			twb.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
	
	protected void insertSKNVV_PartB() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tempSKNVV_PARTB);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(tempSKNVV_PARTA));
			int rowNum = 5;
			XSSFSheet sheetSKNVV = twb.getSheet("SKNVV");
			for (CustomerAccountExportBean caeb : custList) {
				//sheet SKNVV
				
				XSSFRow rowSKNVV = getXSSFRow(sheetSKNVV, rowNum);

				
				XSSFCell cellAA_SKNVV = getXSSFCell(rowSKNVV, column("AA"));
				cellAA_SKNVV.setCellValue("/");
				XSSFCell cellAB_SKNVV = getXSSFCell(rowSKNVV, column("AB"));
				cellAB_SKNVV.setCellValue("/");
				XSSFCell cellAC_SKNVV = getXSSFCell(rowSKNVV, column("AC"));
				cellAC_SKNVV.setCellValue("/");
				XSSFCell cellAD_SKNVV = getXSSFCell(rowSKNVV, column("AD"));
				cellAD_SKNVV.setCellValue("/");
				XSSFCell cellAE_SKNVV = getXSSFCell(rowSKNVV, column("AE"));
				cellAE_SKNVV.setCellValue("/");
				XSSFCell cellAF_SKNVV = getXSSFCell(rowSKNVV, column("AF"));
				cellAF_SKNVV.setCellValue("/");
				XSSFCell cellAG_SKNVV = getXSSFCell(rowSKNVV, column("AG"));
				cellAG_SKNVV.setCellValue("/");
				XSSFCell cellAH_SKNVV = getXSSFCell(rowSKNVV, column("AH"));
				cellAH_SKNVV.setCellValue("/");
				XSSFCell cellAI_SKNVV = getXSSFCell(rowSKNVV, column("AI"));
				cellAI_SKNVV.setCellValue("/");
				XSSFCell cellAJ_SKNVV = getXSSFCell(rowSKNVV, column("AJ"));
				cellAJ_SKNVV.setCellValue("/");
				XSSFCell cellAK_SKNVV = getXSSFCell(rowSKNVV, column("AK"));
				cellAK_SKNVV.setCellValue("/");
				XSSFCell cellAL_SKNVV = getXSSFCell(rowSKNVV, column("AL"));
				cellAL_SKNVV.setCellValue("/");
				XSSFCell cellAM_SKNVV = getXSSFCell(rowSKNVV, column("AM"));
				cellAM_SKNVV.setCellValue("/");
				XSSFCell cellAN_SKNVV = getXSSFCell(rowSKNVV, column("AN"));
				cellAN_SKNVV.setCellValue(caeb.getAddInvoiceToParcel());
				XSSFCell cellAO_SKNVV = getXSSFCell(rowSKNVV, column("AO"));
				cellAO_SKNVV.setCellValue(caeb.getDeliveryPlant());
				XSSFCell cellAP_SKNVV = getXSSFCell(rowSKNVV, column("AP"));
				cellAP_SKNVV.setCellValue(caeb.getPriceList());
				XSSFCell cellAQ_SKNVV = getXSSFCell(rowSKNVV, column("AQ"));
				cellAQ_SKNVV.setCellValue("/");
				XSSFCell cellAR_SKNVV = getXSSFCell(rowSKNVV, column("AR"));
				cellAR_SKNVV.setCellValue("/");
				XSSFCell cellAS_SKNVV = getXSSFCell(rowSKNVV, column("AS"));
				cellAS_SKNVV.setCellValue("/");
				

				rowNum ++;
				System.out.println("SKNVV_PARTB Line --->" + rowNum + " inserted-------------------------------------<" );
			}
			twb.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
	protected void insertSKNVP() {
		
			FileOutputStream fos = null;
			try {
				int rowNum = 5;
				fos = new FileOutputStream(tempSKNVP);
				XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(dataSource));
				
				XSSFSheet sheetSKNVP = twb.getSheet("SKNVP");
				for (CustomerAccountExportBean caeb : custList) {
					// sheet SNKVP
					XSSFRow rowSKNVP = getXSSFRow(sheetSKNVP, rowNum);
					XSSFCell cellA_SKNVP = getXSSFCell(rowSKNVP, column("A"));
					cellA_SKNVP.setCellValue(caeb.getCustomerNumber());
					XSSFCell cellB_SKNVP = getXSSFCell(rowSKNVP, column("B"));
					cellB_SKNVP.setCellValue(caeb.getSalesOrganisation());
					XSSFCell cellC_SKNVP = getXSSFCell(rowSKNVP, column("C"));
					cellC_SKNVP.setCellValue(caeb.getDistributionChannel());
					XSSFCell cellD_SKNVP = getXSSFCell(rowSKNVP, column("D"));
					cellD_SKNVP.setCellValue(caeb.getDivision());
					XSSFCell cellE_SKNVP = getXSSFCell(rowSKNVP, column("E"));
					cellE_SKNVP.setCellValue(caeb.getPartnerFunctionSKNVP());
					XSSFCell cellF_SKNVP = getXSSFCell(rowSKNVP, column("F"));
					cellF_SKNVP.setCellValue(caeb.getParnterCounter());
					XSSFCell cellG_SKNVP = getXSSFCell(rowSKNVP, column("G"));
					cellG_SKNVP.setCellValue(caeb.getCustomerNumberOfPartner());
					XSSFCell cellH_SKNVP = getXSSFCell(rowSKNVP, column("H"));
					cellH_SKNVP.setCellValue("/");
					XSSFCell cellI_SKNVP = getXSSFCell(rowSKNVP, column("I"));
					cellI_SKNVP.setCellValue("/");
					XSSFCell cellJ_SKNVP = getXSSFCell(rowSKNVP, column("J"));
					cellJ_SKNVP.setCellValue("/");
					XSSFCell cellK_SKNVP = getXSSFCell(rowSKNVP, column("K"));
					cellK_SKNVP.setCellValue("/");
					XSSFCell cellL_SKNVP = getXSSFCell(rowSKNVP, column("L"));
					cellL_SKNVP.setCellValue("/");
					XSSFCell cellM_SKNVP = getXSSFCell(rowSKNVP, column("M"));
					cellM_SKNVP.setCellValue("/");
					XSSFCell cellN_SKNVP = getXSSFCell(rowSKNVP, column("N"));
					cellN_SKNVP.setCellValue("/");

					rowNum++;
					System.out.println("SKNVP Line --->" + rowNum + " inserted-------------------------------------<");
				}
				totalLineCount = rowNum;
				twb.write(fos);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	}
	
	protected void udpateSKNVP() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(target);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(tempSKNVP));
			int rowNum = totalLineCount;
			XSSFSheet sheetSKNVP = twb.getSheet("SKNVP");
			for (CustomerAccountExportBean caeb : debtorList) {
				XSSFRow rowSKNVP = getXSSFRow(sheetSKNVP, rowNum);
				XSSFCell cellA_SKNVP = getXSSFCell(rowSKNVP, column("A"));
				cellA_SKNVP.setCellValue(caeb.getCustomerNumber());
				XSSFCell cellB_SKNVP = getXSSFCell(rowSKNVP, column("B"));
				cellB_SKNVP.setCellValue(caeb.getSalesOrganisation());
				XSSFCell cellC_SKNVP = getXSSFCell(rowSKNVP, column("C"));
				cellC_SKNVP.setCellValue(caeb.getDistributionChannel());
				XSSFCell cellD_SKNVP = getXSSFCell(rowSKNVP, column("D"));
				cellD_SKNVP.setCellValue(caeb.getDivision());
				XSSFCell cellE_SKNVP = getXSSFCell(rowSKNVP, column("E"));
				cellE_SKNVP.setCellValue(caeb.getPartnerFunctionSKNVP());
				XSSFCell cellF_SKNVP = getXSSFCell(rowSKNVP, column("F"));
				cellF_SKNVP.setCellValue(caeb.getParnterCounter());
				XSSFCell cellG_SKNVP = getXSSFCell(rowSKNVP, column("G"));
				cellG_SKNVP.setCellValue(caeb.getCustomerNumberOfPartner());
				XSSFCell cellH_SKNVP = getXSSFCell(rowSKNVP, column("H"));
				cellH_SKNVP.setCellValue("/");
				XSSFCell cellI_SKNVP = getXSSFCell(rowSKNVP, column("I"));
				cellI_SKNVP.setCellValue("/");
				XSSFCell cellJ_SKNVP = getXSSFCell(rowSKNVP, column("J"));
				cellJ_SKNVP.setCellValue("/");
				XSSFCell cellK_SKNVP = getXSSFCell(rowSKNVP, column("K"));
				cellK_SKNVP.setCellValue("/");
				XSSFCell cellL_SKNVP = getXSSFCell(rowSKNVP, column("L"));
				cellL_SKNVP.setCellValue("/");
				XSSFCell cellM_SKNVP = getXSSFCell(rowSKNVP, column("M"));
				cellM_SKNVP.setCellValue("/");
				XSSFCell cellN_SKNVP = getXSSFCell(rowSKNVP, column("N"));
				cellN_SKNVP.setCellValue("/");
				System.out.println("Line --->" + rowNum + " updated-------------------------------------<" );
				rowNum ++;
			}
			
			for (CustomerAccountExportBean caeb : goodsReceiptList) {
				XSSFRow rowSKNVP = getXSSFRow(sheetSKNVP, rowNum);
				XSSFCell cellA_SKNVP = getXSSFCell(rowSKNVP, column("A"));
				cellA_SKNVP.setCellValue(caeb.getCustomerNumber());
				XSSFCell cellB_SKNVP = getXSSFCell(rowSKNVP, column("B"));
				cellB_SKNVP.setCellValue(caeb.getSalesOrganisation());
				XSSFCell cellC_SKNVP = getXSSFCell(rowSKNVP, column("C"));
				cellC_SKNVP.setCellValue(caeb.getDistributionChannel());
				XSSFCell cellD_SKNVP = getXSSFCell(rowSKNVP, column("D"));
				cellD_SKNVP.setCellValue(caeb.getDivision());
				XSSFCell cellE_SKNVP = getXSSFCell(rowSKNVP, column("E"));
				cellE_SKNVP.setCellValue(caeb.getPartnerFunctionSKNVP());
				XSSFCell cellF_SKNVP = getXSSFCell(rowSKNVP, column("F"));
				cellF_SKNVP.setCellValue(caeb.getParnterCounter());
				XSSFCell cellG_SKNVP = getXSSFCell(rowSKNVP, column("G"));
				cellG_SKNVP.setCellValue(caeb.getCustomerNumberOfPartner());
				XSSFCell cellH_SKNVP = getXSSFCell(rowSKNVP, column("H"));
				cellH_SKNVP.setCellValue("/");
				XSSFCell cellI_SKNVP = getXSSFCell(rowSKNVP, column("I"));
				cellI_SKNVP.setCellValue("/");
				XSSFCell cellJ_SKNVP = getXSSFCell(rowSKNVP, column("J"));
				cellJ_SKNVP.setCellValue("/");
				XSSFCell cellK_SKNVP = getXSSFCell(rowSKNVP, column("K"));
				cellK_SKNVP.setCellValue("/");
				XSSFCell cellL_SKNVP = getXSSFCell(rowSKNVP, column("L"));
				cellL_SKNVP.setCellValue("/");
				XSSFCell cellM_SKNVP = getXSSFCell(rowSKNVP, column("M"));
				cellM_SKNVP.setCellValue("/");
				XSSFCell cellN_SKNVP = getXSSFCell(rowSKNVP, column("N"));
				cellN_SKNVP.setCellValue("/");
				System.out.println("Line --->" + rowNum + " updated-------------------------------------<" );
				rowNum ++;
			}
			twb.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
}
