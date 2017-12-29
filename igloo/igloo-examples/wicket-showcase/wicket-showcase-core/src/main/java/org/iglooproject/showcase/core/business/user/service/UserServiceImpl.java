package org.iglooproject.showcase.core.business.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.person.service.GenericSimpleUserServiceImpl;
import org.iglooproject.showcase.core.business.user.dao.IUserDao;
import org.iglooproject.showcase.core.business.user.model.User;

@Service("personService")
public class UserServiceImpl extends GenericSimpleUserServiceImpl<User> implements IUserService {

	@Autowired
	public UserServiceImpl(IUserDao userDao) {
		super(userDao);
	}
	
	@Override
	public void updateUserPosition(List<User> userList) throws ServiceException, SecurityServiceException {
		for (User user : userList) {
			update(user);
		}
	}
}
