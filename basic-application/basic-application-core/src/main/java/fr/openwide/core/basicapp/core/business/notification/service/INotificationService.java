package fr.openwide.core.basicapp.core.business.notification.service;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.exception.ServiceException;

public interface INotificationService {

	void sendExampleNotification(User user)
			throws ServiceException;

}
