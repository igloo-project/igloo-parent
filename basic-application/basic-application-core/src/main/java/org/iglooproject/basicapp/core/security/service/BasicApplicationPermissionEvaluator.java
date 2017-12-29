package org.iglooproject.basicapp.core.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.jpa.security.service.AbstractCorePermissionEvaluator;
import org.iglooproject.jpa.util.HibernateUtils;

public class BasicApplicationPermissionEvaluator extends AbstractCorePermissionEvaluator<User> {

	@Autowired
	private IDefaultGenericListItemPermissionEvaluator defaultGenericListItemPermissionEvaluator;
	
	public BasicApplicationPermissionEvaluator() {
	}

	@Override
	protected boolean hasPermission(User user, Object targetDomainObject, Permission permission) {
		// Call your own permissionEvaluators here
		
		if (targetDomainObject != null) {
			targetDomainObject = HibernateUtils.unwrap(targetDomainObject); // NOSONAR
		}
		
		if (user != null) {
			user = HibernateUtils.unwrap(user); // NOSONAR
		}
		
		if (targetDomainObject instanceof GenericListItem) {
			return defaultGenericListItemPermissionEvaluator.hasPermission(user, (GenericListItem<?>) targetDomainObject, permission);
		}
		
		return false;
	}
}