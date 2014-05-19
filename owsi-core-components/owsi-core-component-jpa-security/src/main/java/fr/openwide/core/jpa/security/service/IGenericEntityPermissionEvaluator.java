package fr.openwide.core.jpa.security.service;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;

/**
 * Use IGenericPermissionEvaluator instead.
 */
@Deprecated
public interface IGenericEntityPermissionEvaluator<U extends GenericUser<U, ?>, E extends GenericEntity<Long, ?>>
		extends IGenericPermissionEvaluator<U, E> {

}
