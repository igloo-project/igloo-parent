package org.iglooproject.jpa.security.service;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.security.business.person.model.GenericUser;

/**
 * Use IGenericPermissionEvaluator instead.
 */
@Deprecated
public interface IGenericEntityPermissionEvaluator<U extends GenericUser<U, ?>, E extends GenericEntity<Long, ?>>
		extends IGenericPermissionEvaluator<U, E> {

}
