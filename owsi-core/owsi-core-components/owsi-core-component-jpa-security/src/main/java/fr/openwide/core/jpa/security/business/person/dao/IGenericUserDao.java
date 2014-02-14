package fr.openwide.core.jpa.security.business.person.dao;

import java.util.List;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;

public interface IGenericUserDao<U extends GenericUser<?, ?>>
		extends IGenericEntityDao<Long, U> {

	List<String> listActiveUserNames();
	
	Long countActive();

	U getByUserNameCaseInsensitive(String userName);

}