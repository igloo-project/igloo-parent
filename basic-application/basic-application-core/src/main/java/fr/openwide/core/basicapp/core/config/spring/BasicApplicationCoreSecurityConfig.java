package fr.openwide.core.basicapp.core.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.PermissionEvaluator;

import fr.openwide.core.basicapp.core.security.service.BasicApplicationPermissionEvaluator;
import fr.openwide.core.jpa.security.config.spring.AbstractJpaSecurityConfig;
import fr.openwide.core.jpa.security.service.AuthenticationUserNameComparison;

@Configuration
public class BasicApplicationCoreSecurityConfig extends AbstractJpaSecurityConfig {
	
	@Override
	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public PermissionEvaluator permissionEvaluator() {
		return new BasicApplicationPermissionEvaluator();
	}

	@Bean
	@Override
	public AuthenticationUserNameComparison authenticationUserNameComparison() {
		return AuthenticationUserNameComparison.CASE_SENSITIVE;
	}

	@Override
	public String roleHierarchyAsString() {
		return defaultRoleHierarchyAsString();
	}

	@Override
	public String permissionHierarchyAsString() {
		return defaultPermissionHierarchyAsString();
	}

}
