package org.iglooproject.spring.notification.service;

import org.springframework.context.ApplicationContext;

public interface INotificationBuilderInitState {

  INotificationBuilderBaseState init(ApplicationContext applicationContext);
}
