package fr.openwide.core.jpa.security.business.person.dao;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson_;

public abstract class AbstractPersonDaoImpl<P extends AbstractPerson<P>>
		extends GenericEntityDaoImpl<Integer, P>
		implements IPersonDao<P> {
	
	public AbstractPersonDaoImpl() {
		super();
	}
	
	@Override
	public void save(P person) {
		super.save(person);
		
		flush();
	}
	
	@Override
	public Long countActive() {
		return countByField(AbstractPerson_.active, true);
	}
	
}