package org.iglooproject.test;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.jpa.security.service.ISecurityService;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.service.IMockUserService;
import org.iglooproject.test.jpa.security.service.TestJpaSecurityUserDetailsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {})
public abstract class AbstractJpaSecurityTestCase extends AbstractTestCase {

  public static final String DEFAULT_PASSWORD = "test";

  @Autowired protected IMockUserService mockUserService;

  @Autowired protected IAuthenticationService authenticationService;

  @Autowired protected ISecurityService securityService;

  @Autowired protected ProviderManager authenticationManager;

  @Autowired protected PasswordEncoder passwordEncoder;

  @AfterEach
  @Override
  public void close() throws ServiceException, SecurityServiceException {
    super.close();
  }

  @Override
  protected void cleanAll() throws ServiceException, SecurityServiceException {
    cleanEntities(mockUserService);
  }

  protected MockUser createMockBasicUser() throws ServiceException, SecurityServiceException {
    return createMockUser(
        System.getProperty("user.name"), "firstName", "lastName", "test@example.com");
  }

  protected MockUser createMockTechnicalUser() throws ServiceException, SecurityServiceException {
    return createMockUser(
        TestJpaSecurityUserDetailsServiceImpl.TECHNICAL_USERNAME,
        "firstName",
        "lastName",
        "test@example.com");
  }

  protected MockUser createMockUser(String username, String firstName, String lastName)
      throws ServiceException, SecurityServiceException {
    return createMockUser(username, firstName, lastName, "test@example.com");
  }

  protected MockUser createMockUser(
      String username, String firstName, String lastName, String email)
      throws ServiceException, SecurityServiceException {
    MockUser user = new MockUser();
    user.setUsername(username);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);

    mockUserService.create(user);
    mockUserService.setPasswords(user, DEFAULT_PASSWORD);

    return user;
  }

  protected void authenticateAs(IUser user) {
    authenticateAs(new UsernamePasswordAuthenticationToken(user.getUsername(), DEFAULT_PASSWORD));
  }

  protected void authenticateAs(UsernamePasswordAuthenticationToken authenticationToken) {
    Authentication authentication = authenticationManager.authenticate(authenticationToken);

    SecurityContext secureContext = new SecurityContextImpl();
    secureContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(secureContext);
  }
}
