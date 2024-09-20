package basicapp.back.security.service.permission;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.ROLE_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.ROLE_WRITE;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.user.model.User;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<Role>
    implements IRolePermissionEvaluator {

  @Override
  public boolean hasPermission(User user, Role role, Permission permission) {
    if (is(permission, ROLE_READ)) {
      return canRead(user);
    }
    if (is(permission, ROLE_WRITE)) {
      return canWrite(user);
    }
    return false;
  }

  @VisibleForTesting
  public boolean canRead(User user) {
    return hasPermission(user, GLOBAL_ROLE_READ);
  }

  @VisibleForTesting
  public boolean canWrite(User user) {
    return canRead(user) && hasPermission(user, GLOBAL_ROLE_WRITE);
  }
}
