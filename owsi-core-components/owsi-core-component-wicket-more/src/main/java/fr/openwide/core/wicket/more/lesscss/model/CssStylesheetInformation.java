package fr.openwide.core.wicket.more.lesscss.model;

import java.io.Serializable;

import org.apache.wicket.util.time.Time;

public class CssStylesheetInformation implements Serializable {
	
	private static final long serialVersionUID = 5751644157529838224L;

	private String css;
	
	private Time lastModifiedTime;
	
	public CssStylesheetInformation(String css, Time lastModifiedTime) {
		this.css = css;
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getCss() {
		return css;
	}

	public Time getLastModifiedTime() {
		return lastModifiedTime;
	}

}