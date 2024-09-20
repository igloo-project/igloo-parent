package basicapp.back.business.role.dao;

import basicapp.back.business.role.model.Role;
import java.util.Collection;
import java.util.List;
import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;

public interface IRoleDao extends IGenericEntityDao<Long, Role> {

  List<Role> listByIds(Collection<Long> ids);

  Role getByTitle(String title);
}
