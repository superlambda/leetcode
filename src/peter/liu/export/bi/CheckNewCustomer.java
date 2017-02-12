package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.IOException;

import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;

/**
 * StorageLocationExport
 * 
 * @author pcnsh197
 * 
 */
public class CheckNewCustomer extends BatchRunner {


	@Override
	protected void batchMethod() throws TimestampException, PUserException,
			IOException {
		checkNewCustomer();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CheckNewCustomer().startBatch(args);
	}

	private void checkNewCustomer() throws TimestampException, PUserException,
			IOException {
//		BIDateMapping.newCustomer2011Set.size();
	}

	
}
