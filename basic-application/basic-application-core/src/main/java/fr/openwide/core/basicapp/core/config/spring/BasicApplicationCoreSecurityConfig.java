package fr.openwide.core.basicapp.core.config.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.core.business.user.model.TechnicalUser;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.core.security.model.SecurityOptions;
import fr.openwide.core.basicapp.core.security.model.SecurityPasswordRules;
import fr.openwide.core.basicapp.core.security.service.BasicApplicationPermissionEvaluator;
import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.core.security.service.SecurityManagementServiceImpl;
import fr.openwide.core.jpa.security.config.spring.AbstractJpaSecurityConfig;
import fr.openwide.core.jpa.security.service.AuthenticationUserNameComparison;
import fr.openwide.core.jpa.security.service.ICorePermissionEvaluator;

@Configuration
public class BasicApplicationCoreSecurityConfig extends AbstractJpaSecurityConfig {
	
	@Autowired
	private BasicApplicationConfigurer configurer;
	
	@Override
	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public ICorePermissionEvaluator permissionEvaluator() {
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

	@Bean
	public ISecurityManagementService securityManagementService() {
		SecurityManagementServiceImpl securityManagementService = new SecurityManagementServiceImpl();
		securityManagementService
				.setOptions(
						TechnicalUser.class,
						new SecurityOptions()
								.passwordAdminRecovery()
								.passwordAdminUpdate()
								.passwordExpiration()
								.passwordHistory()
								.passwordUserRecovery()
								.passwordUserUpdate()
								.passwordRules(
										new SecurityPasswordRules()
												.minMaxLength(User.MIN_PASSWORD_LENGTH, User.MAX_PASSWORD_LENGTH)
												.forbiddenUsername()
												.forbiddenPasswords(configurer.getSecurityPasswordUserForbiddenPasswords())
								)
				)
				.setOptions(
						BasicUser.class,
						new SecurityOptions()
								.passwordAdminRecovery()
								.passwordExpiration()
								.passwordHistory()
								.passwordUserRecovery()
								.passwordUserUpdate()
								.passwordRules(
										new SecurityPasswordRules()
												.minMaxLength(User.MIN_PASSWORD_LENGTH, User.MAX_PASSWORD_LENGTH)
												.forbiddenUsername()
												.forbiddenPasswords(configurer.getSecurityPasswordUserForbiddenPasswords())
								)
				)
				.setDefaultOptions(
						SecurityOptions.DEFAULT
				)
		;
		
		return securityManagementService;
	}
}
