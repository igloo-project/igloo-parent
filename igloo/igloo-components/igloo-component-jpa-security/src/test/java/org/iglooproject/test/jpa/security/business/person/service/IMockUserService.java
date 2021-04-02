package org.iglooproject.test.jpa.security.business.person.service;

import org.springframework.security.access.annotation.Secured;

import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.user.service.IGenericUserService;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;

public interface IMockUserService extends IGenericUserService<MockUser> {

	@Secured(CoreAuthorityConstants.ROLE_ADMIN)
	void protectedMethodRoleAdmin();

	@Secured(CoreAuthorityConstants.ROLE_AUTHENTICATED)
	void protectedMethodRoleAuthenticated();

	Long countEnabled();

}
