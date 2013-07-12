package fr.openwide.core.basicapp.core.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.jpa.security.service.CorePermissionEvaluator;
import fr.openwide.core.jpa.security.service.ISecurityService;

public class BasicApplicationPermissionEvaluator extends CorePermissionEvaluator<User> {

	@Autowired
	private ISecurityService securityService;

	@Autowired
	private IUserService userService;

	public BasicApplicationPermissionEvaluator() {
	}

	@Override
	protected boolean hasPermission(User user, Object targetDomainObject, Permission permission) {
		if (securityService.hasAdminRole(user)) {
			return true;
		}

		return false;
	}

	@Override
	protected User getUser(Authentication authentication) {
		if (authentication == null) {
			return null;
		}

		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails details = (UserDetails) authentication.getPrincipal();
			String userName = details.getUsername();
			return userService.getByUserName(userName);
		}

		return null;
	}

}