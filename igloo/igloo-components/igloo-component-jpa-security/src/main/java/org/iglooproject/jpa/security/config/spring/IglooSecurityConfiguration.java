package org.iglooproject.jpa.security.config.spring;

import java.util.List;

import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.security.access.expression.method.CoreMethodSecurityExpressionHandler;
import org.iglooproject.jpa.security.business.JpaSecurityBusinessPackage;
import org.iglooproject.jpa.security.hierarchy.IPermissionHierarchy;
import org.iglooproject.jpa.security.hierarchy.PermissionHierarchyImpl;
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
import org.iglooproject.jpa.security.spring.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.parameters.AnnotationParameterNameDiscoverer;
import org.springframework.security.core.parameters.DefaultSecurityParameterNameDiscoverer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.Lists;

/**
 * Provides spring-security configuration. This implies a working JPA configuration. (TODO igloo-boot)
 */
@Configuration
@EntityScan(basePackageClasses = JpaSecurityBusinessPackage.class)
@ComponentScan(basePackageClasses = JpaSecurityBusinessPackage.class)
public class IglooSecurityConfiguration {

	@Value("${security.runAsKey}")
	private String runAsKey;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public ISecurityService securityService() {
		return new CoreSecurityServiceImpl();
	}
	
	@Bean(name = "authenticationService")
	public IAuthenticationService authenticationService() {
		return new CoreAuthenticationServiceImpl();
	}
	
	@Bean
	public UserDetailsService userDetailsService(AuthenticationUsernameComparison authenticationUsernameComparison) {
		CoreJpaUserDetailsServiceImpl userDetailsService = new CoreJpaUserDetailsServiceImpl();
		userDetailsService.setAuthenticationUsernameComparison(authenticationUsernameComparison);
		return userDetailsService;
	}
	
	@Bean
	public AuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}
	
	/**
	 * authenticationProviders are injected by type lookup. This bean can be overriden by declaring a new bean
	 * with a new method name, an alias for <em>authenticationManager</em> and a @{@link Primary} annotation.
	 */
	@Bean
	public AuthenticationManager authenticationManager(List<AuthenticationProvider> authenticationProviders) {
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
	
	@Bean
	public RoleHierarchy roleHierarchy(@Autowired @Qualifier("roleHierarchyAsString") String roleHierarchyAsString) {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy(roleHierarchyAsString);
		return roleHierarchy;
	}

	protected String permissionHierarchyAsString() {
		return SecurityUtils.defaultPermissionHierarchyAsString();
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
		runAsManager.setKey(runAsKey);
		return runAsManager;
	}

	@Bean
	public RunAsImplAuthenticationProvider runAsAuthenticationProvider() {
		RunAsImplAuthenticationProvider runAsAuthenticationProvider = new RunAsImplAuthenticationProvider();
		runAsAuthenticationProvider.setKey(runAsKey);
		return runAsAuthenticationProvider;
	}

	public String getRunAsKey() {
		return runAsKey;
	}
	
	// TODO igloo-boot: split @Secured
	@Bean
	public MethodSecurityExpressionHandler expressionHandler(ICorePermissionEvaluator corePermissionEvaluator) {
		CoreMethodSecurityExpressionHandler methodSecurityExpressionHandler = new CoreMethodSecurityExpressionHandler();
		methodSecurityExpressionHandler.setCorePermissionEvaluator(corePermissionEvaluator);
		
		// Discover parameter name using the @PermissionObject annotation, too
		methodSecurityExpressionHandler.setParameterNameDiscoverer(new DefaultSecurityParameterNameDiscoverer(
				List.of(new AnnotationParameterNameDiscoverer(PermissionObject.class.getName()))
		));
		
		return methodSecurityExpressionHandler;
	}

	@Configuration
	@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
	public static class JpaMethodSecurity {
	}

}
