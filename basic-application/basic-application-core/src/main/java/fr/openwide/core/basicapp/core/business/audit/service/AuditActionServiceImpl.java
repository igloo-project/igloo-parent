package fr.openwide.core.basicapp.core.business.audit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.audit.dao.IAuditActionDao;
import fr.openwide.core.basicapp.core.business.audit.model.AuditAction;
import fr.openwide.core.basicapp.core.business.audit.model.AuditActionType;
import fr.openwide.core.basicapp.core.business.audit.model.AuditAction_;
import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;

@Service("auditActionService")
public class AuditActionServiceImpl extends GenericEntityServiceImpl<Long, AuditAction>
		implements IAuditActionService {

	private IAuditActionDao auditActionDao;

	@Autowired
	public AuditActionServiceImpl(IAuditActionDao auditActionDao) {
		super(auditActionDao);
		this.auditActionDao = auditActionDao;
	}

	@Override
	public AuditAction getByType(AuditActionType type) {
		return auditActionDao.getByField(AuditAction_.type, type);
	}
}
