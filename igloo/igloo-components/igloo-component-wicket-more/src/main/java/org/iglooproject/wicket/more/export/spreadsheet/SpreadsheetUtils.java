package org.iglooproject.wicket.more.export.spreadsheet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iglooproject.commons.util.mime.MediaType;

public final class SpreadsheetUtils {

  private SpreadsheetUtils() {}

  /**
   * @return A media type for a Microsoft Excel file (xls or xlsx). Media type for ODF Spreadsheet
   *     (ods) is not valid.
   */
  public static MediaType getMediaType(Workbook workbook) {
    if (workbook instanceof HSSFWorkbook) {
      return MediaType.APPLICATION_MS_EXCEL;
    } else if (workbook instanceof XSSFWorkbook && ((XSSFWorkbook) workbook).isMacroEnabled()) {
      return MediaType.APPLICATION_MS_EXCEL_MACRO;
    } else if (workbook instanceof XSSFWorkbook && !((XSSFWorkbook) workbook).isMacroEnabled()) {
      return MediaType.APPLICATION_OPENXML_EXCEL;
    } else if (workbook instanceof SXSSFWorkbook) {
      return MediaType.APPLICATION_OPENXML_EXCEL;
    } else {
      // Default
      return MediaType.APPLICATION_MS_EXCEL;
    }
  }
}
