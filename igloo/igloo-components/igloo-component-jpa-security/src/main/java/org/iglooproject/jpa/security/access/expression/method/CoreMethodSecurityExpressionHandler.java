package org.iglooproject.jpa.security.access.expression.method;

import org.aopalliance.intercept.MethodInvocation;
import org.iglooproject.jpa.security.access.expression.CoreMethodSecurityExpressionRoot;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CoreMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

  private ICorePermissionEvaluator corePermissionEvaluator;

  @Override
  protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
      Authentication authentication, MethodInvocation invocation) {
    CoreMethodSecurityExpressionRoot root = new CoreMethodSecurityExpressionRoot(authentication);
    root.setCorePermissionEvaluator(getCorePermissionEvaluator());

    return root;
  }

  protected ICorePermissionEvaluator getCorePermissionEvaluator() {
    return corePermissionEvaluator;
  }

  public void setCorePermissionEvaluator(ICorePermissionEvaluator corePermissionEvaluator) {
    super.setPermissionEvaluator(corePermissionEvaluator);
    this.corePermissionEvaluator = corePermissionEvaluator;
  }
}
