package org.iglooproject.showcase.core.business.user.dao;

import org.iglooproject.jpa.security.business.person.dao.IGenericUserGroupDao;
import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.core.business.user.model.UserGroup;

public interface IUserGroupDao extends IGenericUserGroupDao<UserGroup, User> {

}
