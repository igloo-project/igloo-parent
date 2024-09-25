package org.iglooproject.test.jpa.security;

import org.assertj.core.api.Assertions;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.config.spring.SpringBootTestJpaSecurity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
    Assertions.assertThat(authentication.getName()).isEqualTo(user.getUsername());
    Assertions.assertThat(authentication.getCredentials()).isEqualTo(DEFAULT_PASSWORD);

    Assertions.assertThat(authentication.isAuthenticated()).isTrue();

    authenticationManager.setEraseCredentialsAfterAuthentication(true);

    authenticationService.signOut();
    Assertions.assertThat(authenticationService.isLoggedIn()).isFalse();
    Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  void testSecurityProxy() throws ServiceException, SecurityServiceException {
    MockUser user = createMockBasicUser();
    authenticateAs(user);

    Assertions.assertThatThrownBy(() -> mockUserService.protectedMethodRoleAdmin())
        .isInstanceOf(AccessDeniedException.class);

    Assertions.assertThatCode(() -> mockUserService.protectedMethodRoleAuthenticated())
        .doesNotThrowAnyException();

    Assertions.assertThatCode(() -> mockUserService.protectedMethodRoleAnonymous())
        .doesNotThrowAnyException();
  }

  @BeforeEach
  @AfterEach
  public void signOut() {
    authenticationService.signOut();
  }
}
