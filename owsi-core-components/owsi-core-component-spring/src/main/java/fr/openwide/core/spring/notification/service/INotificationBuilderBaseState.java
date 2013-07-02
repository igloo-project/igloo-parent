package fr.openwide.core.spring.notification.service;


public interface INotificationBuilderBaseState extends INotificationBuilderToState {
	
	INotificationBuilderToState from(String from);
}
