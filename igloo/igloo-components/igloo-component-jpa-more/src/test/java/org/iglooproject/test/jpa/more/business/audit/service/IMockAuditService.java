package org.iglooproject.test.jpa.more.business.audit.service;

import org.iglooproject.jpa.more.business.audit.service.IAbstractAuditService;
import org.iglooproject.test.jpa.more.business.audit.model.MockAudit;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditAction;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditActionEnum;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditFeature;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditFeatureEnum;

public interface IMockAuditService extends IAbstractAuditService<MockAudit> {

	MockAuditAction getAuditActionByEnum(MockAuditActionEnum auditActionEnum);

	MockAuditFeature getAuditFeatureByEnum(MockAuditFeatureEnum auditFeatureEnum);
}
