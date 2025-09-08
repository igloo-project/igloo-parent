package org.iglooproject.test.jpa.security.business.person.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.user.service.ICoreUserSecurityService;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.springframework.security.access.annotation.Secured;

public interface IMockUserService extends ICoreUserSecurityService<MockUser>, IGenericEntityService<Long, MockUser> {

  @Secured(CoreAuthorityConstants.ROLE_ADMIN)
  void protectedMethodRoleAdmin();

  @Secured(CoreAuthorityConstants.ROLE_AUTHENTICATED)
  void protectedMethodRoleAuthenticated();

  @Secured(CoreAuthorityConstants.ROLE_ANONYMOUS)
  void protectedMethodRoleAnonymous();

  Long countEnabled();

  void setPasswords(MockUser user, String rawPassword)
      throws ServiceException, SecurityServiceException;
}
