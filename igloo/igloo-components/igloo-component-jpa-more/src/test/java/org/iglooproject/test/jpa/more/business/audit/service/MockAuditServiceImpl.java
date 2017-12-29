package org.iglooproject.test.jpa.more.business.audit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.more.business.audit.service.AbstractAuditServiceImpl;
import org.iglooproject.test.jpa.more.business.audit.dao.IMockAuditDao;
import org.iglooproject.test.jpa.more.business.audit.model.MockAudit;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditAction;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditActionEnum;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditFeature;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditFeatureEnum;

@Service("mockAuditService")
public class MockAuditServiceImpl extends AbstractAuditServiceImpl<MockAudit> implements IMockAuditService {

	private IMockAuditDao auditDao;

	@Autowired
	public MockAuditServiceImpl(IMockAuditDao auditDao) {
		super(auditDao);
		
		this.auditDao = auditDao;
	}

	@Override
	public MockAuditAction getAuditActionByEnum(MockAuditActionEnum auditActionEnum) {
		return auditDao.getAuditActionByEnum(auditActionEnum);
	}
	
	@Override
	public MockAuditFeature getAuditFeatureByEnum(MockAuditFeatureEnum auditFeatureEnum) {
		return auditDao.getAuditFeatureByEnum(auditFeatureEnum);
	}
}
