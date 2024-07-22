package basicapp.back.business.user.service;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;
import org.iglooproject.jpa.security.business.user.service.IGenericUserGroupService;

public interface IUserGroupService extends IGenericUserGroupService<UserGroup, User> {}
