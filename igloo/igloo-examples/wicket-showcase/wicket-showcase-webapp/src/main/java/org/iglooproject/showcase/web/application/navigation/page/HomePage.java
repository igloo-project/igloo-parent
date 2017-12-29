package org.iglooproject.showcase.web.application.navigation.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.iglooproject.showcase.web.application.util.template.MainTemplate;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class HomePage extends MainTemplate {
	private static final long serialVersionUID = -4391520503053122686L;
	
	public HomePage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("home.pageTitle")));
	}
	
	@Override
	protected boolean isBreadCrumbDisplayed() {
		return false;
	}
	
	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return HomePage.class;
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}
}
