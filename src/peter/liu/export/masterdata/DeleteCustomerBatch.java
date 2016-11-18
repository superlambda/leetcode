package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.enums.CustomerAccountStatus;
import com.wuerth.phoenix.Phxbasic.models.CustomerAccount;
import com.wuerth.phoenix.Phxbasic.models.PhxbasicController;
import com.wuerth.phoenix.basic.etnax.sap.webservice.service.SylvestrixHelper;

public class DeleteCustomerBatch {
	private PhxbasicController _controller;
	private String dataSource = "../../etc/exportSAP/deleteCustomer.xlsx";
	private String target = "../../var/exportSAP/deleteCustomer_out.xlsx";
	private String rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static void main(String[] args) {
		DeleteCustomerBatch batch = new DeleteCustomerBatch();
		batch.init();
		batch.deleteFromExcel();
		batch.commit();
	}
	
	private void init() {
		_controller = SylvestrixHelper.get().getController();
		System.out.println("inited");
	}
	
	private void commit() {
		SylvestrixHelper.get().commit();
		System.out.println("commited");
	}
	
	public int column(String s) {

		if (s.length() == 1) {
			return rows.indexOf(s);
		}

		return 26 * (rows.indexOf(s.substring(0, 1)) + 1)
				+ column(s.substring(1, s.length()));
	}
	
	private void deleteFromExcel() {
		try {
			FileOutputStream fos = new FileOutputStream(target);
			Workbook twb = new XSSFWorkbook(new FileInputStream(dataSource));
			Sheet sheet = twb.getSheet("待删除客户");
			int lastRowNum = sheet.getLastRowNum() + 1;
			for (int i = 1; i < lastRowNum; i++) {
				Row row = sheet.getRow(i);
				Cell customerNumber_cell = row.getCell(column("A"));
				customerNumber_cell.setCellType(Cell.CELL_TYPE_STRING);
				String customerNumber_S = customerNumber_cell.getStringCellValue();
				int custAccount = Integer.valueOf(customerNumber_S);
				CustomerAccount ca = _controller.lookupCustomerAccount(custAccount);
				if(ca == null) {
					Cell cell = row.createCell(4);
					cell.setCellValue("Null");
					continue;
				}
				ca.setStatus(CustomerAccountStatus.DELETED);
			}
			twb.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
