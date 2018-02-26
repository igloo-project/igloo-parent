package org.iglooproject.sass.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import org.iglooproject.sass.internal.ClasspathUtil;

public class ScssStylesheetInformation implements Serializable {
	
	private static final long serialVersionUID = 5751644157529838224L;
	
	private final String path;
	
	private String source;
	
	private long lastModifiedTime;
	
	private Collection<String> referencedResources = new HashSet<>();
	
	public ScssStylesheetInformation(String path, String newSource) {
		this.path = path;
		this.source = newSource;
	}
	
	public ScssStylesheetInformation(String path, long lastModifiedTime) {
		this.path = path;
		this.referencedResources.add(path);
		this.lastModifiedTime = lastModifiedTime;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public void addImportedStylesheet(ScssStylesheetInformation importedStylesheet) {
		if (importedStylesheet.lastModifiedTime > this.lastModifiedTime) {
			this.lastModifiedTime = importedStylesheet.lastModifiedTime;
		}
		this.referencedResources.addAll(importedStylesheet.referencedResources);
	}
	
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}
	
	public boolean isUpToDate() {
		for (String referencedResource : referencedResources) {
			if (!isUpToDate(referencedResource)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isUpToDate(String path) {
		return ClasspathUtil.lastModified(getClass().getClassLoader(), path) <= lastModifiedTime;
	}

}