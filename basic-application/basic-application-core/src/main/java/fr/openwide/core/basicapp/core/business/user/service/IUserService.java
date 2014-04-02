package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserSearchParameters;
import fr.openwide.core.jpa.security.business.person.service.IGenericUserService;

public interface IUserService extends IGenericUserService<User> {

	List<User> listByUserName(String userName);

	List<User> search(UserSearchParameters searchParams, Integer limit, Integer offset) throws ParseException;
	
	int count(UserSearchParameters searchParams) throws ParseException;

}