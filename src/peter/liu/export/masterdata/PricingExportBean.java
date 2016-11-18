package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

public class PricingExportBean {
	private String distChl;
	private String legacyProductNumber;
	private String productNumber;
	private String validFrom;
	private String validTo;
	private double grossPrice;
	private double netPrice;
	private double vatPrice;
	private int 	priceUnit;
	
	public String getDistChl() {
		return distChl;
	}
	public void setDistChl(String distChl) {
		this.distChl = distChl;
	}
	public String getLegacyProductNumber() {
		return legacyProductNumber;
	}
	public void setLegacyProductNumber(String legacyProductNumber) {
		this.legacyProductNumber = legacyProductNumber;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
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
	public double getGrossPrice() {
		return grossPrice;
	}
	public void setGrossPrice(double grossPrice) {
		this.grossPrice = grossPrice;
	}
	public double getNetPrice() {
		return netPrice;
	}
	public void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
	}
	public int getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(int priceUnit) {
		this.priceUnit = priceUnit;
	}
	public double getVatPrice() {
		return vatPrice;
	}
	public void setVatPrice(double vatPrice) {
		this.vatPrice = vatPrice;
	}
	
}
