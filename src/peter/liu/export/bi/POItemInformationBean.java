package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import com.wuerth.phoenix.basic.etnax.common.utilsdir.DoubleUtils;
import com.wuerth.phoenix.util.PDate;

/**
 * POItemInformationBean
 * 
 * @author pcnsh197
 * 
 */
public class POItemInformationBean {
	private long		orderNumber;

	private int		lineNumber;

	private String	productNumber;

	private String	ws1ProductNumber;

	private String	plant;

	private PDate	deliveryDate;

	private String	textOnPO;

	private double	quantity;

	private double	price;

	private double		priceUnit;
	
	private double	map;

	private double	salesUnit;
	
	private String  supplierProductNumber;

	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public String getWs1ProductNumber() {
		return ws1ProductNumber;
	}

	public void setWs1ProductNumber(String ws1ProductNumber) {
		this.ws1ProductNumber = ws1ProductNumber;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public PDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(PDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getTextOnPO() {
		return textOnPO;
	}

	public void setTextOnPO(String textOnPO) {
		this.textOnPO = textOnPO;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(double priceUnit) {
		this.priceUnit = priceUnit;
	}

	public double getMap() {
		return map;
	}

	public void setMap(double map) {
		this.map = map;
	}

	public double getSalesUnit() {
		return salesUnit;
	}

	public void setSalesUnit(double salesUnit) {
		this.salesUnit = salesUnit;
	}
	
	
	
	public String getSupplierProductNumber() {
		return supplierProductNumber;
	}

	public void setSupplierProductNumber(String supplierProductNumber) {
		this.supplierProductNumber = supplierProductNumber;
	}

	public void roundMAP() {
		if (map < 0) {
			System.out.println("\n(!) MAP is not valid: " + " " + productNumber
					+ " " + map);
		} else if (map == 0.0D) {
			map = 0.01D;
			salesUnit = 1000;
		} else {
			String mapString = String.valueOf(map);
			if (mapString.indexOf(".") >= 0) {
				String decimal = mapString
						.substring(mapString.indexOf(".") + 1);
				if (decimal.length() > 4) {
					System.out.println("\n(!) original map: " + " "
							+ productNumber + " " + map);
					salesUnit = salesUnit * 1000;
					map = DoubleUtils.getRoundedAmount(map * 1000, 2);

				} else if (decimal.length() > 2) {
					System.out.println("\n(!) original map: " + " "
							+ productNumber + " " + map);
					salesUnit = salesUnit * 100;
					map = DoubleUtils.getRoundedAmount(map * 100, 2);
				}
			}
		}

		if (map == 0.0D) {
			map = 0.01D;
			System.out.println("\n(!) MAP zero after multiplation: " + " "
					+ productNumber + " " + map + " " + salesUnit);
		}
	}
	

}
