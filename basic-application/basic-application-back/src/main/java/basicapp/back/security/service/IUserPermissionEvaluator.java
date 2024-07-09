package basicapp.back.security.service;

import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

import basicapp.back.business.user.model.User;

public interface IUserPermissionEvaluator extends IGenericPermissionEvaluator<User, User> {

}
