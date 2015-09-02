package fr.openwide.core.basicapp.web.application.common.template.styles.notification;

import fr.openwide.core.wicket.more.css.lesscss.LessCssResourceReference;

public final class NotificationLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 6317731503959025552L;
	
	private static final NotificationLessCssResourceReference INSTANCE = new NotificationLessCssResourceReference();
	
	private NotificationLessCssResourceReference() {
		super(NotificationLessCssResourceReference.class, "notification.less");
	}

	public static NotificationLessCssResourceReference get() {
		return INSTANCE;
	}

}
