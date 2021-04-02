package org.iglooproject.jpa.security.business.user.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.business.user.model.GenericUserGroup;
import org.iglooproject.jpa.security.business.user.model.QGenericUserGroup;

import com.querydsl.core.types.dsl.PathBuilder;

public abstract class GenericUserGroupDaoImpl<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends GenericEntityDaoImpl<Long, G>
		implements IGenericUserGroupDao<G, U> {

	public GenericUserGroupDaoImpl() {
		super();
	}

	protected PathBuilder<G> getEntityPath() {
		return new PathBuilder<>(getObjectClass(), QGenericUserGroup.genericUserGroup.getMetadata());
	}

	@Override
	public G getByName(String name) {
		PathBuilder<G> qEntity = getEntityPath();
		QGenericUserGroup qGenericUserGroup = new QGenericUserGroup(qEntity);
		return super.getByField(qEntity, qGenericUserGroup.name, name);
	}

}