package org.iglooproject.sass.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.google.common.collect.Lists;

public class ScssStylesheetInformation implements Serializable {
	
	private static final long serialVersionUID = 5751644157529838224L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScssStylesheetInformation.class);
	
	private final String path;
	
	private String source;
	
	private long lastModifiedTime;
	
	private Collection<String> referencedResources = Lists.newArrayList();
	
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
		ClassPathResource resource = new ClassPathResource(path);
		try {
			return resource.lastModified() <= lastModifiedTime;
		} catch (IOException e) {
			LOGGER.error("Error while trying to determine if resource " + path + " is up to date. Assuming it is outdated.", e);
			return false;
		}
	}

}