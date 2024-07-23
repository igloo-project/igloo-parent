package org.iglooproject.spring.notification.util;

public enum NotificationSendMode {

  /** Standard mode, email notifications are sent to targeted recipients */
  SEND,

  /**
   * Filtered mode, email notifications are sent to configured list of recipients see property
   * <b>notification.mail.filter.emails<b>
   *
   * <p>Usually used for preproduction and qualification environments
   */
  FILTER_RECIPIENTS,

  /**
   * No email notifications is sent by the application
   *
   * <p>Usually used for tests
   */
  NO_EMAIL;
}
