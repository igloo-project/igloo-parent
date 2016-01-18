package fr.openwide.core.basicapp.core.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;

import fr.openwide.core.basicapp.core.business.referential.model.City;
import fr.openwide.core.basicapp.core.business.user.model.User;
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
		if (targetDomainObject != null) {
			targetDomainObject = HibernateUtils.unwrap(targetDomainObject); // NOSONAR
		}
		
		if (user != null) {
			user = HibernateUtils.unwrap(user); // NOSONAR
		}
		
		if (targetDomainObject instanceof City) {
			return defaultGenericListItemPermissionEvaluator.hasPermission(user, (City) targetDomainObject, permission);
		}
		
		return false;
	}

}