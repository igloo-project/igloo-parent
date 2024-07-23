package org.iglooproject.spring.notification.service;

import java.util.Locale;
import java.util.Map;

public interface INotificationBuilderTemplateState extends INotificationBuilderSendState {

  INotificationBuilderTemplateState variable(String name, Object value);

  INotificationBuilderTemplateState variable(String name, Object value, Locale locale);

  INotificationBuilderTemplateState variables(Map<String, ?> variables);

  INotificationBuilderTemplateState variables(Map<String, ?> variables, Locale locale);
}
