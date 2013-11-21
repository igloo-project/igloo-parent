package fr.openwide.core.wicket.more.notification.service;

import java.util.Locale;
import java.util.concurrent.Callable;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.core.util.string.ComponentRenderer;
import org.apache.wicket.util.lang.Args;

public abstract class AbstractNotificationPanelRendererServiceImpl extends AbstractBackgroundWicketThreadContextBuilder {
	
	protected String renderComponent(Callable<Component> componentTask, Locale locale) {
		Args.notNull(componentTask, "componentTask");
		
		RequestCycleThreadAttachmentStatus requestCycleStatus = null;
		
		try {
			requestCycleStatus = attachRequestCycleIfNeeded(getApplicationName());
			
			Session session = Session.get();
			
			Locale oldLocale = session.getLocale();
			if (locale != null) {
				session.setLocale(configurer.toAvailableLocale(locale));
			}
			
			Component component = componentTask.call();
			Args.notNull(component, "component");
			
			String panel = ComponentRenderer.renderComponent(component).toString();
			
			session.setLocale(oldLocale);
			
			return panel;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (requestCycleStatus != null) {
				detachRequestCycleIfNeeded(requestCycleStatus);
			}
		}
	}
	
}