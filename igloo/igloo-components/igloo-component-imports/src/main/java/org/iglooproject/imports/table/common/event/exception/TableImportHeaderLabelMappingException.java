package org.iglooproject.imports.table.common.event.exception;

import org.iglooproject.imports.table.common.location.TableImportLocation;

public class TableImportHeaderLabelMappingException extends TableImportMappingException {

  private static final long serialVersionUID = -9130255186999010404L;

  private final String expectedHeaderLabel;

  public TableImportHeaderLabelMappingException(
      String message, String expectedHeaderLabel, TableImportLocation location) {
    super(message, location);
    this.expectedHeaderLabel = expectedHeaderLabel;
  }

  public TableImportHeaderLabelMappingException(String message, String expectedHeaderLabel) {
    super(message);
    this.expectedHeaderLabel = expectedHeaderLabel;
  }

  public String getExpectedHeaderLabel() {
    return expectedHeaderLabel;
  }
}
