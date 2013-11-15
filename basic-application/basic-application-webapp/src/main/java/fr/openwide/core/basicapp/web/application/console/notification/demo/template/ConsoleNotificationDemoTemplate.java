package fr.openwide.core.basicapp.web.application.console.notification.demo.template;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.web.application.console.notification.demo.page.ConsoleNotificationDemoIndexPage;
import fr.openwide.core.wicket.more.console.template.ConsoleTemplate;

public abstract class ConsoleNotificationDemoTemplate extends ConsoleTemplate {

	private static final long serialVersionUID = -3192604063259001201L;
	
	public ConsoleNotificationDemoTemplate(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	protected Class<? extends ConsoleTemplate> getMenuSectionPageClass() {
		return ConsoleNotificationDemoIndexPage.class;
	}
}
