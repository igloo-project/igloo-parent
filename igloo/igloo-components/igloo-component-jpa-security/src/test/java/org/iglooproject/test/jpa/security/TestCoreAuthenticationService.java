package org.iglooproject.test.jpa.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestCoreAuthenticationService extends AbstractJpaSecurityTestCase {

	@Test
	public void testAuthenticationUserInfo() throws ServiceException, SecurityServiceException {
		assertFalse(authenticationService.isLoggedIn());
		
		MockUser user = createMockUser(System.getProperty("user.name"), "firstName", "lastName");
		user.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		mockUserService.update(user);
		
		/*
		 * Pour des raisons de sécurité le mot de passe est effacé après authentification.
		 * on désactive cette option pour les besoins du test suivant.
		 */
		authenticationManager.setEraseCredentialsAfterAuthentication(false);
		
		authenticateAs(user);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		assertTrue(authenticationService.isLoggedIn());
		assertNotNull(authentication);
		assertEquals(user.getUsername(), authentication.getName());
		assertEquals(DEFAULT_PASSWORD, authentication.getCredentials());
		
		assertTrue(authentication.isAuthenticated());
		
		authenticationManager.setEraseCredentialsAfterAuthentication(true);
		
		authenticationService.signOut();
		assertFalse(authenticationService.isLoggedIn());
		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

	@Test
	public void testAuthenticationRoles() throws ServiceException, SecurityServiceException {
		MockUser user = createMockUser(System.getProperty("user.name"), "firstName", "lastName");
		user.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		mockUserService.update(user);
		
		authenticateAs(user);
		
		@SuppressWarnings("unchecked")
		Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) authenticationService.getAuthorities();
		
		assertTrue(grantedAuthorities.size() > 0);
		
		boolean hasRoleSystem = false;
		boolean hasRoleAdmin = false;
		boolean hasRoleAuthenticated = false;
		boolean hasRoleAnonymous = false;
		
		for (GrantedAuthority grantedAuthority : grantedAuthorities) {
			if (CoreAuthorityConstants.ROLE_SYSTEM.equals(grantedAuthority.getAuthority())) {
				hasRoleSystem = true;
			} else if (CoreAuthorityConstants.ROLE_ADMIN.equals(grantedAuthority.getAuthority())) {
				hasRoleAdmin = true;
			} else if (CoreAuthorityConstants.ROLE_AUTHENTICATED.equals(grantedAuthority.getAuthority())) {
				hasRoleAuthenticated = true;
			} else if (CoreAuthorityConstants.ROLE_ANONYMOUS.equals(grantedAuthority.getAuthority())) {
				hasRoleAnonymous = true;
			}
		}
		
		assertFalse(hasRoleSystem);
		assertFalse(hasRoleAdmin);
		assertTrue(hasRoleAuthenticated);
		assertTrue(hasRoleAnonymous);
	}

	@Test
	public void testSecurityProxy() throws ServiceException, SecurityServiceException {
		MockUser user = createMockUser(System.getProperty("user.name"), "firstName", "lastName");
		user.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		mockUserService.update(user);
		authenticateAs(user);
		
		try {
			mockUserService.protectedMethodRoleAdmin();
			Assert.fail("L'accès devrait être interdit.");
		} catch (AccessDeniedException e) {}
		
		mockUserService.protectedMethodRoleAuthenticated();
	}
	
	@Before
	@After
	public void signOut() {
		authenticationService.signOut();
	}
}
