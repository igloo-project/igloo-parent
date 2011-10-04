package fr.openwide.export.excel.example.export;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import fr.openwide.core.export.excel.AbstractExcelTableExport;
import fr.openwide.export.excel.example.person.Person;

public class PersonHSSFExport extends AbstractExcelTableExport {

	public PersonHSSFExport() {
		super(new HSSFWorkbook());

		// C'est entre l'appel au constructeur et le init() qu'il faut
		// surcharger les couleurs, les polices et les formats de données
		setFontName("Calibri");
		setNormalFontHeight((short) 10);
		setHeaderFontHeight((short) 12);
		setHeaderFontColor("#FFFFFF");

		init();
	}

	public HSSFWorkbook generate(List<Person> persons, List<String> columns) {
		HSSFSheet sheet = (HSSFSheet) createSheet("Document .xls");

		int rowIndex = 0;

		addHeadersToSheet(sheet, rowIndex, columns);
		rowIndex++;

		for (Person person : persons) {
			HSSFRow currentRow = sheet.createRow(rowIndex);
			rowIndex++;

			int columnIndex = 0;

			for (String column : columns) {
				addCell(currentRow, columnIndex, person, column);
				columnIndex++;
			}
		}

		finalizeSheet(sheet, columns);

		return (HSSFWorkbook) workbook;
	}

	private void addCell(HSSFRow currentRow, int columnIndex, Person person,
			String column) {
		if ("username".equals(column)) {
			addTextCell(currentRow, columnIndex, person.getUserName());
		} else if ("firstname".equals(column)) {
			addTextCell(currentRow, columnIndex, person.getFirstName());
		} else if ("lastname".equals(column)) {
			addTextCell(currentRow, columnIndex, person.getLastName());
		} else if ("birth date".equals(column)) {
			addDateCell(currentRow, columnIndex, person.getBirthDate());
		} else if ("birth hour".equals(column)) {
			addDateTimeCell(currentRow, columnIndex, person.getBirthDate());
		} else if ("size".equals(column)) {
			addIntegerCell(currentRow, columnIndex, person.getSize());
		} else if ("percentage".equals(column)) {
			addPercentCell(currentRow, columnIndex, person.getPercentage());
		}
	}

	@Override
	protected String getLocalizedLabel(String key) {
		return key;
	}
}
