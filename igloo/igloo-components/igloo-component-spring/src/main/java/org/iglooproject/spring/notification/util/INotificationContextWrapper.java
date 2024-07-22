package org.iglooproject.spring.notification.util;

import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;

public interface INotificationContextWrapper {

  /**
   * @param recipient The notification recipient.
   * @param contextDescriptor The notification content descriptor
   * @return A notification content descriptor that wraps <code>contextDescriptor</code> and sets up
   *     context relative to the recipient before executing the wrapped descriptor. This content
   *     descriptor implements {@link #equals(Object)} and {@link #hashCode()} so that the rendering
   *     of a single notification for multiple recipients whose context is the same may be executed
   *     only once.
   */
  INotificationContentDescriptor contextualize(
      INotificationRecipient recipient, INotificationContentDescriptor contextDescriptor);
}
