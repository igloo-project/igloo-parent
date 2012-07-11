package fr.openwide.core.basicapp.core.business.user.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.security.business.person.dao.AbstractPersonDaoImpl;

@Repository("personDao")
public class UserDaoImpl extends AbstractPersonDaoImpl<User> implements IUserDao {

	public UserDaoImpl() {
		super();
	}
}
