package org.iglooproject.test.jpa.security.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
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

  @Autowired private UserDetailsService userDetailsService;

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
    } catch (AuthenticationException e) {
    }
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Login1", "toto"));

    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Login2", "toto"));
      fail("Le mot de passe est erroné");
    } catch (AuthenticationException e) {
    }
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Login2", "tata"));
  }
}
