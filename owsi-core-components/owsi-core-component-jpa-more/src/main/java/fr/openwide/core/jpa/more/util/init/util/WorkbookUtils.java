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
import org.apache.poi.ss.usermodel.DateUtil;
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
		List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
		Map<Integer, String> header = new HashMap<Integer, String>();
		
		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				for (Cell cell : row) {
					header.put(cell.getColumnIndex(), getCellValue(cell).toString());
				}
			} else {
				Map<String, Object> line = new HashMap<String, Object>();
				for (Cell cell : row) {
					Object cellValue = getCellValue(cell);
					if (cellValue != null) {
						line.put(header.get(cell.getColumnIndex()), getCellValue(cell));
					}
				}
				content.add(line);
			}
		}
		return content;
	}
	
	private static Object getCellValue(Cell cell) {
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
		}
		return null;
	}
	
	private WorkbookUtils() {
	}
}
