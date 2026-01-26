package org.iglooproject.jpa.security.access.expression;

import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CoreMethodSecurityExpressionRoot extends SecurityExpressionRoot
    implements MethodSecurityExpressionOperations {

  private ICorePermissionEvaluator corePermissionEvaluator;

  private Object filterObject;
  private Object returnObject;
  private Object target;

  public CoreMethodSecurityExpressionRoot(Authentication a) {
    // TODO RFO spring 7
    super(a);
  }

  @Override
  public void setFilterObject(Object filterObject) {
    this.filterObject = filterObject;
  }

  @Override
  public Object getFilterObject() {
    return filterObject;
  }

  @Override
  public void setReturnObject(Object returnObject) {
    this.returnObject = returnObject;
  }

  @Override
  public Object getReturnObject() {
    return returnObject;
  }

  /**
   * Sets the "this" property for use in expressions. Typically this will be the "this" property of
   * the {@code JoinPoint} representing the method invocation which is being protected.
   *
   * @param target the target object on which the method in is being invoked.
   */
  void setThis(Object target) {
    this.target = target;
  }

  @Override
  public Object getThis() {
    return target;
  }

  public final boolean hasPermission(Object permission) {
    return corePermissionEvaluator.hasPermission(getAuthentication(), permission);
  }

  public void setCorePermissionEvaluator(ICorePermissionEvaluator permissionEvaluator) {
    setPermissionEvaluator(permissionEvaluator);
    this.corePermissionEvaluator = permissionEvaluator;
  }
}
