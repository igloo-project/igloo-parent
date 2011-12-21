package fr.openwide.core.wicket.more.lesscss.model;

import java.io.Serializable;

public class CssStylesheetInformation implements Serializable {
	
	private static final long serialVersionUID = 5751644157529838224L;

	private String source;
	
	private long lastModifiedTime;
	
	public CssStylesheetInformation(String source, long lastModifiedTime) {
		this.source = source;
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

}