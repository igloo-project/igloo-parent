package fr.openwide.core.wicket.more.util;

public enum DatePattern {
	
	SHORT_DATE("date.format.shortDate", "javascript.date.format.shortDate"),
	SHORT_DATETIME("date.format.shortDateTime", null),
	
	REALLY_SHORT_DATE("date.format.reallyShortDate", "javascript.date.format.reallyShortDate"),
	REALLY_SHORT_DATETIME("date.format.reallyShortDateTime", null),

	YEAR("date.format.year", "javascript.date.format.year");
	
	private String javaPatternKey;
	
	private String javascriptPatternKey;
	
	private DatePattern(String javaPatternKey, String javascriptPatternKey) {
		this.javaPatternKey = javaPatternKey;
		this.javascriptPatternKey = javascriptPatternKey;
	}
	
	public String getJavaPatternKey() {
		if (javaPatternKey != null) {
			return javaPatternKey;
		} else {
			throw new IllegalStateException("Java format not supported for this format");
		}
	}
	
	public String getJavascriptPatternKey() {
		if (javascriptPatternKey != null) {
			return javascriptPatternKey;
		} else {
			throw new IllegalStateException("Javascript format not supported for this format");
		}
	}
	
}