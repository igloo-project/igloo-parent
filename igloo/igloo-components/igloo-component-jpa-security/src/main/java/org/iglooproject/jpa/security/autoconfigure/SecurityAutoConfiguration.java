package org.iglooproject.jpa.security.autoconfigure;

import com.google.common.collect.Lists;
import java.util.List;
import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.security.access.expression.method.CoreMethodSecurityExpressionHandler;
import org.iglooproject.jpa.security.model.NamedPermission;
import org.iglooproject.jpa.security.service.AuthenticationUsernameComparison;
import org.iglooproject.jpa.security.service.CoreAuthenticationServiceImpl;
import org.iglooproject.jpa.security.service.CoreSecurityServiceImpl;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.iglooproject.jpa.security.service.ISecurityService;
import org.iglooproject.jpa.security.service.NamedPermissionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.parameters.AnnotationParameterNameDiscoverer;
import org.springframework.security.core.parameters.DefaultSecurityParameterNameDiscoverer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Provides spring-security configuration. This implies a working JPA configuration. */
@Configuration
@PropertySource(
    name = IglooPropertySourcePriority.FRAMEWORK,
    value = {"classpath:igloo-component-jpa-security/jpa-security.properties"},
    encoding = "UTF-8")
@Import(SecurityComponentScanConfiguration.class)
public class SecurityAutoConfiguration {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  @ConditionalOnMissingBean
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  public ISecurityService securityService() {
    return new CoreSecurityServiceImpl();
  }

  @Bean(name = "authenticationService")
  @ConditionalOnMissingBean
  public IAuthenticationService authenticationService() {
    return new CoreAuthenticationServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public AuthenticationUsernameComparison authenticationUsernameComparison() {
    return AuthenticationUsernameComparison.CASE_SENSITIVE;
  }

  @Bean
  public AuthenticationProvider daoAuthenticationProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  /**
   * authenticationProviders are injected by type lookup. This bean can be overriden by declaring a
   * new bean with a new method name, an alias for <em>authenticationManager</em> and a @{@link
   * Primary} annotation.
   */
  @Bean
  @Primary
  public AuthenticationManager authenticationManager(
      List<AuthenticationProvider> authenticationProviders) {
    List<AuthenticationProvider> providers = Lists.newArrayList();
    providers.addAll(authenticationProviders);
    return new ProviderManager(providers);
  }

  public Class<? extends Permission> permissionClass() {
    return NamedPermission.class;
  }

  @Bean
  @ConditionalOnMissingBean
  public PermissionFactory permissionFactory() {
    return new NamedPermissionFactory(permissionClass());
  }

  @Bean
  public MethodSecurityExpressionHandler expressionHandler(
      ICorePermissionEvaluator corePermissionEvaluator) {
    CoreMethodSecurityExpressionHandler methodSecurityExpressionHandler =
        new CoreMethodSecurityExpressionHandler();
    methodSecurityExpressionHandler.setCorePermissionEvaluator(corePermissionEvaluator);

    // Discover parameter name using the @PermissionObject annotation, too
    methodSecurityExpressionHandler.setParameterNameDiscoverer(
        new DefaultSecurityParameterNameDiscoverer(
            List.of(new AnnotationParameterNameDiscoverer(PermissionObject.class.getName()))));

    return methodSecurityExpressionHandler;
  }

  @Configuration
  @EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
  public static class JpaMethodSecurity {}
}
