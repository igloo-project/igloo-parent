package fr.openwide.core.test.jpa.security.business.person.service;

import org.springframework.security.access.annotation.Secured;

import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.jpa.security.business.person.service.IGenericUserService;
import fr.openwide.core.test.jpa.security.business.person.model.MockUser;

public interface IMockUserService extends IGenericUserService<MockUser> {

	@Secured(CoreAuthorityConstants.ROLE_ADMIN)
	void protectedMethodRoleAdmin();

	@Secured(CoreAuthorityConstants.ROLE_AUTHENTICATED)
	void protectedMethodRoleAuthenticated();

}
