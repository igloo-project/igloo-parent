package fr.openwide.core.hibernate.security.business.person.dao;

import fr.openwide.core.hibernate.business.generic.dao.GenericEntityDao;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPerson;

public interface PersonDao<P extends AbstractPerson<P>>
		extends GenericEntityDao<Integer, P> {
	
	Long countActive();
	
}