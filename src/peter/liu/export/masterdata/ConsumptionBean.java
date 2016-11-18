package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

/**
 * ConsumptionBean
 * 
 * @author pcnsh197
 * 
 */
public class ConsumptionBean {

	private String	productNumber;

	private String	plant;

	private String	period;		// MMYYYY

	private int	consumption;	// demand

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public int getConsumption() {
		return consumption;
	}

	public void setConsumption(int consumption) {
		this.consumption = consumption;
	}

}
