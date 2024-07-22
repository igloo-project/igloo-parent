package basicapp.back.business.user.dao;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;
import org.iglooproject.jpa.security.business.user.dao.GenericUserGroupDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class UserGroupDaoImpl extends GenericUserGroupDaoImpl<UserGroup, User>
    implements IUserGroupDao {

  public UserGroupDaoImpl() {
    super();
  }
}
