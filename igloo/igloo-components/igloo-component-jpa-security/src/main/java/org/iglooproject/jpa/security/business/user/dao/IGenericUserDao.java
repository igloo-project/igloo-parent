package org.iglooproject.jpa.security.business.user.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.business.user.model.GenericUserGroup;

public interface IGenericUserDao<U extends GenericUser<U, G>, G extends GenericUserGroup<G, U>> extends IGenericEntityDao<Long, U> {

	U getByUsernameCaseInsensitive(String username);

}
