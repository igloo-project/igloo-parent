package fr.openwide.core.jpa.security.business.person.dao;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.ComparableExpressionBase;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;
import fr.openwide.core.jpa.security.business.person.model.GenericUserGroup;

public abstract class GenericUserGroupDaoImpl<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends GenericEntityDaoImpl<Long, G> implements IGenericUserGroupDao<G, U> {

	public GenericUserGroupDaoImpl() {
		super();
	}

	@Override
	public <T extends G, V extends Comparable<?>> T getByField(EntityPath<T> entityPath,
			ComparableExpressionBase<V> field, V fieldValue) {
		return super.getByField(entityPath, field, fieldValue);
	}

}