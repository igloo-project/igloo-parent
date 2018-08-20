package org.iglooproject.test.jpa.more.business.audit.dao;

import org.iglooproject.jpa.more.business.audit.dao.IAbstractAuditDao;
import org.iglooproject.test.jpa.more.business.audit.model.MockAudit;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditAction;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditActionEnum;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditFeature;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditFeatureEnum;

public interface IMockAuditDao extends IAbstractAuditDao<MockAudit> {

	MockAuditAction getAuditActionByEnum(MockAuditActionEnum auditActionEnum);

	MockAuditFeature getAuditFeatureByEnum(MockAuditFeatureEnum auditActionEnum);
}
