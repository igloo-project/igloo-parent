package basicapp.back.business.notification.service;

import java.time.Instant;

import org.iglooproject.spring.notification.model.INotificationContentDescriptor;

import basicapp.back.business.user.model.User;

public interface IBasicApplicationNotificationContentDescriptorFactory {

	INotificationContentDescriptor example(User user, Instant instant);

	INotificationContentDescriptor userPasswordRecoveryRequest(User user, Instant instant);

}
