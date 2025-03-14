package org.iglooproject.jpa.security.service;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.springframework.security.acls.model.Permission;

public interface IGenericPermissionEvaluator<U extends GenericEntity<Long, U> & IUser, T> {

  boolean hasPermission(U user, T object, Permission permission);
}
