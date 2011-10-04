package fr.openwide.core.wicket.more.markup.html.template.model;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;

public class BreadCrumbElement implements Serializable {
	
	private static final long serialVersionUID = -44367801976105581L;

	private String labelKey;
	
	private Class<? extends Page> pageClass;
	
	private PageParameters pageParameters;
	
	public BreadCrumbElement(String labelKey) {
		this(labelKey, null);
	}
	
	public BreadCrumbElement(String labelKey, Class<? extends Page> pageClass) {
		this(labelKey, pageClass, null);
	}
	
	public BreadCrumbElement(String labelKey, Class<? extends Page> pageClass, PageParameters pageParameters) {
		this.labelKey = labelKey;
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
	}

	public String getLabelKey() {
		return labelKey;
	}

	public Class<? extends Page> getPageClass() {
		return pageClass;
	}

	public PageParameters getPageParameters() {
		return pageParameters;
	}

}
