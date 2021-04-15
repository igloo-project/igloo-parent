package org.iglooproject.basicapp.core.business.notification.service;

import java.time.Instant;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;

public interface IBasicApplicationNotificationContentDescriptorFactory {

	INotificationContentDescriptor example(User user, Instant instant);

	INotificationContentDescriptor userPasswordRecoveryRequest(User user, Instant instant);

}
