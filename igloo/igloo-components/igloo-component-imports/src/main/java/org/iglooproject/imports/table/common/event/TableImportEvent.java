package org.iglooproject.imports.table.common.event;

public class TableImportEvent {

  public static final ExcelImportErrorEvent FATAL =
      new ExcelImportErrorEvent("FATAL", TableImportErrorSeverity.FATAL);
  public static final ExcelImportErrorEvent ERROR =
      new ExcelImportErrorEvent("ERROR", TableImportErrorSeverity.NON_FATAL);
  public static final ExcelImportInfoEvent WARNING = new ExcelImportInfoEvent("WARNING");
  public static final ExcelImportInfoEvent INFO = new ExcelImportInfoEvent("INFO");
  public static final ExcelImportInfoEvent DEBUG = new ExcelImportInfoEvent("DEBUG");

  /** equals and hashcode are based on this field. It must be immutable. */
  private final String name;

  public static class ExcelImportErrorEvent extends TableImportEvent {
    private final TableImportErrorSeverity severity;

    public ExcelImportErrorEvent(String name, TableImportErrorSeverity severity) {
      super(name);
      this.severity = severity;
    }

    public boolean isFatal() {
      return TableImportErrorSeverity.FATAL.equals(severity);
    }
  }

  public static class ExcelImportInfoEvent extends TableImportEvent {
    public ExcelImportInfoEvent(String name) {
      super(name);
    }
  }

  private TableImportEvent(String name) {
    super();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object == this) {
      return true;
    }
    if (!(object instanceof TableImportEvent)) {
      return false;
    }

    TableImportEvent other = (TableImportEvent) object;
    return getClass().equals(other.getClass()) && name.equals(other.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return name.toString();
  }
}
