package fr.openwide.core.test.jpa.security.business.person.service;

import org.springframework.security.access.annotation.Secured;

import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.jpa.security.business.person.service.IPersonService;
import fr.openwide.core.test.jpa.security.business.person.model.MockPerson;

public interface IMockPersonService extends IPersonService<MockPerson> {

	@Secured(CoreAuthorityConstants.ROLE_ADMIN)
	void protectedMethodRoleAdmin();

	@Secured(CoreAuthorityConstants.ROLE_AUTHENTICATED)
	void protectedMethodRoleAuthenticated();

}
