package org.iglooproject.test.jpa.security.business.person.service;

import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.user.service.ICoreUserService;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.springframework.security.access.annotation.Secured;

public interface IMockUserService extends ICoreUserService<MockUser> {

  @Secured(CoreAuthorityConstants.ROLE_ADMIN)
  void protectedMethodRoleAdmin();

  @Secured(CoreAuthorityConstants.ROLE_AUTHENTICATED)
  void protectedMethodRoleAuthenticated();

  @Secured(CoreAuthorityConstants.ROLE_ANONYMOUS)
  void protectedMethodRoleAnonymous();

  Long countEnabled();
}
