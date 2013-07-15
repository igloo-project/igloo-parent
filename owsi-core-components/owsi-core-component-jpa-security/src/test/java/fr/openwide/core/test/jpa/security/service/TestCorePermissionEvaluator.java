package fr.openwide.core.test.jpa.security.service;

import org.springframework.security.acls.model.Permission;

import fr.openwide.core.jpa.security.service.AbstractCorePermissionEvaluator;
import fr.openwide.core.test.jpa.security.business.person.model.MockPerson;

public class TestCorePermissionEvaluator extends AbstractCorePermissionEvaluator<MockPerson> {

	@Override
	protected boolean hasPermission(MockPerson user, Object targetDomainObject, Permission permission) {
		return true;
	}

}
