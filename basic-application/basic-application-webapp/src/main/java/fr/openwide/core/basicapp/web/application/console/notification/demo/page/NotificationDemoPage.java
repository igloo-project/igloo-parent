package fr.openwide.core.basicapp.web.application.console.notification.demo.page;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.spring.notification.exception.NotificationContentRenderingException;
import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.notification.model.IWicketNotificationDescriptor;

public class NotificationDemoPage extends WebPage {
	
	private static final long serialVersionUID = -1929048481327622623L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationDemoPage.class);
	
	public NotificationDemoPage(IWicketNotificationDescriptor descriptor) {
		super();
		
		MarkupContainer htmlRootElement = new TransparentWebMarkupContainer("htmlRootElement");
		htmlRootElement.add(AttributeAppender.append("lang", AbstractCoreSession.get().getLocale().getLanguage()));
		add(htmlRootElement);
		
		add(new Label("headPageTitle", new ResourceModel("console.notifications")));
		
		String subject;
		try {
			subject = descriptor.renderSubject(AbstractCoreSession.get().getLocale());
		} catch (NotificationContentRenderingException e) {
			LOGGER.error("Erreur lors du rendu du sujet", e);
			getSession().error(getString("common.error.unexpected"));
			throw ConsoleNotificationDemoIndexPage.linkDescriptor().newRestartResponseException();
		}
		add(new Label("subject", subject));
		add(descriptor.createComponent("htmlPanel"));
	}
}
