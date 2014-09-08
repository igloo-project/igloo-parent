package fr.openwide.core.spring.notification.service;

import org.springframework.context.ApplicationContext;

public interface INotificationBuilderInitState extends INotificationBuilderToState {
	
	INotificationBuilderBaseState init(ApplicationContext applicationContext);
}
