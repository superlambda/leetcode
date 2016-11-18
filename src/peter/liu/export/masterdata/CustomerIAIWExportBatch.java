/**
 * 
 */
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
import com.wuerth.phoenix.bcutil.HookException;
import com.wuerth.phoenix.bcutil.IteratorFactory;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;
import com.wuerth.phoenix.util.PDate;


/**
 * @author pcnsh222
 *
 */
public class CustomerIAIWExportBatch {
	private PhxbasicController _controller;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private Properties propertiesForMapping;
	private String dataSource = "../../etc/exportSAP/Customer_master_IAIW_Template.xlsx";
	private String target = "../../var/exportSAP/Customer_master_IAIW_out.xlsx";


	
	private String rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private List<CustomerAccountExportBean> goodsReceiptList = new ArrayList<CustomerAccountExportBean>();

	
	private void init() {
		_controller = SylvestrixHelper.get().getController();
		propertiesForMapping = MappingParam.getInstance().getMappingProperties();
	}
	
	public int column(String s) {

		if (s.length() == 1) {
			return rows.indexOf(s);
		}

		return 26 * (rows.indexOf(s.substring(0, 1)) + 1)
				+ column(s.substring(1, s.length()));
	}
	
	
	private CustomerAccountExportBean setCustomerAccountExportBean(CustomerAccount ca) throws HookException, PUserException {
		CustomerAccountExportBean caeb = new CustomerAccountExportBean();
		caeb.setCustomerNumber(ca.getAccountNumber() + "");
		caeb.setCustomerAccountGroup("IAIW");
    	caeb.setControllingFlagforAccountGroup("/");
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
				caeb.setCity(getEmpty(as.getCity()));
				caeb.setProvince(getEmpty(as.getCounty()));
				caeb.setPoBox(getEmpty(as.getPostBox()));
				caeb.setPostalCode(getEmpty(as.getZip()));
				caeb.setHouseNumberAndStreet(getEmpty(ba.getId()) + " " + getEmpty(as.getStreet()) + " " + getEmpty(as.getStreetNumber()));
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
		caeb.setIndustrykey("");
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
		return caeb;
	}
	
	private List<CustomerAccountExportBean> export() {
		List<CustomerAccountExportBean> list = new ArrayList<CustomerAccountExportBean>();
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
				if(ca.getTextOnOffer().equals("BJ") || ca.getTextOnOffer().equals("QD")) {
					continue;
				}
				Company company = ca.getParentCompany();
				List<BusinessRole> businessRoles = company.getAllChildBusinessRole();
				int countGoodsRecipient = 0;
				for (BusinessRole businessRole : businessRoles) {
					if(businessRole instanceof GoodsRecipient) {
						countGoodsRecipient +=1;
					}
				}
				
				for (BusinessRole businessRole : businessRoles) {
					if(countGoodsRecipient == 1) {
						continue;
					}
					if(businessRole instanceof GoodsRecipient) {
						GoodsRecipient sameCPGoodsRecipient = (GoodsRecipient) businessRole;
						if(sameCPGoodsRecipient.getStatus().equals(GoodsRecipientStatus.DELETED)) {
							continue;
						}
						if(ca.getGoodsRecipientdefault() != null) {
							//System.out.println("GoodsRecipient :" + ca.getGoodsRecipientdefault().getId() + " Customer :" + ca.getAccountNumber() + " sameCPGoodsRecipient:" + sameCPGoodsRecipient.getId());
							if(ca.getGoodsRecipientdefault().getId() != sameCPGoodsRecipient.getId()) {
								CustomerAccountExportBean caeb = setCustomerAccountExportBean(ca);
								list.add(caeb);
							}
						} else {
							if(ca.getAccountNumber() != sameCPGoodsRecipient.getId()) {
//								System.out.println("GoodsRecipient :" + sameCPGoodsRecipient.getId() + " Customer :" + ca.getAccountNumber());
								CustomerAccountExportBean caeb = setCustomerAccountExportBean(ca);
								list.add(caeb);
							}
						}

//						System.out.println("GoodsRecipient :" + sameCPGoodsRecipient.getId() + " Customer :" + ca.getAccountNumber());
					} 
				}
				
			}
			return list;
		} catch (PUserException e) {
			e.printStackTrace();
		} finally {
			penum.destroy();
		}
        return null;
	}
	
	private String getCountryKey(String language) {
		if(language.equals("Chinese")) {
			return "CN";
		}
		return "";
	}
	
	private String getEmpty(String value) {
		if(value == null) return "";
		else return value;
	}
	
	private String getNotFilled(String value){
		if(value == null) return "/";
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
	
	private void insertExcel()  {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(target);
			XSSFWorkbook twb = new XSSFWorkbook(new FileInputStream(dataSource));
			List<CustomerAccountExportBean> list = export();
			int rowNum = 5;
			XSSFSheet sheetSKNA1 = twb.getSheet("SKNA1");
			for (CustomerAccountExportBean caeb : list) {
				
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
				cellK_SKNA1.setCellValue(caeb.getName2());
				XSSFCell cellL_SKNA1 = getXSSFCell(rowSKNA1, column("L"));
				cellL_SKNA1.setCellValue(caeb.getName2());
				XSSFCell cellM_SKNA1 = getXSSFCell(rowSKNA1, column("M"));
				cellM_SKNA1.setCellValue(caeb.getName2());
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
				
				

				
				//sheet Additional fields
				
//				XSSFRow rowAdditional = getXSSFRow(sheetAdditional, rowNum);
//				XSSFCell cellA_Additional = getXSSFCell(rowAdditional, column("A"));
//				cellA_Additional.setCellValue(caeb.getCustomerNumber());
//				XSSFCell cellB_Additional = getXSSFCell(rowAdditional, column("B"));
//				cellB_Additional.setCellValue(caeb.getDeliveryPlant());
				
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
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CustomerIAIWExportBatch batch = new CustomerIAIWExportBatch();
		batch.init();
//		batch.export();
		batch.insertExcel();
//		batch.udpateSKNVP();

	}

}
