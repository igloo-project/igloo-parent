package basicapp.back.security.service;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.model.SecurityOptions;
import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface ISecurityManagementService extends ITransactionalAspectAwareService {

  void updatePassword(User user, String password) throws ServiceException, SecurityServiceException;

  void resetPassword(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws ServiceException, SecurityServiceException;

  void setPassword(User user, String password) throws ServiceException, SecurityServiceException;

  boolean hasPassword(User user);

  boolean checkPassword(String password, User user)
      throws ServiceException, SecurityServiceException;

  boolean isPasswordExpired(User user);

  boolean isPasswordRecoveryRequestValid(User user, String token);

  boolean isPasswordRecoveryRequestExpired(User user);

  void onSignIn(User user) throws ServiceException, SecurityServiceException;

  void onSignInFail(String username) throws ServiceException, SecurityServiceException;

  SecurityOptions getSecurityOptions(UserType userType);

  SecurityOptions getSecurityOptions(User user);
}
