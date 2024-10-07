package basicapp.back.security.service.permission;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.ADMIN_EDIT_PASSWORD;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.ADMIN_RECOVERY_PASSWORD;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_BASIC_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_CLOSE_ANNONCEMENT;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_DISABLE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_EDIT_PASSWORD;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_ENABLE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_OPEN_ANNONCEMENT;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_RECOVERY_PASSWORD;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_TECHNICAL_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_WRITE;
import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.predicate.UserPredicates;
import basicapp.back.security.service.ISecurityManagementService;
import com.google.common.annotations.VisibleForTesting;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<User>
    implements IUserPermissionEvaluator {

  private final ISecurityManagementService securityManagementService;

  @Autowired
  public UserPermissionEvaluatorImpl(ISecurityManagementService securityManagementService) {
    this.securityManagementService = securityManagementService;
  }

  @Override
  public boolean hasPermission(User user, User targetUser, Permission permission) {
    if (is(permission, USER_READ)) {
      return canReadUser(user, targetUser);
    } else if (is(permission, USER_WRITE)) {
      return canWriteUser(user, targetUser);
    } else if (is(permission, USER_TECHNICAL_WRITE)) {
      return canWriteTechicalUser(user);
    } else if (is(permission, USER_BASIC_WRITE)) {
      return canWriteBasicUser(user, targetUser);
    } else if (is(permission, USER_ENABLE)) {
      return canEnableUser(user, targetUser);
    } else if (is(permission, USER_DISABLE)) {
      return canDisableUser(user, targetUser);
    } else if (is(permission, USER_EDIT_PASSWORD)) {
      return canUserEditPassword(user, targetUser);
    } else if (is(permission, USER_RECOVERY_PASSWORD)) {
      return canUserRecoveryPassword(user, targetUser);
    } else if (is(permission, ADMIN_EDIT_PASSWORD)) {
      return canAdminEditPassword(user, targetUser);
    } else if (is(permission, ADMIN_RECOVERY_PASSWORD)) {
      return canAdminRecoveryPassword(user, targetUser);
    } else if (is(permission, USER_OPEN_ANNONCEMENT)) {
      return canOpenAnnouncement(user, targetUser);
    } else if (is(permission, USER_CLOSE_ANNONCEMENT)) {
      return canCloseAnnouncement(user, targetUser);
    }
    return false;
  }

  @VisibleForTesting
  public boolean canReadUser(User user, User targetUser) {
    return UserPredicates.technical().apply(targetUser)
        ? canReadTechnicalUser(user)
        : canReadBasicUser(user, targetUser);
  }

  private boolean canReadTechnicalUser(User user) {
    return hasRole(user, ROLE_ADMIN);
  }

  private boolean canReadBasicUser(User user, User targetUser) {
    return Objects.equals(user, targetUser) || hasPermission(user, GLOBAL_USER_READ);
  }

  @VisibleForTesting
  public boolean canWriteUser(User user, User targetUser) {
    return UserPredicates.technical().apply(targetUser)
        ? canWriteTechicalUser(user)
        : canWriteBasicUser(user, targetUser);
  }

  private boolean canWriteTechicalUser(User user) {
    return hasRole(user, ROLE_ADMIN);
  }

  private boolean canWriteBasicUser(User user, User targetUser) {
    return Objects.equals(user, targetUser) || hasPermission(user, GLOBAL_USER_WRITE);
  }

  @VisibleForTesting
  public boolean canEnableUser(User user, User targetUser) {
    if (targetUser.isEnabled()) {
      return false;
    }

    return UserPredicates.technical().apply(targetUser)
        ? hasRole(user, ROLE_ADMIN)
        : hasPermission(user, GLOBAL_USER_WRITE);
  }

  @VisibleForTesting
  public boolean canDisableUser(User user, User targetUser) {
    if (Objects.equals(user, targetUser) || !targetUser.isEnabled()) {
      return false;
    }

    return UserPredicates.technical().apply(targetUser)
        ? hasRole(user, ROLE_ADMIN)
        : hasPermission(user, GLOBAL_USER_WRITE);
  }

  private boolean canAdminEditPassword(User user, User targetUser) {
    if (!securityManagementService.getSecurityOptions(targetUser).isPasswordAdminUpdateEnabled()) {
      return false;
    }

    return UserPredicates.technical().apply(targetUser)
        ? hasRole(user, ROLE_ADMIN)
        : hasPermission(user, GLOBAL_USER_WRITE);
  }

  @VisibleForTesting
  public boolean canUserEditPassword(User user, User targetUser) {
    if (user != null && !canWriteUser(user, targetUser)) {
      return false;
    }
    return securityManagementService.getSecurityOptions(targetUser).isPasswordUserUpdateEnabled();
  }

  @VisibleForTesting
  public boolean canUserRecoveryPassword(User user, User targetUser) {
    if (user != null && !canWriteUser(user, targetUser)) {
      return false;
    }
    return securityManagementService.getSecurityOptions(targetUser).isPasswordUserRecoveryEnabled();
  }

  @VisibleForTesting
  public boolean canAdminRecoveryPassword(User user, User targetUser) {
    if (!securityManagementService
        .getSecurityOptions(targetUser)
        .isPasswordAdminRecoveryEnabled()) {
      return false;
    }

    return UserPredicates.technical().apply(targetUser)
        ? hasRole(user, ROLE_ADMIN)
        : hasPermission(user, GLOBAL_USER_WRITE);
  }

  @VisibleForTesting
  public boolean canOpenAnnouncement(User user, User targetUser) {
    return Objects.equals(user, targetUser) && UserPredicates.announcementClose().apply(targetUser);
  }

  @VisibleForTesting
  public boolean canCloseAnnouncement(User user, User targetUser) {
    return Objects.equals(user, targetUser) && UserPredicates.announcementOpen().apply(targetUser);
  }
}
