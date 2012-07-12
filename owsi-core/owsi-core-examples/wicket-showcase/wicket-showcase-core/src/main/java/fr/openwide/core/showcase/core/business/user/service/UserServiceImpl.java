package fr.openwide.core.showcase.core.business.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.service.AbstractPersonServiceImpl;
import fr.openwide.core.showcase.core.business.user.dao.IUserDao;
import fr.openwide.core.showcase.core.business.user.model.User;

@Service("personService")
public class UserServiceImpl extends AbstractPersonServiceImpl<User> implements IUserService {

	private IUserDao userDao;

	@Autowired
	public UserServiceImpl(IUserDao userDao) {
		super(userDao);
		this.userDao = userDao;
	}

	@Override
	public List<User> searchAutocomplete(String searchPattern, Integer limit) throws ServiceException {
		return userDao.searchAutocomplete(searchPattern, limit);
	}
}
