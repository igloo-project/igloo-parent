package org.iglooproject.jpa.security.business.person.service;

import org.iglooproject.jpa.security.business.person.dao.IGenericUserDao;
import org.iglooproject.jpa.security.business.person.model.GenericSimpleUser;

public abstract class GenericSimpleUserServiceImpl<U extends GenericSimpleUser<U, ?>> extends GenericUserServiceImpl<U> {

	public GenericSimpleUserServiceImpl(IGenericUserDao<U> dao) {
		super(dao);
	}

}
