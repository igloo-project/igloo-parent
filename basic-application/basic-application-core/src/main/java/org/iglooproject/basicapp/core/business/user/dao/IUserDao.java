package org.iglooproject.basicapp.core.business.user.dao;

import java.util.List;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.security.business.person.dao.IGenericUserDao;

public interface IUserDao extends IGenericUserDao<User> {

	User getByEmailCaseInsensitive(String email);

	List<User> listByUserName(String userName);

}
