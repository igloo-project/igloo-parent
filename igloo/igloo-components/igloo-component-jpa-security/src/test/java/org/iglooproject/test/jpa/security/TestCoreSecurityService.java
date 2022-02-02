package org.iglooproject.test.jpa.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.Callable;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

class TestCoreSecurityService extends AbstractJpaSecurityTestCase {

	@Test
	void testRoleHierarchy() throws ServiceException, SecurityServiceException {
		MockUser admin = createMockUser("admin", "firstName", "lastName");
		admin.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_ADMIN));
		mockUserService.update(admin);
		
		authenticateAs(admin);
		assertFalse(securityService.hasSystemRole(admin));
		assertTrue(securityService.hasAdminRole(admin));
		assertTrue(securityService.hasAuthenticatedRole(admin));
		
		MockUser authenticated = createMockUser("authenticated", "firstName", "lastName");
		authenticated.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		mockUserService.update(authenticated);
		
		authenticateAs(authenticated);
		assertFalse(securityService.hasSystemRole(authenticated));
		assertFalse(securityService.hasAdminRole(authenticated));
		assertTrue(securityService.hasAuthenticatedRole(authenticated));
	}
	
	@Test
	void testRunAsSystem() {
		assertTrue(securityService.runAsSystem(new Callable<Boolean>() {
			@Override
			public Boolean call() {
				return securityService.hasSystemRole(SecurityContextHolder.getContext().getAuthentication());
			}
		}));
	}
}
