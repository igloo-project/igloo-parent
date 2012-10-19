package fr.openwide.core.showcase.core.business.user.service;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.exception.SecurityServiceException;
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
	public List<User> searchAutocomplete(String searchPattern) throws ServiceException {
		return userDao.searchAutocomplete(searchPattern);
	}
	
	@Override
	public List<User> searchByNameActive(String name, Boolean active, Integer limit, Integer offset) throws ParseException {
		return userDao.searchByNameActive(name, active, limit, offset);
	}
	
	@Override
	public int countByNameActive(String name, Boolean active) throws ParseException {
		return userDao.countByNameActive(name, active);
	}
	
	@Override
	public void updateUserPosition(List<User> userList) throws ServiceException, SecurityServiceException {
		for (User user : userList) {
			update(user);
		}
	}
}
