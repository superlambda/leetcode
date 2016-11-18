package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

public class InfoRecordsExportBean {
	private int vendorNumber;
	private String sylProductNumber;
	private String sapProductNumber;
	private String vendorMaterialNumber;
	private int plannedDeliveryTimeinDays;
	private int reminder1;
	private int reminder2;
	private int reminder3;
	private int underdeliveryToleranceLimit;
	private int overdeliberyToleranceLimit;
	private String validFrom;
	private String validTo;
	private double netPrice;
	private String currencyKey;
	private double minimumOrderQuantity;
	private double priceUnit;
	private double orderPriceUnit;
	private String confirmationControlKey;
	private String purchasingGroup;
	private String GR_based_Invoice_Verification;
	private double numeratorConversion;
	private double denominatorConversion;
	
	
	
	

	public double getNumeratorConversion() {
		return numeratorConversion;
	}
	public void setNumeratorConversion(double numeratorConversion) {
		this.numeratorConversion = numeratorConversion;
	}
	public double getDenominatorConversion() {
		return denominatorConversion;
	}
	public void setDenominatorConversion(double denominatorConversion) {
		this.denominatorConversion = denominatorConversion;
	}
	public String getGR_based_Invoice_Verification() {
		return GR_based_Invoice_Verification;
	}
	public void setGR_based_Invoice_Verification(String gR_based_Invoice_Verification) {
		GR_based_Invoice_Verification = gR_based_Invoice_Verification;
	}
	public String getPurchasingGroup() {
		return purchasingGroup;
	}
	public void setPurchasingGroup(String purchasingGroup) {
		this.purchasingGroup = purchasingGroup;
	}
	public int getOverdeliberyToleranceLimit() {
		return overdeliberyToleranceLimit;
	}
	public void setOverdeliberyToleranceLimit(int overdeliberyToleranceLimit) {
		this.overdeliberyToleranceLimit = overdeliberyToleranceLimit;
	}
	public int getUnderdeliveryToleranceLimit() {
		return underdeliveryToleranceLimit;
	}
	public void setUnderdeliveryToleranceLimit(int underdeliveryToleranceLimit) {
		this.underdeliveryToleranceLimit = underdeliveryToleranceLimit;
	}
	public String getVendorMaterialNumber() {
		return vendorMaterialNumber;
	}
	public void setVendorMaterialNumber(String vendorMaterialNumber) {
		this.vendorMaterialNumber = vendorMaterialNumber;
	}
	public int getVendorNumber() {
		return vendorNumber;
	}
	public void setVendorNumber(int vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	public int getReminder1() {
		return reminder1;
	}
	public void setReminder1(int reminder1) {
		this.reminder1 = reminder1;
	}
	public int getReminder2() {
		return reminder2;
	}
	public void setReminder2(int reminder2) {
		this.reminder2 = reminder2;
	}
	public int getReminder3() {
		return reminder3;
	}
	public void setReminder3(int reminder3) {
		this.reminder3 = reminder3;
	}
	public int getPlannedDeliveryTimeinDays() {
		return plannedDeliveryTimeinDays;
	}
	public void setPlannedDeliveryTimeinDays(int plannedDeliveryTimeinDays) {
		this.plannedDeliveryTimeinDays = plannedDeliveryTimeinDays;
	}
	public String getSylProductNumber() {
		return sylProductNumber;
	}
	public void setSylProductNumber(String sylProductNumber) {
		this.sylProductNumber = sylProductNumber;
	}
	public String getSapProductNumber() {
		return sapProductNumber;
	}
	public void setSapProductNumber(String sapProductNumber) {
		this.sapProductNumber = sapProductNumber;
	}

	public String getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}
	public String getValidTo() {
		return validTo;
	}
	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}
	public double getNetPrice() {
		return netPrice;
	}
	public void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
	}
	public String getCurrencyKey() {
		return currencyKey;
	}
	public void setCurrencyKey(String currencyKey) {
		this.currencyKey = currencyKey;
	}
	
	public double getMinimumOrderQuantity() {
		return minimumOrderQuantity;
	}
	public void setMinimumOrderQuantity(double minimumOrderQuantity) {
		this.minimumOrderQuantity = minimumOrderQuantity;
	}

	public String getConfirmationControlKey() {
		return confirmationControlKey;
	}
	public void setConfirmationControlKey(String confirmationControlKey) {
		this.confirmationControlKey = confirmationControlKey;
	}
	public double getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(double priceUnit) {
		this.priceUnit = priceUnit;
	}
	public double getOrderPriceUnit() {
		return orderPriceUnit;
	}
	public void setOrderPriceUnit(double orderPriceUnit) {
		this.orderPriceUnit = orderPriceUnit;
	}
	
	
}
