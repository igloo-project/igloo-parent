package fr.openwide.core.jpa.security.service;

import org.springframework.security.acls.model.Permission;

import fr.openwide.core.jpa.security.business.person.model.GenericUser;

public interface IGenericPermissionEvaluator<U extends GenericUser<U, ?>, E> {

	boolean hasPermission(U user, E genericEntity, Permission permission);

}
