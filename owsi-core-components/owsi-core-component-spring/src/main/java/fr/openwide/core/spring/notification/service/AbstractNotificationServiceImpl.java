package fr.openwide.core.spring.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import fr.openwide.core.spring.util.SpringBeanUtils;

public class AbstractNotificationServiceImpl {
	
	@Autowired
	protected ApplicationContext applicationContext;

	protected INotificationBuilderBaseState builder() {
		INotificationBuilderBaseState notificationBuilder = NotificationBuilder.create();
		SpringBeanUtils.autowireBean(applicationContext, notificationBuilder);
		return notificationBuilder;
	}
}
