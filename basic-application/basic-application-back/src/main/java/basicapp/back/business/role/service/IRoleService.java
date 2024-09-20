package basicapp.back.business.role.service;

import basicapp.back.business.role.model.Role;
import java.util.Collection;
import java.util.List;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface IRoleService extends IGenericEntityService<Long, Role> {

  void saveRole(Role role) throws ServiceException, SecurityServiceException;

  List<Role> listByIds(Collection<Long> ids);

  Role getByTitle(String title);
}
