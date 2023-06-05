package org.iglooproject.test.jpa.security.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.config.spring.SpringBootTestJpaSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootTestJpaSecurity
class TestUserService extends AbstractJpaSecurityTestCase {

	@Autowired
	private UserDetailsService userDetailsService;

	@Test
	void testAuthorities() throws ServiceException, SecurityServiceException {
		MockUser person1 = createMockUser("login1", "firstName1", "lastName1");
		MockUser person2 = createMockUser("login2", "firstName2", "lastName2");
		
		Authority adminAuthority = authorityService.getByName(CoreAuthorityConstants.ROLE_ADMIN);
		Authority group1Authority = authorityService.getByName(ROLE_GROUP_1);
		
		person1.addAuthority(adminAuthority);
		person1.addAuthority(group1Authority);
		
		mockUserService.update(person1);
		
		assertEquals(3, person1.getAuthorities().size());
		
		person2.addAuthority(adminAuthority);
		person2.addAuthority(group1Authority);
		
		mockUserService.update(person2);
		
		assertEquals(3, person2.getAuthorities().size());
		
		mockUserService.delete(person1);
		
		assertEquals(3, person2.getAuthorities().size());
		
		person2.removeAuthority(adminAuthority);
		
		mockUserService.update(person2);
		
		assertEquals(2, person2.getAuthorities().size());
	}

	@Test
	void testCaseInsensitiveUsernameFetch() throws ServiceException, SecurityServiceException {
		MockUser user1 = createMockUser("Login1", "firstName1", "lastName1");
		MockUser user2 = createMockUser("logIn2", "firstName2", "lastName2");
		mockUserService.setPasswords(user1, "toto");
		mockUserService.setPasswords(user2, "tata");
		
		assertEquals(user1, mockUserService.getByUsernameCaseInsensitive("login1"));
		assertEquals(user1, mockUserService.getByUsernameCaseInsensitive("Login1"));
		assertEquals(user1, mockUserService.getByUsernameCaseInsensitive("LogIn1"));
		
		assertEquals(user2, mockUserService.getByUsernameCaseInsensitive("login2"));
		assertEquals(user2, mockUserService.getByUsernameCaseInsensitive("Login2"));
		assertEquals(user2, mockUserService.getByUsernameCaseInsensitive("LogIn2"));
		
		assertEquals("Login1", userDetailsService.loadUserByUsername("Login1").getUsername());
		assertEquals("Login1", userDetailsService.loadUserByUsername("login1").getUsername());
		assertEquals("logIn2", userDetailsService.loadUserByUsername("LOgiN2").getUsername());
		assertEquals("logIn2", userDetailsService.loadUserByUsername("logIn2").getUsername());
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Login1", "tata"));
			fail("Le mot de passe est erroné");
		} catch (AuthenticationException e) {}
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Login1", "toto"));
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Login2", "toto"));
			fail("Le mot de passe est erroné");
		} catch (AuthenticationException e) {}
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Login2", "tata"));
	}
}
