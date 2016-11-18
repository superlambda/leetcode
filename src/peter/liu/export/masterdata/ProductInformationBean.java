package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import com.wuerth.phoenix.util.PDate;

/**
 * ProductInformationBean
 * 
 * @author pcnsh197
 * 
 */
public class ProductInformationBean {
	private String productNumber;

	private String presentation;

	private double packingSize;

	private String productDescriptionCN;

	private String productDescriptionENOrDE;

	private double unitCost;

	private String unit;

	private double currentStockQuantity;

	private double stockValue;

	private PDate creationDate;

	private String productStatus;

	private double grossWeight;

	private String weightUnit;

	private String mainSupplierName;

	private String mainSupplierProductNumber;

	private PDate mainSupplierLastPurchaseDate;

	private String secondSupplierName;

	private String secondnSupplierProductNumber;

	private PDate secondSupplierLastPurchaseDate;

	private double currentOpenPOQuantity;

	private double currentOpenSOQuantity;

	private double salesQuantity2YearsAndCurrentYear=0.0d;

	private double salesTurnover2YearsAndCurrentYear=0.0d;

	private long picks2YearsAndCurrentYear;

	private double salesQuantityLatestYear=0.0d;

	private double salesTurnoverLatestYear=0.0d;

	private double picksLatestYear=0.0d;
	
	private String	lastOneSupplierOfPO;

	private PDate	lastPurchaseDateOfLastOneSupplier;
	
	private String  lastOneSupplierProductNumber;

	private String	lastTwoSupplierOfPO;

	private PDate	lastPurchaseDateOfLastTwoSupplier;
	
