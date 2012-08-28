package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.user.dao.IUserDao;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.User_;
import fr.openwide.core.jpa.security.business.person.service.AbstractPersonServiceImpl;

@Service("personService")
public class UserServiceImpl extends AbstractPersonServiceImpl<User> implements IUserService {

	@Autowired
	public UserServiceImpl(IUserDao userDao) {
		super(userDao);
	}

	@Override
	public List<User> listByUserName(String userName) {
		return listByField(User_.userName, userName);
	}
}
