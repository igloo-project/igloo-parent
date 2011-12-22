package fr.openwide.core.showcase.web.application.navigation.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class HomePage extends MainTemplate {
	private static final long serialVersionUID = -4391520503053122686L;
	
	public HomePage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement("home.pageTitle"));
		
		add(new Label("pageTitle", new ResourceModel("home.pageTitle")));
	}
	
	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return HomePage.class;
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return HomePage.class;
	}
}
