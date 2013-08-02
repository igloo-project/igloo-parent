package fr.openwide.core.web.security.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.openwide.core.jpa.security.service.ICorePermissionEvaluator;
import fr.openwide.core.web.security.access.expression.method.CoreWebSecurityExpressionHandler;

@Configuration
public abstract class AbstractWebappSecurityConfig {
	
	@Bean(name = "coreWebSecurityExpressionHandler")
	public CoreWebSecurityExpressionHandler webSecurityExpressionHandler(ICorePermissionEvaluator corePermissionEvaluator) {
		CoreWebSecurityExpressionHandler webSecurityExpressionHandler = new CoreWebSecurityExpressionHandler();
		webSecurityExpressionHandler.setCorePermissionEvaluator(corePermissionEvaluator);
		return webSecurityExpressionHandler;
	}

}