package org.iglooproject.test.jpa.security.business;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;

public class TestPersonService extends AbstractJpaSecurityTestCase {

	@Autowired
	private UserDetailsService userDetailsService;

	@Test
	public void testAuthorities() throws ServiceException, SecurityServiceException {
		MockUser person1 = createMockPerson("login1", "firstName1", "lastName1");
		MockUser person2 = createMockPerson("login2", "firstName2", "lastName2");
		
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
	public void testCaseInsensitiveUserNameFetch() throws ServiceException, SecurityServiceException {
		MockUser person1 = createMockPerson("Login1", "firstName1", "lastName1");
		MockUser person2 = createMockPerson("logIn2", "firstName2", "lastName2");
		mockUserService.setPasswords(person1, "toto");
		mockUserService.setPasswords(person2, "tata");
		
		assertEquals(person1, mockUserService.getByUserNameCaseInsensitive("login1"));
		assertEquals(person1, mockUserService.getByUserNameCaseInsensitive("Login1"));
		assertEquals(person1, mockUserService.getByUserNameCaseInsensitive("LogIn1"));
		
		assertEquals(person2, mockUserService.getByUserNameCaseInsensitive("login2"));
		assertEquals(person2, mockUserService.getByUserNameCaseInsensitive("Login2"));
		assertEquals(person2, mockUserService.getByUserNameCaseInsensitive("LogIn2"));
		
		assertEquals("Login1", userDetailsService.loadUserByUsername("Login1").getUsername());
		assertEquals("Login1", userDetailsService.loadUserByUsername("login1").getUsername());
		assertEquals("logIn2", userDetailsService.loadUserByUsername("LOgiN2").getUsername());
		assertEquals("logIn2", userDetailsService.loadUserByUsername("logIn2").getUsername());
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Login1", "tata"));
			Assert.fail("Le mot de passe est erroné");
		} catch (AuthenticationException e) {}
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Login1", "toto"));
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Login2", "toto"));
			Assert.fail("Le mot de passe est erroné");
		} catch (AuthenticationException e) {}
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Login2", "tata"));
	}
}
