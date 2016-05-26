package fr.openwide.core.wicket.more.notification.model;

import org.apache.wicket.Component;

import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;

/**
 * @deprecated There is no reason to use this interface: instead of creating the component, you may just ask for a HTML
 * rendering. See the way it's done in the basic application's NotificationDemoPage.
 */
@Deprecated
public interface IWicketNotificationDescriptor extends INotificationContentDescriptor {
	
	String DEFAULT_NOTIFICATION_VARIATION = "notification";
	
	Component createComponent(String id);

}
