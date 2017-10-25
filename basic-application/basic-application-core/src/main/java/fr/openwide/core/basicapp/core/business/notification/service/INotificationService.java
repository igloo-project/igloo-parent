package fr.openwide.core.basicapp.core.business.notification.service;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.exception.ServiceException;

public interface INotificationService {

	void sendExampleNotification(User user) throws ServiceException;
	
	/**
	 * Sends a mail to user from a specified contact
	 * @param userTo : recipient user
	 * @param from : "from" mail address
	 * @throws ServiceException
	 */
	void sendExampleNotification(User userTo, String from) throws ServiceException;

	void sendUserPasswordRecoveryRequest(User user) throws ServiceException;

}
