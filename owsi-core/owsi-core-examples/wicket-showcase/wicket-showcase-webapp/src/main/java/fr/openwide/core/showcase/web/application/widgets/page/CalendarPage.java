package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.showcase.web.application.util.template.MainTemplate;

public class CalendarPage extends MainTemplate {
	private static final long serialVersionUID = -3963117430192776716L;

	public CalendarPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return WidgetsMainPage.class;
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return CalendarPage.class;
	}
}
