package fr.openwide.core.showcase.web.application.navigation.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;

public class HomePage extends MainTemplate {
	private static final long serialVersionUID = -4391520503053122686L;
	
	public HomePage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("home.pageTitle")));
		
		add(new Label("pageTitle", new ResourceModel("home.pageTitle")));
	}
	
	@Override
	protected List<NavigationMenuItem> getSubNav() {
		return Lists.newArrayList();
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
