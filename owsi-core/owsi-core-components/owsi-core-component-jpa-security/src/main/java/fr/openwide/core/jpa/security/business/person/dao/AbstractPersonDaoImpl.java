package fr.openwide.core.hibernate.security.business.person.dao;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.hibernate.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPerson;
import fr.openwide.core.hibernate.security.business.person.model.PersonBinding;

public abstract class AbstractPersonDaoImpl<P extends AbstractPerson<P>>
		extends GenericEntityDaoImpl<Integer, P>
		implements PersonDao<P> {
	
	private static final PersonBinding PERSON_BINDING = new PersonBinding();
	
	@Autowired
	public AbstractPersonDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
	public void save(P person) {
		super.save(person);
		
		// Flush is forced so that the person object has an id
		getSession().flush();
	}
	
	@Override
	public Long countActive() {
		return count(getObjectClass(), Restrictions.eq(PERSON_BINDING.active().getPath(), true),
				null, null, null);
	}
	
}