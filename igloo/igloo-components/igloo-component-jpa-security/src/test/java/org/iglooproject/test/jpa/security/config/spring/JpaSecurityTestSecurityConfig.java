package org.iglooproject.test.jpa.security.config.spring;

import org.iglooproject.jpa.security.config.spring.AbstractJpaSecuritySecuredConfig;
import org.iglooproject.jpa.security.service.AuthenticationUsernameComparison;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.iglooproject.test.jpa.security.service.TestCorePermissionEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class JpaSecurityTestSecurityConfig extends AbstractJpaSecuritySecuredConfig {

  @Override
  public String roleHierarchyAsString() {
    return defaultRoleHierarchyAsString()
        + "ROLE_ADMIN > ROLE_GROUP_1\n"
        + "ROLE_ADMIN > ROLE_GROUP_2\n"
        + "ROLE_GROUP_1 > ROLE_GROUP_3\n";
  }

  @Override
  public String permissionHierarchyAsString() {
    return defaultPermissionHierarchyAsString();
  }

  @Override
  public AuthenticationUsernameComparison authenticationUsernameComparison() {
    return AuthenticationUsernameComparison.CASE_INSENSITIVE;
  }

  @Override
  @Bean
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  public ICorePermissionEvaluator permissionEvaluator() {
    return new TestCorePermissionEvaluator();
  }
}
