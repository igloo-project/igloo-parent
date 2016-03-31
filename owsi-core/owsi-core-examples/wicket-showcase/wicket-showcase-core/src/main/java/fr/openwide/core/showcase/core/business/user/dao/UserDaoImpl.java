package fr.openwide.core.showcase.core.business.user.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.security.business.person.dao.GenericUserDaoImpl;
import fr.openwide.core.showcase.core.business.user.model.User;

@Repository("userDao")
public class UserDaoImpl extends GenericUserDaoImpl<User> implements IUserDao {
	
}
