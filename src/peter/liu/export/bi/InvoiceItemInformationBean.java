package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import com.wuerth.phoenix.util.PDate;

/**
 * InvoiceItemInformationBean
 * 
 * @author pcnsh197
 * 
 */
public class InvoiceItemInformationBean {

	private int		invoiceItem;

	private int		orderItem;

	private String	documentType;

	private String	orderSource;

	private String	shippingType;

	private int		invoiceNumber;

	private PDate	orderDate;

	private PDate	invoiceDate;

	private double	orderQuantity;

	private double	price;

	private double	weight;

	private String	weightUnit;

	private double	invoiceQuantity;

	private double	grossValue;

	private double	netValue;

	private double	glep;

	private double	pfep;

	private double	discount;

	private double	surcharge;

	private double	cogsglep;

	private double	cogspfep;
	
	private double	taxAmount;

	private int		customerNumber;

	private String	articleNumber;
	
	private String	productNumber;

	private int		registerNumber;

	private int	priceUnit;

	private int		orderNumber;

	private int		registerNumber2;

	private String	storageLocation;

	private double	packagingCosts;

	private double	shippingCosts;

	private double	otherCosts;
	
	private String name1;

	private double  freightCost;
	
	/*CSALESORG	Sales Organization*/
	private String	ws1SalesOrganisation;
	/*
	 * CCUST Customer Number (Sold-to-Party) WS1 CHAR 10 4 digits Pre-Number +
	 * Customer Number old (CHAR 6, leading zeros) ; Pre-Number needs to be
	 * asked by HR/SD/Customer Master
	 */
	private String ws1CustomerNumber;
	
	private String payer;
	private String shipToCustomer;
	
	private String debtorName;
	private String goodsRecipientName;

	/*
	 * CTERREP Sales Rep WS1 NUMC 8 Logic defined by Key Users in HR via Sales
	 * Rep Mapping; New_20131210: Field Value is 9997 or 9998, if there is an
	 * export or internal sales between Würth Companies
	 */
	private String ws1RegisterNumber;

	private String	ws1OrderNumber;
	
	/*
	 * CBOD Branch Office Did the Deal WS1 CHAR 4 Logic defined by Key Users in
	 * MM via Plant Mapping
	 */
	private String plant;
	
	/*
	 * CPLT Delivery Plant WS1 CHAR 4 Logic defined by Key Users in MM via Plant
	 * Mapping
	 */
	private String deliveryPlant;
	
	private int numberOfInvoiceDocumentItems;
	
	private int numberOfCreditNoteItems;
	
	private char orderCreditNoteSign;
	
	private double turnover;
	
	private String warehouseNumber;
	
	private int goodsRecipient;
	private int debtor;
	private String orderReason;
	private String orderCategory;
	private String documentCategory;
	private String salesDocumentType;
	
	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public double getFreightCost() {
		return freightCost;
	}

	public void setFreightCost(double freightCost) {
		this.freightCost = freightCost;
	}
	
	public String getSalesDocumentType() {
		return salesDocumentType;
	}

	public void setSalesDocumentType(String salesDocumentType) {
		this.salesDocumentType = salesDocumentType;
	}

	public int getInvoiceItem() {
		return invoiceItem;
	}

	public int getGoodsRecipient() {
		return goodsRecipient;
	}

	public void setGoodsRecipient(int goodsRecipient) {
		this.goodsRecipient = goodsRecipient;
	}

	public int getDebtor() {
		return debtor;
	}

	public void setDebtor(int debtor) {
		this.debtor = debtor;
	}

	public void setInvoiceItem(int invoiceItem) {
		this.invoiceItem = invoiceItem;
	}

