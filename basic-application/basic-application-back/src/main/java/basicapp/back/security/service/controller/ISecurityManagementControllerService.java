package basicapp.back.security.service.controller;

import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_PASSWORD_RESET_ADMIN;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_PASSWORD_UPDATE_USER;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.model.SecurityOptions;
import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ISecurityManagementControllerService {

  @PreAuthorize(USER_PASSWORD_RESET_ADMIN)
  void resetPasswordAdmin(@PermissionObject User user, UserPasswordRecoveryRequestType type)
      throws ServiceException, SecurityServiceException;

  @PreAuthorize(USER_PASSWORD_UPDATE_USER)
  void updatePasswordUser(@PermissionObject User user, String password)
      throws ServiceException, SecurityServiceException;

  void resetPasswordUser(EmailAddress emailAddress)
      throws SecurityServiceException, ServiceException;

  boolean isPasswordRecoveryRequestValid(User user, String token);

  void updatePasswordUserFromRecoveryRequest(User user, String token, String password)
      throws ServiceException, SecurityServiceException;

  boolean checkPassword(String password, User user)
      throws ServiceException, SecurityServiceException;

  boolean isPasswordExpired(User user);

  void onSignIn(User user) throws ServiceException, SecurityServiceException;

  void onSignInFail(String username) throws ServiceException, SecurityServiceException;

  SecurityOptions getSecurityOptions(UserType userType);

  SecurityOptions getSecurityOptions(User user);
}
