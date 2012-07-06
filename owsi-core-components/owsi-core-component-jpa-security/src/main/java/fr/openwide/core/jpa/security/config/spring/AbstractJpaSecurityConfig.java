package fr.openwide.core.jpa.security.config.spring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.dao.SystemWideSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.security.acl.CoreAclPermissionEvaluator;
import fr.openwide.core.jpa.security.acl.JpaSecurityAclPackage;
import fr.openwide.core.jpa.security.acl.domain.CorePermissionConstants;
import fr.openwide.core.jpa.security.acl.domain.hierarchy.IPermissionHierarchy;
import fr.openwide.core.jpa.security.acl.domain.hierarchy.PermissionHierarchyImpl;
import fr.openwide.core.jpa.security.acl.service.CorePermissionRegistryServiceImpl;
import fr.openwide.core.jpa.security.acl.service.CoreSidRetrievalServiceImpl;
import fr.openwide.core.jpa.security.acl.service.IPermissionRegistryService;
import fr.openwide.core.jpa.security.acl.service.ISidRetrievalService;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.jpa.security.runas.CoreRunAsManagerImpl;
import fr.openwide.core.jpa.security.service.AuthenticationUserNameComparison;
import fr.openwide.core.jpa.security.service.CoreAuthenticationServiceImpl;
import fr.openwide.core.jpa.security.service.CoreJpaUserDetailsServiceImpl;
import fr.openwide.core.jpa.security.service.CoreSecurityServiceImpl;
import fr.openwide.core.jpa.security.service.IAuthenticationService;
import fr.openwide.core.jpa.security.service.ISecurityService;

@Configuration
@ComponentScan(basePackageClasses = JpaSecurityAclPackage.class)
@Import(DefaultJpaSecurityConfig.class)
public abstract class AbstractJpaSecurityConfig {

	@Autowired
	private DefaultJpaSecurityConfig defaultJpaSecurityConfig;

	/**
	 * Cette méthode n'a pas besoin d'être annotée {@link Bean}
	 * 
	 * Voir {@link AbstractJpaSecurityConfig#defaultPermissionClass()}
	 */
	public abstract Class<? extends Permission> permissionClass();

	/**
	 * Cette méthode n'a pas besoin d'être annotée {@link Bean}
	 * 
	 * Voir {@link AbstractJpaSecurityConfig#defaultRoleHierarchyAsString()}
	 */
	public abstract String roleHierarchyAsString();

	/**
	 * Cette méthode n'a pas besoin d'être annotée {@link Bean}
	 * 
	 * Voir {@link AbstractJpaSecurityConfig#defaultPermissionHierarchyAsString()}
	 */
	public abstract String permissionHierarchyAsString();

	/**
	 * N'est pas basculé en configuration car on n'est pas censé basculé d'un mode à un autre au cours de la vie
	 * de l'application. Doit être décidé au début, avec mise en place des contraintes nécessaires à la création
	 * d'utilisateur si on choisit le mode CASE INSENSITIVE.
	 * 
	 * @see AuthenticationUserNameComparison
	 */
	@Bean
	public abstract AuthenticationUserNameComparison authenticationUserNameComparison();

	@Bean
	public abstract AclService aclService();

	@Bean
	public IPermissionRegistryService permissionRegistryService() {
		return new CorePermissionRegistryServiceImpl();
	}

	@Bean
	public ISecurityService securityService() {
		return new CoreSecurityServiceImpl();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
			RunAsImplAuthenticationProvider runAsProvider, PasswordEncoder passwordEncoder, SaltSource saltSource) {
		List<AuthenticationProvider> providers = Lists.newArrayList();
		providers.add(runAsProvider);
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		authenticationProvider.setSaltSource(saltSource);
		providers.add(authenticationProvider);
		AuthenticationManager authenticationManager = new ProviderManager(providers);
		return authenticationManager;
	}

	/**
	 * Le {@link ScopedProxyMode} est nécessaire si on désire utiliser les annotations de sécurité. En effet,
	 * l'activation des annotations de sécurité nécessite la construction du sous-système de sécurité dès le début de
	 * l'instantiation des beans (de manière à pouvoir mettre en place les intercepteurs de sécurité). Or le système
	 * de sécurité provoque le chargement du entitymanager et d'autres beans alors que leur dépendances ne sont pas
	 * prêtes. La mise en place d'un proxy permet de reporter à plus tard l'instanciation du système de sécurité.
	 */
	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public PermissionEvaluator permissionEvaluator(AclService aclService, PermissionFactory permissionFactory,
			SidRetrievalStrategy sidRetrievalStrategy, ISecurityService securityService) {
		CoreAclPermissionEvaluator permissionEvaluator = new CoreAclPermissionEvaluator(aclService, securityService);
		permissionEvaluator.setSidRetrievalStrategy(sidRetrievalStrategy);
		permissionEvaluator.setPermissionFactory(permissionFactory);
		return permissionEvaluator;
	}

	@Bean
	public MethodSecurityExpressionHandler expressionHandler(PermissionEvaluator permissionEvaluator) {
		DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
		methodSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
		return methodSecurityExpressionHandler;
	}

	@Bean
	public PermissionFactory permissionFactory() {
		return new DefaultPermissionFactory(permissionClass());
	}

	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy(roleHierarchyAsString());
		return roleHierarchy;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new ShaPasswordEncoder(256);
	}

	@Bean
	public SaltSource saltSource() {
		SystemWideSaltSource saltSource = new SystemWideSaltSource();
		saltSource.setSystemWideSalt(defaultJpaSecurityConfig.getPasswordSalt());
		return saltSource;
	}

	@Bean(name = "authenticationService")
	public IAuthenticationService authenticationService() {
		return new CoreAuthenticationServiceImpl();
	}

	@Bean
	public ISidRetrievalService sidRetrievalService() {
		return new CoreSidRetrievalServiceImpl();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CoreJpaUserDetailsServiceImpl();
	}

	@Bean
	@Autowired
	public IPermissionHierarchy permissionHierarchy(PermissionFactory permissionFactory) {
		PermissionHierarchyImpl hierarchy = new PermissionHierarchyImpl(permissionFactory);
		hierarchy.setHierarchy(CorePermissionConstants.ADMINISTRATION + " > " + CorePermissionConstants.WRITE + "\n"
				+ CorePermissionConstants.WRITE + " > " + CorePermissionConstants.READ);
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
		RunAsImplAuthenticationProvider runAsAuthenticationProvider = new RunAsImplAuthenticationProvider();
		runAsAuthenticationProvider.setKey(defaultJpaSecurityConfig.getRunAsKey());
		return runAsAuthenticationProvider;
	}

	protected Class<? extends Permission> defaultPermissionClass() {
		return BasePermission.class;
	}

	protected String defaultPermissionHierarchyAsString() {
		return CorePermissionConstants.ADMINISTRATION + " > " + CorePermissionConstants.WRITE + "\n"
				+ CorePermissionConstants.WRITE + " > " + CorePermissionConstants.READ;
	}

	protected String defaultRoleHierarchyAsString() {
		return CoreAuthorityConstants.ROLE_SYSTEM + " > " + CoreAuthorityConstants.ROLE_ADMIN + "\n"
				+ CoreAuthorityConstants.ROLE_ADMIN + " > " + CoreAuthorityConstants.ROLE_AUTHENTICATED + "\n"
				+ CoreAuthorityConstants.ROLE_AUTHENTICATED + " > " + CoreAuthorityConstants.ROLE_ANONYMOUS + "\n";
	}

}
