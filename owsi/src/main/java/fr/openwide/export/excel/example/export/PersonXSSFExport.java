package fr.openwide.export.excel.example.export;

import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.openwide.core.export.excel.AbstractExcelTableExport;
import fr.openwide.export.excel.example.person.Person;

public class PersonXSSFExport extends AbstractExcelTableExport {

	private static final String STYLE_TITLE_NAME = "titleStyle";
	private static final String FONT_TITLE_NAME = "titleFont";
	private static final String COLOR_TITLE_HEXA = "#007CAF";
	private static final short COLOR_TITLE_INDEX = 80;

	public PersonXSSFExport() {
		super(new XSSFWorkbook());

		// C'est entre l'appel au constructeur et le init() qu'il faut
		// surcharger les couleurs, les polices et les formats de données
		setFontName("Calibri");
		setNormalFontHeight((short) 10);
		setHeaderFontHeight((short) 12);
		setHeaderFontColor("#067CAF");
		setHeaderBackgroundColor("#BBD9EE");
		
		init();
		
		registerCustomStyles();
	}

	public XSSFWorkbook generate(List<Person> persons, List<String> columns) {
		XSSFSheet sheet = (XSSFSheet) createSheet("Document .xlsx");

		addTitleCell(sheet);

		int rowIndex = 1;
		
		addHeadersToSheet(sheet, rowIndex, columns);
		rowIndex++;

		for (Person person : persons) {
			XSSFRow currentRow = sheet.createRow(rowIndex);
			rowIndex++;

			int columnIndex = 0;

			for (String column : columns) {
				addCell(currentRow, columnIndex, person, column);
				columnIndex++;
			}
		}

		finalizeSheet(sheet, columns);

		return (XSSFWorkbook) workbook;
	}

	private void addCell(XSSFRow currentRow, int columnIndex, Person person, String column) {
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
			addNumericCell(currentRow, columnIndex, person.getSize());
		} else if ("percentage".equals(column)) {
			addPercentCell(currentRow, columnIndex, person.getPercentage());
		}
	}
	
	/**
	 * Exemple d'ajout d'une cellule de titre composée de plusieurs cellules
	 * fusionnées.
	 * 
	 * @param sheet
	 * @return
	 */
	private Cell addTitleCell(Sheet sheet) {
		CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, 6);
		sheet.addMergedRegion(mergedRegion);
		
		Row titleRow = sheet.createRow(0);
		
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellStyle(getStyle(STYLE_TITLE_NAME));
		titleCell.setCellType(Cell.CELL_TYPE_STRING);
		titleCell.setCellValue(creationHelper.createRichTextString("Title of the document"));
 
		return titleCell;
	}
	
	private void registerCustomStyles() {
		Font titleFont = workbook.createFont();
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		setFontColor(titleFont, colorRegistry, HSSFColor.WHITE.index);
		titleFont.setFontHeightInPoints((short) 18);
		registerFont(FONT_TITLE_NAME, titleFont);

		registerColor(COLOR_TITLE_INDEX, COLOR_TITLE_HEXA);

		CellStyle titleStyle = cloneStyle(styleRegistry.get(STYLE_HEADER_NAME));
		setStyleFillForegroundColor(titleStyle, colorRegistry, COLOR_TITLE_INDEX);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		titleStyle.setFont(getFont(FONT_TITLE_NAME));
		registerStyle(STYLE_TITLE_NAME, titleStyle);
	}

	@Override
	protected String getLocalizedLabel(String key) {
		return key;
	}
}
