package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.models.AbstractLocation;
import com.wuerth.phoenix.Phxbasic.models.CompoundLocation;
import com.wuerth.phoenix.Phxbasic.models.WarehouseLocation;
import com.wuerth.phoenix.basic.etnax.exception.ProcessException;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;

/**
 * BIDateMapping
 * 
 * @author pcnsh197
 * 
 */
public class LocationWarehouseMapping extends BatchRunner {

	private static String	locationMappingdataSource	= "../../etc/exportSAP/locationssearch.xlsx";

	private String			target						= "../../var/exportSAP/locationssearch.xlsx";

	@Override
	protected void batchMethod() throws TimestampException, PUserException {
		readLocationMappingFromExcel();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new LocationWarehouseMapping().startBatch(args);
	}

	private void readLocationMappingFromExcel() {
		System.out.println("\n(!) Read locations mapping file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					locationMappingdataSource));
			XSSFSheet sheet = workbook.getSheetAt(0);
			int startRow = 1;
			XSSFRow row = sheet.getRow(startRow);
			int productMappingLoaded = 0;
			while (row != null) {
				XSSFCell cellLocation = row.getCell(0);
				if (cellLocation == null) {
					break;
				}

				String locationName = cellLocation.getRichStringCellValue()
						.getString();
				if (locationName == null || "".equals(locationName.trim())) {
					break;
				}

				XSSFCell cellWarehouse = row.getCell(1);
				if (cellWarehouse == null) {
					cellWarehouse = row.createCell(1);
				}
				AbstractLocation location = queryWarehouseLocation(locationName);
				if (location != null) {
					if (location instanceof WarehouseLocation) {
						cellWarehouse
								.setCellValue(((WarehouseLocation) location)
										.getWarehouseNumber());
					} else {
						cellWarehouse
								.setCellValue(((CompoundLocation) location)
										.getParentWarehouse().getNumber());
					}
				} else {
					System.out
							.println("\n(!) WARNING location can not be found: "
									+ locationName);
				}
				
				productMappingLoaded++;
				System.out.println("\n(!) " + sheet.getSheetName()
						+ " locations loaded: " + productMappingLoaded);
				startRow++;
				row = sheet.getRow(startRow);
			}

			FileOutputStream fos = new FileOutputStream(target);
			BufferedOutputStream bout = new BufferedOutputStream(fos);
			workbook.write(bout);
			bout.flush();
			bout.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n(*) Read locations mapping file  end.");

	}

	private AbstractLocation queryWarehouseLocation(String warehouseLocation) {
		QueryHelper qh = Query.newQueryHelper();
		QueryPredicate p = null;
		p = qh.attr(AbstractLocation.FULLLOCATIONNAME).eq()
				.val(warehouseLocation).predicate();
		qh.setDeepSelect(true);
		qh.setClass(AbstractLocation.class);
		Condition cond = qh.condition(p);

		AbstractLocation location = null;
		try {
			PEnumeration penum = _controller.createIteratorFactory().getCursor(
					cond);
			if (penum.hasMoreElements()) {
				location = (AbstractLocation) penum.nextElement();
				penum.destroy();
			}
		} catch (PUserException e) {
			ProcessException.getIntance().process(e);
		}
		return location;
	}

}
