package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.enums.CustomerOrderStatus;
import com.wuerth.phoenix.Phxbasic.enums.DemandType;
import com.wuerth.phoenix.Phxbasic.enums.LanguageCode;
import com.wuerth.phoenix.Phxbasic.enums.ProductType;
import com.wuerth.phoenix.Phxbasic.enums.PurchaseOrderLineStatus;
import com.wuerth.phoenix.Phxbasic.models.CustomerOrder;
import com.wuerth.phoenix.Phxbasic.models.CustomerOrderLine;
import com.wuerth.phoenix.Phxbasic.models.DWCustomerInvoiceLine;
import com.wuerth.phoenix.Phxbasic.models.DWProduct;
import com.wuerth.phoenix.Phxbasic.models.DWRetoureLine;
import com.wuerth.phoenix.Phxbasic.models.Demand;
import com.wuerth.phoenix.Phxbasic.models.Description;
import com.wuerth.phoenix.Phxbasic.models.Product;
import com.wuerth.phoenix.Phxbasic.models.PurchaseOrder;
import com.wuerth.phoenix.Phxbasic.models.PurchaseOrderLine;
import com.wuerth.phoenix.Phxbasic.models.Supplier;
import com.wuerth.phoenix.Phxbasic.models.SupplierProduct;
import com.wuerth.phoenix.Phxbasic.models.WarehouseProductAllowance;
import com.wuerth.phoenix.basic.etnax.common.utilsdir.DoubleUtils;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi.BIDateMapping;
import com.wuerth.phoenix.bcutil.IteratorFactory;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryParseException;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;
import com.wuerth.phoenix.internal.bc.server.query.QueryResultEntry;
import com.wuerth.phoenix.util.PDate;

/**
 * ProductInformationExport
 * 
 * @author pcnsh197
 * 
 */
public class ProductInformationExport extends BatchRunner {

	private int NUMBER_OF_BATCH_TO_FECTCH = 100;

	LinkedList<ProductInformationBean> productInfoList = new LinkedList<ProductInformationBean>();
	
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	private String				dataSource					= "../../etc/exportSAP/DC_Material-template.xlsx";
	
