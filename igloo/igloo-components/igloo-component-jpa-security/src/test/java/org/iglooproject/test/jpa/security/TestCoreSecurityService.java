package org.iglooproject.test.jpa.security;

import org.assertj.core.api.Assertions;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.config.spring.SpringBootTestJpaSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTestJpaSecurity
class TestCoreSecurityService extends AbstractJpaSecurityTestCase {

  @Test
  // TODO delete
  void testRoleHierarchy() throws ServiceException, SecurityServiceException {
    MockUser admin = createMockTechnicalUser();
    authenticateAs(admin);
    Assertions.assertThat(securityService.hasSystemRole(admin)).isFalse();
    Assertions.assertThat(securityService.hasAdminRole(admin)).isTrue();
    Assertions.assertThat(securityService.hasAuthenticatedRole(admin)).isTrue();

    // TODO AUTH
    MockUser basic = createMockBasicUser();

    authenticateAs(basic);
    //    assertFalse(securityService.hasSystemRole(authenticated));
    //    assertFalse(securityService.hasAdminRole(authenticated));
    //    assertTrue(securityService.hasAuthenticatedRole(authenticated));
  }

  @Test
  void testRunAsSystem() {
    Assertions.assertThat(
            securityService.runAsSystem(
                () ->
                    securityService.hasSystemRole(
                        SecurityContextHolder.getContext().getAuthentication())))
        .isTrue();
  }
}
