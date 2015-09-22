package fr.openwide.core.basicapp.core.business.audit.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.jpa.more.business.audit.dao.AbstractAuditDaoImpl;

@Repository("auditDao")
public class AuditDaoImpl extends AbstractAuditDaoImpl<Audit> implements IAuditDao {

}
