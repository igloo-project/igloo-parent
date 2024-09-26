package basicapp.back.business.role.dao;

import basicapp.back.business.role.model.Role;
import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;

public interface IRoleDao extends IGenericEntityDao<Long, Role> {

  Role getByTitle(String title);
}
