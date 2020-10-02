package org.iglooproject.basicapp.core.security.service;

import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.security.service.AbstractCorePermissionEvaluator;
import org.iglooproject.jpa.util.HibernateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;

public class BasicApplicationPermissionEvaluator extends AbstractCorePermissionEvaluator<User> {

	@Autowired
	private IUserPermissionEvaluator userPermissionEvaluator;

	@Autowired
	private IUserGroupPermissionEvaluator userGroupPermissionEvaluator;

	@Autowired
	private IReferenceDataPermissionEvaluator referenceDataPermissionEvaluator;

	@Override
	protected boolean hasPermission(User user, Object targetDomainObject, Permission permission) {
		if (targetDomainObject != null) {
			targetDomainObject = HibernateUtils.unwrap(targetDomainObject); // NOSONAR
		}
		
		if (user != null) {
			user = HibernateUtils.unwrap(user); // NOSONAR
		}
		
		if (targetDomainObject instanceof User) {
			return userPermissionEvaluator.hasPermission(user, (User) targetDomainObject, permission);
		} else if (targetDomainObject instanceof UserGroup) {
			return userGroupPermissionEvaluator.hasPermission(user, (UserGroup) targetDomainObject, permission);
		} else if (targetDomainObject instanceof ReferenceData) {
			return referenceDataPermissionEvaluator.hasPermission(user, (ReferenceData<?>) targetDomainObject, permission);
		}
		
		return false;
	}

}