package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.showcase.web.application.util.template.MainTemplate;

public class AutocompletePage extends MainTemplate {
	private static final long serialVersionUID = 1019469897091555748L;

	public AutocompletePage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return WidgetsMainPage.class;
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AutocompletePage.class;
	}
}
