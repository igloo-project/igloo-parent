package test.jpa.more.business.audit.dao;

import org.iglooproject.jpa.more.business.audit.dao.IAbstractAuditDao;

import test.jpa.more.business.audit.model.MockAudit;
import test.jpa.more.business.audit.model.MockAuditAction;
import test.jpa.more.business.audit.model.MockAuditActionEnum;
import test.jpa.more.business.audit.model.MockAuditFeature;
import test.jpa.more.business.audit.model.MockAuditFeatureEnum;

public interface IMockAuditDao extends IAbstractAuditDao<MockAudit> {

	MockAuditAction getAuditActionByEnum(MockAuditActionEnum auditActionEnum);

	MockAuditFeature getAuditFeatureByEnum(MockAuditFeatureEnum auditActionEnum);
}
