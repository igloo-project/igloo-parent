package basicapp.back.security.service.permission;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.user.model.User;
import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

public interface IRolePermissionEvaluator extends IGenericPermissionEvaluator<User, Role> {}
