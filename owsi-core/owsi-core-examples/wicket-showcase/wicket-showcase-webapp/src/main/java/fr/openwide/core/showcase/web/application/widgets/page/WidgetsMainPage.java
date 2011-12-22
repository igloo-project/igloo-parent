package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class WidgetsMainPage extends MainTemplate {
	private static final long serialVersionUID = 3092941096047935122L;
	
	public WidgetsMainPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement("widgets.pageTitle"));
		
		add(new Label("pageTitle", new ResourceModel("widgets.pageTitle")));
	}
	
	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return WidgetsMainPage.class;
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}
}
