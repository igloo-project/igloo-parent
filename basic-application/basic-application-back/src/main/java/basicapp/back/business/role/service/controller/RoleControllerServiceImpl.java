package basicapp.back.business.role.service.controller;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.service.IRoleService;
import igloo.loginmdc.annotation.LogExecution;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class RoleControllerServiceImpl implements IRoleControllerService {

  private final IRoleService roleService;

  public RoleControllerServiceImpl(IRoleService roleService) {
    this.roleService = roleService;
  }

  @Override
  @LogExecution
  public void saveRole(Role role) throws ServiceException, SecurityServiceException {
    roleService.saveRole(role);
  }

  @Override
  public @Nullable Role getByTitle(@Nullable String title) {
    return roleService.getByTitle(title);
  }
}
