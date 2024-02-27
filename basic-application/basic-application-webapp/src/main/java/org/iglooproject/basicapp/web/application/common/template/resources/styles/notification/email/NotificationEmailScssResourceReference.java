package org.iglooproject.basicapp.web.application.common.template.resources.styles.notification.email;

import org.iglooproject.wicket.more.css.scss.ScssResourceReference;

public final class NotificationEmailScssResourceReference extends ScssResourceReference {

	private static final long serialVersionUID = 1L;

	private static final NotificationEmailScssResourceReference INSTANCE = new NotificationEmailScssResourceReference();

	private NotificationEmailScssResourceReference() {
		super(NotificationEmailScssResourceReference.class, "notification-email.scss");
	}

	public static NotificationEmailScssResourceReference get() {
		return INSTANCE;
	}

}
