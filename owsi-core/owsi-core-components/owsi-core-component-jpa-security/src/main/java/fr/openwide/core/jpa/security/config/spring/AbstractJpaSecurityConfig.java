package fr.openwide.core.jpa.security.config.spring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.dao.SystemWideSaltSource;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.jpa.security.hierarchy.IPermissionHierarchy;
import fr.openwide.core.jpa.security.hierarchy.PermissionHierarchyImpl;
import fr.openwide.core.jpa.security.model.CorePermissionConstants;
import fr.openwide.core.jpa.security.model.NamedPermission;
import fr.openwide.core.jpa.security.runas.CoreRunAsManagerImpl;
import fr.openwide.core.jpa.security.service.AuthenticationUserNameComparison;
import fr.openwide.core.jpa.security.service.CoreAuthenticationServiceImpl;
import fr.openwide.core.jpa.security.service.CoreJpaUserDetailsServiceImpl;
import fr.openwide.core.jpa.security.service.CoreSecurityServiceImpl;
import fr.openwide.core.jpa.security.service.IAuthenticationService;
import fr.openwide.core.jpa.security.service.ISecurityService;
import fr.openwide.core.jpa.security.service.NamedPermissionFactory;

@Configuration
@Import(DefaultJpaSecurityConfig.class)
public abstract class AbstractJpaSecurityConfig {

	@Autowired
	private DefaultJpaSecurityConfig defaultJpaSecurityConfig;

	/**
	 * N'est pas basculé en configuration car on n'est pas censé basculé d'un
	 * mode à un autre au cours de la vie de l'application. Doit être décidé au
	 * début, avec mise en place des contraintes nécessaires à la création
	 * d'utilisateur si on choisit le mode CASE INSENSITIVE. Cette méthode n'a
	 * pas besoin d'être annotée {@link Bean}
	 * 
	 * @see AuthenticationUserNameComparison
	 */
	public AuthenticationUserNameComparison authenticationUserNameComparison() {
		return AuthenticationUserNameComparison.CASE_SENSITIVE;
	}

	public Class<? extends NamedPermission> permissionClass() {
		return NamedPermission.class;
	}

	public String roleHierarchyAsString() {
		return defaultRoleHierarchyAsString();
	}

	public String permissionHierarchyAsString() {
		return defaultPermissionHierarchyAsString();
	}

	@Bean
	public ISecurityService securityService() {
		return new CoreSecurityServiceImpl();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
			RunAsImplAuthenticationProvider runAsProvider, MessageDigestPasswordEncoder passwordEncoder,
			SaltSource saltSource) {
		List<AuthenticationProvider> providers = Lists.newArrayList();
		providers.add(runAsProvider);
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		authenticationProvider.setSaltSource(saltSource);
		providers.add(authenticationProvider);
		return new ProviderManager(providers);
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
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public abstract PermissionEvaluator permissionEvaluator();

	@Bean
	public MethodSecurityExpressionHandler expressionHandler(PermissionEvaluator permissionEvaluator) {
		DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
		methodSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
		return methodSecurityExpressionHandler;
	}

	@Bean
	public PermissionFactory permissionFactory() {
		return new NamedPermissionFactory(permissionClass());
	}

	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy(roleHierarchyAsString());
		return roleHierarchy;
	}

	@Bean
	public MessageDigestPasswordEncoder passwordEncoder() {
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
	public UserDetailsService userDetailsService() {
		CoreJpaUserDetailsServiceImpl detailsService = new CoreJpaUserDetailsServiceImpl();
		detailsService.setAuthenticationUserNameComparison(authenticationUserNameComparison());
		return detailsService;
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
		RunAsImplAuthenticationProvider runAsAuthenticationProvider = new RunAsImplAuthenticationProvider();
		runAsAuthenticationProvider.setKey(defaultJpaSecurityConfig.getRunAsKey());
		return runAsAuthenticationProvider;
	}

	protected Class<? extends NamedPermission> defaultPermissionClass() {
		return NamedPermission.class;
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
