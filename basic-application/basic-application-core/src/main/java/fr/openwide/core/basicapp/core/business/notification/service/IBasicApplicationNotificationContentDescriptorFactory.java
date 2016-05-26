package fr.openwide.core.basicapp.core.business.notification.service;

import java.util.Date;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;

public interface IBasicApplicationNotificationContentDescriptorFactory {

	INotificationContentDescriptor example(User user, Date date);

	INotificationContentDescriptor userPasswordRecoveryRequest(User user);

}
