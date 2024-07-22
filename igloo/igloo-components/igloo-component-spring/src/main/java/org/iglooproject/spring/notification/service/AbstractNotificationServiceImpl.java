package org.iglooproject.spring.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class AbstractNotificationServiceImpl {

  @Autowired protected ApplicationContext applicationContext;

  protected INotificationBuilderBaseState builder() {
    INotificationBuilderInitState notificationBuilder = NotificationBuilder.create();
    return notificationBuilder.init(applicationContext);
  }
}
