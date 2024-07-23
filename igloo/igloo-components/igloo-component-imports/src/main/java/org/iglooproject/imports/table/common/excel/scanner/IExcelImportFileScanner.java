package org.iglooproject.imports.table.common.excel.scanner;

import java.io.File;
import java.io.InputStream;
import org.iglooproject.imports.table.common.event.exception.TableImportException;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;

public interface IExcelImportFileScanner<TWorkbook, TSheet, TRow, TCell, TCellReference> {

  enum SheetSelection {
    ALL,
    NON_HIDDEN_ONLY
  }

  interface IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell, TCellReference> {
    void visitSheet(
        ITableImportNavigator<TSheet, TRow, TCell, TCellReference> navigator,
        TWorkbook workbook,
        TSheet sheet)
        throws TableImportException;
  }

  void scanRecursively(
      File file,
      String filename,
      SheetSelection selection,
      IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell, TCellReference> visitor)
      throws TableImportException;

  void scan(
      File file,
      String filename,
      SheetSelection selection,
      IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell, TCellReference> visitor)
      throws TableImportException;

  void scan(
      InputStream stream,
      String filename,
      SheetSelection selection,
      IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell, TCellReference> visitor)
      throws TableImportException;
}
