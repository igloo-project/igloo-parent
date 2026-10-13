package basicapp.back.security.service.permission;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_ANNONCEMENT_CLOSE_USER;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_ANNONCEMENT_OPEN_USER;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_BASIC_WRITE_ADMIN;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_DISABLE_ADMIN;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_ENABLE_ADMIN;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_PASSWORD_RESET_ADMIN;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_PASSWORD_UPDATE_USER;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_READ_ADMIN;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_TECHNICAL_WRITE_ADMIN;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_WRITE_ADMIN;
import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.predicate.UserPredicates;
import basicapp.back.security.service.ISecurityManagementService;
import com.google.common.annotations.VisibleForTesting;
import java.util.Objects;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<User>
    implements IUserPermissionEvaluator {

  private final ISecurityManagementService securityManagementService;

  public UserPermissionEvaluatorImpl(ISecurityManagementService securityManagementService) {
    this.securityManagementService = securityManagementService;
  }

  @Override
  public boolean hasPermission(User user, User targetUser, Permission permission) {
    // Admin
    if (is(permission, USER_READ_ADMIN)) {
      return canReadAdmin(user, targetUser);
    } else if (is(permission, USER_WRITE_ADMIN)) {
      return canWriteAdmin(user, targetUser);
    } else if (is(permission, USER_TECHNICAL_WRITE_ADMIN)) {
      return canWriteTechnicalUserAdmin(user);
    } else if (is(permission, USER_BASIC_WRITE_ADMIN)) {
      return canWriteBasicUserAdmin(user);
    } else if (is(permission, USER_ENABLE_ADMIN)) {
      return canEnableAdmin(user, targetUser);
    } else if (is(permission, USER_DISABLE_ADMIN)) {
      return canDisableAdmin(user, targetUser);
    } else if (is(permission, USER_PASSWORD_RESET_ADMIN)) {
      return canResetPasswordAdmin(user, targetUser);
    }

    // User
    if (is(permission, USER_PASSWORD_UPDATE_USER)) {
      return canUpdatePasswordUser(user, targetUser);
    } else if (is(permission, USER_ANNONCEMENT_OPEN_USER)) {
      return canOpenAnnouncementUser(user, targetUser);
    } else if (is(permission, USER_ANNONCEMENT_CLOSE_USER)) {
      return canCloseAnnouncementUser(user, targetUser);
    }

    return false;
  }

  // Admin

  @VisibleForTesting
  public boolean canReadAdmin(User user, User targetUser) {
    return UserPredicates.technical().apply(targetUser)
        ? canReadTechnicalUserAdmin(user)
        : canReadBasicUserAdmin(user);
  }

  private boolean canReadTechnicalUserAdmin(User user) {
    return hasRole(user, ROLE_ADMIN);
  }

  private boolean canReadBasicUserAdmin(User user) {
    return hasPermission(user, GLOBAL_USER_READ);
  }

  @VisibleForTesting
  public boolean canWriteAdmin(User user, User targetUser) {
    return UserPredicates.technical().apply(targetUser)
        ? canWriteTechnicalUserAdmin(user)
        : canWriteBasicUserAdmin(user);
  }

  private boolean canWriteTechnicalUserAdmin(User user) {
    return hasRole(user, ROLE_ADMIN);
  }

  private boolean canWriteBasicUserAdmin(User user) {
    return hasPermission(user, GLOBAL_USER_WRITE);
  }

  @VisibleForTesting
  public boolean canEnableAdmin(User user, User targetUser) {
    return !targetUser.isEnabled() && canWriteAdmin(user, targetUser);
  }

  @VisibleForTesting
  public boolean canDisableAdmin(User user, User targetUser) {
    return !Objects.equals(user, targetUser)
        && targetUser.isEnabled()
        && canWriteAdmin(user, targetUser);
  }

  @VisibleForTesting
  public boolean canResetPasswordAdmin(User user, User targetUser) {
    return canWriteAdmin(user, targetUser)
        && securityManagementService
            .getSecurityOptions(targetUser)
            .isPasswordAdminRecoveryEnabled();
  }

  // User

  @VisibleForTesting
  public boolean canUpdatePasswordUser(User user, User targetUser) {
    return Objects.equals(user, targetUser);
  }

  @VisibleForTesting
  public boolean canOpenAnnouncementUser(User user, User targetUser) {
    return Objects.equals(user, targetUser);
  }

  @VisibleForTesting
  public boolean canCloseAnnouncementUser(User user, User targetUser) {
    return Objects.equals(user, targetUser);
  }
}
