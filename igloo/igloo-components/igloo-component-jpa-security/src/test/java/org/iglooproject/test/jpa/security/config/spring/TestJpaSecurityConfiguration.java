package org.iglooproject.test.jpa.security.config.spring;

import igloo.security.ICoreUserDetailsService;
import org.iglooproject.jpa.security.service.AuthenticationUsernameComparison;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.iglooproject.test.jpa.security.business.JpaSecurityTestBusinessPackage;
import org.iglooproject.test.jpa.security.service.TestCorePermissionEvaluator;
import org.iglooproject.test.jpa.security.service.TestJpaSecurityUserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = JpaSecurityTestBusinessPackage.class)
@ComponentScan(basePackageClasses = JpaSecurityTestBusinessPackage.class)
public class TestJpaSecurityConfiguration {

  @Bean
  public AuthenticationUsernameComparison authenticationUsernameComparison() {
    return AuthenticationUsernameComparison.CASE_INSENSITIVE;
  }

  @Bean
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  public ICorePermissionEvaluator permissionEvaluator() {
    return new TestCorePermissionEvaluator();
  }

  @Bean
  public ICoreUserDetailsService userDetailsService(
      AuthenticationUsernameComparison authenticationUsernameComparison) {
    TestJpaSecurityUserDetailsServiceImpl userDetailsService =
        new TestJpaSecurityUserDetailsServiceImpl();
    userDetailsService.setAuthenticationUsernameComparison(authenticationUsernameComparison);
    return userDetailsService;
  }
}
