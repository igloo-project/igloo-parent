package org.iglooproject.spring.notification.service;

import java.util.Locale;

public interface INotificationBuilderBodyState {

  INotificationBuilderSendState textBody(String textBody);

  INotificationBuilderSendState textBody(String textBody, Locale locale);

  INotificationBuilderSendState htmlBody(String htmlBody);

  INotificationBuilderSendState htmlBody(String htmlBody, Locale locale);
}
