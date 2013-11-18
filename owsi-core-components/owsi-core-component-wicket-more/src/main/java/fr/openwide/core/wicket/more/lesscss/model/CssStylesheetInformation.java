package fr.openwide.core.wicket.more.lesscss.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.google.common.collect.Lists;

public class CssStylesheetInformation implements Serializable {
	
	private static final long serialVersionUID = 5751644157529838224L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CssStylesheetInformation.class);
	
	private final Class<?> scope;
	
	private final String name;
	
	private String source;
	
	private long lastModifiedTime;
	
	private Collection<Pair<? extends Class<?>, String>> referencedResources = Lists.newArrayList();
	
	public CssStylesheetInformation(CssStylesheetInformation reference, String newSource) {
		this.scope = reference.scope;
		this.name = reference.name;
		this.referencedResources.addAll(reference.referencedResources);
		this.source = newSource;
		this.lastModifiedTime = reference.lastModifiedTime;
	}
	
	public CssStylesheetInformation(Class<?> scope, String name, String source, long lastModifiedTime) {
		this.scope = scope;
		this.name = name;
		this.referencedResources.add(Pair.of(scope, name));
		this.source = source;
		this.lastModifiedTime = lastModifiedTime;
	}
	
	public Class<?> getScope() {
		return scope;
	}
	
	public String getName() {
		return name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public void addImportedStylesheet(CssStylesheetInformation importedStylesheet) {
		if (importedStylesheet.lastModifiedTime > this.lastModifiedTime) {
			this.lastModifiedTime = importedStylesheet.lastModifiedTime;
		}
		this.referencedResources.addAll(importedStylesheet.referencedResources);
	}
	
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}
	
	public boolean isUpToDate() {
		for (Pair<? extends Class<?>, String> referencedResource : referencedResources) {
			if (!isUpToDate(referencedResource.getLeft(), referencedResource.getRight())) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isUpToDate(Class<?> scope, String name) {
		ClassPathResource resource = new ClassPathResource(name, scope);
		try {
			return resource.lastModified() <= lastModifiedTime;
		} catch (IOException e) {
			LOGGER.error("Error while trying to determine if resource " + scope.getName() + "/" + name + " is up to date. Assuming it is outdated.", e);
			return false;
		}
	}

}