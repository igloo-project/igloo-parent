package org.iglooproject.jpa.security.business.user.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.business.user.model.QGenericUser;

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
	public U getByUsernameCaseInsensitive(String username) {
		QGenericUser qUser = new QGenericUser(getEntityPath());
		return new JPAQuery<U>(getEntityManager())
			.from(qUser)
			.where(qUser.username.lower().eq(username.toLowerCase()))
			.fetchOne();
	}

}
