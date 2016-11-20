package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import com.wuerth.phoenix.util.PDate;

/**
 * OrderItemInformationBean
 * 
 * @author pcnsh197
 * 
 */
public class OrderItemInformationBean {

	private int		orderItem;

	private String	documentType;

	private String	orderSource;

	private PDate	orderDate;

	private double	orderQuantity;

	private double	price;

	private double	weight;

	private String	weightUnit;

	private double	grossValue;

	private double	netValue;

	private int		customerNumber;

	private String	articleNumber;

	private int		registerNumber;

	private int	priceUnit;

	private int		orderNumber;

	private int		registerNumber2;

	private String	storageLocation;

	private double	cogsglep;

	private double	cogspfep;

	private String	ws1SalesOrganisation;

	private String	ws1CustomerNumber;

	private String	ws1ArticleNumber;

	private String	ws1RegisterNumber;

	private String	ws1OrderNumber;

	private String	plant;
	
	private String deliveryPlant;
	
	private char orderCreditNoteSign;
	
	private double orderValue;
	private double discount;
	
	private String name1;
	
	private int goodsRecipient;
	private int debtor;
	private String WarehouseNumber;
	
	private String orderReason;
	private String orderCategory;
	private String documentCategory;
	private String salesDocumentType;
	private double  freightCost;
	private double	taxAmount;
	
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

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getDeliveryPlant() {
		return deliveryPlant;
	}

	public double getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(double orderValue) {
		this.orderValue = orderValue;
	}

	public void setDeliveryPlant(String deliveryPlant) {
		this.deliveryPlant = deliveryPlant;
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

	public PDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(PDate orderDate) {
		this.orderDate = orderDate;
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

	public String getWs1CustomerNumber() {
		return ws1CustomerNumber;
	}

	public void setWs1CustomerNumber(String ws1CustomerNumber) {
		this.ws1CustomerNumber = ws1CustomerNumber;
	}

	public String getWs1ArticleNumber() {
		return ws1ArticleNumber;
	}

	public void setWs1ArticleNumber(String ws1ArticleNumber) {
		this.ws1ArticleNumber = ws1ArticleNumber;
	}

	public String getWs1RegisterNumber() {
		return ws1RegisterNumber;
	}

	public char getOrderCreditNoteSign() {
		return orderCreditNoteSign;
	}

	public void setOrderCreditNoteSign(char orderCreditNoteSign) {
		this.orderCreditNoteSign = orderCreditNoteSign;
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

	public String getWs1SalesOrganisation() {
		return ws1SalesOrganisation;
	}

	public void setWs1SalesOrganisation(String ws1SalesOrganisation) {
		this.ws1SalesOrganisation = ws1SalesOrganisation;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getWarehouseNumber() {
		return WarehouseNumber;
	}

	public void setWarehouseNumber(String warehouseNumber) {
		WarehouseNumber = warehouseNumber;
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

	public String getSalesDocumentType() {
		return salesDocumentType;
	}

	public void setSalesDocumentType(String salesDocumentType) {
		this.salesDocumentType = salesDocumentType;
	}

	public double getFreightCost() {
		return freightCost;
	}

	public void setFreightCost(double freightCost) {
		this.freightCost = freightCost;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	
}
