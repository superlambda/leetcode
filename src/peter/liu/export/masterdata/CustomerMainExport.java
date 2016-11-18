package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.IOException;

import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;

/**
 * CustomerMainExport
 * 
 * @author pcnsh197
 * 
 */
public class CustomerMainExport extends BatchRunner {

	private static String ownCompanyName= null;
	
	@Override
	protected void batchMethod() throws TimestampException, PUserException,
			IOException {
		ownCompanyName= _controller.getSingletonOwnCompany().getName();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CustomerMainExport().startBatch(args);
		new CustomerExportBatchPart().startBatch();
		new CustomerExportBatch().startBatch();
//		if (ownCompanyName.equals("伍尔特（天津）国际贸易有限公司")) {
//			new CustomerExportBatchBJPart().startBatch();
//			new CustomerExportBatchBJ().startBatch();
//			new CustomerExportBatchQDPart().startBatch();
//			new CustomerExportBatchQD().startBatch();
//
//		}
	}
}
