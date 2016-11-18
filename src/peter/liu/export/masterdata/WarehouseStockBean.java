package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import com.wuerth.phoenix.util.PDate;

/**
 * WarehouseStockBean
 * 
 * @author pcnsh197
 * 
 */
public class WarehouseStockBean {

	private String	productNumber;		// MATNR

	private String	valuationType;	    // CHARG
	
	private String	storageType;		// LETYP

	private double	stock;				// ANFME

	private String	plant;				// WERKS

	private String	storageLocation;	// LGORT

	private String	warehouseNumber;	// LGNUM
	
	private String  sapWarehouseNumber;
	
	private String     salesmanNumber;

	private String	binType;			// LGTYP

	private String	storageSection;	// LGBER

	private String	warehouseLocation;	//sylvestrix location
	
	private String  ws1WarehouseLocation;// NLPLA

	private PDate	dateofMigration;	// WDATU
	
	private boolean	discarded;			// If the data is merged to twin product

	private boolean	carboot;			// If the location is carboot location
	
	private boolean	consignment;			// If the location is consignment location
	
	private boolean hasQualityProblem;
	
	private int customerNumber;
	
	private String storageUnitType;
	
	private String sapProductNumber;

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	
//	public String getBatch() {
//		return batch;
//	}
//
//	public void setBatch(String batch) {
//		this.batch = batch;
//	}

	public String getValuationType() {
		return valuationType;
	}

	public void setValuationType(String valuationType) {
		this.valuationType = valuationType;
	}

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getStorageSection() {
		return storageSection;
	}

	public void setStorageSection(String storageSection) {
		this.storageSection = storageSection;
	}

	public PDate getDateofMigration() {
		return dateofMigration;
	}

	public void setDateofMigration(PDate dateofMigration) {
		this.dateofMigration = dateofMigration;
	}

	public String getWarehouseNumber() {
		return warehouseNumber;
	}

	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}

	public String getWarehouseLocation() {
		return warehouseLocation;
	}

	public void setWarehouseLocation(String warehouseLocation) {
		this.warehouseLocation = warehouseLocation;
	}

	public String getBinType() {
		return binType;
	}

	public void setBinType(String binType) {
		this.binType = binType;
	}

	public boolean isDiscarded() {
		return discarded;
	}

	public void setDiscarded(boolean discarded) {
		this.discarded = discarded;
	}

	public boolean isCarboot() {
		return carboot;
	}

	public void setCarboot(boolean carboot) {
		this.carboot = carboot;
	}

	public String getWs1WarehouseLocation() {
		return ws1WarehouseLocation;
	}

	public void setWs1WarehouseLocation(String ws1WarehouseLocation) {
		this.ws1WarehouseLocation = ws1WarehouseLocation;
	}

	public boolean isConsignment() {
		return consignment;
	}

	public void setConsignment(boolean consignment) {
		this.consignment = consignment;
	}

	public String getSapWarehouseNumber() {
		return sapWarehouseNumber;
	}

	public void setSapWarehouseNumber(String sapWarehouseNumber) {
		this.sapWarehouseNumber = sapWarehouseNumber;
	}

	public String getSalesmanNumber() {
		return salesmanNumber;
	}

	public void setSalesmanNumber(String salesmanNumber) {
		this.salesmanNumber = salesmanNumber;
	}

	public boolean isHasQualityProblem() {
		return hasQualityProblem;
	}

	public void setHasQualityProblem(boolean hasQualityProblem) {
		this.hasQualityProblem = hasQualityProblem;
	}

	public int getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getStorageUnitType() {
		return storageUnitType;
	}

	public void setStorageUnitType(String storageUnitType) {
		this.storageUnitType = storageUnitType;
	}

	public String getSapProductNumber() {
		return sapProductNumber;
	}

	public void setSapProductNumber(String sapProductNumber) {
		if(sapProductNumber!=null){
			this.sapProductNumber = sapProductNumber;
		}else{
			this.sapProductNumber="";
		}
		
	}
	
	
}