	public int getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(int orderItem) {
		this.orderItem = orderItem;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public int getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(int invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public PDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(PDate orderDate) {
		this.orderDate = orderDate;
	}

	public PDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(PDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public double getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(double orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getInvoiceQuantity() {
		return invoiceQuantity;
	}

	public void setInvoiceQuantity(double invoiceQuantity) {
		this.invoiceQuantity = invoiceQuantity;
	}

	public double getGrossValue() {
		return grossValue;
	}

	public void setGrossValue(double grossValue) {
		this.grossValue = grossValue;
	}

	public double getNetValue() {
		return netValue;
	}

	public void setNetValue(double netValue) {
		this.netValue = netValue;
	}

	public double getGlep() {
		return glep;
	}

	public void setGlep(double glep) {
		this.glep = glep;
	}

	public double getPfep() {
		return pfep;
	}

	public void setPfep(double pfep) {
		this.pfep = pfep;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getCogsglep() {
		return cogsglep;
	}

	public void setCogsglep(double cogsglep) {
		this.cogsglep = cogsglep;
	}

	public double getCogspfep() {
		return cogspfep;
	}

	public void setCogspfep(double cogspfep) {
		this.cogspfep = cogspfep;
	}

	public int getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getArticleNumber() {
		return articleNumber;
	}

	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}

	public int getRegisterNumber() {
		return registerNumber;
	}

	public void setRegisterNumber(int registerNumber) {
		this.registerNumber = registerNumber;
	}

	public int getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(int priceUnit) {
		this.priceUnit = priceUnit;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getRegisterNumber2() {
		return registerNumber2;
	}

	public void setRegisterNumber2(int registerNumber2) {
		this.registerNumber2 = registerNumber2;
	}

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public double getPackagingCosts() {
		return packagingCosts;
	}

	public void setPackagingCosts(double packagingCosts) {
		this.packagingCosts = packagingCosts;
	}

	public double getShippingCosts() {
		return shippingCosts;
	}

	public void setShippingCosts(double shippingCosts) {
		this.shippingCosts = shippingCosts;
	}

	public double getOtherCosts() {
		return otherCosts;
	}

	public void setOtherCosts(double otherCosts) {
		this.otherCosts = otherCosts;
	}

	public double getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(double surcharge) {
		this.surcharge = surcharge;
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

	public String getWs1OrderNumber() {
		return ws1OrderNumber;
	}

	public void setWs1OrderNumber(String ws1OrderNumber) {
		this.ws1OrderNumber = ws1OrderNumber;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getDeliveryPlant() {
		return deliveryPlant;
	}

	public void setDeliveryPlant(String deliveryPlant) {
		this.deliveryPlant = deliveryPlant;
	}

	public char getOrderCreditNoteSign() {
		return orderCreditNoteSign;
	}

	public void setOrderCreditNoteSign(char orderCreditNoteSign) {
		this.orderCreditNoteSign = orderCreditNoteSign;
	}

	public double getTurnover() {
		return turnover;
	}

	public void setTurnover(double turnover) {
		this.turnover = turnover;
	}

	public int getNumberOfInvoiceDocumentItems() {
		return numberOfInvoiceDocumentItems;
	}

	public void setNumberOfInvoiceDocumentItems(int numberOfInvoiceDocumentItems) {
		this.numberOfInvoiceDocumentItems = numberOfInvoiceDocumentItems;
	}

	public int getNumberOfCreditNoteItems() {
		return numberOfCreditNoteItems;
	}

	public void setNumberOfCreditNoteItems(int numberOfCreditNoteItems) {
		this.numberOfCreditNoteItems = numberOfCreditNoteItems;
	}

	public String getWarehouseNumber() {
		return warehouseNumber;
	}

	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}

	public String getOrderReason() {
		return orderReason;
	}

	public void setOrderReason(String orderReason) {
		this.orderReason = orderReason;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getShipToCustomer() {
		return shipToCustomer;
	}

	public void setShipToCustomer(String shipToCustomer) {
		this.shipToCustomer = shipToCustomer;
	}

	public String getDebtorName() {
		return debtorName;
	}

	public void setDebtorName(String debtorName) {
		this.debtorName = debtorName;
	}

	public String getGoodsRecipientName() {
		return goodsRecipientName;
	}

	public void setGoodsRecipientName(String goodsRecipientName) {
		this.goodsRecipientName = goodsRecipientName;
	}
}
