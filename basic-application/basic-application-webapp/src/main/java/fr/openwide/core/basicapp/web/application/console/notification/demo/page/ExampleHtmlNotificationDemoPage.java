package fr.openwide.core.basicapp.web.application.console.notification.demo.page;

import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.console.notification.demo.template.NotificationDemoTemplate;
import fr.openwide.core.basicapp.web.application.notification.component.ExampleHtmlNotificationPanel;

public class ExampleHtmlNotificationDemoPage extends NotificationDemoTemplate {
	
	private static final long serialVersionUID = 6917445111427453920L;

	public ExampleHtmlNotificationDemoPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	protected Component buildNotificationPanel(String wicketId, PageParameters parameters) {
		return new ExampleHtmlNotificationPanel(wicketId, getFirstInRange(User.class, 1L, 100L), Model.of(new Date()));
	}

}
