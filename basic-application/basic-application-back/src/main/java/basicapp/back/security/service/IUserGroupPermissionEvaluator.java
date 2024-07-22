package basicapp.back.security.service;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;
import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

public interface IUserGroupPermissionEvaluator
    extends IGenericPermissionEvaluator<User, UserGroup> {}
