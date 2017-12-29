package org.iglooproject.showcase.core.business.user.dao;

import org.springframework.stereotype.Repository;

import org.iglooproject.jpa.security.business.person.dao.GenericUserDaoImpl;
import org.iglooproject.showcase.core.business.user.model.User;

@Repository("userDao")
public class UserDaoImpl extends GenericUserDaoImpl<User> implements IUserDao {
	
}
