package org.iglooproject.jpa.security.config.spring;

import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.security.access.expression.method.CoreMethodSecurityExpressionHandler;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.parameters.AnnotationParameterNameDiscoverer;
import org.springframework.security.core.parameters.DefaultSecurityParameterNameDiscoverer;

import com.google.common.collect.ImmutableList;

/**
 * Par rapport à son parent, cette classe active la protection via les
 * annotations de sécurité spring.
 * 
 * @see Secured
 */
@Configuration
// définition des proxys Secured
public abstract class AbstractJpaSecuritySecuredConfig extends AbstractJpaSecurityConfig {
	
	@Override
	public MethodSecurityExpressionHandler expressionHandler(ICorePermissionEvaluator corePermissionEvaluator) {
		CoreMethodSecurityExpressionHandler methodSecurityExpressionHandler = new CoreMethodSecurityExpressionHandler();
		methodSecurityExpressionHandler.setCorePermissionEvaluator(corePermissionEvaluator);
		
		// Discover parameter name using the @PermissionObject annotation, too
		methodSecurityExpressionHandler.setParameterNameDiscoverer(new DefaultSecurityParameterNameDiscoverer(
				ImmutableList.of(
						new AnnotationParameterNameDiscoverer(PermissionObject.class.getName())
				)
		));
		
		return methodSecurityExpressionHandler;
	}

	@Configuration
	@EnableGlobalMethodSecurity(order = 0, prePostEnabled = true, securedEnabled = true)
	public static class JpaGlobalMethodSecurity extends GlobalMethodSecurityConfiguration {
		@Autowired
		@Qualifier("runAsManager")
		private RunAsManager runAsManager;
		
		@Autowired
		@Qualifier("expressionHandler")
		private MethodSecurityExpressionHandler methodSecurityExpressionHandler;
		
		@Override
		protected RunAsManager runAsManager() {
			return runAsManager;
		}
		
		@Override
		protected MethodSecurityExpressionHandler createExpressionHandler() {
			return methodSecurityExpressionHandler;
		}
	}

}
