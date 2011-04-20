package fr.openwide.core.hibernate.security.business.person.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.hibernate.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPerson;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPersonGroup;

public abstract class AbstractPersonGroupDaoImpl<G extends AbstractPersonGroup<G, P>, P extends AbstractPerson<P>>
		extends GenericEntityDaoImpl<Integer, G> implements PersonGroupDao<G, P> {

	@Autowired
	public AbstractPersonGroupDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	

}