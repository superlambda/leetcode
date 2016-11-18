package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import com.wuerth.phoenix.basic.etnax.common.utilsdir.DoubleUtils;

/**
 * 
 * @author pcnsh197
 * 
 */
public class StockValueBean {
	private String	plant;

	private String	productNumber;

	private String	validKey;

	private double	totalStock;

	private double	map;

	private double	priceUnit;

	private double	totalStockValue;

	private String	warehouseNumber;

	private String	location;
	
	private String sapProductNumber;

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public String getValidKey() {
		return validKey;
	}

	public void setValidKey(String validKey) {
		this.validKey = validKey;
	}

	public double getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(double totalStock) {
		this.totalStock = totalStock;
	}

	public double getMap() {
		return map;
	}

	public void setMap(double map) {
		this.map = map;
	}

	public double getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(double priceUnit) {
		this.priceUnit = priceUnit;
	}

	public double getTotalStockValue() {
		return totalStockValue;
	}

	public void setTotalStockValue(double totalStockValue) {
		this.totalStockValue = totalStockValue;
	}

	public String getWarehouseNumber() {
		return warehouseNumber;
	}

	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public void roundMAP() {
		if (map < 0) {
			System.out.println("\n(!) MAP is not valid: " + warehouseNumber
					+ " " + productNumber + " " + map);
		} else if (map == 0.0D) {
			map = 0.01D;
			priceUnit = 1000;
		} else {
			String mapString = String.valueOf(map);
			if (mapString.indexOf(".") >= 0) {
				String decimal = mapString
						.substring(mapString.indexOf(".") + 1);
				if (decimal.length() > 4) {
					System.out.println("\n(!) original map: " + warehouseNumber
							+ " " + productNumber + " " + map);
					priceUnit = priceUnit * 1000;
					map = DoubleUtils.getRoundedAmount(map * 1000, 2);

				} else if (decimal.length() > 2) {
					System.out.println("\n(!) original map: " + warehouseNumber
							+ " " + productNumber + " " + map);
					priceUnit = priceUnit * 100;
					map = DoubleUtils.getRoundedAmount(map * 100, 2);
				}
			}
		}

		if (map == 0.0D) {
			map = 0.01D;
			System.out.println("\n(!) MAP zero after multiplation: "
					+ warehouseNumber + " " + productNumber + " " + map + " "
					+ priceUnit);
		}
	}
}
