package basicapp.back.security.service.controller;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.model.SecurityOptions;
import basicapp.back.security.service.ISecurityManagementService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityManagerControllerService implements ISecurityManagementControllerService {

  private final ISecurityManagementService securityManagementService;

  @Autowired
  public SecurityManagerControllerService(ISecurityManagementService securityManagementService) {
    this.securityManagementService = securityManagementService;
  }

  @Override
  public SecurityOptions getSecurityOptions(UserType userType) {
    return securityManagementService.getSecurityOptions(userType);
  }

  @Override
  public SecurityOptions getSecurityOptions(User user) {
    return securityManagementService.getSecurityOptions(user);
  }

  @Override
  public boolean isPasswordExpired(User user) {
    return securityManagementService.isPasswordExpired(user);
  }

  @Override
  public boolean isPasswordRecoveryRequestExpired(User user) {
    return securityManagementService.isPasswordRecoveryRequestExpired(user);
  }

  @Override
  public void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator,
      User author)
      throws ServiceException, SecurityServiceException {
    securityManagementService.initiatePasswordRecoveryRequest(user, type, initiator, author);
  }

  @Override
  public void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws ServiceException, SecurityServiceException {
    securityManagementService.initiatePasswordRecoveryRequest(user, type, initiator, user);
  }

  @Override
  public void updatePassword(User user, String password)
      throws ServiceException, SecurityServiceException {
    securityManagementService.updatePassword(user, password, user);
  }

  @Override
  public void updatePassword(User user, String password, User author)
      throws ServiceException, SecurityServiceException {
    securityManagementService.updatePassword(user, password, author);
  }

  @Override
  public boolean checkPassword(String password, User user)
      throws ServiceException, SecurityServiceException {
    return securityManagementService.checkPassword(password, user);
  }
}
