package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.user.dao.IUserDao;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserSearchParameters;
import fr.openwide.core.jpa.security.business.person.model.GenericUser_;
import fr.openwide.core.jpa.security.business.person.service.GenericSimpleUserServiceImpl;

@Service("personService")
public class UserServiceImpl extends GenericSimpleUserServiceImpl<User> implements IUserService {

	@Autowired
	private IUserDao userDao;
	
	@Autowired
	public UserServiceImpl(IUserDao userDao) {
		super(userDao);
		this.userDao = userDao;
	}

	@Override
	public List<User> listByUserName(String userName) {
		return listByField(GenericUser_.userName, userName);
	}
	
	@Override
	public <U extends User> List<U> search(Class<U> clazz, UserSearchParameters searchParameters, Integer limit, Integer offset) throws ParseException {
		return userDao.search(clazz, searchParameters, limit, offset);
	}
	
	@Override
	public <U extends User> int count(Class<U> clazz, UserSearchParameters searchParameters) throws ParseException {
		return userDao.count(clazz, searchParameters);
	}
}
