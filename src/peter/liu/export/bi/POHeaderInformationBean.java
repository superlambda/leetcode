package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import com.wuerth.phoenix.util.PDate;

/**
 * POHeaderInformationBean
 * 
 * @author pcnsh197
 * 
 */
public class POHeaderInformationBean {
	private long		orderNumber;

	private PDate	orderDate;

	private int		supplierNumber;

	private String	ws1VendorNumber;

	private String	currency;

	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public PDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(PDate orderDate) {
		this.orderDate = orderDate;
	}

	public int getSupplierNumber() {
		return supplierNumber;
	}

	public void setSupplierNumber(int supplierNumber) {
		this.supplierNumber = supplierNumber;
	}

	public String getWs1VendorNumber() {
		return ws1VendorNumber;
	}

	public void setWs1VendorNumber(String ws1VendorNumber) {
		this.ws1VendorNumber = ws1VendorNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
