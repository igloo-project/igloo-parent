package fr.openwide.core.wicket.more.notification.model;

import org.apache.wicket.Component;

import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;

public interface IWicketNotificationDescriptor extends INotificationContentDescriptor {
	
	String DEFAULT_NOTIFICATION_VARIATION = "notification";
	
	Component createComponent(String id);

}
