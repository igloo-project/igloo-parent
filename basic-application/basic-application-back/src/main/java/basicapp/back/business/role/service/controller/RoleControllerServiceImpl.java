package basicapp.back.business.role.service.controller;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.service.IRoleService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleControllerServiceImpl implements IRoleControllerService {

  private final IRoleService roleService;

  @Autowired
  public RoleControllerServiceImpl(IRoleService roleService) {
    this.roleService = roleService;
  }

  @Override
  public void saveRole(Role role) throws ServiceException, SecurityServiceException {
    roleService.saveRole(role);
  }

  @Override
  public Role getByTitle(String title) {
    return roleService.getByTitle(title);
  }
}
