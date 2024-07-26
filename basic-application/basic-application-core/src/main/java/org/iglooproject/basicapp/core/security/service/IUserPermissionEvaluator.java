package org.iglooproject.basicapp.core.security.service;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

public interface IUserPermissionEvaluator extends IGenericPermissionEvaluator<User, User> {}
