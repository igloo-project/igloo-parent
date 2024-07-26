package org.iglooproject.imports.table.common.event.exception;

import org.iglooproject.imports.table.common.location.TableImportLocation;

public class TableImportFileException extends TableImportException {

  private static final long serialVersionUID = 3491493477155038190L;

  public TableImportFileException(String message, Throwable cause, TableImportLocation location) {
    super(message, cause, location);
  }

  public TableImportFileException(String message, Throwable cause) {
    super(message, cause);
  }

  public TableImportFileException(Throwable cause) {
    super(cause);
  }

  public TableImportFileException(Throwable cause, TableImportLocation location) {
    super(cause, location);
  }
}
