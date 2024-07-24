package basicapp.back.business.user.dao;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;
import org.iglooproject.jpa.security.business.user.dao.IGenericUserGroupDao;

public interface IUserGroupDao extends IGenericUserGroupDao<UserGroup, User> {}
