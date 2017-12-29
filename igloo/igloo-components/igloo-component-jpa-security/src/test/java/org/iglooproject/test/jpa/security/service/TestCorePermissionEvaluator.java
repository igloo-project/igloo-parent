package org.iglooproject.test.jpa.security.service;

import org.springframework.security.acls.model.Permission;

import org.iglooproject.jpa.security.service.AbstractCorePermissionEvaluator;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;

public class TestCorePermissionEvaluator extends AbstractCorePermissionEvaluator<MockUser> {

	@Override
	protected boolean hasPermission(MockUser user, Object targetDomainObject, Permission permission) {
		return true;
	}

}
