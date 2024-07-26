package org.iglooproject.basicapp.core.security.service;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<User>
    implements IUserPermissionEvaluator {

  @Override
  public boolean hasPermission(User user, User targetUser, Permission permission) {
    return false;
  }
}
