package basicapp.back.security.service;

import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

import basicapp.back.business.user.model.User;

@Service
public class UserPermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<User> implements IUserPermissionEvaluator {

	@Override
	public boolean hasPermission(User user, User targetUser, Permission permission) {
		return false;
	}

}
