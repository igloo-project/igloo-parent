package basicapp.back.security.service.controller;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.model.BasicApplicationSecurityExpressionConstants;
import basicapp.back.security.model.SecurityOptions;
import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ISecurityManagementControllerService {

  @PreAuthorize(BasicApplicationSecurityExpressionConstants.USER_EDIT_PASSWORD)
  void updatePassword(@PermissionObject User user, String password)
      throws ServiceException, SecurityServiceException;

  @PreAuthorize(BasicApplicationSecurityExpressionConstants.USER_RECOVERY_PASSWORD)
  void initiatePasswordRecoveryRequest(
      @PermissionObject User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws ServiceException, SecurityServiceException;

  @PreAuthorize(BasicApplicationSecurityExpressionConstants.ADMIN_EDIT_PASSWORD)
  void updatePassword(@PermissionObject User user, String password, User author)
      throws ServiceException, SecurityServiceException;

  @PreAuthorize(BasicApplicationSecurityExpressionConstants.ADMIN_RECOVERY_PASSWORD)
  void initiatePasswordRecoveryRequest(
      @PermissionObject User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator,
      User author)
      throws ServiceException, SecurityServiceException;

  boolean checkPassword(String password, User user)
      throws ServiceException, SecurityServiceException;

  boolean isPasswordExpired(User user);

  boolean isPasswordRecoveryRequestExpired(User user);

  SecurityOptions getSecurityOptions(UserType userType);

  SecurityOptions getSecurityOptions(User user);
}
