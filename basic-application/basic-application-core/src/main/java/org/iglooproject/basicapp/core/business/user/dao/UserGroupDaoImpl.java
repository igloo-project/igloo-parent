package org.iglooproject.basicapp.core.business.user.dao;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.security.business.user.dao.GenericUserGroupDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class UserGroupDaoImpl extends GenericUserGroupDaoImpl<UserGroup, User>
    implements IUserGroupDao {

  public UserGroupDaoImpl() {
    super();
  }
}
