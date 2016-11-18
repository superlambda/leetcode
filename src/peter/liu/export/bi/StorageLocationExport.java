package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.wuerth.phoenix.Phxbasic.models.WarehouseLocation;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;

/**
 * StorageLocationExport
 * 
 * @author pcnsh197
 * 
 */
public class StorageLocationExport extends BatchRunner {

	private int						NUMBER_OF_BATCH_TO_FECTCH	= 1000;

	LinkedList<StockLocationBean>	locationInfoList			= new LinkedList<StockLocationBean>();

	private String					targetTxt					= "../../var/exportSAP/MappingStorageLocationW-TJ.txt";

	private PrintWriter				out1						= null;

	private int						numberLocationssWritten		= 0;

	private String					csvSeperator				= ",";

	@Override
	protected void batchMethod() throws TimestampException, PUserException,
			IOException {
		exportOrder();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new StorageLocationExport().startBatch(args);
	}

	private void exportOrder() throws TimestampException, PUserException,
			IOException {
		FileWriter outFile = new FileWriter(targetTxt);
		out1 = new PrintWriter(outFile);
		System.out.println("\n(!) Write txt file  start.\n");
		searchCustomerOrder();
		out1.close();
		System.out.println("\n(*) Write txt file  end.");
	}

	private void searchCustomerOrder() throws TimestampException,
			PUserException {
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(WarehouseLocation.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(WarehouseLocation.WAREHOUSENUMBER);
		qh.addAscendingOrdering(WarehouseLocation.FULLLOCATIONNAME);
		Condition cond = qh.condition();
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<WarehouseLocation> list = new ArrayList<WarehouseLocation>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			locationInfoList = new LinkedList<StockLocationBean>();
			for (WarehouseLocation location : list) {
				StockLocationBean slb = new StockLocationBean();
				// slb.setCompanyId(companyId)
				slb.setWarehouseNumber(location.getWarehouseNumber());
				slb.setStorageLocation(location.getFullLocationName());
				slb.setDescription(location.getLogicalZoneType()
						.getDescription());
				locationInfoList.addLast(slb);
			}
			writeOrderInfoToTxt();
			_context.commit();
		}
		penum.destroy();
	}

	private void writeOrderInfoToTxt() {
		for (int i = 0; i < locationInfoList.size(); i++) {
			StockLocationBean slb = locationInfoList.get(i);
			StringBuffer sb = new StringBuffer();
			sb.append(slb.getWarehouseNumber()).append(csvSeperator);
			sb.append(slb.getStorageLocation().replace("-", "")).append(
					csvSeperator);
			sb.append(slb.getDescription());
			out1.println(sb.toString());
			numberLocationssWritten++;
			System.out.println("\n Number of locations writed in txt 1: "
					+ numberLocationssWritten);
		}
	}
}
