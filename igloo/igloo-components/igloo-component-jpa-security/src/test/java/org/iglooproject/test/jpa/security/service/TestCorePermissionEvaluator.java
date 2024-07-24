package org.iglooproject.test.jpa.security.service;

import org.iglooproject.jpa.security.service.AbstractCorePermissionEvaluator;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.springframework.security.acls.model.Permission;

public class TestCorePermissionEvaluator extends AbstractCorePermissionEvaluator<MockUser> {

  @Override
  protected boolean hasPermission(MockUser user, Object targetDomainObject, Permission permission) {
    return true;
  }
}
