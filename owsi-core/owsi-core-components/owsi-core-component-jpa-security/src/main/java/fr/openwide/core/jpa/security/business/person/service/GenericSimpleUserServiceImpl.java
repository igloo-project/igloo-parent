package fr.openwide.core.jpa.security.business.person.service;

import fr.openwide.core.jpa.security.business.person.dao.IGenericUserDao;
import fr.openwide.core.jpa.security.business.person.model.GenericSimpleUser;

public abstract class GenericSimpleUserServiceImpl<U extends GenericSimpleUser<U, ?>> extends GenericUserServiceImpl<U> {
	
	public GenericSimpleUserServiceImpl(IGenericUserDao<U> personDao) {
		super(personDao);
	}

}
