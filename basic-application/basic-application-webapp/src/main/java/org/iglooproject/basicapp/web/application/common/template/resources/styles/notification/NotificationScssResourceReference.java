package org.iglooproject.basicapp.web.application.common.template.resources.styles.notification;

import org.iglooproject.wicket.more.css.scss.ScssResourceReference;

public final class NotificationScssResourceReference extends ScssResourceReference {

  private static final long serialVersionUID = 6317731503959025552L;

  private static final NotificationScssResourceReference INSTANCE =
      new NotificationScssResourceReference();

  private NotificationScssResourceReference() {
    super(NotificationScssResourceReference.class, "notification.scss");
  }

  public static NotificationScssResourceReference get() {
    return INSTANCE;
  }
}
