package fr.openwide.core.test.jpa.security.service;

import org.springframework.security.acls.model.Permission;

import fr.openwide.core.jpa.security.service.AbstractCorePermissionEvaluator;
import fr.openwide.core.test.jpa.security.business.person.model.MockUser;

public class TestCorePermissionEvaluator extends AbstractCorePermissionEvaluator<MockUser> {

	@Override
	protected boolean hasPermission(MockUser user, Object targetDomainObject, Permission permission) {
		return true;
	}

}
