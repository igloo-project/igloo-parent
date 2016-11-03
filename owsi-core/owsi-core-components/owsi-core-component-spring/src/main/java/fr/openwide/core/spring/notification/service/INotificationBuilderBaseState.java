package fr.openwide.core.spring.notification.service;

public interface INotificationBuilderBaseState extends INotificationBuilderReplyToState {
	
	INotificationBuilderReplyToState from(String from);
	
}
