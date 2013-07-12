package fr.openwide.core.test.jpa.security.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.PermissionEvaluator;

import fr.openwide.core.jpa.security.config.spring.AbstractJpaSecuritySecuredConfig;
import fr.openwide.core.jpa.security.service.AuthenticationUserNameComparison;
import fr.openwide.core.test.jpa.security.service.TestCorePermissionEvaluator;

@Configuration
public class JpaSecurityTestSecurityConfig extends AbstractJpaSecuritySecuredConfig {

	@Override
	public String roleHierarchyAsString() {
		return defaultRoleHierarchyAsString() +
				"ROLE_ADMIN > ROLE_GROUP_1\n" +
				"ROLE_ADMIN > ROLE_GROUP_2\n" +
				"ROLE_GROUP_1 > ROLE_GROUP_3\n";
	}

	@Override
	public String permissionHierarchyAsString() {
		return defaultPermissionHierarchyAsString();
	}

	@Override
	public AuthenticationUserNameComparison authenticationUserNameComparison() {
		return AuthenticationUserNameComparison.CASE_INSENSITIVE;
	}

	@Override
	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public PermissionEvaluator permissionEvaluator() {
		return new TestCorePermissionEvaluator();
	}

}