	private int currentYearAndPeriod=0;
	private String								ownCompanyName;
	
	
	@Override
	protected void batchMethod() throws TimestampException, PUserException {
		PDate currentDate=new PDate();
		ownCompanyName = _controller.getSingletonOwnCompany().getName();
		currentYearAndPeriod=currentDate.getYear()*100+currentDate.getMonth();
//		searchProduct();
		searchProductInExcel();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ProductInformationExport().startBatch(args);
	}
	
	
	private void searchProductInExcel() throws TimestampException, PUserException {
		Map<String,double[]> sales2YearAndCurrentMap=new HashMap<String,double[]>(10000);
		Map<String,double[]> salesLatestyearMap=new HashMap<String,double[]>(10000);
		//Here to change the to date
		sales2YearAndCurrentMap = calculateSalesAndTurnoverBetweenDateRange(
				new PDate(2011, 0, 1), new PDate(2014, 8, 30),
				sales2YearAndCurrentMap);
		salesLatestyearMap = calculateSalesAndTurnoverBetweenDateRange(
				new PDate(2013, 0, 1), new PDate(2014, 8, 30),
				salesLatestyearMap);
		
		Map<String, Long> picks2YearsAndCurrentYearMap = new HashMap<String, Long>(
				10000);
		Map<String, Long> picksLatestYearMap = new HashMap<String, Long>(10000);
		picks2YearsAndCurrentYearMap = calculatePicksBetweenDateRange(201101,
				currentYearAndPeriod, picks2YearsAndCurrentYearMap);
		picksLatestYearMap = calculatePicksBetweenDateRange(201301,
				currentYearAndPeriod, picksLatestYearMap);
		
		int batch = 1;
		for (String productNumber : BIDateMapping.productGZMissingset) {
			Product product=_controller.lookupProduct(productNumber);
			ProductInformationBean pib = new ProductInformationBean();
			if (product == null) {
				System.out
						.println("\n WARING product in excel can not be found: "
								+ productNumber);
				continue;
			}
			pib.setProductNumber(product.getProductNumber());
			pib.setPackingSize(product.getOwnCompanyProductSalesUnit()
					.getPackageSize1().getAmount());
			pib.setProductDescriptionCN(product.getShortName());

			Description desENOrDE = product
					.lookupDescription(LanguageCode.ENGLISH);
			if (desENOrDE != null) {
				pib.setProductDescriptionENOrDE(desENOrDE.getTranslation());
			} else {
				desENOrDE = product.lookupDescription(LanguageCode.GERMAN);
				if (desENOrDE != null) {
					pib.setProductDescriptionENOrDE(desENOrDE.getTranslation());
				}
			}

			pib.setUnitCost(product.getStandardCostPrice().getAmount());
			pib.setUnit(product.getOwnCompanyProductSalesUnit()
					.getPrefixLabel());
			if (product.getType() != ProductType.SERVICE) {
				List<WarehouseProductAllowance> wpas = new ArrayList<WarehouseProductAllowance>(
						product.getAllChildWarehouseProductAllowance());
				double currentStock = 0.0D;
				for (WarehouseProductAllowance warehouseProductAllowance : wpas) {
					currentStock += warehouseProductAllowance
							.getPhysicalStock().getQuantityUnit().getAmount();
				}
				pib.setCurrentStockQuantity(currentStock);
			} else {
				pib.setCurrentStockQuantity(0D);
			}
			pib.setStockValue(DoubleUtils.getRoundedAmount(pib
					.getCurrentStockQuantity() * pib.getUnitCost()));
			pib.setCreationDate(product.getProductCreationDate());
			pib.setProductStatus(product.getStatus().getDescription());
			pib.setGrossWeight(product.getOwnCompanyProductSalesUnit()
					.getWeight().getAmount());
			pib.setWeightUnit(product.getOwnCompanyProductSalesUnit()
					.getWeight().getWeightMeasureUnit().getDescription());
			Supplier mainSupplier = product.getSuppliermainSupplier();
			if (mainSupplier != null) {
				pib.setMainSupplierName(mainSupplier.getName());
				SupplierProduct sp = mainSupplier.getSupplierProductFor(pib
						.getProductNumber());
				if (sp != null) {
					pib.setMainSupplierProductNumber(sp
							.getSupplierProductNumber());
				}
				PurchaseOrder latestPO = getLastestPOOfSupplier(mainSupplier,
						product);
				if (latestPO != null) {
					pib.setMainSupplierLastPurchaseDate(latestPO.getOrderDate());
				}
			}
			pib.setCurrentOpenPOQuantity(getCurrentOpenPOQuantity(product));
			pib.setCurrentOpenSOQuantity(getCurrentOpenSOQuantity(product));
			double[] sales2YearAndCurrent = sales2YearAndCurrentMap.get(product
					.getProductNumber());
			if (sales2YearAndCurrent != null) {
				pib.setSalesTurnover2YearsAndCurrentYear(sales2YearAndCurrent[0]);
				pib.setSalesQuantity2YearsAndCurrentYear(sales2YearAndCurrent[1]);
			}
			Long picks2YearsAndCurrentYear = picks2YearsAndCurrentYearMap
					.get(product.getProductNumber());
			if (picks2YearsAndCurrentYear != null) {
				pib.setPicks2YearsAndCurrentYear(picks2YearsAndCurrentYear);
			}

			double[] salesLatestyear = salesLatestyearMap.get(product
					.getProductNumber());
			if (salesLatestyear != null) {
				pib.setSalesTurnoverLatestYear(salesLatestyear[0]);
				pib.setSalesQuantityLatestYear(salesLatestyear[1]);
			}

			Long picksLatestYear = picksLatestYearMap.get(product
					.getProductNumber());
			if (picksLatestYear != null) {
				pib.setPicksLatestYear(picksLatestYear);
			}
			getLastTwoSupplier(product, pib);

			productInfoList.addLast(pib);
			if (productInfoList.size() == 6000) {
				writeProductInfoToExcel(batch);
				batch++;
				productInfoList = new LinkedList<ProductInformationBean>();
			}
			System.out.println("\n Number of products loaded: "
					+ productInfoList.size());
		}
		writeProductInfoToExcel(batch);
	}
	
	
	
	

