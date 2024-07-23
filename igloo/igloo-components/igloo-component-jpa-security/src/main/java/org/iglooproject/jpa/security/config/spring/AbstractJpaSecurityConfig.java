package org.iglooproject.jpa.security.config.spring;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.iglooproject.jpa.security.access.expression.method.CoreMethodSecurityExpressionHandler;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.hierarchy.IPermissionHierarchy;
import org.iglooproject.jpa.security.hierarchy.PermissionHierarchyImpl;
import org.iglooproject.jpa.security.model.CorePermissionConstants;
import org.iglooproject.jpa.security.model.NamedPermission;
import org.iglooproject.jpa.security.runas.CoreRunAsManagerImpl;
import org.iglooproject.jpa.security.service.AuthenticationUsernameComparison;
import org.iglooproject.jpa.security.service.CoreAuthenticationServiceImpl;
import org.iglooproject.jpa.security.service.CoreJpaUserDetailsServiceImpl;
import org.iglooproject.jpa.security.service.CoreSecurityServiceImpl;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.iglooproject.jpa.security.service.ISecurityService;
import org.iglooproject.jpa.security.service.NamedPermissionFactory;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Import(DefaultJpaSecurityConfig.class)
public abstract class AbstractJpaSecurityConfig {

  @Autowired private DefaultJpaSecurityConfig defaultJpaSecurityConfig;

  @Autowired protected IPropertyService propertyService;

  /**
   * N'est pas basculé en configuration car on n'est pas censé basculer d'un mode à un autre au
   * cours de la vie de l'application. Doit être décidé au début, avec mise en place des contraintes
   * nécessaires à la création d'utilisateur si on choisit le mode CASE INSENSITIVE. Cette méthode
   * n'a pas besoin d'être annotée {@link Bean}
   *
   * @see AuthenticationUsernameComparison
   */
  public AuthenticationUsernameComparison authenticationUsernameComparison() {
    return AuthenticationUsernameComparison.CASE_SENSITIVE;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    return passwordEncoder;
  }

  @Bean
  public ISecurityService securityService() {
    return new CoreSecurityServiceImpl();
  }

  @Bean(name = "authenticationService")
  public IAuthenticationService authenticationService() {
    return new CoreAuthenticationServiceImpl();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    CoreJpaUserDetailsServiceImpl userDetailsService = new CoreJpaUserDetailsServiceImpl();
    userDetailsService.setAuthenticationUsernameComparison(authenticationUsernameComparison());
    return userDetailsService;
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
  public PermissionFactory permissionFactory() {
    return new NamedPermissionFactory(permissionClass());
  }

  /**
   * Le {@link ScopedProxyMode} est nécessaire si on désire utiliser les annotations de sécurité. En
   * effet, l'activation des annotations de sécurité nécessite la construction du sous-système de
   * sécurité dès le début de l'instantiation des beans (de manière à pouvoir mettre en place les
   * intercepteurs de sécurité). Or le système de sécurité provoque le chargement du entitymanager
   * et d'autres beans alors que leur dépendances ne sont pas prêtes. La mise en place d'un proxy
   * permet de reporter à plus tard l'instanciation du système de sécurité.
   */
  @Bean
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  public abstract ICorePermissionEvaluator permissionEvaluator();

  @Bean
  public MethodSecurityExpressionHandler expressionHandler(
      ICorePermissionEvaluator corePermissionEvaluator) {
    CoreMethodSecurityExpressionHandler methodSecurityExpressionHandler =
        new CoreMethodSecurityExpressionHandler();
    methodSecurityExpressionHandler.setCorePermissionEvaluator(corePermissionEvaluator);
    return methodSecurityExpressionHandler;
  }

  protected String roleHierarchyAsString() {
    return defaultRoleHierarchyAsString();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy(roleHierarchyAsString());
    return roleHierarchy;
  }

  protected String permissionHierarchyAsString() {
    return defaultPermissionHierarchyAsString();
  }

  @Bean
  @Autowired
  public IPermissionHierarchy permissionHierarchy(PermissionFactory permissionFactory) {
    PermissionHierarchyImpl hierarchy = new PermissionHierarchyImpl(permissionFactory);
    hierarchy.setHierarchy(permissionHierarchyAsString());
    return hierarchy;
  }

  @Bean
  public RunAsManager runAsManager() {
    CoreRunAsManagerImpl runAsManager = new CoreRunAsManagerImpl();
    runAsManager.setKey(defaultJpaSecurityConfig.getRunAsKey());
    return runAsManager;
  }

  @Bean
  public RunAsImplAuthenticationProvider runAsAuthenticationProvider() {
    RunAsImplAuthenticationProvider runAsAuthenticationProvider =
        new RunAsImplAuthenticationProvider();
    runAsAuthenticationProvider.setKey(defaultJpaSecurityConfig.getRunAsKey());
    return runAsAuthenticationProvider;
  }

  protected static String defaultPermissionHierarchyAsString() {
    return hierarchyAsStringFromMap(
        ImmutableMultimap.<String, String>builder()
            .put(CorePermissionConstants.ADMINISTRATION, CorePermissionConstants.WRITE)
            .put(CorePermissionConstants.WRITE, CorePermissionConstants.READ)
            .build());
  }

  protected static String defaultRoleHierarchyAsString() {
    return hierarchyAsStringFromMap(
        ImmutableMultimap.<String, String>builder()
            .put(CoreAuthorityConstants.ROLE_SYSTEM, CoreAuthorityConstants.ROLE_ADMIN)
            .put(CoreAuthorityConstants.ROLE_ADMIN, CoreAuthorityConstants.ROLE_AUTHENTICATED)
            .put(CoreAuthorityConstants.ROLE_AUTHENTICATED, CoreAuthorityConstants.ROLE_ANONYMOUS)
            .build());
  }

  protected static String hierarchyAsStringFromMap(Multimap<String, String> multimap) {
    return hierarchyAsStringFromMap(multimap.asMap());
  }

  protected static String hierarchyAsStringFromMap(Map<String, ? extends Collection<String>> map) {
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<String, ? extends Collection<String>> entry : map.entrySet()) {
      String parent = entry.getKey();
      for (String child : entry.getValue()) {
        builder.append(parent).append(" > ").append(child).append("\n");
      }
    }
    return builder.toString();
  }
}
