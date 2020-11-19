package org.iglooproject.basicapp.core.security.service;

import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;
import static org.iglooproject.jpa.security.model.CorePermissionConstants.CREATE;
import static org.iglooproject.jpa.security.model.CorePermissionConstants.DELETE;
import static org.iglooproject.jpa.security.model.CorePermissionConstants.READ;
import static org.iglooproject.jpa.security.model.CorePermissionConstants.WRITE;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.predicate.UserGroupPredicates;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
public class UserGroupPermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<UserGroup> implements IUserGroupPermissionEvaluator {

	@Override
	public boolean hasPermission(User user, UserGroup userGroup, Permission permission) {
		if (is(permission, READ)) {
			return true;
		} else if (is(permission, CREATE)) {
			return hasRole(user, ROLE_ADMIN);
		} else if (is(permission, WRITE)) {
			return hasRole(user, ROLE_ADMIN)
				&& UserGroupPredicates.unlocked().apply(userGroup);
		} else if (is(permission, DELETE)) {
			return hasRole(user, ROLE_ADMIN)
				&& UserGroupPredicates.unlocked().apply(userGroup);
		}
		
		return false;
	}

}
