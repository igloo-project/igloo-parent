package org.iglooproject.basicapp.web.application.common.template.resources.styles.notification.head;

import org.iglooproject.wicket.more.css.scss.ScssResourceReference;

public final class NotificationHeadScssResourceReference extends ScssResourceReference {

	private static final long serialVersionUID = 8223184641393550144L;

	private static final NotificationHeadScssResourceReference INSTANCE = new NotificationHeadScssResourceReference();

	private NotificationHeadScssResourceReference() {
		super(NotificationHeadScssResourceReference.class, "notification-head.scss");
	}

	public static NotificationHeadScssResourceReference get() {
		return INSTANCE;
	}

}
