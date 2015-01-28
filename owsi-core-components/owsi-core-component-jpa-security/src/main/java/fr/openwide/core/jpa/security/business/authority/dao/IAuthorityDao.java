package fr.openwide.core.jpa.security.business.authority.dao;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.ComparableExpressionBase;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.security.business.authority.model.Authority;

public interface IAuthorityDao extends IGenericEntityDao<Long, Authority> {

	<T extends Authority, V extends Comparable<?>> T getByField(EntityPath<T> entityPath,
			ComparableExpressionBase<V> field, V fieldValue);

}