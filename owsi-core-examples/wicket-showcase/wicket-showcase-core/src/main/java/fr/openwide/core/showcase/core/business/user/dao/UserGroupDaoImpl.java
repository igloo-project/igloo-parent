package fr.openwide.core.showcase.core.business.user.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.security.business.person.dao.AbstractPersonGroupDaoImpl;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserGroup;

@Repository("userGroupDao")
public class UserGroupDaoImpl extends AbstractPersonGroupDaoImpl<UserGroup, User> implements IUserGroupDao {

}
