package fr.openwide.core.jpa.security.service;

import org.springframework.security.acls.model.Permission;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;

public interface IGenericEntityPermissionEvaluator<U extends AbstractPerson<U>, E extends GenericEntity<Long, E>> {

	boolean hasPermission(U user, E genericEntity, Permission permission);

}
