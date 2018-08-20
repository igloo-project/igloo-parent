package org.iglooproject.web.security.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.iglooproject.web.security.access.expression.method.CoreWebSecurityExpressionHandler;

@Configuration
public abstract class AbstractWebappSecurityConfig {
	
	@Bean(name = { "webSecurityExpressionHandler", "coreWebSecurityExpressionHandler" })
	public CoreWebSecurityExpressionHandler webSecurityExpressionHandler(ICorePermissionEvaluator corePermissionEvaluator) {
		CoreWebSecurityExpressionHandler webSecurityExpressionHandler = new CoreWebSecurityExpressionHandler();
		webSecurityExpressionHandler.setCorePermissionEvaluator(corePermissionEvaluator);
		return webSecurityExpressionHandler;
	}

}