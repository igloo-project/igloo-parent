package basicapp.back.business.role.service.business;

import basicapp.back.business.role.model.Role;
import java.util.List;

public interface IRoleService {

  void saveRole(Role role);

  Role getByTitle(String title);

  List<Role> findAll();
}
