package org.iglooproject.basicapp.core.security.service;

import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.security.service.AbstractCorePermissionEvaluator;
import org.iglooproject.jpa.util.HibernateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;

public class BasicApplicationPermissionEvaluator extends AbstractCorePermissionEvaluator<User> {

	@Autowired
	private ILocalizedReferenceDataPermissionEvaluator localizedReferenceDataPermissionEvaluator;
	
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
		
		if (targetDomainObject instanceof LocalizedReferenceData) {
			return localizedReferenceDataPermissionEvaluator.hasPermission(user, (LocalizedReferenceData<?>) targetDomainObject, permission);
		}
		
		return false;
	}
}