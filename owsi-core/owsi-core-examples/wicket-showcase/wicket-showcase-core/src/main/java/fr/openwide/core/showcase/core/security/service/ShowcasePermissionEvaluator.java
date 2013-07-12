package fr.openwide.core.showcase.core.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import fr.openwide.core.jpa.security.service.AbstractCorePermissionEvaluator;
import fr.openwide.core.jpa.security.service.ISecurityService;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;

public class ShowcasePermissionEvaluator extends AbstractCorePermissionEvaluator<User> {

	@Autowired
	private ISecurityService securityService;

	@Autowired
	private IUserService userService;

	public ShowcasePermissionEvaluator() {
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