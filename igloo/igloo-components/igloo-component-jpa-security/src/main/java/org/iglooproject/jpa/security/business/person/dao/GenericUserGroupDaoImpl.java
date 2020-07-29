package org.iglooproject.jpa.security.business.person.dao;

import com.querydsl.core.types.dsl.PathBuilder;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.jpa.security.business.person.model.GenericUser;
import org.iglooproject.jpa.security.business.person.model.GenericUserGroup;
import org.iglooproject.jpa.security.business.person.model.QGenericUserGroup;

public abstract class GenericUserGroupDaoImpl<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends GenericEntityDaoImpl<Long, G> implements IGenericUserGroupDao<G, U> {

	public GenericUserGroupDaoImpl() {
		super();
	}

	@Override
	public G getByName(String name) {
		PathBuilder<G> qEntity = new PathBuilder<>(getObjectClass(), "rootAlias");
		QGenericUserGroup qEntityAsGenericUserGroup = new QGenericUserGroup(qEntity);
		return super.getByField(qEntity, qEntityAsGenericUserGroup.name, name);
	}

}