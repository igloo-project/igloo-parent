package org.iglooproject.jpa.security.business.person.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.jpa.security.business.person.model.GenericUser;
import org.iglooproject.jpa.security.business.person.model.QGenericUser;

import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

public abstract class GenericUserDaoImpl<U extends GenericUser<?, ?>> extends GenericEntityDaoImpl<Long, U> implements IGenericUserDao<U> {
	
	public GenericUserDaoImpl() {
		super();
	}
	
	@Override
	public void save(U person) {
		super.save(person);
		
		flush();
	}
	
	protected BeanPath<U> getEntityPath() {
		// obtention d'un mapper querydsl branché à l'implémentation concrète
		return new BeanPath<>(getObjectClass(), QGenericUser.genericUser.getMetadata());
	}

	@Override
	public U getByUsernameCaseInsensitive(String username) {
		if (username == null) {
			return null;
		}
		
		QGenericUser qUser = new QGenericUser(getEntityPath());
		
		JPQLQuery<U> query = new JPAQuery<U>(getEntityManager()).from(qUser);
		query.where(qUser.username.lower().eq(username.toLowerCase()));
		return query.fetchOne();
	}

}