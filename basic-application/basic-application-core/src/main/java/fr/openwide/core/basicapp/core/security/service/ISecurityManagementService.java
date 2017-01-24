package fr.openwide.core.basicapp.core.security.service;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import fr.openwide.core.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import fr.openwide.core.basicapp.core.security.model.SecurityOptions;
import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface ISecurityManagementService extends ITransactionalAspectAwareService {

	SecurityOptions getOptions(Class<? extends User> clazz);

	SecurityOptions getOptions(User user);

	void initiatePasswordRecoveryRequest(User user, UserPasswordRecoveryRequestType type,
			UserPasswordRecoveryRequestInitiator initiator) throws ServiceException, SecurityServiceException;

	void initiatePasswordRecoveryRequest(User user, UserPasswordRecoveryRequestType type,
			UserPasswordRecoveryRequestInitiator initiator, User author) throws ServiceException,
			SecurityServiceException;

	boolean isPasswordExpired(User user);

	boolean isPasswordRecoveryRequestExpired(User user);

	void updatePassword(User user, String password) throws ServiceException, SecurityServiceException;

	void updatePassword(User user, String password, User author) throws ServiceException, SecurityServiceException;

	boolean checkPassword(String password, User user) throws ServiceException, SecurityServiceException;

}
