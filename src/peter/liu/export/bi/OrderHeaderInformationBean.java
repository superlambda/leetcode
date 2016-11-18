package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import com.wuerth.phoenix.util.PDate;

/**
 * 
 * @author pcnsh197
 * 
 */
public class OrderHeaderInformationBean {
	private String	documentType;

	private int	orderNumber;

	private PDate	orderDate;

	private int		customerNumber;

	private int		registerNumber;

	private String	ws1SalesOrganisation;

	private String	ws1CustomerNumber;

	private String	ws1RegisterNumber;

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public PDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(PDate orderDate) {
		this.orderDate = orderDate;
	}

	public int getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}

	public int getRegisterNumber() {
		return registerNumber;
	}

	public void setRegisterNumber(int registerNumber) {
		this.registerNumber = registerNumber;
	}

	public String getWs1SalesOrganisation() {
		return ws1SalesOrganisation;
	}

	public void setWs1SalesOrganisation(String ws1SalesOrganisation) {
		this.ws1SalesOrganisation = ws1SalesOrganisation;
	}

	public String getWs1CustomerNumber() {
		return ws1CustomerNumber;
	}

	public void setWs1CustomerNumber(String ws1CustomerNumber) {
		this.ws1CustomerNumber = ws1CustomerNumber;
	}

	public String getWs1RegisterNumber() {
		return ws1RegisterNumber;
	}

	public void setWs1RegisterNumber(String ws1RegisterNumber) {
		this.ws1RegisterNumber = ws1RegisterNumber;
	}

}
