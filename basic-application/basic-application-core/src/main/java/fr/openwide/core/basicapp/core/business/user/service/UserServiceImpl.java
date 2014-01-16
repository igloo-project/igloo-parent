package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.user.dao.IUserDao;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.User_;
import fr.openwide.core.jpa.security.business.person.service.AbstractPersonServiceImpl;

@Service("personService")
public class UserServiceImpl extends AbstractPersonServiceImpl<User> implements IUserService {

	@Autowired
	private IUserDao userDao;
	
	@Autowired
	public UserServiceImpl(IUserDao userDao) {
		super(userDao);
		this.userDao = userDao;
	}

	@Override
	public List<User> listByUserName(String userName) {
		return listByField(User_.userName, userName);
	}
	
	@Override
	public List<User> searchByNameActive(String name, Boolean active, Integer limit, Integer offset) throws ParseException {
		return userDao.searchByNameActive(name, active, limit, offset);
	}
	
	@Override
	public int countByNameActive(String name, Boolean active) throws ParseException {
		return userDao.countByNameActive(name, active);
	}
}
