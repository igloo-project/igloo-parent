package org.iglooproject.spring.notification.service;

public interface INotificationBuilderBaseState extends INotificationBuilderReplyToState {

  /**
   * Defines the "from" email address
   *
   * @param from
   * @return
   */
  INotificationBuilderReplyToState from(String from);

  /**
   * Defines the sender email address that may need to differ from the "from" address due to SPF
   * rules Default value is defined through the notification.mail.sender parameter
   *
   * @param sender
   * @return
   */
  INotificationBuilderBaseState sender(String sender);
}
