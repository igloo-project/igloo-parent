package fr.openwide.core.basicapp.core.business.notification.service;

import java.util.Date;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;
import fr.openwide.core.spring.notification.util.NotificationContentDescriptors;


/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
public class EmptyNotificationContentDescriptorFactoryImpl implements IBasicApplicationNotificationContentDescriptorFactory {
	
	private static final INotificationContentDescriptor DEFAULT_DESCRIPTOR =
			NotificationContentDescriptors.explicit("defaultSubject", "defaultTextBody", "defaultHtmlBody");

	@Override
	public INotificationContentDescriptor example(User user, Date date) {
		return DEFAULT_DESCRIPTOR;
	}

	@Override
	public INotificationContentDescriptor userPasswordRecoveryRequest(User user) {
		return DEFAULT_DESCRIPTOR;
	}

}
