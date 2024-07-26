package org.iglooproject.basicapp.core.business.user.dao;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.security.business.user.dao.IGenericUserGroupDao;

public interface IUserGroupDao extends IGenericUserGroupDao<UserGroup, User> {}
