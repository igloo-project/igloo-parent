package fr.openwide.core.spring.notification.service;

public interface INotificationBuilderBaseState extends INotificationBuilderReplyToState {
	
	INotificationBuilderBaseState from(String from);
	
	INotificationBuilderBaseState sender(String sender);
}
