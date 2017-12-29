package org.iglooproject.showcase.core.business.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.security.business.person.service.GenericUserGroupServiceImpl;
import org.iglooproject.showcase.core.business.user.dao.IUserGroupDao;
import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.core.business.user.model.UserGroup;

@Service("userGroupService")
public class UserGroupServiceImpl extends GenericUserGroupServiceImpl<UserGroup, User> implements IUserGroupService {

	@Autowired
	public UserGroupServiceImpl(IUserGroupDao userGroupDao) {
		super(userGroupDao);
	}
}
