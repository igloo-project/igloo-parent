package fr.openwide.core.basicapp.core.business.audit.service;

import fr.openwide.core.basicapp.core.business.audit.model.AuditAction;
import fr.openwide.core.basicapp.core.business.audit.model.AuditActionType;
import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;

public interface IAuditActionService extends IGenericEntityService<Long, AuditAction> {

	AuditAction getByType(AuditActionType type);

}
