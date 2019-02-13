package test.jpa.more.business.audit.service;

import org.iglooproject.jpa.more.business.audit.service.IAbstractAuditService;

import test.jpa.more.business.audit.model.MockAudit;
import test.jpa.more.business.audit.model.MockAuditAction;
import test.jpa.more.business.audit.model.MockAuditActionEnum;
import test.jpa.more.business.audit.model.MockAuditFeature;
import test.jpa.more.business.audit.model.MockAuditFeatureEnum;

public interface IMockAuditService extends IAbstractAuditService<MockAudit> {

	MockAuditAction getAuditActionByEnum(MockAuditActionEnum auditActionEnum);

	MockAuditFeature getAuditFeatureByEnum(MockAuditFeatureEnum auditFeatureEnum);
}
