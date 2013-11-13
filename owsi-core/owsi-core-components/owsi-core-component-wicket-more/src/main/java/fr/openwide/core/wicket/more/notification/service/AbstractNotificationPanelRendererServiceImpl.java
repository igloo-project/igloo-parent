package fr.openwide.core.wicket.more.notification.service;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.core.util.string.ComponentRenderer;
import org.apache.wicket.util.lang.Args;

public abstract class AbstractNotificationPanelRendererServiceImpl extends AbstractBackgroundWicketThreadContextBuilder {
	
	protected String renderComponent(Component component, Locale locale) {
		Args.notNull(component, "component");
		
		RequestCycleThreadAttachmentStatus requestCycleStatus = null;
		
		try {
			requestCycleStatus = attachRequestCycleIfNeeded(getApplicationName());
			
			Session session = Session.get();
			
			Locale oldLocale = session.getLocale();
			if (locale != null) {
				session.setLocale(configurer.toAvailableLocale(locale));
			}
			String panel = ComponentRenderer.renderComponent(component).toString();
			
			session.setLocale(oldLocale);
			
			return panel;
		} finally {
			if (requestCycleStatus != null) {
				detachRequestCycleIfNeeded(requestCycleStatus);
			}
		}
	}
	
}