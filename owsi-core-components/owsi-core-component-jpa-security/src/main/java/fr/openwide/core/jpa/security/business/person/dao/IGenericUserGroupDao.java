package fr.openwide.core.jpa.security.business.person.dao;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;
import fr.openwide.core.jpa.security.business.person.model.GenericUserGroup;

public interface IGenericUserGroupDao<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends IGenericEntityDao<Long, G> {

	G getByName(String name);

}