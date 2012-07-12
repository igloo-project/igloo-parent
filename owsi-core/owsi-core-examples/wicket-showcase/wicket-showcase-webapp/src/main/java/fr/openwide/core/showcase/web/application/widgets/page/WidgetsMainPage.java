package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class WidgetsMainPage extends WidgetsTemplate {
	private static final long serialVersionUID = 3092941096047935122L;
	
	public WidgetsMainPage(PageParameters parameters) {
		super(parameters);
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
