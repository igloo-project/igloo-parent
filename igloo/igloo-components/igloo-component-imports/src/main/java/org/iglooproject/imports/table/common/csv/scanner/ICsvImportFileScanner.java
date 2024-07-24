package org.iglooproject.imports.table.common.csv.scanner;

import java.io.File;
import java.io.InputStream;
import org.iglooproject.imports.table.common.event.exception.TableImportException;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;

public interface ICsvImportFileScanner<TTable, TRow, TCell, TCellReference> {

  interface ICsvImportFileVisitor<TTable, TRow, TCell, TCellReference> {
    void visitTable(
        ITableImportNavigator<TTable, TRow, TCell, TCellReference> navigator, TTable sheet)
        throws TableImportException;
  }

  void scanRecursively(
      File file,
      String filename,
      ICsvImportFileVisitor<TTable, TRow, TCell, TCellReference> visitor)
      throws TableImportException;

  void scan(
      File file,
      String filename,
      ICsvImportFileVisitor<TTable, TRow, TCell, TCellReference> visitor)
      throws TableImportException;

  void scan(
      InputStream stream,
      String filename,
      ICsvImportFileVisitor<TTable, TRow, TCell, TCellReference> visitor)
      throws TableImportException;
}
