package fr.openwide.core.basicapp.web.application.notification.util;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.common.util.UserTypeDescriptor;

public abstract class NotificationUserTypeDescriptor<U extends User> extends
		UserTypeDescriptor<U> implements
		INotificationTypeDescriptor {

	private static final long serialVersionUID = -349656773642244352L;

	public static final NotificationUserTypeDescriptor<User> USER = new NotificationUserTypeDescriptor<User>(User.class, "user") {
		private static final long serialVersionUID = 1L;
		
		@Override
		protected Object readResolve() {
			return USER;
		}
		
		@Override
		public String notificationRessourceKey(String suffix) {
			return "notification.panel.user." + suffix;
		}
	};

	private NotificationUserTypeDescriptor(Class<U> clazz, String name) {
		super(clazz, name);
	}

}
