package org.iglooproject.spring.notification.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Callable;
import org.iglooproject.spring.notification.service.NotificationBuilder;

public final class NotificationUtils {

  private NotificationUtils() {}

  private static final ThreadLocal<Boolean> NOTIFICATIONS_ENABLED =
      new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
          return true;
        }
      };

  public static boolean isNotificationsEnabled() {
    return NOTIFICATIONS_ENABLED.get();
  }

  /**
   * Executes a {@link Runnable} with all notifications from all {@link NotificationBuilder}s
   * disabled.
   */
  public static void executeSilently(Runnable runnable) {
    checkNotNull(runnable);
    try {
      NOTIFICATIONS_ENABLED.set(false);
      runnable.run();
    } finally {
      NOTIFICATIONS_ENABLED.set(true);
    }
  }

  /**
   * Executes a {@link Callable} with all notifications from all {@link NotificationBuilder}s
   * disabled.
   *
   * @throws Exception The exact same exception thrown by {@code callable}, if any.
   */
  public static <T> T executeSilently(Callable<T> callable) throws Exception {
    checkNotNull(callable);
    try {
      NOTIFICATIONS_ENABLED.set(false);
      return callable.call();
    } finally {
      NOTIFICATIONS_ENABLED.set(true);
    }
  }
}
