package fr.openwide.core.basicapp.core.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.core.business.user.model.TechnicalUser;
import fr.openwide.core.basicapp.core.security.model.SecurityOptions;
import fr.openwide.core.basicapp.core.security.model.SecurityPasswordRules;
import fr.openwide.core.basicapp.core.security.service.ISecurityOptionsService;
import fr.openwide.core.basicapp.core.security.service.SecurityOptionsServiceImpl;

@Configuration
public class BasicApplicationCoreSecurityOptionsConfig {

	@Bean
	public ISecurityOptionsService securityOptionsService() {
		SecurityOptionsServiceImpl securityOptionsService = new SecurityOptionsServiceImpl();
		securityOptionsService
				.setOptions(
						TechnicalUser.class,
						new SecurityOptions()
								.passwordAdminRecovery()
								.passwordAdminUpdate()
								.passwordExpires()
								.passwordHistory()
								.passwordUserRecovery()
								.passwordUserUpdate()
								.passwordRules(
										new SecurityPasswordRules()
												.minMaxLength(5, 12)
												.mandatoryDigits(2)
												.mandatoryNonAlphanumericCharacters()
												.mandatoryUpperLowerCase()
												.forbiddenWhiteSpace()
												.forbiddenCharacters("@#")
								)
				)
				.setOptions(
						BasicUser.class,
						new SecurityOptions()
								.passwordAdminRecovery()
								.passwordExpires()
								.passwordHistory()
								.passwordUserRecovery()
								.passwordUserUpdate()
								.passwordRules(
										new SecurityPasswordRules()
												.minMaxLength(5, 12)
												.mandatoryDigits(2)
												.mandatoryNonAlphanumericCharacters()
												.mandatoryUpperLowerCase()
												.forbiddenWhiteSpace()
												.forbiddenCharacters("@#")
								)
				)
				.setDefaultOptions(
						SecurityOptions.DEFAULT
				)
		;
		
		return securityOptionsService;
	}
}
