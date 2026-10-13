package basicapp.back.security.service.controller;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.service.business.IUserService;
import basicapp.back.security.model.SecurityOptions;
import basicapp.back.security.service.ISecurityManagementService;
import java.util.Objects;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class SecurityManagerControllerService implements ISecurityManagementControllerService {

  private final ISecurityManagementService securityManagementService;
  private final IUserService userService;

  public SecurityManagerControllerService(
      ISecurityManagementService securityManagementService, IUserService userService) {
    this.securityManagementService = securityManagementService;
    this.userService = userService;
  }

  @Override
  public void resetPasswordAdmin(User user, UserPasswordRecoveryRequestType type)
      throws ServiceException, SecurityServiceException {
    securityManagementService.resetPassword(user, type, UserPasswordRecoveryRequestInitiator.ADMIN);
  }

  @Override
  public void updatePasswordUser(User user, String password)
      throws ServiceException, SecurityServiceException {
    securityManagementService.updatePassword(user, password);
  }

  @Override
  public void resetPasswordUser(EmailAddress emailAddress)
      throws SecurityServiceException, ServiceException {
    User user = userService.getByEmailAddressCaseInsensitive(emailAddress);

    if (user != null
        && user.isEnabled()
        && user.isNotificationEnabled()
        && securityManagementService.getSecurityOptions(user).isPasswordUserRecoveryEnabled()) {
      securityManagementService.resetPassword(
          user,
          securityManagementService.hasPassword(user)
              ? UserPasswordRecoveryRequestType.RESET
              : UserPasswordRecoveryRequestType.CREATION,
          UserPasswordRecoveryRequestInitiator.USER);
    }
  }

  @Override
  public boolean isPasswordRecoveryRequestValid(User user, String token) {
    return securityManagementService.isPasswordRecoveryRequestValid(user, token);
  }

  @Override
  public void updatePasswordUserFromRecoveryRequest(User user, String token, String password)
      throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    if (!securityManagementService.isPasswordRecoveryRequestValid(user, token)) {
      throw new SecurityServiceException("Invalid or expired password recovery request");
    }
    securityManagementService.updatePassword(user, password);
  }

  @Override
  public boolean checkPassword(String password, User user)
      throws ServiceException, SecurityServiceException {
    return securityManagementService.checkPassword(password, user);
  }

  @Override
  public boolean isPasswordExpired(User user) {
    return securityManagementService.isPasswordExpired(user);
  }

  @Override
  public void onSignIn(User user) throws ServiceException, SecurityServiceException {
    securityManagementService.onSignIn(user);
  }

  @Override
  public void onSignInFail(String username) throws ServiceException, SecurityServiceException {
    securityManagementService.onSignInFail(username);
  }

  @Override
  public SecurityOptions getSecurityOptions(UserType userType) {
    return securityManagementService.getSecurityOptions(userType);
  }

  @Override
  public SecurityOptions getSecurityOptions(User user) {
    return securityManagementService.getSecurityOptions(user);
  }
}
