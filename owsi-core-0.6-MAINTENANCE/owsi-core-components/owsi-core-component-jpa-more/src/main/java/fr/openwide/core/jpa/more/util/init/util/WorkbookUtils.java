package fr.openwide.core.jpa.more.util.init.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import fr.openwide.core.spring.util.StringUtils;

public final class WorkbookUtils {
	
	public static final NumberFormat DECIMAL_FORMAT;
	
	static {
		DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
		// period character must be used as decimal separator, otherwise BigDecimal creation will fail.
		decimalFormatSymbols.setDecimalSeparator('.');
		DECIMAL_FORMAT = new DecimalFormat("#.######", decimalFormatSymbols);
	}
	
	public static List<Map<String, Object>> getSheetContent(Sheet sheet) {
		FormulaEvaluator formulaEvaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
		
		List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
		Map<Integer, String> header = new HashMap<Integer, String>();
		
		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				for (Cell cell : row) {
					header.put(cell.getColumnIndex(), getCellValue(formulaEvaluator, cell).toString());
				}
			} else {
				Map<String, Object> line = new HashMap<String, Object>();
				for (Cell cell : row) {
					Object cellValue = getCellValue(formulaEvaluator, cell);
					if (cellValue != null) {
						line.put(header.get(cell.getColumnIndex()), cellValue);
					}
				}
				content.add(line);
			}
		}
		return content;
	}
	
	private static Object getCellValue(FormulaEvaluator formulaEvaluator, Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				Calendar calendar = GregorianCalendar.getInstance();
				calendar.setTime(DateUtil.getJavaDate(cell.getNumericCellValue()));
				
				return calendar.getTime();
			} else {
				return DECIMAL_FORMAT.format(cell.getNumericCellValue());
			}
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			if (StringUtils.hasText(cell.getStringCellValue())) {
				return cell.getStringCellValue();
			}
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return getCellValueFromFormula(formulaEvaluator, cell);
		}
		return null;
	}
	
	private static Object getCellValueFromFormula(FormulaEvaluator formulaEvaluator, Cell cell) {
		CellValue cellValue = formulaEvaluator.evaluate(cell);
		
		if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				Calendar calendar = GregorianCalendar.getInstance();
				calendar.setTime(DateUtil.getJavaDate(cellValue.getNumberValue()));
				
				return calendar.getTime();
			} else {
				return DECIMAL_FORMAT.format(cellValue.getNumberValue());
			}
		} else if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
			if (StringUtils.hasText(cellValue.getStringValue())) {
				return cellValue.getStringValue();
			}
		}
		
		return null;
	}
	
	private WorkbookUtils() {
	}
}
