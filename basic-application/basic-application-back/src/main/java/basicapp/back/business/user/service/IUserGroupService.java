package basicapp.back.business.user.service;

import org.iglooproject.jpa.security.business.user.service.IGenericUserGroupService;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;

public interface IUserGroupService extends IGenericUserGroupService<UserGroup, User> {

}
