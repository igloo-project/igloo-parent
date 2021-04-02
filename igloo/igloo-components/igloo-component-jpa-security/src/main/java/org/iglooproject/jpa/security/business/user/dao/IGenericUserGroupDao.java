package org.iglooproject.jpa.security.business.user.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.business.user.model.GenericUserGroup;

public interface IGenericUserGroupDao<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>> extends IGenericEntityDao<Long, G> {

	G getByName(String name);

}