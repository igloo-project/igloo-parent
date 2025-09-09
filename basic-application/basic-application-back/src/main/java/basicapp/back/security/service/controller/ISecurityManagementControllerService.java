package basicapp.back.security.service.controller;

import basicapp.back.business.notification.service.exception.NotificationException;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.model.BasicApplicationSecurityExpressionConstants;
import basicapp.back.security.model.SecurityOptions;
import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ISecurityManagementControllerService {

  @PreAuthorize(BasicApplicationSecurityExpressionConstants.USER_EDIT_PASSWORD)
  void updatePassword(@PermissionObject User user, String password) throws SecurityServiceException;

  @PreAuthorize(BasicApplicationSecurityExpressionConstants.USER_RECOVERY_PASSWORD)
  void initiatePasswordRecoveryRequest(
      @PermissionObject User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws NotificationException;

  @PreAuthorize(BasicApplicationSecurityExpressionConstants.ADMIN_EDIT_PASSWORD)
  void updatePassword(@PermissionObject User user, String password, User author)
      throws SecurityServiceException;

  @PreAuthorize(BasicApplicationSecurityExpressionConstants.ADMIN_RECOVERY_PASSWORD)
  void initiatePasswordRecoveryRequest(
      @PermissionObject User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator,
      User author)
      throws NotificationException;

  boolean checkPassword(String password, User user);

  boolean isPasswordExpired(User user);

  boolean isPasswordRecoveryRequestExpired(User user);

  SecurityOptions getSecurityOptions(UserType userType);

  SecurityOptions getSecurityOptions(User user);
}
