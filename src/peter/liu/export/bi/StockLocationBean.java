package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

/**
 * StockLocationBean
 * 
 * @author pcnsh197
 * 
 */
public class StockLocationBean {
	private int		companyId;

	private String	warehouseNumber;

	private String	storageLocation;

	private String	description;

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getWarehouseNumber() {
		return warehouseNumber;
	}

	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
