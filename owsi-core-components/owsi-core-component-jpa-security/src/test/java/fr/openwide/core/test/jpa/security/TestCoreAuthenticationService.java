package fr.openwide.core.test.jpa.security;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.test.AbstractJpaSecurityTestCase;
import fr.openwide.core.test.jpa.security.business.person.model.MockPerson;

public class TestCoreAuthenticationService extends AbstractJpaSecurityTestCase {

	@Test
	public void testAuthenticationUserInfo() throws ServiceException, SecurityServiceException {
		assertFalse(authenticationService.isLoggedIn());
		
		MockPerson user = createMockPerson(System.getProperty("user.name"), "firstName", "lastName");
		user.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		mockPersonService.update(user);
		
		/*
		 * Pour des raisons de sécurité le mot de passe est effacé après authentification.
		 * on désactive cette option pour les besoins du test suivant.
		 */
		authenticationManager.setEraseCredentialsAfterAuthentication(false);
		
		authenticateAs(user);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		assertTrue(authenticationService.isLoggedIn());
		assertNotNull(authentication);
		assertEquals(user.getUserName(), authentication.getName());
		assertEquals(DEFAULT_PASSWORD, authentication.getCredentials());
		
		assertTrue(authentication.isAuthenticated());
		
		authenticationManager.setEraseCredentialsAfterAuthentication(true);
		
		authenticationService.signOut();
		assertFalse(authenticationService.isLoggedIn());
		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

	@Test
	public void testAuthenticationRoles() throws ServiceException, SecurityServiceException {
		MockPerson user = createMockPerson(System.getProperty("user.name"), "firstName", "lastName");
		user.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		mockPersonService.update(user);
		
		authenticateAs(user);
		
		@SuppressWarnings("unchecked")
		Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) authenticationService.getAuthorities();
		
		assertTrue(grantedAuthorities.size() > 0);
		
		boolean hasRoleSystem = false;
		boolean hasRoleAdmin = false;
		boolean hasRoleAuthenticated = false;
		boolean hasRoleAnonymous = false;
		
		for (GrantedAuthority grantedAuthority : grantedAuthorities) {
			if(CoreAuthorityConstants.ROLE_SYSTEM.equals(grantedAuthority.getAuthority())) {
				hasRoleSystem = true;
			} else if(CoreAuthorityConstants.ROLE_ADMIN.equals(grantedAuthority.getAuthority())) {
				hasRoleAdmin = true;
			} else if(CoreAuthorityConstants.ROLE_AUTHENTICATED.equals(grantedAuthority.getAuthority())) {
				hasRoleAuthenticated = true;
			} else if(CoreAuthorityConstants.ROLE_ANONYMOUS.equals(grantedAuthority.getAuthority())) {
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
		MockPerson user = createMockPerson(System.getProperty("user.name"), "firstName", "lastName");
		user.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		mockPersonService.update(user);
		authenticateAs(user);
		
		try {
			mockPersonService.protectedMethodRoleAdmin();
			Assert.fail("L'accès devrait être interdit.");
		} catch (AccessDeniedException e) {}
		
		mockPersonService.protectedMethodRoleAuthenticated();
	}
}
