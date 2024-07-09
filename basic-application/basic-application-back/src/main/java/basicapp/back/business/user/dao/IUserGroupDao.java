package basicapp.back.business.user.dao;

import org.iglooproject.jpa.security.business.user.dao.IGenericUserGroupDao;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;

public interface IUserGroupDao extends IGenericUserGroupDao<UserGroup, User> {

}
