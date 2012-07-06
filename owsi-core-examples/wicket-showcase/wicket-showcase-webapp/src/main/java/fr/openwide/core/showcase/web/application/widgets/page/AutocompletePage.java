package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AutocompletePage extends WidgetsTemplate {
	private static final long serialVersionUID = 1019469897091555748L;

	public AutocompletePage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.autocomplete"), AutocompletePage.class));
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AutocompletePage.class;
	}
}
