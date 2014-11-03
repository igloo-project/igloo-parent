package fr.openwide.core.basicapp.core.business.audit.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.basicapp.core.business.audit.model.AuditAction;
import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;

@Repository("auditActionDao")
public class AuditActionDaoImpl extends GenericEntityDaoImpl<Long, AuditAction> implements IAuditActionDao {

}
