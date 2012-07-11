package fr.openwide.core.basicapp.core.business.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.user.dao.IUserGroupDao;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

@Service("personGroupService")
public class UserGroupServiceImpl extends GenericEntityServiceImpl<Long, UserGroup>
		implements IUserGroupService {

	@Autowired
	private IUserService userService;

	@Autowired
	public UserGroupServiceImpl(IUserGroupDao userGroupDao) {
		super(userGroupDao);
	}

	@Override
	public void addPerson(UserGroup group, User user)
			throws ServiceException, SecurityServiceException {
		user.getUserGroups().add(group);
		userService.update(user);
	}
}
