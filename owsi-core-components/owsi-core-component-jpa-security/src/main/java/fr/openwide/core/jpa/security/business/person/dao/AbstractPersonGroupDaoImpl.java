package fr.openwide.core.jpa.security.business.person.dao;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.AbstractPersonGroup;

public abstract class AbstractPersonGroupDaoImpl<G extends AbstractPersonGroup<G, P>, P extends AbstractPerson<P>>
		extends GenericEntityDaoImpl<Integer, G> implements IPersonGroupDao<G, P> {

	@Autowired
	public AbstractPersonGroupDaoImpl() {
		super();
	}
	

}