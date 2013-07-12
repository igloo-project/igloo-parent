package fr.openwide.core.test.jpa.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import fr.openwide.core.jpa.security.service.AbstractCorePermissionEvaluator;
import fr.openwide.core.test.jpa.security.business.person.model.MockPerson;
import fr.openwide.core.test.jpa.security.business.person.service.IMockPersonService;

public class TestCorePermissionEvaluator extends AbstractCorePermissionEvaluator<MockPerson> {
	
	@Autowired
	private IMockPersonService personService;

	@Override
	protected boolean hasPermission(MockPerson user, Object targetDomainObject, Permission permission) {
		return true;
	}

	@Override
	protected MockPerson getUser(Authentication authentication) {
		if (authentication == null) {
			return null;
		}

		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails details = (UserDetails) authentication.getPrincipal();
			String userName = details.getUsername();
			return personService.getByUserName(userName);
		}

		return null;
	}

}
