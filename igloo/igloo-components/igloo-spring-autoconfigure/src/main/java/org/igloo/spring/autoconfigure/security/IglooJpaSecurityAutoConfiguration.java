package org.igloo.spring.autoconfigure.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.igloo.spring.autoconfigure.jpa.IglooJpaAutoConfiguration;
import org.igloo.spring.autoconfigure.security.stub.StubSecurityUserServiceImpl;
import org.igloo.spring.autoconfigure.security.util.SecurityUtils;
import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.security.access.expression.method.CoreMethodSecurityExpressionHandler;
import org.iglooproject.jpa.security.business.JpaSecurityBusinessPackage;
import org.iglooproject.jpa.security.business.user.service.ISecurityUserService;
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
import org.iglooproject.jpa.security.service.JpaSecurityServicePackage;
import org.iglooproject.jpa.security.service.NamedPermissionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.AnnotationParameterNameDiscoverer;
import org.springframework.security.core.parameters.DefaultSecurityParameterNameDiscoverer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.jpa-security.disabled", havingValue = "false", matchIfMissing = true)
@Import(IglooJpaSecurityRunAsConfig.class)
@AutoConfigureAfter({ IglooJpaAutoConfiguration.class })
@ComponentScan(basePackageClasses = {
		JpaSecurityBusinessPackage.class,
		JpaSecurityServicePackage.class
		
})
public class IglooJpaSecurityAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(IglooJpaSecurityAutoConfiguration.class);

	/**
	 * N'est pas basculé en configuration car on n'est pas censé basculer d'un
	 * mode à un autre au cours de la vie de l'application. Doit être décidé au
	 * début, avec mise en place des contraintes nécessaires à la création
	 * d'utilisateur si on choisit le mode CASE INSENSITIVE. Cette méthode n'a
	 * pas besoin d'être annotée {@link Bean}
	 * 
	 * @see AuthenticationUsernameComparison
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthenticationUsernameComparison authenticationUsernameComparison() {
		return AuthenticationUsernameComparison.CASE_SENSITIVE;
	}
	
	@SuppressWarnings("rawtypes")
	@Bean
	@ConditionalOnMissingBean
	public ISecurityUserService<?> userServiceImpl() {
		return new StubSecurityUserServiceImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return passwordEncoder;
	}

	@Bean
	@ConditionalOnMissingBean
	public ISecurityService securityService() {
		return new CoreSecurityServiceImpl();
	}
	
	@Bean(name = "authenticationService")
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	@ConditionalOnMissingBean
	public IAuthenticationService authenticationService() {
		return new CoreAuthenticationServiceImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public UserDetailsService userDetailsService(AuthenticationUsernameComparison authenticationUsernameComparison) {
		CoreJpaUserDetailsServiceImpl detailsService = new CoreJpaUserDetailsServiceImpl();
		detailsService.setAuthenticationUsernameComparison(authenticationUsernameComparison());
		return detailsService;
	}

	@Bean(name = "mainAuthenticationProvider")
	@ConditionalOnMissingBean(name = "mainAuthenticationProvider")
	public AuthenticationProvider mainAuthenticationProvider(UserDetailsService userDetailsService,
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
	@ConditionalOnMissingBean(name = "authenticationManager")
	public AuthenticationManager authenticationManager(List<AuthenticationProvider> authenticationProviders) {
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

	/**
	 * Le {@link ScopedProxyMode} est nécessaire si on désire utiliser les
	 * annotations de sécurité. En effet, l'activation des annotations de
	 * sécurité nécessite la construction du sous-système de sécurité dès le
	 * début de l'instantiation des beans (de manière à pouvoir mettre en place
	 * les intercepteurs de sécurité). Or le système de sécurité provoque le
	 * chargement du entitymanager et d'autres beans alors que leur dépendances
	 * ne sont pas prêtes. La mise en place d'un proxy permet de reporter à plus
	 * tard l'instanciation du système de sécurité.
	 */
	@Bean
	@ConditionalOnMissingBean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public ICorePermissionEvaluator permissionEvaluator() {
		LOGGER.warn("No permissions found, please define your own.");
		return new ICorePermissionEvaluator() {
			
			@Override
			public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
					Object permission) {
				return false;
			}
			
			@Override
			public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
				return false;
			}
			
			@Override
			public boolean isSuperUser(Authentication authentication) {
				return false;
			}
			
			@Override
			public boolean hasPermission(Authentication authentication, Object requirePermission) {
				return false;
			}
			
			@Override
			public Collection<? extends Permission> getPermissions(Authentication authentication) {
				return null;
			}
		};
	}

	@Bean
	public MethodSecurityExpressionHandler expressionHandler(ICorePermissionEvaluator corePermissionEvaluator) {
		CoreMethodSecurityExpressionHandler methodSecurityExpressionHandler = new CoreMethodSecurityExpressionHandler();
		methodSecurityExpressionHandler.setCorePermissionEvaluator(corePermissionEvaluator);
		
		// Discover parameter name using the @PermissionObject annotation, too
		methodSecurityExpressionHandler.setParameterNameDiscoverer(new DefaultSecurityParameterNameDiscoverer(
				ImmutableList.of(
						new AnnotationParameterNameDiscoverer(PermissionObject.class.getName())
				)
		));
		
		return methodSecurityExpressionHandler;
	}
	
	protected String roleHierarchyAsString() {
		return SecurityUtils.defaultRoleHierarchyAsString();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy(roleHierarchyAsString());
		return roleHierarchy;
	}
	
	protected String permissionHierarchyAsString() {
		return SecurityUtils.defaultPermissionHierarchyAsString();
	}

	@Bean
	@ConditionalOnMissingBean
	public IPermissionHierarchy permissionHierarchy(PermissionFactory permissionFactory) {
		PermissionHierarchyImpl hierarchy = new PermissionHierarchyImpl(permissionFactory);
		hierarchy.setHierarchy(permissionHierarchyAsString());
		return hierarchy;
	}

	@Bean
	@ConditionalOnMissingBean
	public RunAsManager runAsManager(SecurityProperties securityProperties) {
		CoreRunAsManagerImpl runAsManager = new CoreRunAsManagerImpl();
		runAsManager.setKey(securityProperties.getRunAsKey());
		return runAsManager;
	}

	@Bean
	@ConditionalOnMissingBean
	public RunAsImplAuthenticationProvider runAsAuthenticationProvider(SecurityProperties securityProperties) {
		RunAsImplAuthenticationProvider runAsAuthenticationProvider = new RunAsImplAuthenticationProvider();
		runAsAuthenticationProvider.setKey(securityProperties.getRunAsKey());
		return runAsAuthenticationProvider;
	}
	
	@Bean
	public JpaPackageScanProvider jpaSecurityPackageScanProvider() {
		return new JpaPackageScanProvider(JpaSecurityBusinessPackage.class.getPackage());
	}

	@Configuration
	@EnableGlobalMethodSecurity(order = 0, prePostEnabled = true, securedEnabled = true)
	public static class JpaGlobalMethodSecurity extends GlobalMethodSecurityConfiguration {
		@Autowired
		@Qualifier("runAsManager")
		private RunAsManager runAsManager;
		
		@Autowired
		@Qualifier("expressionHandler")
		private MethodSecurityExpressionHandler methodSecurityExpressionHandler;
		
		@Override
		protected RunAsManager runAsManager() {
			return runAsManager;
		}
		
		@Override
		protected MethodSecurityExpressionHandler createExpressionHandler() {
			return methodSecurityExpressionHandler;
		}
	}

}
