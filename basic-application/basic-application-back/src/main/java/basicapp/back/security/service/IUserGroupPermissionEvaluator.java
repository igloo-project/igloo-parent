package basicapp.back.security.service;

import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;

public interface IUserGroupPermissionEvaluator extends IGenericPermissionEvaluator<User, UserGroup> {

}
