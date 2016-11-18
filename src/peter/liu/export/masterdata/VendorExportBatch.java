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

import com.wuerth.phoenix.Phxbasic.enums.SupplierStatus;
import com.wuerth.phoenix.Phxbasic.models.AddressStruct;
import com.wuerth.phoenix.Phxbasic.models.BankAccount;
import com.wuerth.phoenix.Phxbasic.models.BusinessAddress;
import com.wuerth.phoenix.Phxbasic.models.CommunicationStruct;
import com.wuerth.phoenix.Phxbasic.models.Contact;
import com.wuerth.phoenix.Phxbasic.models.Currency;
import com.wuerth.phoenix.Phxbasic.models.PhxbasicController;
import com.wuerth.phoenix.Phxbasic.models.Supplier;
import com.wuerth.phoenix.basic.etnax.sap.webservice.service.SylvestrixHelper;
import com.wuerth.phoenix.bcserver.base.DbPeer;
import com.wuerth.phoenix.bcutil.IteratorFactory;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;

public class VendorExportBatch {
	private PhxbasicController _controller;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private Properties propertiesForMapping;
	private String dataSource = "../../etc/exportSAP/Vendor_master_Template.xlsx";
	private String target = "../../var/exportSAP/Vendor_master_Template_out.xlsx";
	private String rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private String								ownCompanyName;
	private void init() {
		_controller = SylvestrixHelper.get().getController();
		propertiesForMapping = MappingParam.getInstance().getMappingProperties();
		ownCompanyName = _controller.getSingletonOwnCompany().getName();
	}
	
	public int column(String s) {

		if (s.length() == 1) {
			return rows.indexOf(s);
		}

		return 26 * (rows.indexOf(s.substring(0, 1)) + 1)
				+ column(s.substring(1, s.length()));
	}
	
	public String repString(String value, String repvalue) {
		if(value == null) {
			return repvalue;
		} else {
			return value;
		}
	}
	
