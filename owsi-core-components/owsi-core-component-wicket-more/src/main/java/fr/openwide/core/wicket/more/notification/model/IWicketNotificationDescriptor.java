package fr.openwide.core.wicket.more.notification.model;

import org.apache.wicket.Component;

import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;

public interface IWicketNotificationDescriptor extends INotificationContentDescriptor {
	
	Component createComponent(String id);

}
