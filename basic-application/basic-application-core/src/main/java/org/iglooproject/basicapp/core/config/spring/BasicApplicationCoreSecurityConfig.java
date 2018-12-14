package org.iglooproject.basicapp.core.config.spring;

import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.security.model.BasicApplicationPermission;
import org.iglooproject.basicapp.core.security.model.SecurityOptions;
import org.iglooproject.basicapp.core.security.service.BasicApplicationPermissionEvaluator;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.core.security.service.SecurityManagementServiceImpl;
import org.iglooproject.jpa.security.config.spring.AbstractJpaSecuritySecuredConfig;
import org.iglooproject.jpa.security.password.rule.SecurityPasswordRulesBuilder;
import org.iglooproject.jpa.security.service.AuthenticationUsernameComparison;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.iglooproject.jpa.security.service.NamedPermissionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.acls.domain.PermissionFactory;

@Configuration
public class BasicApplicationCoreSecurityConfig extends AbstractJpaSecuritySecuredConfig {

	@Override
	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public ICorePermissionEvaluator permissionEvaluator() {
		return new BasicApplicationPermissionEvaluator();
	}

	@Bean
	@Override
	public AuthenticationUsernameComparison authenticationUsernameComparison() {
		return AuthenticationUsernameComparison.CASE_SENSITIVE;
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
	public PermissionFactory permissionFactory() {
		return new NamedPermissionFactory(BasicApplicationPermission.ALL);
	}

	@Bean
	public ISecurityManagementService securityManagementService() {
		SecurityManagementServiceImpl securityManagementService = new SecurityManagementServiceImpl();
		securityManagementService
			.setOptions(
				TechnicalUser.class,
				new SecurityOptions()
					.passwordAdminRecovery()
					.passwordAdminUpdate()
					.passwordUserRecovery()
					.passwordUserUpdate()
					.passwordRules(
						SecurityPasswordRulesBuilder.start()
							.minMaxLength(User.MIN_PASSWORD_LENGTH, User.MAX_PASSWORD_LENGTH)
							.forbiddenUsername()
							.forbiddenPasswords(propertyService.get(SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS))
							.build()
					)
			)
			.setOptions(
				BasicUser.class,
				new SecurityOptions()
					.passwordExpiration()
					.passwordHistory()
					.passwordUserRecovery()
					.passwordUserUpdate()
					.passwordRules(
						SecurityPasswordRulesBuilder.start()
							.minMaxLength(User.MIN_PASSWORD_LENGTH, User.MAX_PASSWORD_LENGTH)
							.forbiddenUsername()
							.forbiddenPasswords(propertyService.get(SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS))
							.build()
					)
			)
			.setDefaultOptions(
				SecurityOptions.DEFAULT
			);
		
		return securityManagementService;
	}

}
