package org.iglooproject.jpa.security.business.user.service;

import org.iglooproject.jpa.security.business.user.dao.IGenericUserDao;
import org.iglooproject.jpa.security.business.user.model.GenericSimpleUser;
import org.iglooproject.jpa.security.business.user.model.GenericUserGroup;

public abstract class GenericSimpleUserServiceImpl<U extends GenericSimpleUser<U, G>, G extends GenericUserGroup<G, U>> extends GenericUserServiceImpl<U, G> {

	public GenericSimpleUserServiceImpl(IGenericUserDao<U, G> dao) {
		super(dao);
	}

}
