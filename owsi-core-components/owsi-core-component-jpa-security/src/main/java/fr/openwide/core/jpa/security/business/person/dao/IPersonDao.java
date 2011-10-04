package fr.openwide.core.jpa.security.business.person.dao;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;

public interface IPersonDao<P extends AbstractPerson<P>>
		extends IGenericEntityDao<Integer, P> {
	
	Long countActive();
	
}