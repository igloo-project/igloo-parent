package fr.openwide.core.spring.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import fr.openwide.core.spring.util.SpringBeanUtils;

public class AbstractNotificationServiceImpl {
	
	@Autowired
	protected ApplicationContext applicationContext;

	protected final INotificationBuilderBaseState builder() {
		INotificationBuilderBaseState notificationBuilder = new NotificationBuilder();
		SpringBeanUtils.autowireBean(applicationContext, notificationBuilder);
		return notificationBuilder;
	}
}
