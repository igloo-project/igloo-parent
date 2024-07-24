package org.iglooproject.imports.table.common.event.exception;

import org.iglooproject.imports.table.common.location.TableImportLocation;

public class TableImportException extends Exception {

  private static final long serialVersionUID = -4988670342820463252L;

  private final TableImportLocation location;

  public TableImportException(String message) {
    super(message);
    this.location = null;
  }

  public TableImportException(Throwable cause) {
    super(cause);
    this.location = null;
  }

  public TableImportException(String message, Throwable cause) {
    super(message, cause);
    this.location = null;
  }

  public TableImportException(String message, TableImportLocation location) {
    super(message);
    this.location = location;
  }

  public TableImportException(Throwable cause, TableImportLocation location) {
    super(cause);
    this.location = location;
  }

  public TableImportException(String message, Throwable cause, TableImportLocation location) {
    super(message, cause);
    this.location = location;
  }

  public TableImportLocation getLocation() {
    return location;
  }
}
