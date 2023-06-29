package org.iglooproject.basicapp.web.application.config.spring;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.iglooproject.web.security.access.expression.method.CoreWebSecurityExpressionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class BasicApplicationSecurityConfiguration {

	@Bean
	@Order(1)
	public SecurityFilterChain consoleSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
			.securityMatcher(antMatcher("/console/**"))
			.headers().disable()
			.csrf().disable()
			.formLogin().disable()
			.securityContext().requireExplicitSave(false).and()
			.anonymous().authorities(CoreAuthorityConstants.ROLE_ANONYMOUS).and()
			.exceptionHandling()
				.accessDeniedPage("/console/access-denied/")
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/console/login/")).and()
			.authorizeHttpRequests(requests -> requests
				.requestMatchers(
					antMatcher("/console/login/**"),
					antMatcher("/console/access-denied/**")).permitAll()
				.requestMatchers(antMatcher("/console/**")).hasAnyAuthority(CoreAuthorityConstants.ROLE_ADMIN)
			)
			.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain ressourcesSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
			.securityMatcher(antMatcher("/wicket/resource/**"))
			.headers().disable()
			.csrf().disable()
			.formLogin().disable()
			.securityContext().requireExplicitSave(false).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
			.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint()).and()
			.authorizeHttpRequests(requests -> requests
				.requestMatchers(
					regexMatcher("^/wicket/resource/org.iglooproject.basicapp.web.application.common.template.resources.js.[^/]+.*"),
					regexMatcher("^/wicket/resource/org.iglooproject.basicapp.web.application.common.template.resources.styles.[^/]+.*"),
					regexMatcher("^/wicket/resource/org.iglooproject.basicapp.web.application.common.template.resources.images.[^/]+.*")
				).hasAnyAuthority(CoreAuthorityConstants.ROLE_ANONYMOUS)
				.requestMatchers(regexMatcher("^/wicket/resource/org.iglooproject.basicapp.web.application.[^/]+.*")).hasAnyAuthority(CoreAuthorityConstants.ROLE_AUTHENTICATED)
				.requestMatchers(antMatcher("/wicket/resource/**")).hasAnyAuthority(CoreAuthorityConstants.ROLE_ANONYMOUS)
			)
			.build();
	}

	@Bean
	@Order(3)
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.headers().disable()
			.csrf().disable()
			.formLogin().disable()
			.securityContext().requireExplicitSave(false).and()
			.anonymous().authorities(CoreAuthorityConstants.ROLE_ANONYMOUS).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).sessionFixation().changeSessionId().and()
			.exceptionHandling()
				.accessDeniedPage("/access-denied/")
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/")).and()
			.authorizeHttpRequests(requests -> requests
				.requestMatchers(
					antMatcher("/login/"),
					antMatcher("/login/failure/"),
					antMatcher("/access-denied/"),
					antMatcher("/security/password/recovery/request/creation/"),
					antMatcher("/security/password/recovery/request/reset/"),
					antMatcher("/security/password/creation/"),
					antMatcher("/security/password/reset/"),
					antMatcher("/maintenance/")).permitAll()
				.requestMatchers(antMatcher("/**")).hasAnyAuthority(CoreAuthorityConstants.ROLE_AUTHENTICATED)
			)
			.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer(ICorePermissionEvaluator corePermissionEvaluator) {
		CoreWebSecurityExpressionHandler webSecurityExpressionHandler = new CoreWebSecurityExpressionHandler();
		webSecurityExpressionHandler.setCorePermissionEvaluator(corePermissionEvaluator);
		return web -> web
			.expressionHandler(webSecurityExpressionHandler)
			.ignoring()
			.requestMatchers(
				antMatcher("/static/**"),
				antMatcher("/monitoring/**"),
				antMatcher("/webjars/**"),
				antMatcher("/android-chrome-192x192.png"),
				antMatcher("/android-chrome-512x512.png"),
				antMatcher("/apple-touch-icon.png"),
				antMatcher("/browserconfig.xml"),
				antMatcher("/favicon.ico"),
				antMatcher("/favicon-16x16.png"),
				antMatcher("/favicon-32x32.png"),
				antMatcher("/mstile-150x150.png"),
				antMatcher("/safari-pinned-tab.svg"),
				antMatcher("/site.webmanifest"));
	}

}
