package basicapp.back.security.service.permission;

import basicapp.back.business.user.model.User;
import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

public interface IUserPermissionEvaluator extends IGenericPermissionEvaluator<User, User> {}
