package org.iglooproject.spring.config.util;

import org.iglooproject.spring.notification.service.NotificationBuilder;

/** Behavior applicable when sender is not explicitly set on {@link NotificationBuilder}. */
public enum MailSenderBehavior {

  /** Sender set from configuration. */
  FALLBACK_TO_CONFIGURATION,

  /** Sender set from notification From: value. */
  FALLBACK_TO_FROM,

  /** Sender not modified on NotificationBuilder. Empty values are kept unset. */
  EXPLICIT;
}
