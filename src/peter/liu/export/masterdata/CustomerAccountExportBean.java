	package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

public class CustomerAccountExportBean implements Cloneable{
	private String prefixCustomerNumber = "";
	private String customerNumber;
	private String name1;
	private String name2;
	private String countryKey;
	private String city;
	private String poBox;
	private String postalCode;
	private String sortField;
	private String languageKey;
	private String houseNumberAndStreet;
	private String firstTelephoneNumber;
	private String faxNumber;
	private String companyCode;
	private String salesRepNo;
	private String province;
	private String customerStatus;
	private String creationDate;
	private String creditControlArea;
	private String bankCountryKey;
	private String bankKeysBankIdentificationCode;
	private String referenceSpecificationsForBankDetails;
	private String bankAccountNumber;
	private String termsOfPaymentKey;
	private String customerAccountGroup;
	private String controllingFlagforAccountGroup;
	private String VATRegistrationNumber;
	private double creditlimit;
	private String dunningProcedure;
	private String salesOrganisation;
	private String distributionChannel;
	private String division;
	private String deliveryPlant;
	private String priceList;
	private String industrykey;
	private int frozenYearlyNumberofEmployeesForTargetFigure;
	private String contactPersonName;
	private String contackPersonTel;
	private String partnerFunctionSKNVK;
	private String partnerFunctionSKNVP;
	private String mainPartner;
	private String telphone;
	private String mobilephone;
	private String email;
	private String accountingClerk;
	private String reconciliationAccountInGeneralLedger;
	private String partialDeliveryAtItemLevel;
	private String addInvoiceToParcel;
	private int parnterCounter;
	private String customerNumberOfPartner;
	
	
	
	
	public String getCustomerNumberOfPartner() {
		return customerNumberOfPartner;
	}
	public void setCustomerNumberOfPartner(String customerNumberOfPartner) {
		this.customerNumberOfPartner = customerNumberOfPartner;
	}
	public int getParnterCounter() {
		return parnterCounter;
	}
	public void setParnterCounter(int parnterCounter) {
		this.parnterCounter = parnterCounter;
	}
	public String getAddInvoiceToParcel() {
		return addInvoiceToParcel;
	}
	public void setAddInvoiceToParcel(String addInvoiceToParcel) {
		this.addInvoiceToParcel = addInvoiceToParcel;
	}
	public String getPartialDeliveryAtItemLevel() {
		return partialDeliveryAtItemLevel;
	}
	public void setPartialDeliveryAtItemLevel(String partialDeliveryAtItemLevel) {
		this.partialDeliveryAtItemLevel = partialDeliveryAtItemLevel;
	}
	public String getAccountingClerk() {
		return accountingClerk;
	}
	public void setAccountingClerk(String accountingClerk) {
		this.accountingClerk = accountingClerk;
	}
	public String getReconciliationAccountInGeneralLedger() {
		return reconciliationAccountInGeneralLedger;
	}
	public void setReconciliationAccountInGeneralLedger(String reconciliationAccountInGeneralLedger) {
		this.reconciliationAccountInGeneralLedger = reconciliationAccountInGeneralLedger;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMainPartner() {
		return mainPartner;
	}
	public void setMainPartner(String mainPartner) {
		this.mainPartner = mainPartner;
	}

	public String getPartnerFunctionSKNVK() {
		return partnerFunctionSKNVK;
	}
	public void setPartnerFunctionSKNVK(String partnerFunctionSKNVK) {
		this.partnerFunctionSKNVK = partnerFunctionSKNVK;
	}
	public String getPartnerFunctionSKNVP() {
		return partnerFunctionSKNVP;
	}
	public void setPartnerFunctionSKNVP(String partnerFunctionSKNVP) {
		this.partnerFunctionSKNVP = partnerFunctionSKNVP;
	}
	public String getContactPersonName() {
		return contactPersonName;
	}
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	public String getContackPersonTel() {
		return contackPersonTel;
	}
	public void setContackPersonTel(String contackPersonTel) {
		this.contackPersonTel = contackPersonTel;
	}
	public String getIndustrykey() {
		return industrykey;
	}
	public void setIndustrykey(String industrykey) {
		this.industrykey = industrykey;
	}
	public String getPriceList() {
		return priceList;
	}
	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}
	public String getDeliveryPlant() {
		return deliveryPlant;
	}
	public void setDeliveryPlant(String deliveryPlant) {
		this.deliveryPlant = deliveryPlant;
	}
	public String getDistributionChannel() {
		return distributionChannel;
	}
	public void setDistributionChannel(String distributionChannel) {
		this.distributionChannel = distributionChannel;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getSalesOrganisation() {
		return salesOrganisation;
	}
	public void setSalesOrganisation(String salesOrganisation) {
		this.salesOrganisation = salesOrganisation;
	}
	public String getDunningProcedure() {
		return dunningProcedure;
	}
	public void setDunningProcedure(String dunningProcedure) {
		this.dunningProcedure = dunningProcedure;
	}
	public String getReferenceSpecificationsForBankDetails() {
		return referenceSpecificationsForBankDetails;
	}
	public void setReferenceSpecificationsForBankDetails(String referenceSpecificationsForBankDetails) {
		this.referenceSpecificationsForBankDetails = referenceSpecificationsForBankDetails;
	}
	public double getCreditlimit() {
		return creditlimit;
	}
	public void setCreditlimit(double creditlimit) {
		this.creditlimit = creditlimit;
	}
	public String getVATRegistrationNumber() {
		return VATRegistrationNumber;
	}
	public void setVATRegistrationNumber(String vATRegistrationNumber) {
		VATRegistrationNumber = vATRegistrationNumber;
	}
	public String getControllingFlagforAccountGroup() {
		return controllingFlagforAccountGroup;
	}
	public void setControllingFlagforAccountGroup(String controllingFlagforAccountGroup) {
		this.controllingFlagforAccountGroup = controllingFlagforAccountGroup;
	}
	public String getCustomerAccountGroup() {
		return customerAccountGroup;
	}
	public void setCustomerAccountGroup(String customerAccountGroup) {
		this.customerAccountGroup = customerAccountGroup;
	}
	public String getBankCountryKey() {
		return bankCountryKey;
	}
	public void setBankCountryKey(String bankCountryKey) {
		this.bankCountryKey = bankCountryKey;
	}
	public String getBankKeysBankIdentificationCode() {
		return bankKeysBankIdentificationCode;
	}
	public void setBankKeysBankIdentificationCode(String bankKeysBankIdentificationCode) {
		this.bankKeysBankIdentificationCode = bankKeysBankIdentificationCode;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getTermsOfPaymentKey() {
		return termsOfPaymentKey;
	}
	public void setTermsOfPaymentKey(String termsOfPaymentKey) {
		this.termsOfPaymentKey = termsOfPaymentKey;
	}
	public String getCreditControlArea() {
		return creditControlArea;
	}
	public void setCreditControlArea(String creditControlArea) {
		this.creditControlArea = creditControlArea;
	}

	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getSalesRepNo() {
		return salesRepNo;
	}
	public void setSalesRepNo(String salesRepNo) {
		this.salesRepNo = salesRepNo;
	}
	public String getFirstTelephoneNumber() {
		return firstTelephoneNumber;
	}
	public void setFirstTelephoneNumber(String firstTelephoneNumber) {
		this.firstTelephoneNumber = firstTelephoneNumber;
	}
	public String getHouseNumberAndStreet() {
		return houseNumberAndStreet;
	}
	public void setHouseNumberAndStreet(String houseNumberAndStreet) {
		this.houseNumberAndStreet = houseNumberAndStreet;
	}
	public String getPrefixCustomerNumber() {
		return prefixCustomerNumber;
	}
	public void setPrefixCustomerNumber(String prefixCustomerNumber) {
		this.prefixCustomerNumber = prefixCustomerNumber;
	}
	public String getPoBox() {
		return poBox;
	}
	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getLanguageKey() {
		return languageKey;
	}
	public void setLanguageKey(String languageKey) {
		this.languageKey = languageKey;
	}
	public String getCustomerNumber() {
		return prefixCustomerNumber +customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getCountryKey() {
		return countryKey;
	}
	public void setCountryKey(String countryKey) {
		this.countryKey = countryKey;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getFrozenYearlyNumberofEmployeesForTargetFigure() {
		return frozenYearlyNumberofEmployeesForTargetFigure;
	}
	public void setFrozenYearlyNumberofEmployeesForTargetFigure(int frozenYearlyNumberofEmployeesForTargetFigure) {
		this.frozenYearlyNumberofEmployeesForTargetFigure = frozenYearlyNumberofEmployeesForTargetFigure;
	}
	
	@Override
	protected CustomerAccountExportBean clone() throws CloneNotSupportedException {
		try {
			return (CustomerAccountExportBean) super.clone();
		}
		catch(CloneNotSupportedException e) {
	           throw new AssertionError();  // Can't happen
	      }
	}
	
	
	
}
