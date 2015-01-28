package fr.openwide.core.basicapp.core.business.audit.dao;

import fr.openwide.core.basicapp.core.business.audit.model.AuditAction;
import fr.openwide.core.basicapp.core.business.audit.model.AuditActionType;
import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;

public interface IAuditActionDao extends IGenericEntityDao<Long, AuditAction> {

	AuditAction getByType(AuditActionType fieldValue);

}
