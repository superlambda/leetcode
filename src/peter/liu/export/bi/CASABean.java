package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

/**
 * CASABean
 * 
 * @author pcnsh197
 * 
 */
public class CASABean {

	// YYYYMM
	private int		period;

	private int		customerNumber;

	private int		registerNumber;

	private boolean	orsy;

	private int		customerStatus;

	// Turnover of current monthSample: 201208
	private double	turnover_cm=0.0D;
	
	// Turnover before current month
    private double	turnover_bf_cm=0.0D;;

	// Turnover of the rolling last 12 month based on current year Sample: 201109 - 201208
	private double	turnover_r12cy=0.0D;;

	// Turnover of the rolling last 12 month based on last year Sample: 201009 - 201108
	private double	turnover_r12ly=0.0D;;

	// COGS PFEP of the rolling last 12 month based on current year Sample: 201109 - 201208
	private double	cogspfep_r12cy=0.0D;;

	// COGS PFEP of the rolling last 12 month based on last year Sample: 201009 - 201108
	private double	cogspfep_r12ly=0.0D;;

	// COGS PFEP of current month Sample: 201208
	private double	cogspfep_cm=0.0D;;

	// COGS GLEP of the rolling last 12 month based on current year Sample: 201109 - 201208
	private double	cogsglep_r12cy=0.0D;;

	// COGS GLEP of the rolling last 12 month based on last year Sample: 201009 - 201108
	private double	cogsglep_r12ly=0.0D;;

	// COGS GLEP of current month Sample: 201208
	private double	cogsglep_cm=0.0D;;
	
	// Freight cost current monthSample: 201208
    private double	freightcost_cm=0.0D;
    
	private double	num_of_cn_cm=0.0D;;

	private double	num_of_cn_r12cy=0.0D;;

	private double	num_of_cn_r12ly=0.0D;;
	
	private double	num_of_ord_cm=0.0D;;

	private double	num_of_ord_r12cy=0.0D;;

	private double	num_of_ord_r12ly=0.0D;;
	
	// The calculation is based on the customer turnover of the rolling 12 months current year. If the
	// customer has done turnover in the last 12 rolling months (Active Customer R12 CY), the customers are
	// classified into the dedicated SML-Ranges.Sample: SML for 201208 based on 201109 - 201208
	private String	sml_r12cy;

	// The classification of the customers for the dedictaed month of last year into the dedicated SML-Ranges
	// based on on the customer turnover of the rolling 12 months last yearSample: SML for 201208 based on
	// 201009 - 201108
	private String	sml_r12ly;
	
	// The classification of the customers for the current month into the
	// dedicated SML-Ranges based on on the customer Potential of the rolling 12
	// months current year: SML for 201208 based on Potential 201109 - 201208
	private String sml_potential_r12cy;
	
	// The classification of the customers potential class based on the
	// calculated potential of rolling 12 month current year in relation to the
	// number of employees
	private String sml_n_potential_r12cy;
	// The classification of the customers potential class based on the
	// calculated potential of rolling 12 month last year in relation to the
	// number of employees
	private String sml_n_potential_r12ly;

	// The calculation is based on the customer turnover. If the customer has done the first turnover ever in
	// the current month and has done no turnover ever before that month, he is a new customer in the current
	// month.
	private boolean	newcustomer_cm;
	
	// If the customer has been a new customer in the last 12 month, he gets a
	// flag 'X': 201109-201208
	private boolean newcustomer_r12cy;

	// The calculation is based on the customer turnover of the rolling 12 months current year. If the
	// customer has done no turnover in the last 12 rolling months and has done turnover ever before these
	// last 12 rolling months, he is a Zero Customer R12CY in the current month. Sample: For 201208 based on
	// 201109 - 201208
	private boolean	zerocustomer_r12cy;
	//if the customer became a Zero customer in the current month 201208 and the field CCCASASTA is 4, set a 'X'  into the field
	private boolean	lostcustomer_cm;
	//If the Customer became a Zero customer in the last 12 month and the field CCCASASTA is 4, set a 'X' into the field
	private boolean	lostcustomer_r12cy;

