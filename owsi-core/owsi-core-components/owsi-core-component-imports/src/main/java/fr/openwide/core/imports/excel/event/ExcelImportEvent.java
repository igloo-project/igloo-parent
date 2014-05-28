package fr.openwide.core.imports.excel.event;

public class ExcelImportEvent {
	
	public static ExcelImportErrorEvent FATAL = new ExcelImportErrorEvent("FATAL", ExcelImportErrorSeverity.FATAL);
	public static ExcelImportErrorEvent ERROR = new ExcelImportErrorEvent("ERROR", ExcelImportErrorSeverity.NON_FATAL);
	public static ExcelImportInfoEvent WARNING = new ExcelImportInfoEvent("WARNING");
	public static ExcelImportInfoEvent INFO = new ExcelImportInfoEvent("INFO");
	
	private final String name;
	
	public static class ExcelImportErrorEvent extends ExcelImportEvent {
		private final ExcelImportErrorSeverity severity;
		
		public ExcelImportErrorEvent(String name, ExcelImportErrorSeverity severity) {
			super(name);
			this.severity = severity;
		}
		
		@Override
		public boolean equals(Object object) {
			return super.equals(object) && (object instanceof ExcelImportErrorEvent);
		}
		
		public boolean isFatal() {
			return ExcelImportErrorSeverity.FATAL.equals(severity);
		}
	}
	
	public static class ExcelImportInfoEvent extends ExcelImportEvent {
		public ExcelImportInfoEvent(String name) {
			super(name);
		}
		
		@Override
		public boolean equals(Object object) {
			return super.equals(object) && (object instanceof ExcelImportInfoEvent);
		}
	}
	
	private ExcelImportEvent(String name) {
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
		if (!(object instanceof ExcelImportEvent)) {
			return false;
		}
		
		ExcelImportEvent other = (ExcelImportEvent) object;
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
