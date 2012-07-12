package fr.openwide.core.showcase.core.business.user.dao;

import java.util.List;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.dao.IPersonDao;
import fr.openwide.core.showcase.core.business.user.model.User;

public interface IUserDao extends IPersonDao<User> {

	List<User> searchAutocomplete(String searchPattern, Integer limit) throws ServiceException;

}
