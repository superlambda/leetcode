package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.enums.ProductStatus;
import com.wuerth.phoenix.Phxbasic.models.Product;
import com.wuerth.phoenix.Phxbasic.models.StatusChangeLog;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi.BIDateMapping;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.util.PDate;

/**
 * ProductCreationDateExport
 * 
 * @author pcnsh197
 * 
 */
public class ProductCreationDateExport extends BatchRunner {

	private String				dataSource				= "../../etc/exportSAP/productcreationdate.xlsx";

	private String				target					= "../../var/exportSAP/productcreationdate.xlsx";

	private Map<String, PDate>	productCreationDateMap	= new HashMap<String, PDate>();

	@Override
	protected void batchMethod() throws TimestampException, PUserException {
		searchProductCreateionDate();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ProductCreationDateExport().startBatch(args);
	}

	private void searchProductCreateionDate() throws TimestampException,
			PUserException {

		for (String productNumber : BIDateMapping.productMap.keySet()) {
			Product product = _controller.lookupProduct(productNumber);
			String sapProductNumber = BIDateMapping.productMap
					.get(productNumber);
			if (product == null) {
				System.out.println("\n(!) product can not be found: "
						+ productNumber);
				continue;
			}
			PDate newToActiveDate = null;
			List<StatusChangeLog> changeList = new ArrayList<StatusChangeLog>(
					product.getAllChildStatusChangeLog());
			for (StatusChangeLog statusChangeLog : changeList) {
				if (statusChangeLog.getOldStatus().equals(ProductStatus.NEW)
						&& statusChangeLog.getNewStatus().equals(
								ProductStatus.ACTIVE)) {
					newToActiveDate = statusChangeLog.getChangeTime();
				}
			}

			if (!productCreationDateMap.containsKey(sapProductNumber)
					|| productCreationDateMap.get(sapProductNumber) == null) {
				productCreationDateMap.put(sapProductNumber, newToActiveDate);
			} else if (newToActiveDate != null
					&& productCreationDateMap.get(sapProductNumber).after(
							newToActiveDate)) {
				productCreationDateMap.put(sapProductNumber, newToActiveDate);
				System.out.println("\n(!) find old creation date: "
						+ sapProductNumber + " " + newToActiveDate);
			}

		}
		writeCustomerInfoToExcel();
	}

	private void writeCustomerInfoToExcel() {
		System.out.println("\n(!) Read excel file  start.\n");
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					dataSource));
			XSSFSheet sheet = workbook.getSheetAt(0);
			System.out.println("sheet1: " + sheet);
			int startRow = 1;
			for (String sapProductNumber : productCreationDateMap.keySet()) {
				PDate creationDate = productCreationDateMap
						.get(sapProductNumber);
				XSSFRow row = sheet.getRow(startRow);
				if (row == null) {
					row = sheet.createRow(startRow);
				}

				XSSFCell cellSapProductNumber = row.getCell(0);
				if (cellSapProductNumber == null) {
					cellSapProductNumber = row.createCell(0);
				}
				XSSFCell cellCreationDate = row.getCell(1);
				if (cellCreationDate == null) {
					cellCreationDate = row.createCell(1);
				}

				cellSapProductNumber.setCellValue(sapProductNumber);
				if(creationDate!=null){
					cellCreationDate.setCellValue(new XSSFRichTextString(
							BIDateMapping.dateFormat.format(creationDate)));
				}
			

				startRow++;
				System.out.println("\n Number of products writed in sheet 1: "
						+ (startRow - 1));
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
		System.out.println("\n(*) Read excel file  end.");

	}

}
