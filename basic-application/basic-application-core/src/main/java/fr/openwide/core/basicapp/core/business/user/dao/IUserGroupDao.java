package fr.openwide.core.basicapp.core.business.user.dao;

import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;

public interface IUserGroupDao extends IGenericEntityDao<Long, UserGroup> {

	UserGroup getByName(String name);
}
