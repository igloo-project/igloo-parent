package org.iglooproject.jpa.more.util.init.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.EnumUtils;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.iglooproject.spring.util.StringUtils;

import com.google.common.collect.Sets;

public final class WorkbookUtils {
	
	public static final NumberFormat DECIMAL_FORMAT;
	
	static {
		DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
		// period character must be used as decimal separator, otherwise BigDecimal creation will fail.
		decimalFormatSymbols.setDecimalSeparator('.');
		DECIMAL_FORMAT = new DecimalFormat("#.######", decimalFormatSymbols);
	}
	
	// Allow to get cached values from cells if the formula cannot be evaluated
	// We may want to allow only given features.
	private static final Set<FormulaFeature> ALLOWED_NOT_IMPLEMENTED_FORMULA_FEATURES = Sets.newHashSet();
	
	public static void addAllowedNotImplementedFormulaFeature(FormulaFeature feature) {
		ALLOWED_NOT_IMPLEMENTED_FORMULA_FEATURES.add(feature);
	}
	public static void removeAllowedNotImplementedFormulaFeature(FormulaFeature feature) {
		ALLOWED_NOT_IMPLEMENTED_FORMULA_FEATURES.remove(feature);
	}
	public static void clearAllowedNotImplementedFormulaFeatures() {
		ALLOWED_NOT_IMPLEMENTED_FORMULA_FEATURES.clear();
	}
	
	public static List<Map<String, Object>> getSheetContent(Sheet sheet) {
		FormulaEvaluator formulaEvaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
		
		List<Map<String, Object>> content = new ArrayList<>();
		Map<Integer, String> header = new HashMap<>();
		
		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				for (Cell cell : row) {
					// null-safe way to call toString on cell value
					header.put(cell.getColumnIndex(), String.format("%s", getCellValue(formulaEvaluator, cell)));
				}
			} else {
				Map<String, Object> line = new HashMap<>();
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
		Object cellPrimitiveValue = getCellPrimitiveValue(cell, cell.getCellType());
		if (cellPrimitiveValue != null) {
			return cellPrimitiveValue;
		} else if (cell.getCellType().equals(CellType.FORMULA)) {
			return getCellValueFromFormula(formulaEvaluator, cell);
		}
		return null;
	}
	
	private static Object getCellValueFromFormula(FormulaEvaluator formulaEvaluator, Cell cell) {
		try {
			CellValue cellValue = formulaEvaluator.evaluate(cell);
			
			if (cellValue.getCellType().equals(CellType.NUMERIC)) {
				if (DateUtil.isCellDateFormatted(cell)) {
					Calendar calendar = GregorianCalendar.getInstance();
					calendar.setTime(DateUtil.getJavaDate(cellValue.getNumberValue()));
					
					return calendar.getTime();
				} else {
					return DECIMAL_FORMAT.format(cellValue.getNumberValue());
				}
			} else if (cellValue.getCellType().equals(CellType.STRING)) {
				if (StringUtils.hasText(cellValue.getStringValue())) {
					return cellValue.getStringValue();
				}
			}
		} catch (NotImplementedException e) {
			// If formula use Excel features not implemented in POI (like proper),
			// we can retrieve the cached value (which may no longer be correct, depending of what you do on your file).
			FormulaFeature feature = EnumUtils.getEnum(FormulaFeature.class, e.getCause().getMessage());
			if (ALLOWED_NOT_IMPLEMENTED_FORMULA_FEATURES.contains(feature)) {
				return getCellPrimitiveValue(cell, cell.getCachedFormulaResultType());
			} else {
				throw e;
			}
		}
		
		return null;
	}
	
	private static Object getCellPrimitiveValue(Cell cell, CellType cellType) {
		if (cellType.equals(CellType.NUMERIC)) {
			if (DateUtil.isCellDateFormatted(cell)) {
				Calendar calendar = GregorianCalendar.getInstance();
				calendar.setTime(DateUtil.getJavaDate(cell.getNumericCellValue()));
				
				return calendar.getTime();
			} else {
				return DECIMAL_FORMAT.format(cell.getNumericCellValue());
			}
		} else if (cellType.equals(CellType.STRING)) {
			if (StringUtils.hasText(cell.getStringCellValue())) {
				return cell.getStringCellValue();
			}
		}
		return null;
	}
	
	public enum FormulaFeature {
		PROPER;
	}
	
	private WorkbookUtils() {
	}
}