	private String getEmpty(String value) {
		if(value == null) return "";
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
	
	private String getLanguage(String value) {
		if(value.equals("English")) {
			return "E";
		} else if(value.equals("Chinese")) {
			return "C";
		}
		return "E";
	}
	
	private String getCurrency(int  currencyNumber) {
		Currency currency = _controller.lookupCurrency(currencyNumber);
		if(currency == null) {
			return "CNY";
		}
//		if(currency.getName().equals("CNY")) {
//			return "RMB";
//		} else if(currency.getName().equals("EUR")) {
//			return "EUR";
//		} else if(currency.getName().equals("USD")) {
//			return "USD";
//		}
		return currency.getCurrencyCode();
	}
	
	private String getPurchasingValueKey(String currency) {
		if(currency.equals("CNY")) {
			return "local";
		} else {
			return "overseas";
		}
	}
	
	private List<VendorExportBean> export() {
		List<VendorExportBean> list = new ArrayList<VendorExportBean>();
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p = qh.attr(Supplier.STATUS).ne().val(SupplierStatus.DELETED).predicate();
		qh.setClass(Supplier.class);
		qh.addAscendingOrdering(Supplier.ACCOUNTNUMBER);
		PEnumeration penum = null;
        Condition condition = qh.condition(p);
        IteratorFactory fac = (IteratorFactory) _controller.createIteratorFactory();
        try {
			penum = fac.getCursor(condition);
			while (penum.hasMoreElements()) {
				Supplier supplier = (Supplier) penum.nextElement();
				
				if (ownCompanyName.equals("伍尔特（沈阳）五金工具有限公司")) {
					if (MasterDateMapping.syExistingVender.contains(supplier
							.getAccountNumber())) {
						continue;
					}
				}
				if (ownCompanyName.equals("伍尔特（天津）国际贸易有限公司")) {
					if (MasterDateMapping.tjExistingVender.contains(supplier
							.getAccountNumber())) {
						continue;
					}
				}
				
				System.out.println("Supplier :" + supplier.getAccountNumber() + "(" + supplier.getStatus().getDescription() + ")");
				VendorExportBean veb = new VendorExportBean();
				veb.setActionCode("009");
				veb.setVendorNumber(supplier.getAccountNumber() + "");
				if(supplier.getCreationDate() !=null) {
					veb.setCreationDate(sdf.format(supplier.getCreationDate()));
				} else {
					DbPeer dpBR = (DbPeer)supplier;
					veb.setCreationDate(sdf.format(dpBR.getCreateDate()));
				}
				if(supplier.getPaymentModedefault() != null) {
					veb.setPaymentMethods(propertiesForMapping.getProperty("vendor.paymentMode." + supplier.getPaymentModedefault().getName()));
				} else {
					veb.setPaymentMethods("");
				}
 				
				if (supplier.getParentCompany().getEnterpriseAccount() != null && !supplier.getParentCompany().getEnterpriseAccount().equals("")) {
					veb.setAccountGroup("ZLIK");
				} else {
					veb.setAccountGroup("ZLNK");
				}
				veb.setPaymentBlock("X");
				if(supplier.getPaymentTermdefault() != null) {
					veb.setTermsOfPaymentKey(supplier.getPaymentTermdefault().getName());
				} else {
					veb.setTermsOfPaymentKey("");
				}
				
				veb.setCheckFlagForDoubleInvoicesOrCreditMemos("X");
				veb.setToleranceGroup("Z001");
				veb.setPrepayment("C");
				BankAccount ba = supplier.getBankAccountDefault();
				if(ba != null) {
					veb.setBankAccount(getNotFilled(ba.getAccountId()));
					if(ba.getBankBranch() != null) {
						veb.setBankName(getNotFilled(ba.getBankBranch().getName()));
					} else {
						veb.setBankName("/");
					}
				} else {
					veb.setBankAccount("/");
					veb.setBankName("/");
				}
				veb.setTitle("Company");
				veb.setIndustry("0013");
				BusinessAddress businessAddress = supplier.getBusinessAddressdefault();
				if(businessAddress != null) {
					if(businessAddress.getAddress() != null) {
						AddressStruct as = businessAddress.getAddress();
						veb.setCountry(as.getCountryCode());
						veb.setCity(getNotFilled(as.getCity()));
						//Find the city!!!!!, try each way you can get
						
						if (as.getZip() != null
								&& !as.getZip().trim().equals("")) {
							veb.setPostcode(getNotFilled(as.getZip()));
						} else {
							if (as.getCity() != null
									&& !as.getCity().trim().equals("")) {
								String postCode = MasterDateMapping.postCodeMap
										.get(as.getCity());
								if (postCode != null) {
									veb.setPostcode(postCode);
								} else {
									veb.setPostcode(getNotFilled(as.getZip()));
								}
							} else {
								veb.setPostcode(getNotFilled(as.getZip()));
							}
						}
						String houserAndStreet = getNotFilled(businessAddress
								.getId())
								+ ""
								+ getEmpty(as.getStreet())
								+ ""
								+ getEmpty(as.getStreetNumber());
						
						veb.setPoBox(getNotFilled(as.getPostBox()));
						veb.setProvince(getNotFilled(as.getCounty()));
						veb.setHouserAndStreet(houserAndStreet);
						
						if (veb.getPostcode().equals("/")
								|| veb.getCity().equals("/")
								|| veb.getProvince().equals("/")) {
							if (houserAndStreet != null
									&& !"/".equals(houserAndStreet)) {
								int numberOfCityFound=0;
								for (String city : MasterDateMapping.cityList) {
									if (houserAndStreet.contains(city)) {
										String postCode = MasterDateMapping.postCodeMap
												.get(city);
										if (veb.getProvince() == null
												|| veb.getProvince().trim()
														.equals("")
												|| "/".equals(veb.getProvince()
														.trim())) {
											String province = MasterDateMapping.provinceMap
													.get(city);
											if (province != null) {
												veb.setProvince(province);
											}
										}
										if (postCode != null) {
											veb.setPostcode(postCode);
											veb.setCity(city);
										} else {
											System.out
													.println("\n(*) WARNING! post code of city : "
															+ as.getCity()
															+ " not found.");
											veb.setPostcode(getNotFilled(as
													.getZip()));
										}
										numberOfCityFound++;
//										break;
									}
								}
								if (numberOfCityFound > 1) {
									System.out
											.println("\n(*) WARNING! more than 1 city found! "
													+ veb.getVendorNumber()
													+ "  " + houserAndStreet);
								}
							}
						}
						
					} else {
						veb.setCountry("CN");
					}
				} else {
					veb.setCountry("CN");
				}
				Contact contact = supplier.getContactdefault();
				if(contact != null) {
					CommunicationStruct cs = contact.getCommunication();
					if(cs != null) {
						veb.setTelphone(getNotFilled(cs.getPhone()));
						veb.setFax(getNotFilled(cs.getFax()));
						veb.setEmail(getNotFilled(cs.getEmail()));
					}
				}
				veb.setName(supplier.getName());
				veb.setName2(getNotFilled(supplier.getName2()));
				veb.setSearchTerm(getNotFilled(supplier.getShortName()));
				veb.setLanguage(getLanguage(supplier.getLanguage().getDescription()));
				veb.setPurchaseOrganisation("");
				veb.setOrderCurrency(getCurrency(supplier.getDefaultCurrencyIntNumber()));
//				String paymentTerm = propertiesForMapping.getProperty("vendor.paymentTerm." + supplier.getPaymentTermdefault().getName());
				if(supplier.getPaymentTermdefault() != null) {
					veb.setPaymentTerm(supplier.getPaymentTermdefault().getName());
				} else {
					veb.setPaymentTerm("");
				}
				
				veb.setPurchasingValueKey(getPurchasingValueKey(veb.getOrderCurrency()));
				list.add(veb);
			}
			
			return list;
		} catch (PUserException e) {
			e.printStackTrace();
		} finally {
			penum.destroy();
		}
        return null;
	}
	
	private void insertExcel()  {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(target);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(dataSource));
			List<VendorExportBean> list = export();
			int rowNum = 1;
			XSSFSheet sheetVendorAccount = twb.getSheet("Vendor Account");
			XSSFSheet sheetVendorVendorBank = twb.getSheet("Vendor Bank");
			XSSFSheet sheetVendorGeneral = twb.getSheet("Vendor General");
			XSSFSheet sheetVendorPurchasing = twb.getSheet("Vendor Purchasing");
			
			for (VendorExportBean veb : list) {
				//sheet Vendor Account
				
				XSSFRow rowVendorAccount = getXSSFRow(sheetVendorAccount, rowNum);
				XSSFCell cellA_VendorAccount = getXSSFCell(rowVendorAccount, column("A"));
				cellA_VendorAccount.setCellValue(veb.getActionCode());
				XSSFCell cellB_VendorAccount = getXSSFCell(rowVendorAccount, column("B"));
				cellB_VendorAccount.setCellValue(veb.getVendorNumber());
				XSSFCell cellC_VendorAccount = getXSSFCell(rowVendorAccount, column("C"));
				cellC_VendorAccount.setCellValue("/");
				XSSFCell cellD_VendorAccount = getXSSFCell(rowVendorAccount, column("D"));
				cellD_VendorAccount.setCellValue("/");
				XSSFCell cellE_VendorAccount = getXSSFCell(rowVendorAccount, column("E"));
				cellE_VendorAccount.setCellValue("/");
				XSSFCell cellF_VendorAccount = getXSSFCell(rowVendorAccount, column("F"));
				cellF_VendorAccount.setCellValue("/");
				XSSFCell cellG_VendorAccount = getXSSFCell(rowVendorAccount, column("G"));
				cellG_VendorAccount.setCellValue("/");
				XSSFCell cellH_VendorAccount = getXSSFCell(rowVendorAccount, column("H"));
				cellH_VendorAccount.setCellValue(veb.getPaymentMethods());
				XSSFCell cellI_VendorAccount = getXSSFCell(rowVendorAccount, column("I"));
				cellI_VendorAccount.setCellValue(veb.getPaymentBlock());
				XSSFCell cellJ_VendorAccount = getXSSFCell(rowVendorAccount, column("J"));
				cellJ_VendorAccount.setCellValue(veb.getTermsOfPaymentKey());
				XSSFCell cellK_VendorAccount = getXSSFCell(rowVendorAccount, column("K"));
				cellK_VendorAccount.setCellValue("/");
				XSSFCell cellL_VendorAccount = getXSSFCell(rowVendorAccount, column("L"));
				cellL_VendorAccount.setCellValue(veb.getCheckFlagForDoubleInvoicesOrCreditMemos());
				XSSFCell cellM_VendorAccount = getXSSFCell(rowVendorAccount, column("M"));
				cellM_VendorAccount.setCellValue("/");
				XSSFCell cellN_VendorAccount = getXSSFCell(rowVendorAccount, column("N"));
				cellN_VendorAccount.setCellValue("/");
				XSSFCell cellO_VendorAccount = getXSSFCell(rowVendorAccount, column("O"));
				cellO_VendorAccount.setCellValue(veb.getToleranceGroup());
				XSSFCell cellP_VendorAccount = getXSSFCell(rowVendorAccount, column("P"));
				cellP_VendorAccount.setCellValue(veb.getPrepayment());
				
				//sheet Vendor Bank
				XSSFRow rowVendorBank = getXSSFRow(sheetVendorVendorBank, rowNum);
				XSSFCell cellA_VendorBank = getXSSFCell(rowVendorBank, column("A"));
				cellA_VendorBank.setCellValue(veb.getActionCode());
				XSSFCell cellB_VendorBank = getXSSFCell(rowVendorBank, column("B"));
				cellB_VendorBank.setCellValue(veb.getVendorNumber());
				XSSFCell cellC_VendorBank = getXSSFCell(rowVendorBank, column("C"));
				cellC_VendorBank.setCellValue("/");
				XSSFCell cellD_VendorBank = getXSSFCell(rowVendorBank, column("D"));
				cellD_VendorBank.setCellValue(veb.getBankName());
				XSSFCell cellE_VendorBank = getXSSFCell(rowVendorBank, column("E"));
				cellE_VendorBank.setCellValue(veb.getBankAccount());
				XSSFCell cellF_VendorBank = getXSSFCell(rowVendorBank, column("F"));
				cellF_VendorBank.setCellValue("/");
				
				//sheet Vendor General
				XSSFRow rowVendorGeneral = getXSSFRow(sheetVendorGeneral, rowNum);
				XSSFCell cellA_VendorGeneral = getXSSFCell(rowVendorGeneral, column("A"));
				cellA_VendorGeneral.setCellValue(veb.getActionCode());
				XSSFCell cellB_VendorGeneral = getXSSFCell(rowVendorGeneral, column("B"));
				cellB_VendorGeneral.setCellValue(veb.getVendorNumber());
				XSSFCell cellC_VendorGeneral = getXSSFCell(rowVendorGeneral, column("C"));
				cellC_VendorGeneral.setCellValue(veb.getTitle());
				XSSFCell cellD_VendorGeneral = getXSSFCell(rowVendorGeneral, column("D"));
				cellD_VendorGeneral.setCellValue(veb.getIndustry());
				XSSFCell cellE_VendorGeneral = getXSSFCell(rowVendorGeneral, column("E"));
				cellE_VendorGeneral.setCellValue(veb.getCreationDate());
				XSSFCell cellF_VendorGeneral = getXSSFCell(rowVendorGeneral, column("F"));
				cellF_VendorGeneral.setCellValue("/");
				XSSFCell cellG_VendorGeneral = getXSSFCell(rowVendorGeneral, column("G"));
				cellG_VendorGeneral.setCellValue(veb.getAccountGroup());
				XSSFCell cellH_VendorGeneral = getXSSFCell(rowVendorGeneral, column("H"));	
				cellH_VendorGeneral.setCellValue(veb.getCountry());
				XSSFCell cellI_VendorGeneral = getXSSFCell(rowVendorGeneral, column("I"));
				cellI_VendorGeneral.setCellValue(veb.getName());
				XSSFCell cellJ_VendorGeneral = getXSSFCell(rowVendorGeneral, column("J"));
				cellJ_VendorGeneral.setCellValue(veb.getName2());
				XSSFCell cellK_VendorGeneral = getXSSFCell(rowVendorGeneral, column("K"));
				cellK_VendorGeneral.setCellValue("/");
				XSSFCell cellL_VendorGeneral = getXSSFCell(rowVendorGeneral, column("L"));
				cellL_VendorGeneral.setCellValue("/");
				XSSFCell cellM_VendorGeneral = getXSSFCell(rowVendorGeneral, column("M"));
				cellM_VendorGeneral.setCellValue(veb.getCity());
				XSSFCell cellN_VendorGeneral = getXSSFCell(rowVendorGeneral, column("N"));
				cellN_VendorGeneral.setCellValue(veb.getPoBox());
				XSSFCell cellO_VendorGeneral = getXSSFCell(rowVendorGeneral, column("O"));
				cellO_VendorGeneral.setCellValue(veb.getPostcode());
				XSSFCell cellP_VendorGeneral = getXSSFCell(rowVendorGeneral, column("P"));
				cellP_VendorGeneral.setCellValue(veb.getPostcode());
				XSSFCell cellQ_VendorGeneral = getXSSFCell(rowVendorGeneral, column("Q"));
				cellQ_VendorGeneral.setCellValue(veb.getProvince());
				XSSFCell cellR_VendorGeneral = getXSSFCell(rowVendorGeneral, column("R"));
				cellR_VendorGeneral.setCellValue(veb.getSearchTerm());
				XSSFCell cellS_VendorGeneral = getXSSFCell(rowVendorGeneral, column("S"));
				cellS_VendorGeneral.setCellValue(veb.getLanguage());
				XSSFCell cellT_VendorGeneral = getXSSFCell(rowVendorGeneral, column("T"));
				cellT_VendorGeneral.setCellValue("/");
				XSSFCell cellU_VendorGeneral = getXSSFCell(rowVendorGeneral, column("U"));
				cellU_VendorGeneral.setCellValue("/");
				XSSFCell cellV_VendorGeneral = getXSSFCell(rowVendorGeneral, column("V"));
				cellV_VendorGeneral.setCellValue(veb.getHouserAndStreet());
				XSSFCell cellW_VendorGeneral = getXSSFCell(rowVendorGeneral, column("W"));
				cellW_VendorGeneral.setCellValue(veb.getTelphone());
				XSSFCell cellX_VendorGeneral = getXSSFCell(rowVendorGeneral, column("X"));
				cellX_VendorGeneral.setCellValue(veb.getFax());
				XSSFCell cellY_VendorGeneral = getXSSFCell(rowVendorGeneral, column("Y"));
				cellY_VendorGeneral.setCellValue("/");
				XSSFCell cellZ_VendorGeneral = getXSSFCell(rowVendorGeneral, column("Z"));
				cellZ_VendorGeneral.setCellValue(veb.getEmail());
				
				//sheet Vendor Purchasing
				XSSFRow rowVendorPurchasing = getXSSFRow(sheetVendorPurchasing, rowNum);
				XSSFCell cellA_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("A"));
				cellA_VendorPurchasing.setCellValue(veb.getActionCode());
				XSSFCell cellB_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("B"));
				cellB_VendorPurchasing.setCellValue(veb.getVendorNumber());
				XSSFCell cellC_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("C"));
				cellC_VendorPurchasing.setCellValue(veb.getPurchaseOrganisation());
				XSSFCell cellD_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("D"));
				cellD_VendorPurchasing.setCellValue("/");
				XSSFCell cellE_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("E"));
				cellE_VendorPurchasing.setCellValue("/");
				XSSFCell cellF_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("F"));
				cellF_VendorPurchasing.setCellValue(veb.getOrderCurrency());
				XSSFCell cellG_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("G"));
				cellG_VendorPurchasing.setCellValue(veb.getPaymentTerm());
				XSSFCell cellH_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("H"));
				cellH_VendorPurchasing.setCellValue("/");
				XSSFCell cellI_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("I"));
				cellI_VendorPurchasing.setCellValue("/");
				XSSFCell cellJ_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("J"));
				cellJ_VendorPurchasing.setCellValue("/");
				XSSFCell cellK_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("K"));
				cellK_VendorPurchasing.setCellValue("/");
				XSSFCell cellL_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("L"));
				cellL_VendorPurchasing.setCellValue("/");
				XSSFCell cellM_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("M"));
				cellM_VendorPurchasing.setCellValue("/");
				XSSFCell cellN_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("N"));
				cellN_VendorPurchasing.setCellValue("/");
				XSSFCell cellO_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("O"));
				cellO_VendorPurchasing.setCellValue("/");
				XSSFCell cellP_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("P"));
				cellP_VendorPurchasing.setCellValue("/");
				XSSFCell cellQ_VendorPurchasing = getXSSFCell(rowVendorPurchasing, column("Q"));
				cellQ_VendorPurchasing.setCellValue(veb.getPurchasingValueKey());
				
				rowNum++;
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
	
	private String getNotFilled(String value){
		if(value == null) return "/";
		else if(value.equals("")) return "/";
		else return value;
	}
	
	public static void main(String args[]) {
		VendorExportBatch batch = new VendorExportBatch();
		batch.init();
		batch.insertExcel();
	}
}
