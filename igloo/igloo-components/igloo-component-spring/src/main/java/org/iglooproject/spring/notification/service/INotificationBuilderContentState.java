package org.iglooproject.spring.notification.service;

public interface INotificationBuilderContentState extends INotificationBuilderSendState {

  INotificationBuilderTemplateState template(String templateKey);
}
