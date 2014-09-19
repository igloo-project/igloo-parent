package fr.openwide.core.imports.table.common.event;

public class TableImportEvent {
	
	public static ExcelImportErrorEvent FATAL = new ExcelImportErrorEvent("FATAL", TableImportErrorSeverity.FATAL);
	public static ExcelImportErrorEvent ERROR = new ExcelImportErrorEvent("ERROR", TableImportErrorSeverity.NON_FATAL);
	public static ExcelImportInfoEvent WARNING = new ExcelImportInfoEvent("WARNING");
	public static ExcelImportInfoEvent INFO = new ExcelImportInfoEvent("INFO");
	public static ExcelImportInfoEvent DEBUG = new ExcelImportInfoEvent("DEBUG");
	
	private final String name;
	
	public static class ExcelImportErrorEvent extends TableImportEvent {
		private final TableImportErrorSeverity severity;
		
		public ExcelImportErrorEvent(String name, TableImportErrorSeverity severity) {
			super(name);
			this.severity = severity;
		}
		
		@Override
		public boolean equals(Object object) {
			return super.equals(object) && (object instanceof ExcelImportErrorEvent);
		}
		
		public boolean isFatal() {
			return TableImportErrorSeverity.FATAL.equals(severity);
		}
	}
	
	public static class ExcelImportInfoEvent extends TableImportEvent {
		public ExcelImportInfoEvent(String name) {
			super(name);
		}
		
		@Override
		public boolean equals(Object object) {
			return super.equals(object) && (object instanceof ExcelImportInfoEvent);
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
		return name.equals(other.name);
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
