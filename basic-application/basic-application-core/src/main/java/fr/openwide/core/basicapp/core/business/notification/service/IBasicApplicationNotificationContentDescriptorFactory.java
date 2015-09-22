package fr.openwide.core.basicapp.core.business.notification.service;

import java.util.Date;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;

public interface IBasicApplicationNotificationContentDescriptorFactory<TDescriptor extends INotificationContentDescriptor> {

	TDescriptor example(User user, Date date);

	TDescriptor userPasswordRecoveryRequest(User user);

}
