package org.iglooproject.jpa.security.business.person.dao;

import java.util.List;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.security.business.person.model.GenericUser;

public interface IGenericUserDao<U extends GenericUser<?, ?>>
		extends IGenericEntityDao<Long, U> {

	List<String> listActiveUsernames();
	
	Long countActive();

	U getByUsernameCaseInsensitive(String username);

}