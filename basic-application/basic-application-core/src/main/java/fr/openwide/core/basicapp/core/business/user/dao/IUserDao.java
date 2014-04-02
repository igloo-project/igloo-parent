package fr.openwide.core.basicapp.core.business.user.dao;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserSearchParameters;
import fr.openwide.core.jpa.security.business.person.dao.IGenericUserDao;

public interface IUserDao extends IGenericUserDao<User> {
	
	List<User> search(UserSearchParameters searchParams, Integer limit, Integer offset)
			throws ParseException;	
	
	int count(UserSearchParameters searchParams) throws ParseException;

}
