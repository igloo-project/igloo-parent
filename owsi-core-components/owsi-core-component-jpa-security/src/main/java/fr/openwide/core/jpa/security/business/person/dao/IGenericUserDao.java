package fr.openwide.core.jpa.security.business.person.dao;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;

public interface IGenericUserDao<U extends GenericUser<U, ?>>
		extends IGenericEntityDao<Long, U> {
	
	Long countActive();

	U getByUserNameCaseInsensitive(String userName);

}