package fr.openwide.core.jpa.security.business.person.dao;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.ComparableExpressionBase;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;
import fr.openwide.core.jpa.security.business.person.model.GenericUserGroup;

public interface IGenericUserGroupDao<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends IGenericEntityDao<Long, G> {

	<T extends G, V extends Comparable<?>> T getByField(EntityPath<T> entityPath,
			ComparableExpressionBase<V> field, V fieldValue);

}