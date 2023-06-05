package org.iglooproject.test.jpa.security.config.spring;

import org.iglooproject.jpa.config.spring.IglooJpaConfiguration;
import org.iglooproject.jpa.security.config.spring.IglooSecurityConfiguration;
import org.iglooproject.jpa.security.service.AuthenticationUsernameComparison;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.iglooproject.jpa.security.spring.SecurityUtils;
import org.iglooproject.test.jpa.security.business.JpaSecurityTestBusinessPackage;
import org.iglooproject.test.jpa.security.service.TestCorePermissionEvaluator;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
@Import({ IglooSecurityConfiguration.class, IglooJpaConfiguration.class })
@EntityScan(basePackageClasses = JpaSecurityTestBusinessPackage.class)
@ComponentScan(basePackageClasses = JpaSecurityTestBusinessPackage.class)
public class TestJpaSecurityConfiguration {

	@Bean
	public String roleHierarchyAsString() {
		return SecurityUtils.defaultRoleHierarchyAsString() +
				"ROLE_ADMIN > ROLE_GROUP_1\n" +
				"ROLE_ADMIN > ROLE_GROUP_2\n" +
				"ROLE_GROUP_1 > ROLE_GROUP_3\n";
	}

	@Bean
	public String permissionHierarchyAsString() {
		return SecurityUtils.defaultPermissionHierarchyAsString();
	}

	@Bean
	public AuthenticationUsernameComparison authenticationUsernameComparison() {
		return AuthenticationUsernameComparison.CASE_INSENSITIVE;
	}

	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public ICorePermissionEvaluator permissionEvaluator() {
		return new TestCorePermissionEvaluator();
	}

}