	private String  lastTwoSupplierProductNumber;
	
	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public String getPresentation() {
		return presentation;
	}

	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}

	public double getPackingSize() {
		return packingSize;
	}

	public void setPackingSize(double packingSize) {
		this.packingSize = packingSize;
	}

	public String getProductDescriptionCN() {
		return productDescriptionCN;
	}

	public void setProductDescriptionCN(String productDescriptionCN) {
		this.productDescriptionCN = productDescriptionCN;
	}

	public String getProductDescriptionENOrDE() {
		return productDescriptionENOrDE;
	}

	public void setProductDescriptionENOrDE(String productDescriptionENOrDE) {
		this.productDescriptionENOrDE = productDescriptionENOrDE;
	}

	public double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getCurrentStockQuantity() {
		return currentStockQuantity;
	}

	public void setCurrentStockQuantity(double currentStockQuantity) {
		this.currentStockQuantity = currentStockQuantity;
	}

	public double getStockValue() {
		return stockValue;
	}

	public void setStockValue(double stockValue) {
		this.stockValue = stockValue;
	}

	public PDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(PDate creationDate) {
		this.creationDate = creationDate;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public String getMainSupplierName() {
		return mainSupplierName;
	}

	public void setMainSupplierName(String mainSupplierName) {
		this.mainSupplierName = mainSupplierName;
	}

	public String getMainSupplierProductNumber() {
		return mainSupplierProductNumber;
	}

	public void setMainSupplierProductNumber(String mainSupplierProductNumber) {
		this.mainSupplierProductNumber = mainSupplierProductNumber;
	}

	public PDate getMainSupplierLastPurchaseDate() {
		return mainSupplierLastPurchaseDate;
	}

	public void setMainSupplierLastPurchaseDate(PDate mainSupplierLastPurchaseDate) {
		this.mainSupplierLastPurchaseDate = mainSupplierLastPurchaseDate;
	}

	public String getSecondSupplierName() {
		return secondSupplierName;
	}

	public void setSecondSupplierName(String secondSupplierName) {
		this.secondSupplierName = secondSupplierName;
	}

	public String getSecondnSupplierProductNumber() {
		return secondnSupplierProductNumber;
	}

	public void setSecondnSupplierProductNumber(String secondnSupplierProductNumber) {
		this.secondnSupplierProductNumber = secondnSupplierProductNumber;
	}

	public PDate getSecondSupplierLastPurchaseDate() {
		return secondSupplierLastPurchaseDate;
	}

	public void setSecondSupplierLastPurchaseDate(PDate secondSupplierLastPurchaseDate) {
		this.secondSupplierLastPurchaseDate = secondSupplierLastPurchaseDate;
	}

	public double getCurrentOpenPOQuantity() {
		return currentOpenPOQuantity;
	}

	public void setCurrentOpenPOQuantity(double currentOpenPOQuantity) {
		this.currentOpenPOQuantity = currentOpenPOQuantity;
	}

	public double getCurrentOpenSOQuantity() {
		return currentOpenSOQuantity;
	}

	public void setCurrentOpenSOQuantity(double currentOpenSOQuantity) {
		this.currentOpenSOQuantity = currentOpenSOQuantity;
	}

	public double getSalesQuantity2YearsAndCurrentYear() {
		return salesQuantity2YearsAndCurrentYear;
	}

	public void setSalesQuantity2YearsAndCurrentYear(
			double salesQuantity2YearsAndCurrentYear) {
		this.salesQuantity2YearsAndCurrentYear = salesQuantity2YearsAndCurrentYear;
	}

	public double getSalesTurnover2YearsAndCurrentYear() {
		return salesTurnover2YearsAndCurrentYear;
	}

	public void setSalesTurnover2YearsAndCurrentYear(
			double salesTurnover2YearsAndCurrentYear) {
		this.salesTurnover2YearsAndCurrentYear = salesTurnover2YearsAndCurrentYear;
	}

	public long getPicks2YearsAndCurrentYear() {
		return picks2YearsAndCurrentYear;
	}

	public void setPicks2YearsAndCurrentYear(long picks2YearsAndCurrentYear) {
		this.picks2YearsAndCurrentYear = picks2YearsAndCurrentYear;
	}

	public double getSalesQuantityLatestYear() {
		return salesQuantityLatestYear;
	}

	public void setSalesQuantityLatestYear(double salesQuantityLatestYear) {
		this.salesQuantityLatestYear = salesQuantityLatestYear;
	}

	public double getSalesTurnoverLatestYear() {
		return salesTurnoverLatestYear;
	}

	public void setSalesTurnoverLatestYear(double salesTurnoverLatestYear) {
		this.salesTurnoverLatestYear = salesTurnoverLatestYear;
	}

	public double getPicksLatestYear() {
		return picksLatestYear;
	}

	public void setPicksLatestYear(double picksLatestYear) {
		this.picksLatestYear = picksLatestYear;
	}

	public String getLastOneSupplierOfPO() {
		return lastOneSupplierOfPO;
	}

	public void setLastOneSupplierOfPO(String lastOneSupplierOfPO) {
		this.lastOneSupplierOfPO = lastOneSupplierOfPO;
	}

	public String getLastTwoSupplierOfPO() {
		return lastTwoSupplierOfPO;
	}

	public void setLastTwoSupplierOfPO(String lastTwoSupplierOfPO) {
		this.lastTwoSupplierOfPO = lastTwoSupplierOfPO;
	}

	public PDate getLastPurchaseDateOfLastOneSupplier() {
		return lastPurchaseDateOfLastOneSupplier;
	}

	public void setLastPurchaseDateOfLastOneSupplier(
			PDate lastPurchaseDateOfLastOneSupplier) {
		this.lastPurchaseDateOfLastOneSupplier = lastPurchaseDateOfLastOneSupplier;
	}

	public PDate getLastPurchaseDateOfLastTwoSupplier() {
		return lastPurchaseDateOfLastTwoSupplier;
	}

	public void setLastPurchaseDateOfLastTwoSupplier(
			PDate lastPurchaseDateOfLastTwoSupplier) {
		this.lastPurchaseDateOfLastTwoSupplier = lastPurchaseDateOfLastTwoSupplier;
	}

	public String getLastOneSupplierProductNumber() {
		return lastOneSupplierProductNumber;
	}

	public void setLastOneSupplierProductNumber(String lastOneSupplierProductNumber) {
		this.lastOneSupplierProductNumber = lastOneSupplierProductNumber;
	}

	public String getLastTwoSupplierProductNumber() {
		return lastTwoSupplierProductNumber;
	}

	public void setLastTwoSupplierProductNumber(String lastTwoSupplierProductNumber) {
		this.lastTwoSupplierProductNumber = lastTwoSupplierProductNumber;
	}
}
