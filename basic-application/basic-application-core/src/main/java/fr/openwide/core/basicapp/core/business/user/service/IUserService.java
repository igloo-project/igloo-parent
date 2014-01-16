package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.security.business.person.service.IPersonService;

public interface IUserService extends IPersonService<User> {

	List<User> listByUserName(String userName);

	List<User> searchByNameActive(String name, Boolean active, Integer limit, Integer offset) throws ParseException;
	
	int countByNameActive(String name, Boolean active) throws ParseException;
}