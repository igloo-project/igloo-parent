package org.iglooproject.basicapp.core.config.spring;

import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_LENGTH_MAX;
import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_LENGTH_MIN;
import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.security.model.BasicApplicationPermission;
import org.iglooproject.basicapp.core.security.model.SecurityOptions;
import org.iglooproject.basicapp.core.security.service.BasicApplicationAuthenticationServiceImpl;
import org.iglooproject.basicapp.core.security.service.BasicApplicationPermissionEvaluator;
import org.iglooproject.basicapp.core.security.service.BasicApplicationSecurityServiceImpl;
import org.iglooproject.basicapp.core.security.service.BasicApplicationUserDetailsServiceImpl;
import org.iglooproject.basicapp.core.security.service.IBasicApplicationAuthenticationService;
import org.iglooproject.basicapp.core.security.service.IBasicApplicationSecurityService;
import org.iglooproject.basicapp.core.security.service.IBasicApplicationUserDetailsService;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.core.security.service.SecurityManagementServiceImpl;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.service.AuthenticationUsernameComparison;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.iglooproject.jpa.security.service.NamedPermissionFactory;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.acls.domain.PermissionFactory;

import com.google.common.collect.ImmutableMap;

@Configuration
public class BasicApplicationCoreSecurityConfig {

	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public ICorePermissionEvaluator permissionEvaluator() {
		return new BasicApplicationPermissionEvaluator();
	}

	@Bean
	public IBasicApplicationAuthenticationService authenticationService() {
		return new BasicApplicationAuthenticationServiceImpl();
	}

	@Bean
	public IBasicApplicationUserDetailsService userDetailsService(AuthenticationUsernameComparison authenticationUsernameComparison) {
		BasicApplicationUserDetailsServiceImpl userDetailsService = new BasicApplicationUserDetailsServiceImpl();
		userDetailsService.setAuthenticationUsernameComparison(authenticationUsernameComparison);
		return userDetailsService;
	}
	
	@Bean
	public AuthenticationUsernameComparison authenticationUsernameComparison() {
		return AuthenticationUsernameComparison.CASE_SENSITIVE;
	}

	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public IBasicApplicationSecurityService securityService() {
		return new BasicApplicationSecurityServiceImpl();
	}

	@Bean
	@ConditionalOnMissingBean
	// TODO igloo-boot: replace with a customizer for permission list
	public PermissionFactory permissionFactory() {
		return new NamedPermissionFactory(BasicApplicationPermission.ALL);
	}

	@Bean
	public ISecurityManagementService securityManagementService(IPropertyService propertyService) {
		int passwordLengthMin = propertyService.get(SECURITY_PASSWORD_LENGTH_MIN);
		int passwordLengthMax = propertyService.get(SECURITY_PASSWORD_LENGTH_MAX);
		
		return new SecurityManagementServiceImpl(
			SecurityOptions.create(securityOptions -> securityOptions
				.passwordUserRecovery()
				.passwordUserUpdate()
				.passwordRules(
					rules -> rules
						.minMaxLength(passwordLengthMin, passwordLengthMax)
						.forbiddenUsername()
						.forbiddenPasswords(propertyService.get(SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS))
				)
			),
			ImmutableMap.<Class<? extends GenericUser<?, ?>>, SecurityOptions>builder()
				.put(
					TechnicalUser.class,
					SecurityOptions.create(securityOptions -> securityOptions
						.passwordAdminRecovery()
						.passwordAdminUpdate()
						.passwordUserRecovery()
						.passwordUserUpdate()
						.passwordRules(
							rules -> rules
								.minMaxLength(passwordLengthMin, passwordLengthMax)
								.forbiddenUsername()
								.forbiddenPasswords(propertyService.get(SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS))
							
						)
					)
				)
				.put(
					BasicUser.class,
					SecurityOptions.create(securityOptions -> securityOptions
						.passwordExpiration()
						.passwordHistory()
						.passwordUserRecovery()
						.passwordUserUpdate()
						.passwordRules(
							rules -> rules
								.minMaxLength(passwordLengthMin, passwordLengthMax)
								.forbiddenUsername()
								.forbiddenPasswords(propertyService.get(SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS))
						)
					)
				)
				.build()
		);
	}

}
