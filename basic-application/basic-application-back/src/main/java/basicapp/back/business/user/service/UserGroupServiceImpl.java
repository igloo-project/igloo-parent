package basicapp.back.business.user.service;

import basicapp.back.business.user.dao.IUserGroupDao;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;
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
