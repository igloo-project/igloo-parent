package fr.openwide.core.basicapp.core.business.audit.dao;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.ComparableExpressionBase;

import fr.openwide.core.basicapp.core.business.audit.model.AuditAction;
import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;

public interface IAuditActionDao extends IGenericEntityDao<Long, AuditAction> {

	<T extends AuditAction, V extends Comparable<?>> T getByField(EntityPath<T> entityPath,
			ComparableExpressionBase<V> field, V fieldValue);

}
