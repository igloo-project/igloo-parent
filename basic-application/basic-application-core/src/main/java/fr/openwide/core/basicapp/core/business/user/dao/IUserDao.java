package fr.openwide.core.basicapp.core.business.user.dao;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.security.business.person.dao.IPersonDao;

public interface IUserDao extends IPersonDao<User> {
	
	List<User> searchByNameActive(String name, Boolean active, Integer limit, Integer offset)
			throws ParseException;	
	
	int countByNameActive(String name, Boolean active) throws ParseException;
}
