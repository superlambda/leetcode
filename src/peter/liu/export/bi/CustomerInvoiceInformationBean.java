package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import com.wuerth.phoenix.util.PDate;

/**
 * 
 * @author pcnsh197
 * 
 */
public class CustomerInvoiceInformationBean {

	private int		customerNumber;

	private PDate	lastInvoiceDate;

	private double	turnoverBefore2011=0.0D;
	
	private String	ws1SalesOrganisation;

	private String	ws1CustomerNumber;

	public int getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}

	public PDate getLastInvoiceDate() {
		return lastInvoiceDate;
	}

	public void setLastInvoiceDate(PDate lastInvoiceDate) {
		this.lastInvoiceDate = lastInvoiceDate;
	}

	public double getTurnoverBefore2011() {
		return turnoverBefore2011;
	}

	public void setTurnoverBefore2011(double turnoverBefore2011) {
		this.turnoverBefore2011 = turnoverBefore2011;
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

	
	
}
