package fr.openwide.core.test.jpa.more.business.audit.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import fr.openwide.core.jpa.more.business.audit.model.util.AbstractAuditFeature;

@Entity
public class MockAuditFeature extends AbstractAuditFeature {
	private static final long serialVersionUID = 6640266250096631993L;

	@Enumerated(EnumType.STRING)
	private MockAuditFeatureEnum auditFeatureEnum;

	public MockAuditFeature(String label, MockAuditFeatureEnum auditFeatureEnum, Integer position) {
		super(label, position);
		
		this.auditFeatureEnum = auditFeatureEnum;
	}

	public MockAuditFeatureEnum getAuditFeatureEnum() {
		return auditFeatureEnum;
	}

	public void setAuditFeatureEnum(MockAuditFeatureEnum auditFeatureEnum) {
		this.auditFeatureEnum = auditFeatureEnum;
	}
}
