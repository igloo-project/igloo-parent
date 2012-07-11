package fr.openwide.core.basicapp.core.business.user.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup_;
import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;

@Repository("personGroupDao")
public class UserGroupDaoImpl extends GenericEntityDaoImpl<Long, UserGroup> implements IUserGroupDao {

	public UserGroupDaoImpl() {
		super();
	}

	@Override
	public UserGroup getByName(String name) {
		return getByField(UserGroup_.name, name);
	}
}
