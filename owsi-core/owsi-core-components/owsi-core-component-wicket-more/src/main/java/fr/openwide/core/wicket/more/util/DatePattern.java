package fr.openwide.core.wicket.more.util;

public enum DatePattern {
	
	SHORT_DATE("date.format.short.date", "javascript.date.format.short.date"),
	SHORT_DATETIME("date.format.short.datetime", null),
	
	REALLY_SHORT_DATE("date.format.really.short.date", "javascript.date.format.really.short.date"),
	REALLY_SHORT_DATETIME("date.format.really.short.datetime", null);
	
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