	// The calculation is based on the customer turnover of the rolling 12 months current year. If the
	// customer has done no turnover in the last 12 rolling months and has done again turnover in the current
	// month, he is a Reactivated Customer R12CY in the current month.
	private boolean	reactivatedcustomer_cm;
	//If the customer has been reactivated in the last 12 month, he gets a flag 'X': 201109-201208
	private boolean	reactivatedcustomer_r12cy;
	// If customer has done turnover more than 500 EUR in the last rolling 12 months current year, he gets a
	// flag 'x' Sample: For 201208 based on 201109 - 201208
	private boolean	buyingcustomer500_r12cy;

	private String	ws1SalesOrganisation;

	private String	ws1CustomerNumber;

	private String	ws1RegisterNumber;

	private int		ws1CustomerStatus;
	
	private String name1;
	
	private double potential;
	
	

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
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

	public boolean isOrsy() {
		return orsy;
	}

	public void setOrsy(boolean orsy) {
		this.orsy = orsy;
	}

	public int getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(int customerStatus) {
		this.customerStatus = customerStatus;
	}

	public double getTurnover_cm() {
		return turnover_cm;
	}

	public void setTurnover_cm(double turnover_cm) {
		this.turnover_cm = turnover_cm;
	}

	public double getTurnover_r12cy() {
		return turnover_r12cy;
	}

	public void setTurnover_r12cy(double turnover_r12cy) {
		this.turnover_r12cy = turnover_r12cy;
	}

	public double getTurnover_r12ly() {
		return turnover_r12ly;
	}

	public void setTurnover_r12ly(double turnover_r12ly) {
		this.turnover_r12ly = turnover_r12ly;
	}

	public double getCogspfep_r12cy() {
		return cogspfep_r12cy;
	}

	public void setCogspfep_r12cy(double cogspfep_r12cy) {
		this.cogspfep_r12cy = cogspfep_r12cy;
	}

	public double getCogspfep_r12ly() {
		return cogspfep_r12ly;
	}

	public void setCogspfep_r12ly(double cogspfep_r12ly) {
		this.cogspfep_r12ly = cogspfep_r12ly;
	}

	public double getCogsglep_r12cy() {
		return cogsglep_r12cy;
	}

	public void setCogsglep_r12cy(double cogsglep_r12cy) {
		this.cogsglep_r12cy = cogsglep_r12cy;
	}

	public double getCogsglep_r12ly() {
		return cogsglep_r12ly;
	}

	public void setCogsglep_r12ly(double cogsglep_r12ly) {
		this.cogsglep_r12ly = cogsglep_r12ly;
	}

	public double getCogspfep_cm() {
		return cogspfep_cm;
	}

	public void setCogspfep_cm(double cogspfep_cm) {
		this.cogspfep_cm = cogspfep_cm;
	}

	public double getCogsglep_cm() {
		return cogsglep_cm;
	}

	public void setCogsglep_cm(double cogsglep_cm) {
		this.cogsglep_cm = cogsglep_cm;
	}

	public String getSml_r12cy() {
		return sml_r12cy;
	}

	public void setSml_r12cy(String sml_r12cy) {
		this.sml_r12cy = sml_r12cy;
	}

	public String getSml_r12ly() {
		return sml_r12ly;
	}

	public void setSml_r12ly(String sml_r12ly) {
		this.sml_r12ly = sml_r12ly;
	}

	public boolean isNewcustomer_cm() {
		return newcustomer_cm;
	}

	public void setNewcustomer_cm(boolean newcustomer_cm) {
		this.newcustomer_cm = newcustomer_cm;
	}

	public boolean isZerocustomer_r12cy() {
		return zerocustomer_r12cy;
	}

	public void setZerocustomer_r12cy(boolean zerocustomer_r12cy) {
		this.zerocustomer_r12cy = zerocustomer_r12cy;
	}

	public boolean isReactivatedcustomer_cm() {
		return reactivatedcustomer_cm;
	}

	public void setReactivatedcustomer_cm(boolean reactivatedcustomer_cm) {
		this.reactivatedcustomer_cm = reactivatedcustomer_cm;
	}

	public boolean isBuyingcustomer500_r12cy() {
		return buyingcustomer500_r12cy;
	}

