package fr.openwide.core.showcase.core.business.user.dao;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.dao.IPersonDao;
import fr.openwide.core.showcase.core.business.user.model.User;

public interface IUserDao extends IPersonDao<User> {

	List<User> searchAutocomplete(String searchPattern) throws ServiceException;
	
	List<User> searchByNameActive(String name, Boolean active, Integer limit, Integer offset)
			throws ParseException;	
	
	int countByNameActive(String name, Boolean active) throws ParseException;
}
