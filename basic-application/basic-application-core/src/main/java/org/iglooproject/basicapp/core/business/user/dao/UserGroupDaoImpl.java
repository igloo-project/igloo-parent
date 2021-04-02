package org.iglooproject.basicapp.core.business.user.dao;

import org.springframework.stereotype.Repository;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.security.business.user.dao.GenericUserGroupDaoImpl;

@Repository("personGroupDao")
public class UserGroupDaoImpl extends GenericUserGroupDaoImpl<UserGroup, User> implements IUserGroupDao {

	public UserGroupDaoImpl() {
		super();
	}

}