	public void setBuyingcustomer500_r12cy(boolean buyingcustomer500_r12cy) {
		this.buyingcustomer500_r12cy = buyingcustomer500_r12cy;
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

	public int getWs1CustomerStatus() {
		return ws1CustomerStatus;
	}

	public void setWs1CustomerStatus(int ws1CustomerStatus) {
		this.ws1CustomerStatus = ws1CustomerStatus;
	}

	public double getTurnover_bf_cm() {
		return turnover_bf_cm;
	}

	public void setTurnover_bf_cm(double turnover_bf_cm) {
		this.turnover_bf_cm = turnover_bf_cm;
	}

	public boolean isNewcustomer_r12cy() {
		return newcustomer_r12cy;
	}

	public void setNewcustomer_r12cy(boolean newcustomer_r12cy) {
		this.newcustomer_r12cy = newcustomer_r12cy;
	}

	public boolean isReactivatedcustomer_r12cy() {
		return reactivatedcustomer_r12cy;
	}

	public void setReactivatedcustomer_r12cy(boolean reactivatedcustomer_r12cy) {
		this.reactivatedcustomer_r12cy = reactivatedcustomer_r12cy;
	}

	public boolean isLostcustomer_cm() {
		return lostcustomer_cm;
	}

	public void setLostcustomer_cm(boolean lostcustomer_cm) {
		this.lostcustomer_cm = lostcustomer_cm;
	}

	public boolean isLostcustomer_r12cy() {
		return lostcustomer_r12cy;
	}

	public void setLostcustomer_r12cy(boolean lostcustomer_r12cy) {
		this.lostcustomer_r12cy = lostcustomer_r12cy;
	}

	public String getSml_potential_r12cy() {
		return sml_potential_r12cy;
	}

	public void setSml_potential_r12cy(String sml_potential_r12cy) {
		this.sml_potential_r12cy = sml_potential_r12cy;
	}

	public String getSml_n_potential_r12cy() {
		return sml_n_potential_r12cy;
	}

	public void setSml_n_potential_r12cy(String sml_n_potential_r12cy) {
		this.sml_n_potential_r12cy = sml_n_potential_r12cy;
	}

	public String getSml_n_potential_r12ly() {
		return sml_n_potential_r12ly;
	}

	public void setSml_n_potential_r12ly(String sml_n_potential_r12ly) {
		this.sml_n_potential_r12ly = sml_n_potential_r12ly;
	}

	public double getPotential() {
		return potential;
	}

	public void setPotential(double potential) {
		this.potential = potential;
	}

	public double getFreightcost_cm() {
		return freightcost_cm;
	}

	public void setFreightcost_cm(double freightcost_cm) {
		this.freightcost_cm = freightcost_cm;
	}

	public double getNum_of_cn_cm() {
		return num_of_cn_cm;
	}

	public void setNum_of_cn_cm(double num_of_cn_cm) {
		this.num_of_cn_cm = num_of_cn_cm;
	}

	public double getNum_of_cn_r12cy() {
		return num_of_cn_r12cy;
	}

	public void setNum_of_cn_r12cy(double num_of_cn_r12cy) {
		this.num_of_cn_r12cy = num_of_cn_r12cy;
	}

	public double getNum_of_cn_r12ly() {
		return num_of_cn_r12ly;
	}

	public void setNum_of_cn_r12ly(double num_of_cn_r12ly) {
		this.num_of_cn_r12ly = num_of_cn_r12ly;
	}

	public double getNum_of_ord_cm() {
		return num_of_ord_cm;
	}

	public void setNum_of_ord_cm(double num_of_ord_cm) {
		this.num_of_ord_cm = num_of_ord_cm;
	}

	public double getNum_of_ord_r12cy() {
		return num_of_ord_r12cy;
	}

	public void setNum_of_ord_r12cy(double num_of_ord_r12cy) {
		this.num_of_ord_r12cy = num_of_ord_r12cy;
	}

	public double getNum_of_ord_r12ly() {
		return num_of_ord_r12ly;
	}

	public void setNum_of_ord_r12ly(double num_of_ord_r12ly) {
		this.num_of_ord_r12ly = num_of_ord_r12ly;
	}
}
