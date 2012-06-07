package fr.openwide.core.jpa.security.business.person.dao;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.BeanPath;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson_;
import fr.openwide.core.jpa.security.business.person.model.QAbstractPerson;

public abstract class AbstractPersonDaoImpl<P extends AbstractPerson<P>>
		extends GenericEntityDaoImpl<Long, P>
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

	@SuppressWarnings("unchecked")
	@Override
	public P getByUserNameCaseInsensitive(String userName) {
		// obtention d'un mapper querydsl branché à l'implémentation concrète
		BeanPath<P> path = new BeanPath<P>(getObjectClass(), QAbstractPerson.abstractPerson.getMetadata());
		QAbstractPerson qPerson = new QAbstractPerson(path);
		
		JPQLQuery query = new JPAQuery(getEntityManager()).from(qPerson);
		query.where(qPerson.userName.lower().eq(userName.toLowerCase()));
		return (P) query.singleResult(qPerson);
	}

}