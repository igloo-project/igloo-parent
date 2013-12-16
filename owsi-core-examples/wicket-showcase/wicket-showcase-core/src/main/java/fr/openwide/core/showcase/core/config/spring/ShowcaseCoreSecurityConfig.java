package fr.openwide.core.showcase.core.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import fr.openwide.core.jpa.security.config.spring.AbstractJpaSecuritySecuredConfig;
import fr.openwide.core.jpa.security.service.AuthenticationUserNameComparison;
import fr.openwide.core.jpa.security.service.ICorePermissionEvaluator;
import fr.openwide.core.showcase.core.security.service.ShowcasePermissionEvaluator;

@Configuration
public class ShowcaseCoreSecurityConfig extends AbstractJpaSecuritySecuredConfig {
	
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
	public ICorePermissionEvaluator permissionEvaluator() {
		return new ShowcasePermissionEvaluator();
	}
	
}
