package fr.openwide.core.showcase.core.config.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import fr.openwide.core.jpa.security.config.spring.AbstractJpaSecuritySecuredConfig;
import fr.openwide.core.jpa.security.service.AuthenticationUserNameComparison;
import fr.openwide.core.jpa.security.service.ISecurityService;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.core.security.service.ShowcasePermissionEvaluator;

@Configuration
public class ShowcaseCoreSecurityConfig extends AbstractJpaSecuritySecuredConfig {
	
	@Autowired
	private ISecurityService securityService;

	@Autowired
	private IUserService userService;

	@Bean
	@Override
	public AuthenticationUserNameComparison authenticationUserNameComparison() {
		return AuthenticationUserNameComparison.CASE_INSENSITIVE;
	}

	@Override
	public String roleHierarchyAsString() {
		return defaultRoleHierarchyAsString();
	}

	@Override
	public String permissionHierarchyAsString() {
		return defaultPermissionHierarchyAsString();
	}

	@Override
	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public ShowcasePermissionEvaluator permissionEvaluator() {
		return new ShowcasePermissionEvaluator();
	}
	
}
