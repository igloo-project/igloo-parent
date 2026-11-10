package org.iglooproject.wicket.more.export.spreadsheet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iglooproject.commons.util.mime.IMediaType;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.commons.util.mime.MediaTypeDefinition;

public final class SpreadsheetUtils {

  private enum SpreadsheetMediaType implements IMediaType {
    APPLICATION_MS_EXCEL_MACRO(
        MediaTypeDefinition.of("application/vnd.ms-excel.sheet.macroenabled.12")
            .withExtension("xlsm"));

    private final MediaTypeDefinition definition;

    SpreadsheetMediaType(MediaTypeDefinition definition) {
      this.definition = definition;
    }

    @Override
    public MediaTypeDefinition definition() {
      return definition;
    }
  }

  private SpreadsheetUtils() {}

  /**
   * @return A media type for a Microsoft Excel file (xls or xlsx). Media type for ODF Spreadsheet
   *     (ods) is not valid.
   */
  public static IMediaType getMediaType(Workbook workbook) {
    if (workbook instanceof HSSFWorkbook) {
      return MediaType.APPLICATION_MSEXCEL;
    } else if (workbook instanceof XSSFWorkbook && ((XSSFWorkbook) workbook).isMacroEnabled()) {
      return SpreadsheetMediaType.APPLICATION_MS_EXCEL_MACRO;
    } else if (workbook instanceof XSSFWorkbook && !((XSSFWorkbook) workbook).isMacroEnabled()) {
      return MediaType.APPLICATION_XLSX;
    } else if (workbook instanceof SXSSFWorkbook) {
      return MediaType.APPLICATION_XLSX;
    } else {
      // Default
      return MediaType.APPLICATION_MSEXCEL;
    }
  }
}
