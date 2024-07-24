package org.iglooproject.imports.table.common.event.exception;

import org.iglooproject.imports.table.common.location.TableImportLocation;

public class TableImportContentException extends TableImportException {

  private static final long serialVersionUID = 2545352534086805755L;

  public TableImportContentException(String message, TableImportLocation location) {
    super(message, location);
  }

  public TableImportContentException(String message) {
    super(message);
  }
}
