package fr.openwide.core.basicapp.core.business.user.dao;

import java.util.List;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.security.business.person.dao.IPersonDao;

public interface IUserDao extends IPersonDao<User> {
	
	List<User> search(String searchTerm, Integer limit, Integer offset);

	int countSearch(String searchTerm);
}
