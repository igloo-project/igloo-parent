package fr.openwide.core.test.jpa.more.business.audit.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import fr.openwide.core.jpa.more.business.audit.model.util.AbstractAuditAction;

@Entity
public class MockAuditAction extends AbstractAuditAction {
	private static final long serialVersionUID = -1905233653967986654L;

	@Enumerated(EnumType.STRING)
	private MockAuditActionEnum auditActionEnum;
	
	protected MockAuditAction() {
	}

	public MockAuditAction(String label, MockAuditActionEnum auditActionEnum, Integer position) {
		super(label, position);
		
		this.auditActionEnum = auditActionEnum;
	}

	public MockAuditActionEnum getAuditActionEnum() {
		return auditActionEnum;
	}

	public void setAuditActionEnum(MockAuditActionEnum auditActionEnum) {
		this.auditActionEnum = auditActionEnum;
	}
}
