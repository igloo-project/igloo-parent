package org.iglooproject.jpa.security.business.person.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.jpa.security.business.person.model.GenericUser;
import org.iglooproject.jpa.security.business.person.model.QGenericUser;

import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.jpa.impl.JPAQuery;

public abstract class GenericUserDaoImpl<U extends GenericUser<?, ?>> extends GenericEntityDaoImpl<Long, U> implements IGenericUserDao<U> {

	public GenericUserDaoImpl() {
		super();
	}

	protected BeanPath<U> getEntityPath() {
		return new BeanPath<>(getObjectClass(), QGenericUser.genericUser.getMetadata());
	}

	@Override
	public void save(U user) {
		super.save(user);
		
		flush();
	}

	@Override
	public U getByUsernameCaseInsensitive(String username) {
		QGenericUser qUser = new QGenericUser(getEntityPath());
		return new JPAQuery<U>(getEntityManager())
			.from(qUser)
			.where(qUser.username.lower().eq(username.toLowerCase()))
			.fetchOne();
	}

}