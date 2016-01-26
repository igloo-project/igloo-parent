package fr.openwide.core.basicapp.core.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.security.service.AbstractCorePermissionEvaluator;
import fr.openwide.core.jpa.security.service.ISecurityService;
import fr.openwide.core.jpa.util.HibernateUtils;

public class BasicApplicationPermissionEvaluator extends AbstractCorePermissionEvaluator<User> {

	@Autowired
	private ISecurityService securityService;

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