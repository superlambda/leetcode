package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuerth.phoenix.Phxbasic.enums.SalesmanStatus;
import com.wuerth.phoenix.Phxbasic.models.CompoundSalesStructureElement;
import com.wuerth.phoenix.Phxbasic.models.SalesStructureElement;
import com.wuerth.phoenix.Phxbasic.models.Salesman;
import com.wuerth.phoenix.basic.etnax.utilities.batch.BatchRunner;
import com.wuerth.phoenix.bcutil.HookException;
import com.wuerth.phoenix.bcutil.PEnumeration;
import com.wuerth.phoenix.bcutil.PUserException;
import com.wuerth.phoenix.bcutil.TimestampException;
import com.wuerth.phoenix.bcutil.query.Condition;
import com.wuerth.phoenix.bcutil.query.Query;
import com.wuerth.phoenix.bcutil.query.QueryHelper;
import com.wuerth.phoenix.bcutil.query.QueryPredicate;
import com.wuerth.phoenix.util.PDate;

public class SalesmanExport extends BatchRunner {
	private int				NUMBER_OF_BATCH_TO_FECTCH	= 100;

	LinkedList<Salesman>	salesmanList				= new LinkedList<Salesman>();

	private String			dataSource					= "../../etc/exportSAP/Salesman.xlsx";

	private String			target						= "../../var/exportSAP/Salesman.xlsx";

	@Override
	protected void batchMethod() throws TimestampException, PUserException {
		searchSalesman();
		System.out.println("\n[OK]");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SalesmanExport().startBatch(args);
	}

	private void searchSalesman() throws TimestampException, PUserException {
		QueryHelper qh = Query.newQueryHelper();
		qh.setClass(Salesman.class);
		qh.setDeepSelect(true);
		qh.addAscendingOrdering(Salesman.REGISTERNUMBER);
		QueryPredicate qp = qh.attr(Salesman.SALESMANSTATUS).eq()
				.val(SalesmanStatus.ACTIVE).predicate();
		QueryPredicate qp2 = qh.attr(Salesman.SALESMANSTATUS).eq()
				.val(SalesmanStatus.NEW).predicate();
		Condition cond = qh.condition(qp.or(qp2));
		PEnumeration penum = _controller.createIteratorFactory()
				.getCursorFetch(cond);
		while (penum.hasMoreElements()) {
			List<Salesman> list = new ArrayList<Salesman>(
					penum.nextBatch(NUMBER_OF_BATCH_TO_FECTCH));
			salesmanList.addAll(list);
		}
		penum.destroy();
		writeSalesmanInfoToExcel();
	}

	private void writeSalesmanInfoToExcel() throws HookException,
			PUserException {
		System.out.println("\n(!) Read excel file  start.\n");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					dataSource));
			XSSFSheet sheet = workbook.getSheetAt(0);
			System.out.println("sheet1: " + sheet);
			int startRow = 1;
			for (int i = 0; i < salesmanList.size(); i++) {
				Salesman salesman = salesmanList.get(i);
				XSSFRow row = sheet.getRow(startRow);
				if (row == null) {
					row = sheet.createRow(startRow);
				}
				XSSFCell cellRegisterNumber = row.getCell(0);
				if (cellRegisterNumber == null) {
					cellRegisterNumber = row.createCell(0);
				}

				XSSFCell cellChineseName = row.getCell(1);
				if (cellChineseName == null) {
					cellChineseName = row.createCell(1);
				}

				XSSFCell cellEnglishName = row.getCell(2);
				if (cellEnglishName == null) {
					cellEnglishName = row.createCell(2);
				}

				XSSFCell cellSalesarea = row.getCell(3);
				if (cellSalesarea == null) {
					cellSalesarea = row.createCell(3);
				}

				cellRegisterNumber.setCellValue(salesman.getRegisterNumber());
				String[] names = salesman.getName().split(" ");
				cellChineseName.setCellValue(names[0]);
				cellEnglishName.setCellValue(names[1]);
				List<SalesStructureElement> sses = new ArrayList<SalesStructureElement>(
						salesman.getAllSalesStructureElements(new PDate()));

				if (sses.size() > 0) {
					for (SalesStructureElement sse : sses) {
						CompoundSalesStructureElement csse = sse
								.getParentCompoundSalesStructureElement();
						while (csse != null
								&& csse.getSalesDimension().getNumber() != 1) {
							csse = csse
									.getParentCompoundSalesStructureElement();
						}
						if (csse != null) {
							cellSalesarea.setCellValue(csse.getDescription());
							break;
						}
					}
				}
				startRow++;
				System.out.println("\n Number of salesmen writed: " + (i + 1));
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
