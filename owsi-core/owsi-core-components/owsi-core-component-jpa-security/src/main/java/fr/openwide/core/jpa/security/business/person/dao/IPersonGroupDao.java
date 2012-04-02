package fr.openwide.core.jpa.security.business.person.dao;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.AbstractPersonGroup;

public interface IPersonGroupDao<G extends AbstractPersonGroup<G, P>, P extends AbstractPerson<P>>
		extends IGenericEntityDao<Long, G> {
	
}