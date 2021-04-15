package org.iglooproject.basicapp.core.business.notification.service;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.exception.ServiceException;

public interface INotificationService {

	void sendExampleNotification(User user) throws ServiceException;

	void sendExampleNotification(User userTo, String from) throws ServiceException;

	void sendUserPasswordRecoveryRequest(User user) throws ServiceException;

}
