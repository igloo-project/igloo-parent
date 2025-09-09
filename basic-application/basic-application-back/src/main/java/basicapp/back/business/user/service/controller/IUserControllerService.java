package basicapp.back.business.user.service.controller;

import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_BASIC_WRITE;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_CLOSE_ANNONCEMENT;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_DISABLE;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_ENABLE;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_OPEN_ANNONCEMENT;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_TECHNICAL_WRITE;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_WRITE;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.notification.service.exception.NotificationException;
import basicapp.back.business.user.model.User;
import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IUserControllerService {

  @PreAuthorize(USER_BASIC_WRITE)
  void saveBasicUser(@PermissionObject User user, String password)
      throws SecurityServiceException, NotificationException;

  @PreAuthorize(USER_TECHNICAL_WRITE)
  void saveTechnicalUser(@PermissionObject User user, String password)
      throws SecurityServiceException, NotificationException;

  @PreAuthorize(USER_WRITE)
  void updateRoles(@PermissionObject User user);

  @PreAuthorize(USER_ENABLE)
  void enable(@PermissionObject User user);

  @PreAuthorize(USER_DISABLE)
  void disable(@PermissionObject User user);

  @PreAuthorize(USER_OPEN_ANNONCEMENT)
  void openAnnouncement(@PermissionObject User user);

  @PreAuthorize(USER_CLOSE_ANNONCEMENT)
  void closeAnnouncement(@PermissionObject User user);

  void initPasswordRecoveryRequest(EmailAddress emailAddress) throws NotificationException;

  void onSignIn(User user);

  void onSignInFail(User user);

  User getByEmailAddressCaseInsensitive(EmailAddress emailAddress);

  User getByUsername(String value);
}
