/**
 * 
 */
package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;



/**
 * @author pcnsh222
 *
 */
public class CustomerExportBatchPart extends AbstractCustomerExport{
	private void insert() {
		insertSKNA1();
		insertSKNKK();
		insertSKNVK();
		insertSKNBK();
		insertSKNB1();
		insertSKNB5();
		insertSKNVV_PartA();
		insertSKNVV_PartB();
//		insertSKNVP();
	}
	/**
	 * @param args
	 */
	public void startBatch() {
		init();
		export(null);
		commit();
		insert();
		udpateSKNVP();
	}

}
