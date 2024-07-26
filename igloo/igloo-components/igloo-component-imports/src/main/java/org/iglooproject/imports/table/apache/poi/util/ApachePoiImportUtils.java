package org.iglooproject.imports.table.apache.poi.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public final class ApachePoiImportUtils {

  private ApachePoiImportUtils() {}

  /**
   * @return The actual value type of {@code cell}. This is all about formulas : if the given cell
   *     is of type {@link Cell#CELL_TYPE_FORMULA}, this returns the return type of the formula.
   */
  public static CellType getCellActualValueType(Cell cell) {
    CellType cellType = cell.getCellType();
    if (cellType.equals(CellType.FORMULA)) {
      return cell.getCachedFormulaResultType();
    } else {
      return cellType;
    }
  }
}
