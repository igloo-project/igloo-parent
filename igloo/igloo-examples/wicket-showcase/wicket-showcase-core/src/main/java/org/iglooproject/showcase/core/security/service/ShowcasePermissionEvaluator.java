package org.iglooproject.showcase.core.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;

import org.iglooproject.jpa.security.service.AbstractCorePermissionEvaluator;
import org.iglooproject.jpa.security.service.ISecurityService;
import org.iglooproject.showcase.core.business.user.model.User;

public class ShowcasePermissionEvaluator extends AbstractCorePermissionEvaluator<User> {

	@Autowired
	private ISecurityService securityService;

	public ShowcasePermissionEvaluator() {
	}

	@Override
	protected boolean hasPermission(User user, Object targetDomainObject, Permission permission) {
		if (securityService.hasAdminRole(user)) {
			return true;
		}

		return false;
	}

}