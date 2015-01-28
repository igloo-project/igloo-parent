package fr.openwide.core.jpa.security.business.authority.dao;

import org.springframework.stereotype.Repository;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.ComparableExpressionBase;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.security.business.authority.model.Authority;

@Repository("authorityDao")
public class AuthorityDaoImpl extends GenericEntityDaoImpl<Long, Authority> implements IAuthorityDao {
	
	public AuthorityDaoImpl() {
		super();
	}

	@Override
	public <T extends Authority, V extends Comparable<?>> T getByField(EntityPath<T> entityPath,
			ComparableExpressionBase<V> field, V fieldValue) {
		return super.getByField(entityPath, field, fieldValue);
	}
}