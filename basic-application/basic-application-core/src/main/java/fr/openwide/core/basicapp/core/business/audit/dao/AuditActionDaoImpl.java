package fr.openwide.core.basicapp.core.business.audit.dao;

import org.springframework.stereotype.Repository;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.ComparableExpressionBase;

import fr.openwide.core.basicapp.core.business.audit.model.AuditAction;
import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;

@Repository("auditActionDao")
public class AuditActionDaoImpl extends GenericEntityDaoImpl<Long, AuditAction> implements IAuditActionDao {

	@Override
	public <T extends AuditAction, V extends Comparable<?>> T getByField(EntityPath<T> entityPath,
			ComparableExpressionBase<V> field, V fieldValue) {
		return super.getByField(entityPath, field, fieldValue);
	}

}
