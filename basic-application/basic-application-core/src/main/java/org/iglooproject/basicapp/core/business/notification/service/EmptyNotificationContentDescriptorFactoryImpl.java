package org.iglooproject.basicapp.core.business.notification.service;

import java.time.Instant;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.spring.notification.util.NotificationContentDescriptors;

public class EmptyNotificationContentDescriptorFactoryImpl implements IBasicApplicationNotificationContentDescriptorFactory {
	
	private static final INotificationContentDescriptor DEFAULT_DESCRIPTOR =
		NotificationContentDescriptors.explicit("defaultSubject", "defaultTextBody", "defaultHtmlBody");

	@Override
	public INotificationContentDescriptor example(User user, Instant date) {
		return DEFAULT_DESCRIPTOR;
	}

	@Override
	public INotificationContentDescriptor userPasswordRecoveryRequest(User user) {
		return DEFAULT_DESCRIPTOR;
	}

}
