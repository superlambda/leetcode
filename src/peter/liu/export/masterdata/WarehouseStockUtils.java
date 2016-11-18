package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.wuerth.phoenix.Phxbasic.models.Product;
import com.wuerth.phoenix.Phxbasic.models.Quant;
import com.wuerth.phoenix.Phxbasic.models.Warehouse;
import com.wuerth.phoenix.Phxbasic.models.WarehouseLocation;
import com.wuerth.phoenix.Phxbasic.models.WarehouseProductAllowance;
import com.wuerth.phoenix.basic.etnax.common.utilsdir.QuantityUtils;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.util.quantityunit.QuantityUnit;

/**
 * WarehouseStockUtils
 * 
 * @author pcnsh197
 * 
 */
public class WarehouseStockUtils {

	public static List<WarehouseStockBean> getSortedWarehouseStockBeanList(
			List<Quant> quantList) throws PUserException {
		HashMap<String, WarehouseStockBean> mapSQ = getWarehouseStockBeanMap(quantList);
		List<WarehouseStockBean> WarehouseStockBeanList = getWarehouseStockBeanList(mapSQ);
		Collections.sort(WarehouseStockBeanList,
				new WarehouseStockBeanComparator());
		return WarehouseStockBeanList;
	}

	private static List<WarehouseStockBean> getWarehouseStockBeanList(
			HashMap<String, WarehouseStockBean> hashMap) {
		List<WarehouseStockBean> WarehouseStockBeanList = new ArrayList<WarehouseStockBean>();
		Object[] object = hashMap.values().toArray();
		for (Object element : object) {
			WarehouseStockBeanList.add((WarehouseStockBean) element);
		}
		return WarehouseStockBeanList;
	}

	private static HashMap<String, WarehouseStockBean> getWarehouseStockBeanMap(
			List<Quant> quantList) throws PUserException {
		HashMap<String, WarehouseStockBean> warehouseStockBeanMap = new HashMap<String, WarehouseStockBean>();
		Product product;
		Warehouse warehouse;
		WarehouseProductAllowance wpa;
		WarehouseStockBean warehouseStockBean;
		for (Quant quant : quantList) {
			wpa = quant.getParentWarehouseProductAllowance();
			if (wpa == null) {
				continue;
			}
			warehouse = wpa.getWarehouse();
			product = wpa.getParentProduct();
			warehouseStockBean = new WarehouseStockBean();
			WarehouseLocation wl = quant.getWarehouseLocation();
			String key = product.getProductNumber() + "_"
					+ warehouse.getNumber() + "_" + wl.getFullLocationName();

			QuantityUnit physicalStock = QuantityUtils.createQuantityUnit(
					product, quant.getPhysicalStock().getAmount());

			warehouseStockBean.setWarehouseNumber(warehouse.getNumber());
			warehouseStockBean.setPlant(warehouse.getNumber());
			warehouseStockBean.setWarehouseLocation(wl.getFullLocationName());
			warehouseStockBean.setStock(physicalStock.getAmount());
			warehouseStockBean.setProductNumber(product.getProductNumber());
			warehouseStockBean.setStorageType(wl.getLogicalZoneType()
					.getDescription());
			wl.getLogicalZoneType().getDescription();
			if (!warehouseStockBeanMap.containsKey(key)) {
				warehouseStockBeanMap.put(key, warehouseStockBean);
			} else {
				updateHashMap(warehouseStockBeanMap, warehouseStockBean, key,
						product);
			}
		}
		return warehouseStockBeanMap;
	}

	/**
	 * Method upDateHashMap
	 * 
	 * @param hashMap
	 *            HashMap
	 * @param newWarehouseStockBean
	 *            WarehouseStockBean
	 * @param key
	 *            List
	 * @param product
	 *            Product
	 */
	private static void updateHashMap(
			HashMap<String, WarehouseStockBean> hashMap,
			WarehouseStockBean newWarehouseStockBean, String key,
			Product product) {
		WarehouseStockBean warehouseStockBean = hashMap.get(key);
		// add
		double stock = warehouseStockBean.getStock()
				+ newWarehouseStockBean.getStock();
		// update the old WarehouseStockBean
		warehouseStockBean.setStock(stock);
	}

	private static class WarehouseStockBeanComparator implements
			Comparator<WarehouseStockBean> {
		public int compare(WarehouseStockBean sq1, WarehouseStockBean sq2) {
			String warehouse1 = sq1.getWarehouseNumber();
			String warehouse2 = sq2.getWarehouseNumber();
			if (warehouse1.equals(warehouse2)) {
				String fullname1 = sq1.getWarehouseLocation();
				String fullname2 = sq2.getWarehouseLocation();
				if (fullname1.equals(fullname2)) {
					String product0 = sq1.getProductNumber();
					String product1 = sq2.getProductNumber();
					return product0.compareTo(product1);
				}
				return fullname1.compareTo(fullname2);
			}
			return warehouse1.compareTo(warehouse2);
		}
	}

}
