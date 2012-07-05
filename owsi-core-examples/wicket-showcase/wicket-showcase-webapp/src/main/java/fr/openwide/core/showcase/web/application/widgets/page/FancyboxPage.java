package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class FancyboxPage extends WidgetsMainPage {
	private static final long serialVersionUID = -4802009584951257187L;

	public FancyboxPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement("widgets.menu.fancybox", FancyboxPage.class));
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return FancyboxPage.class;
	}
}
