package org.iglooproject.showcase.core.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import org.iglooproject.jpa.security.config.spring.AbstractJpaSecuritySecuredConfig;
import org.iglooproject.jpa.security.service.AuthenticationUserNameComparison;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.iglooproject.showcase.core.security.service.ShowcasePermissionEvaluator;

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
