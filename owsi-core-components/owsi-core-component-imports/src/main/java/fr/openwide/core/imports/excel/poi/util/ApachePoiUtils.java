package fr.openwide.core.imports.excel.poi.util;

import org.apache.poi.ss.usermodel.Cell;

public final class ApachePoiUtils {
	
	private ApachePoiUtils() { }
	
	/**
	 * @return The actual value type of {@code cell}.
	 *         This is all about formulas : if the given cell is of type {@link Cell#CELL_TYPE_FORMULA}, this returns the return type of the formula.
	 */
	public static int getCellActualValueType(Cell cell) {
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_FORMULA) {
			return cell.getCachedFormulaResultType();
		} else {
			return cellType;
		}
	}

}