	private void searchProduct() throws TimestampException, PUserException {
		Map<String,double[]> sales2YearAndCurrentMap=new HashMap<String,double[]>(10000);
		Map<String,double[]> salesLatestyearMap=new HashMap<String,double[]>(10000);
		//Here to change the to date
		sales2YearAndCurrentMap = calculateSalesAndTurnoverBetweenDateRange(
				new PDate(2011, 0, 1), new PDate(2014, 8, 30),
				sales2YearAndCurrentMap);
		salesLatestyearMap = calculateSalesAndTurnoverBetweenDateRange(
				new PDate(2013, 0, 1), new PDate(2014, 8, 30),
				salesLatestyearMap);
		
		Map<String, Long> picks2YearsAndCurrentYearMap = new HashMap<String, Long>(
				10000);
		Map<String, Long> picksLatestYearMap = new HashMap<String, Long>(10000);
		picks2YearsAndCurrentYearMap = calculatePicksBetweenDateRange(201101,
				currentYearAndPeriod, picks2YearsAndCurrentYearMap);
		picksLatestYearMap = calculatePicksBetweenDateRange(201301,
				currentYearAndPeriod, picksLatestYearMap);
		
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(Product.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(Product.PRODUCTNUMBER);
		Condition cond = qh.condition();
		PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
        int batch=1;
		while (penum.hasMoreElements()) {
			List<Product> list = new ArrayList<Product>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			for (Product product : list) {
				ProductInformationBean pib = new ProductInformationBean();
				pib.setProductNumber(product.getProductNumber());
				pib.setPackingSize(product.getOwnCompanyProductSalesUnit()
						.getPackageSize1().getAmount());
				pib.setProductDescriptionCN(product.getShortName());

				Description desENOrDE = product.lookupDescription(LanguageCode.ENGLISH);
				if (desENOrDE != null) {
					pib.setProductDescriptionENOrDE(desENOrDE.getTranslation());
				} else {
					desENOrDE = product.lookupDescription(LanguageCode.GERMAN);
					if (desENOrDE != null) {
						pib.setProductDescriptionENOrDE(desENOrDE.getTranslation());
					}
				}

				pib.setUnitCost(product.getStandardCostPrice().getAmount());
				pib.setUnit(product.getOwnCompanyProductSalesUnit().getPrefixLabel());
				if (product.getType() != ProductType.SERVICE) {
					List<WarehouseProductAllowance> wpas = new ArrayList<WarehouseProductAllowance>(
							product.getAllChildWarehouseProductAllowance());
					double currentStock = 0.0D;
					for (WarehouseProductAllowance warehouseProductAllowance : wpas) {
						currentStock += warehouseProductAllowance.getPhysicalStock()
								.getQuantityUnit().getAmount();
					}
					pib.setCurrentStockQuantity(currentStock);
				} else {
					pib.setCurrentStockQuantity(0D);
				}
				pib.setStockValue(DoubleUtils.getRoundedAmount(pib
						.getCurrentStockQuantity() * pib.getUnitCost()));
				pib.setCreationDate(product.getProductCreationDate());
				pib.setProductStatus(product.getStatus().getDescription());
				pib.setGrossWeight(product.getOwnCompanyProductSalesUnit().getWeight()
						.getAmount());
				pib.setWeightUnit(product.getOwnCompanyProductSalesUnit().getWeight()
						.getWeightMeasureUnit().getDescription());
				Supplier mainSupplier = product.getSuppliermainSupplier();
				if (mainSupplier != null) {
					pib.setMainSupplierName(mainSupplier.getName());
					SupplierProduct sp = mainSupplier.getSupplierProductFor(pib
							.getProductNumber());
					if (sp != null) {
						pib.setMainSupplierProductNumber(sp.getSupplierProductNumber());
					}
					PurchaseOrder latestPO = getLastestPOOfSupplier(mainSupplier,product);
					if (latestPO != null) {
						pib.setMainSupplierLastPurchaseDate(latestPO.getOrderDate());
					}
				}
				pib.setCurrentOpenPOQuantity(getCurrentOpenPOQuantity(product));
				pib.setCurrentOpenSOQuantity(getCurrentOpenSOQuantity(product));
				double[] sales2YearAndCurrent = sales2YearAndCurrentMap
						.get(product.getProductNumber());
				if (sales2YearAndCurrent != null) {
					pib.setSalesTurnover2YearsAndCurrentYear(sales2YearAndCurrent[0]);
					pib.setSalesQuantity2YearsAndCurrentYear(sales2YearAndCurrent[1]);
				}
				Long picks2YearsAndCurrentYear = picks2YearsAndCurrentYearMap
						.get(product.getProductNumber());
				if (picks2YearsAndCurrentYear != null) {
					pib.setPicks2YearsAndCurrentYear(picks2YearsAndCurrentYear);
				}

				double[] salesLatestyear = salesLatestyearMap.get(product
						.getProductNumber());
				if (salesLatestyear != null) {
					pib.setSalesTurnoverLatestYear(salesLatestyear[0]);
					pib.setSalesQuantityLatestYear(salesLatestyear[1]);
				}

				Long picksLatestYear = picksLatestYearMap.get(product
						.getProductNumber());
				if (picksLatestYear != null) {
					pib.setPicksLatestYear(picksLatestYear);
				}
				getLastTwoSupplier(product,pib);
				
				productInfoList.addLast(pib);
				if(productInfoList.size()==6000){
					writeProductInfoToExcel(batch);
					batch++;
					productInfoList=new LinkedList<ProductInformationBean>();
				}
				System.out.println("\n Number of products loaded: "+productInfoList.size());
			}
			_controller.getContext().commit();
		}
		writeProductInfoToExcel(batch);
		penum.destroy();
	}

	private PurchaseOrder getLastestPOOfSupplier(Supplier supplier, Product product)
			throws PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p = qh.attr(PurchaseOrderLine.COMPANYPRODUCTNUMBER).eq()
				.val(product.getProductNumber()).predicate();
		QueryPredicate p1 = qh
				.attr(PurchaseOrderLine.PARENT_PURCHASEORDER_REF
						+ PurchaseOrder.ASS_SUPPLIER).eq().val(supplier).predicate();
		qh.setClass(PurchaseOrderLine.class);
		qh.setDeepSelect(true);
		qh.addDescendingOrdering(PurchaseOrderLine.CREATEDATE);
		Condition cond = qh.condition(p.and(p1));
		PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
		PurchaseOrder po = null;
		if (penum.hasMoreElements()) {
			po = ((PurchaseOrderLine) penum.nextElement()).getParentPurchaseOrder();
		}
		penum.destroy();
		return po;
	}

