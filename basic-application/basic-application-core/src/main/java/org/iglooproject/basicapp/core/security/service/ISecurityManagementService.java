package org.iglooproject.basicapp.core.security.service;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import org.iglooproject.basicapp.core.security.model.SecurityOptions;
import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface ISecurityManagementService extends ITransactionalAspectAwareService {

  SecurityOptions getSecurityOptionsDefault();

  SecurityOptions getSecurityOptions(Class<? extends User> clazz);

  SecurityOptions getSecurityOptions(User user);

  void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws ServiceException, SecurityServiceException;

  void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator,
      User author)
      throws ServiceException, SecurityServiceException;

  boolean isPasswordExpired(User user);

  boolean isPasswordRecoveryRequestExpired(User user);

  void updatePassword(User user, String password) throws ServiceException, SecurityServiceException;

  void updatePassword(User user, String password, User author)
      throws ServiceException, SecurityServiceException;

  boolean checkPassword(String password, User user)
      throws ServiceException, SecurityServiceException;
}
