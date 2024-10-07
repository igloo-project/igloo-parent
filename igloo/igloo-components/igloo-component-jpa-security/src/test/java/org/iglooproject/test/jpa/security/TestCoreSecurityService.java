package org.iglooproject.test.jpa.security;

import org.assertj.core.api.Assertions;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.config.spring.SpringBootTestJpaSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTestJpaSecurity
class TestCoreSecurityService extends AbstractJpaSecurityTestCase {

  @Test
  void testRunAsSystem() {
    Assertions.assertThat(
            securityService.runAsSystem(
                () ->
                    securityService.hasSystemRole(
                        SecurityContextHolder.getContext().getAuthentication())))
        .isTrue();
    Assertions.assertThat(
            securityService.runAsSystem(
                () ->
                    securityService.hasAdminRole(
                        SecurityContextHolder.getContext().getAuthentication())))
        .isTrue();

    Assertions.assertThat(
            securityService.runAsSystem(
                () ->
                    securityService.hasAuthenticatedRole(
                        SecurityContextHolder.getContext().getAuthentication())))
        .isTrue();
  }
}
