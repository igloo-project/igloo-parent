package fr.openwide.core.basicapp.core.business.user.dao;

import java.util.List;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.security.business.person.dao.IGenericUserDao;

public interface IUserDao extends IGenericUserDao<User> {

	User getByEmailCaseInsensitive(String email);

	List<User> listByUserName(String userName);

}
