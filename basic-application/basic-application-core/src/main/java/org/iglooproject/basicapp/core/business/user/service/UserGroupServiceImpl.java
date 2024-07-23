package org.iglooproject.basicapp.core.business.user.service;

import org.iglooproject.basicapp.core.business.user.dao.IUserGroupDao;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.security.business.user.service.GenericUserGroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGroupServiceImpl extends GenericUserGroupServiceImpl<UserGroup, User>
    implements IUserGroupService {

  @Autowired
  public UserGroupServiceImpl(IUserGroupDao userGroupDao) {
    super(userGroupDao);
  }
}