	private double getCurrentOpenPOQuantity(Product product) throws PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p1 = qh.attr(PurchaseOrderLine.STATUS).eq()
				.val(PurchaseOrderLineStatus.OPEN).predicate();
		QueryPredicate p2 = qh.attr(PurchaseOrderLine.STATUS).eq()
				.val(PurchaseOrderLineStatus.INCOMING).predicate();
		QueryPredicate p3 = qh.attr(PurchaseOrderLine.COMPANYPRODUCTNUMBER).eq()
				.val(product.getProductNumber()).predicate();
		qh.setClass(PurchaseOrderLine.class);
		qh.setDeepSelect(true);
		Condition cond = qh.condition((p1.or(p2)).and(p3));
		PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
		double openPOQuantity = 0.0D;
		while (penum.hasMoreElements()) {
			PurchaseOrderLine pol = (PurchaseOrderLine) penum.nextElement();
			openPOQuantity += pol.getOrderedQuantityOBU().getQuantityUnit().getAmount();
		}
		penum.destroy();
		return openPOQuantity;
	}
	
	private String[] getLastTwoSupplier(Product product,ProductInformationBean pib) throws PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p1 = qh.attr(PurchaseOrderLine.COMPANYPRODUCTNUMBER).eq()
				.val(product.getProductNumber()).predicate();
		QueryPredicate p2 = qh.attr(PurchaseOrderLine.PARENT_PURCHASEORDER_REF+PurchaseOrder.ORDERDATE).lte()
				.val(new PDate(2014, 8, 30)).predicate();
		qh.setClass(PurchaseOrderLine.class);
		qh.addDescendingOrdering(PurchaseOrderLine.CREATEDATE);
		qh.setDeepSelect(true);
		Condition cond = qh.condition(p1.and(p2));
		PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
		String[] supplierName=new String[2];
		if (penum.hasMoreElements()) {
			PurchaseOrderLine pol = (PurchaseOrderLine) penum.nextElement();
			pib.setLastOneSupplierOfPO(pol.getParentPurchaseOrder()
					.getSupplier().getName());
			pib.setLastPurchaseDateOfLastOneSupplier(pol
					.getParentPurchaseOrder().getOrderDate());

			pib.setLastOneSupplierProductNumber(pol
					.getSupplierProductNumber());
			
//			if (pib.getLastOneSupplierOfPO().equals("伍尔特（重庆）五金工具有限公司")
//					|| pib.getLastOneSupplierOfPO().equals("伍尔特（重庆）五金工具有限公司")
//					|| pib.getLastOneSupplierOfPO().equals("伍尔特（广州）国际贸易有限公司")) {
//				pib.setLastOneSupplierProductNumber(pol
//						.getSupplierProductNumber());
//
//			}

			if (penum.hasMoreElements()) {
				pol = (PurchaseOrderLine) penum.nextElement();
				pib.setLastTwoSupplierOfPO(pol.getParentPurchaseOrder()
						.getSupplier().getName());
				pib.setLastPurchaseDateOfLastTwoSupplier(pol
						.getParentPurchaseOrder().getOrderDate());
				pib.setLastTwoSupplierProductNumber(pol
						.getSupplierProductNumber());
//				if (pib.getLastTwoSupplierOfPO().equals("伍尔特（重庆）五金工具有限公司")
//						|| pib.getLastTwoSupplierOfPO().equals(
//								"伍尔特（重庆）五金工具有限公司")
//						|| pib.getLastTwoSupplierOfPO().equals(
//								"伍尔特（广州）国际贸易有限公司")) {
//					pib.setLastTwoSupplierProductNumber(pol
//							.getSupplierProductNumber());
//
//				}
			}
		}
		penum.destroy();
		return supplierName;
	}

	private double getCurrentOpenSOQuantity(Product product) throws PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p1 = qh.attr(CustomerOrderLine.PRODUCTNUMBER).eq()
				.val(product.getProductNumber()).predicate();
		QueryPredicate p2 = qh
				.attr(CustomerOrderLine.PARENT_CUSTOMERORDER_REF
						+ CustomerOrder.ORDERSTATUS).eq()
				.val(CustomerOrderStatus.PROCESSING).predicate();
		QueryPredicate p3 = qh
				.attr(CustomerOrderLine.PARENT_CUSTOMERORDER_REF
						+ CustomerOrder.ORDERSTATUS).eq()
				.val(CustomerOrderStatus.INWAREHOUSE).predicate();
		qh.setClass(CustomerOrderLine.class);
		qh.setDeepSelect(true);
		Condition cond = qh.condition(p1.and(p2.or(p3)));
		PEnumeration penum = _controller.createIteratorFactory().getCursorFetch(cond);
		double openCOQuantity = 0.0D;
		while (penum.hasMoreElements()) {
			CustomerOrderLine col = (CustomerOrderLine) penum.nextElement();
			openCOQuantity += col.getOrderquantity().getAmount();
		}
		penum.destroy();
		return openCOQuantity;
	}


	
	private Map<String, double[]> calculateSalesAndTurnoverBetweenDateRange(
			PDate fromDate, PDate toDate,
			Map<String, double[]> salesAndTurnoverMap)
			throws QueryParseException, PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate qp = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).gte()
				.val(fromDate).predicate();
		QueryPredicate qp1 = qh.attr(DWCustomerInvoiceLine.INVOICEDATE).lte()
				.val(toDate).predicate();
		qh.addResultAttribute(DWCustomerInvoiceLine.ASS_DWPRODUCT_REF
				+ DWProduct.PRODUCTNUMBER);
		qh.addResultAttributeAsSum(DWCustomerInvoiceLine.QUANTITYAMOUNT,
				"salesQuantity");
		qh.addResultAttributeAsSum(DWCustomerInvoiceLine.NETAMOUNTAMOUNT,
				"netAmount");
		qh.setGrouping(DWCustomerInvoiceLine.ASS_DWPRODUCT_REF
				+ DWProduct.PRODUCTNUMBER);
		qh.setDeepSelect(true);
		qp = qp.and(qp1);

		qh.setClass(DWCustomerInvoiceLine.class);
		Condition cond = qh.condition(qp);
		System.out.println(Query.getQueryString(cond));
		
		IteratorFactory fac = (IteratorFactory) _controller
				.createIteratorFactory();
		PEnumeration penum = fac.getCursor(cond);
		while (penum.hasMoreElements()) {
			double totalSalesQuantity = 0.0D;
			double totalSalesAmount = 0.0D;
			QueryResultEntry resultEntry = (QueryResultEntry) penum
					.nextElement();
			String productNumber = (String) resultEntry.get(0);
			if (resultEntry.get(1) != null) {
				totalSalesQuantity = (Double) resultEntry.get(1);
			}
			if (resultEntry.get(2) != null) {
				totalSalesAmount = (Double) resultEntry.get(2);
			}
			double[] amountArray = { totalSalesAmount, totalSalesQuantity };
			salesAndTurnoverMap.put(productNumber, amountArray);
		}
		penum.destroy();

		qh = Query.newQueryHelper();
		qp = qh.attr(DWRetoureLine.DATE).gte().val(fromDate).predicate();
		qp1 = qh.attr(DWRetoureLine.DATE).lte().val(toDate).predicate();
		qh.addResultAttribute(DWRetoureLine.ASS_DWPRODUCT_REF
				+ DWProduct.PRODUCTNUMBER);
		qh.addResultAttributeAsSum(DWRetoureLine.CCBRUTCREDITAMOUNTAMOUNT,
				"brutCreditAmount");
		qh.addResultAttributeAsSum(DWRetoureLine.CCDISCOUNTCREDITAMOUNTAMOUNT,
				"discountCreditAmount");
		qh.addResultAttributeAsSum(DWRetoureLine.QUANTITYRETURNEDAMOUNT,
				"returnedQuantity");
		qh.setGrouping(DWRetoureLine.ASS_DWPRODUCT_REF
				+ DWProduct.PRODUCTNUMBER);
		qh.setDeepSelect(true);
		qp = qp.and(qp1);

		qh.setClass(DWRetoureLine.class);
		cond = qh.condition(qp);
		fac = (IteratorFactory) _controller.createIteratorFactory();
		penum = fac.getCursor(cond);
		while (penum.hasMoreElements()) {
			double totalSalesQuantity = 0.0D;
			double totalSalesAmount = 0.0D;
			QueryResultEntry resultEntry = (QueryResultEntry) penum
					.nextElement();
			String productNumber = (String) resultEntry.get(0);
			if (resultEntry.get(1) != null) {
				totalSalesAmount -= (Double) resultEntry.get(1);
			}
			if (resultEntry.get(2) != null) {
				totalSalesAmount -= (Double) resultEntry.get(2);
			}
			if (resultEntry.get(3) != null) {
				totalSalesQuantity -= (Double) resultEntry.get(3);
			}
			double[] amountArray = salesAndTurnoverMap.get(productNumber);
			if (amountArray == null) {
				salesAndTurnoverMap.put(productNumber, amountArray);
			} else {
				amountArray[0] = amountArray[0] - totalSalesAmount;
				amountArray[1] = amountArray[1] - totalSalesQuantity;
			}
			salesAndTurnoverMap.put(productNumber, amountArray);
		}
		penum.destroy();
		return salesAndTurnoverMap;

	}

	private Map<String, Long> calculatePicksBetweenDateRange(int fromPeriod,
			int toPeriod, Map<String, Long> totalPicksMap)
			throws QueryParseException, PUserException {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate qp = qh.attr(Demand.DEMANDPERIOD).gte().val(fromPeriod)
				.predicate();
		QueryPredicate qp1 = qh.attr(Demand.DEMANDPERIOD).lte().val(toPeriod)
				.predicate();
		QueryPredicate qp2 = qh.attr(Demand.DEMANDTYPE).ne()
				.val(DemandType.SALES).predicate();
		qh.addResultAttribute(Demand.PARENT_WAREHOUSEPRODUCTALLOWANCE_REF
				+ WarehouseProductAllowance.PRODUCTNUMBER);
		qh.addResultAttributeAsSum(Demand.NUMBERACCESSES, "numberOfAccess");
		qh.setGrouping(Demand.PARENT_WAREHOUSEPRODUCTALLOWANCE_REF
				+ WarehouseProductAllowance.PRODUCTNUMBER);
		qh.setDeepSelect(true);
		qp = qp.and(qp1).and(qp2);

		qh.setClass(Demand.class);
		Condition cond = qh.condition(qp);
		IteratorFactory fac = (IteratorFactory) _controller
				.createIteratorFactory();
		PEnumeration penum = fac.getCursor(cond);
		while (penum.hasMoreElements()) {
			QueryResultEntry resultEntry = (QueryResultEntry) penum
					.nextElement();
			String productNumber = (String) resultEntry.get(0);
			if (resultEntry.get(1) != null) {
				totalPicksMap.put(productNumber, (Long) resultEntry.get(1));
			}
		}
		penum.destroy();
		return totalPicksMap;
	}

	private void writeProductInfoToExcel(int batch) {
		System.out.println("\n(!) Read excel file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(dataSource));
			XSSFSheet sheet = workbook.getSheetAt(0);
			System.out.println("sheet: "+sheet);
			int startRow = 2;
			for (int i=0;i<productInfoList.size();i++ ) {
				ProductInformationBean pib=productInfoList.get(i);
				XSSFRow row = sheet.getRow(startRow);
				if(row==null){
					row = sheet.createRow(startRow);
				}
				
				
				XSSFCell cellProductNumber = row.getCell(0);
				if(cellProductNumber==null){
					cellProductNumber=row.createCell(0);
				}
				XSSFCell cellPackagingSize = row.getCell(2);
				if(cellPackagingSize==null){
					cellPackagingSize=row.createCell(2);
				}
				
				XSSFCell cellProductDescriotionCN = row.getCell(3);
				if(cellProductDescriotionCN==null){
					cellProductDescriotionCN=row.createCell(3);
				}
				
				XSSFCell cellProductDescriotionEnOrDE = row.getCell(4);
				if(cellProductDescriotionEnOrDE==null){
					cellProductDescriotionEnOrDE=row.createCell(4);
				}
				
				XSSFCell cellUnitCost = row.getCell(5);
				if(cellUnitCost==null){
					cellUnitCost=row.createCell(5);
				}
				
				XSSFCell cellUnit = row.getCell(6);
				if(cellUnit==null){
					cellUnit=row.createCell(6);
				}
				
				XSSFCell cellCurrentStockQuantity = row.getCell(7);
				if(cellCurrentStockQuantity==null){
					cellCurrentStockQuantity=row.createCell(7);
				}
				
				XSSFCell cellStockValue = row.getCell(8);
				if(cellStockValue==null){
					cellStockValue=row.createCell(8);
				}
				
				XSSFCell cellCreationDate = row.getCell(9);
				if(cellCreationDate==null){
					cellCreationDate=row.createCell(9);
				}
				
				XSSFCell cellproductStatus = row.getCell(10);
				if(cellproductStatus==null){
					cellproductStatus=row.createCell(10);
				}
				
				XSSFCell cellgrossWeight = row.getCell(11);
				if(cellgrossWeight==null){
					cellgrossWeight=row.createCell(11);
				}
				
				XSSFCell cellWeightUnit = row.getCell(12);
				if(cellWeightUnit==null){
					cellWeightUnit=row.createCell(12);
				}
				
				XSSFCell cellMainSupplierName = row.getCell(13);
				if(cellMainSupplierName==null){
					cellMainSupplierName=row.createCell(13);
				}
				
				
				XSSFCell cellMainSupplierProductNumber = row.getCell(14);
				if(cellMainSupplierProductNumber==null){
					cellMainSupplierProductNumber=row.createCell(14);
				}
				
				XSSFCell cellMainSupplierLastPurchaseDate = row.getCell(15);
				if(cellMainSupplierLastPurchaseDate==null){
					cellMainSupplierLastPurchaseDate=row.createCell(15);
				}
				
				XSSFCell cellCurrentOpenPOQuantity = row.getCell(19);
				if(cellCurrentOpenPOQuantity==null){
					cellCurrentOpenPOQuantity=row.createCell(19);
				}
				
				XSSFCell cellCurrentOpenSOQuantity = row.getCell(20);
				if(cellCurrentOpenSOQuantity==null){
					cellCurrentOpenSOQuantity=row.createCell(20);
				}
				
				XSSFCell cellSalesQuantity2YearsAndCurrentYear = row.getCell(21);
				if(cellSalesQuantity2YearsAndCurrentYear==null){
					cellSalesQuantity2YearsAndCurrentYear=row.createCell(21);
				}
				XSSFCell cellSalesTurnover2YearsAndCurrentYear = row.getCell(22);
				if(cellSalesTurnover2YearsAndCurrentYear==null){
					cellSalesTurnover2YearsAndCurrentYear=row.createCell(22);
				}
				XSSFCell cellPicks2YearsAndCurrentYear = row.getCell(23);
				if(cellPicks2YearsAndCurrentYear==null){
					cellPicks2YearsAndCurrentYear=row.createCell(23);
				}
				
				XSSFCell cellSalesQuantityLatestYear= row.getCell(24);
				if(cellSalesQuantityLatestYear==null){
					cellSalesQuantityLatestYear=row.createCell(24);
				}
				XSSFCell cellSalesTurnoverLatestYear = row.getCell(25);
				if(cellSalesTurnoverLatestYear==null){
					cellSalesTurnoverLatestYear=row.createCell(25);
				}
				XSSFCell cellPicksLatestYear = row.getCell(26);
				if(cellPicksLatestYear==null){
					cellPicksLatestYear=row.createCell(26);
				}
				
				XSSFCell cellLastOneSupplierOfPO = row.getCell(27);
				if(cellLastOneSupplierOfPO==null){
					cellLastOneSupplierOfPO=row.createCell(27);
				}
				XSSFCell cellLastPurchaseDateOfLastOneSupplier = row.getCell(28);
				if(cellLastPurchaseDateOfLastOneSupplier==null){
					cellLastPurchaseDateOfLastOneSupplier=row.createCell(28);
				}
				
				XSSFCell cellLastOneSupplierProductNumber = row.getCell(29);
				if(cellLastOneSupplierProductNumber==null){
					cellLastOneSupplierProductNumber=row.createCell(29);
				}
				
				XSSFCell cellLastTwoSupplierOfPO = row.getCell(30);
				if(cellLastTwoSupplierOfPO==null){
					cellLastTwoSupplierOfPO=row.createCell(30);
				}
				XSSFCell cellLastPurchaseDateOfLastTwoSupplier = row.getCell(31);
				if(cellLastPurchaseDateOfLastTwoSupplier==null){
					cellLastPurchaseDateOfLastTwoSupplier=row.createCell(31);
				}
				
				XSSFCell cellLastTwoSupplierProductNumber = row.getCell(32);
				if(cellLastTwoSupplierProductNumber==null){
					cellLastTwoSupplierProductNumber=row.createCell(32);
				}
				
				
				cellProductNumber.setCellValue(new XSSFRichTextString(pib
						.getProductNumber()));
				cellPackagingSize.setCellValue(pib.getPackingSize());
				cellProductDescriotionCN.setCellValue(new XSSFRichTextString(pib
						.getProductDescriptionCN()));
				cellProductDescriotionEnOrDE.setCellValue(new XSSFRichTextString(pib.getProductDescriptionENOrDE()));
				cellUnitCost.setCellValue(pib.getUnitCost());
				cellUnit.setCellValue(pib.getUnit());
				cellCurrentStockQuantity.setCellValue(pib.getCurrentStockQuantity());
				cellStockValue.setCellValue(pib.getStockValue());
				cellCreationDate.setCellValue(new XSSFRichTextString(dateFormat.format(pib.getCreationDate())));
				cellproductStatus.setCellValue(pib.getProductStatus());
				cellgrossWeight.setCellValue(pib.getGrossWeight());
				cellWeightUnit.setCellValue(pib.getWeightUnit());
				cellMainSupplierName.setCellValue(new XSSFRichTextString(pib.getMainSupplierName()));
				cellMainSupplierProductNumber.setCellValue(new XSSFRichTextString(pib.getMainSupplierProductNumber()));
				if(pib.getMainSupplierLastPurchaseDate()!=null){
					cellMainSupplierLastPurchaseDate.setCellValue(new XSSFRichTextString(dateFormat.format(pib.getMainSupplierLastPurchaseDate())));
				}
				cellCurrentOpenPOQuantity.setCellValue(pib.getCurrentOpenPOQuantity());
				cellCurrentOpenSOQuantity.setCellValue(pib.getCurrentOpenSOQuantity());
				
				cellSalesQuantity2YearsAndCurrentYear.setCellValue(pib.getSalesQuantity2YearsAndCurrentYear());
				cellSalesTurnover2YearsAndCurrentYear.setCellValue(pib.getSalesTurnover2YearsAndCurrentYear());
				cellPicks2YearsAndCurrentYear.setCellValue(pib.getPicks2YearsAndCurrentYear());
				cellSalesQuantityLatestYear.setCellValue(pib.getSalesQuantityLatestYear());
				cellSalesTurnoverLatestYear.setCellValue(pib.getSalesTurnoverLatestYear());
				cellPicksLatestYear.setCellValue(pib.getPicksLatestYear());
				
				cellLastOneSupplierOfPO.setCellValue(new XSSFRichTextString(pib
						.getLastOneSupplierOfPO()));

				if (pib.getLastPurchaseDateOfLastOneSupplier() != null) {
					cellLastPurchaseDateOfLastOneSupplier
							.setCellValue(new XSSFRichTextString(
									dateFormat.format(pib
											.getLastPurchaseDateOfLastOneSupplier())));
				}

				cellLastOneSupplierProductNumber.setCellValue(new XSSFRichTextString(pib
						.getLastOneSupplierProductNumber()));
				cellLastTwoSupplierOfPO.setCellValue(new XSSFRichTextString(pib
						.getLastTwoSupplierOfPO()));

				if (pib.getLastPurchaseDateOfLastTwoSupplier() != null) {
					cellLastPurchaseDateOfLastTwoSupplier
							.setCellValue(new XSSFRichTextString(
									dateFormat.format(pib
											.getLastPurchaseDateOfLastTwoSupplier())));
				}
				cellLastTwoSupplierProductNumber.setCellValue(new XSSFRichTextString(pib
						.getLastTwoSupplierProductNumber()));
				
				startRow++;
				System.out.println("\n Number of products writed: "+(i+1));
				productInfoList.set(i, null);
			}
			String fileToBeWrite = "../../var/exportSAP/DC_Material-"+batch+".xlsx";
			FileOutputStream fos = new FileOutputStream(fileToBeWrite);
			BufferedOutputStream bout = new BufferedOutputStream(fos);
			workbook.write(bout);
			bout.flush();
			bout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n(*) Read excel file  end.");

	}

}
