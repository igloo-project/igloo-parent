package basicapp.back.business.role.service.controller;

import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.ROLE_WRITE;

import basicapp.back.business.role.model.Role;
import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IRoleControllerService {

  @PreAuthorize(ROLE_WRITE)
  void saveRole(@PermissionObject Role role) throws ServiceException, SecurityServiceException;

  Role getByTitle(String title);
}
