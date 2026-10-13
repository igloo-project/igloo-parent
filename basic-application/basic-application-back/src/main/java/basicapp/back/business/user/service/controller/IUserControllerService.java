package basicapp.back.business.user.service.controller;

import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_ANNONCEMENT_CLOSE_USER;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_ANNONCEMENT_OPEN_USER;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_BASIC_WRITE_ADMIN;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_DISABLE_ADMIN;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_ENABLE_ADMIN;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_TECHNICAL_WRITE_ADMIN;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.USER_WRITE_ADMIN;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.user.model.User;
import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IUserControllerService {

  @PreAuthorize(USER_BASIC_WRITE_ADMIN)
  void saveBasicUserAdmin(@PermissionObject User user)
      throws SecurityServiceException, ServiceException;

  @PreAuthorize(USER_TECHNICAL_WRITE_ADMIN)
  void saveTechnicalUserAdmin(@PermissionObject User user)
      throws SecurityServiceException, ServiceException;

  @PreAuthorize(USER_WRITE_ADMIN)
  void updateRolesAdmin(@PermissionObject User user)
      throws SecurityServiceException, ServiceException;

  @PreAuthorize(USER_ENABLE_ADMIN)
  void enableAdmin(@PermissionObject User user) throws SecurityServiceException, ServiceException;

  @PreAuthorize(USER_DISABLE_ADMIN)
  void disableAdmin(@PermissionObject User user) throws SecurityServiceException, ServiceException;

  @PreAuthorize(USER_ANNONCEMENT_OPEN_USER)
  void openAnnouncementUser(@PermissionObject User user)
      throws ServiceException, SecurityServiceException;

  @PreAuthorize(USER_ANNONCEMENT_CLOSE_USER)
  void closeAnnouncementUser(@PermissionObject User user)
      throws ServiceException, SecurityServiceException;

  User getByEmailAddressCaseInsensitive(EmailAddress emailAddress);

  User getByUsername(String value);
}
