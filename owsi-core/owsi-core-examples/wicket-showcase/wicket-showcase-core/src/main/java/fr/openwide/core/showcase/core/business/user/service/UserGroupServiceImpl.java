package fr.openwide.core.showcase.core.business.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.security.business.person.service.AbstractPersonGroupServiceImpl;
import fr.openwide.core.showcase.core.business.user.dao.IUserGroupDao;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserGroup;

@Service("userGroupService")
public class UserGroupServiceImpl extends AbstractPersonGroupServiceImpl<UserGroup, User> implements IUserGroupService {

	@Autowired
	public UserGroupServiceImpl(IUserGroupDao userGroupDao) {
		super(userGroupDao);
	}
}
