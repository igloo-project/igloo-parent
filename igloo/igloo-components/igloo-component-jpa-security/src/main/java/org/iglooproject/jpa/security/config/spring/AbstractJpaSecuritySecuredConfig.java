package org.iglooproject.jpa.security.config.spring;

import com.google.common.collect.ImmutableList;
import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.security.access.expression.method.CoreMethodSecurityExpressionHandler;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.parameters.AnnotationParameterNameDiscoverer;
import org.springframework.security.core.parameters.DefaultSecurityParameterNameDiscoverer;

/**
 * Par rapport à son parent, cette classe active la protection via les annotations de sécurité
 * spring.
 *
 * @see Secured
 */
@Configuration
// définition des proxys Secured
public abstract class AbstractJpaSecuritySecuredConfig extends AbstractJpaSecurityConfig {

  @Override
  public MethodSecurityExpressionHandler expressionHandler(
      ICorePermissionEvaluator corePermissionEvaluator) {
    CoreMethodSecurityExpressionHandler methodSecurityExpressionHandler =
        new CoreMethodSecurityExpressionHandler();
    methodSecurityExpressionHandler.setCorePermissionEvaluator(corePermissionEvaluator);

    // Discover parameter name using the @PermissionObject annotation, too
    methodSecurityExpressionHandler.setParameterNameDiscoverer(
        new DefaultSecurityParameterNameDiscoverer(
            ImmutableList.of(
                new AnnotationParameterNameDiscoverer(PermissionObject.class.getName()))));

    return methodSecurityExpressionHandler;
  }

  @Configuration
  @EnableGlobalMethodSecurity(order = 0, prePostEnabled = true, securedEnabled = true)
  public static class JpaGlobalMethodSecurity extends GlobalMethodSecurityConfiguration {
    @Autowired private ApplicationContext applicationContext;

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

    /**
     * Load authenticationManager bean as late as possible (do not use an @Autowired attribute here,
     * as it silently breaks transaction interceptors)
     */
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
      return applicationContext.getBean("authenticationManager", AuthenticationManager.class);
    }
  }
}
