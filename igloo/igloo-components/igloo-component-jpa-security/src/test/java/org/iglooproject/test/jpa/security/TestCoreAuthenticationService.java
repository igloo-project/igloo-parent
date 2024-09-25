package org.iglooproject.test.jpa.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import org.assertj.core.api.Assertions;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.config.spring.SpringBootTestJpaSecurity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTestJpaSecurity
class TestCoreAuthenticationService extends AbstractJpaSecurityTestCase {

  @Test
  void testAuthenticateAs() throws ServiceException, SecurityServiceException {
    Assertions.assertThat(authenticationService.isLoggedIn()).isFalse();

    MockUser user = createMockUser(System.getProperty("user.name"), "firstName", "lastName");

    mockUserService.update(user);

    /*
     * Pour des raisons de sécurité le mot de passe est effacé après authentification.
     * on désactive cette option pour les besoins du test suivant.
     */
    authenticationManager.setEraseCredentialsAfterAuthentication(false);

    authenticateAs(user);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Assertions.assertThat(authenticationService.isLoggedIn()).isTrue();
    Assertions.assertThat(authentication).isNotNull();
    Assertions.assertThat(user.getUsername()).isEqualTo(authentication.getName());
    Assertions.assertThat(DEFAULT_PASSWORD).isEqualTo(authentication.getCredentials());

    Assertions.assertThat(authentication.isAuthenticated()).isTrue();

    authenticationManager.setEraseCredentialsAfterAuthentication(true);

    authenticationService.signOut();
    Assertions.assertThat(authenticationService.isLoggedIn()).isFalse();
    Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  // TODO SUPPRIMER
  @Test
  void testAuthenticationRoles() throws ServiceException, SecurityServiceException {
    MockUser user = createMockUser(System.getProperty("user.name"), "firstName", "lastName");
    // TODO RFO AUTHORITIES !
    // user.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED));
    mockUserService.update(user);

    authenticateAs(user);

    @SuppressWarnings("unchecked")
    Collection<GrantedAuthority> grantedAuthorities =
        (Collection<GrantedAuthority>) authenticationService.getAuthorities();

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
      } else if (CoreAuthorityConstants.ROLE_AUTHENTICATED.equals(
          grantedAuthority.getAuthority())) {
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
  void testSecurityProxy() throws ServiceException, SecurityServiceException {
    MockUser user = createMockBasicUser();
    authenticateAs(user);

    Assertions.assertThatThrownBy(() -> mockUserService.protectedMethodRoleAdmin())
        .isInstanceOf(AccessDeniedException.class);

    Assertions.assertThatCode(() -> mockUserService.protectedMethodRoleAuthenticated())
        .doesNotThrowAnyException();
  }

  @BeforeEach
  @AfterEach
  public void signOut() {
    authenticationService.signOut();
  }
}
