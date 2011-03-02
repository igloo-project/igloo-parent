package fr.openwide.core.hibernate.security.business.person.dao;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.hibernate.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.hibernate.security.business.person.model.CorePerson;

public class CorePersonDaoImpl extends GenericEntityDaoImpl<Integer, CorePerson> implements PersonDao {
	
	@Autowired
	public CorePersonDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
	public void save(CorePerson person) {
		super.save(person);
		
		// Flush is forced so that the person object has an id
		getSession().flush();
	}
	
	@Override
	public Long countActive() {
		return count(getObjectClass(), Restrictions.eq("active", true), null, null, null);
	}
	
}