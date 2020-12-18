package org.iglooproject.jpa.security.service;

import org.iglooproject.jpa.security.business.person.model.GenericUser;
import org.springframework.security.acls.model.Permission;

public interface IGenericPermissionEvaluator<U extends GenericUser<U, ?>, T> {

	boolean hasPermission(U user, T object, Permission permission);

}
