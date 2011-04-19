package fr.openwide.core.hibernate.security.business.person.dao;

import fr.openwide.core.hibernate.business.generic.dao.GenericEntityDao;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPerson;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPersonGroup;

public interface PersonGroupDao<G extends AbstractPersonGroup<G, P>, P extends AbstractPerson<P>>
		extends GenericEntityDao<Integer, G> {
	
}