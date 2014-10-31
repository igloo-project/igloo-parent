package fr.openwide.core.basicapp.core.business.user.dao;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserSearchParameters;
import fr.openwide.core.jpa.security.business.person.dao.IGenericUserDao;

public interface IUserDao extends IGenericUserDao<User> {

	<U extends User> List<U> search(Class<U> clazz, UserSearchParameters searchParams, Integer limit, Integer offset) throws ParseException;
	
	<U extends User> int count(Class<U> clazz, UserSearchParameters searchParams) throws ParseException;

	User getByEmailCaseInsensitive(String email);

}
