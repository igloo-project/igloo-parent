package test.jpa.more.business.audit.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.audit.model.AbstractAudit;
import org.iglooproject.jpa.more.business.audit.model.util.AbstractAuditFeature;

@Entity
public class MockAudit extends AbstractAudit<MockAuditAction> {
	private static final long serialVersionUID = -8944353148532999908L;

	@ManyToOne
	private MockAuditAction action;

	@ManyToOne
	private MockAuditFeature feature;

	public MockAudit(String service, String method, GenericEntity<Long, ?> context, GenericEntity<Long, ?> subject,
			AbstractAuditFeature feature, MockAuditAction action, String message, GenericEntity<Long, ?> object,
			GenericEntity<Long, ?> secondaryObject) {
		this(new Date(), service, method, context, subject, feature, action, message, object, secondaryObject);
	}

	public MockAudit(Date date, String service, String method, GenericEntity<Long, ?> context, GenericEntity<Long, ?> subject,
			AbstractAuditFeature feature, MockAuditAction action, String message, GenericEntity<Long, ?> object,
			GenericEntity<Long, ?> secondaryObject) {
		super(date, service, method, context, subject, feature, action, message, object, secondaryObject);
	}

	@Override
	public MockAuditAction getAction() {
		return action;
	}

	@Override
	public void setAction(MockAuditAction action) {
		this.action = action;
	}

	@Override
	public MockAuditFeature getFeature() {
		return feature;
	}

	public void setFeature(MockAuditFeature feature) {
		this.feature = feature;
	}

	@Override
	public void setFeature(AbstractAuditFeature feature) {
		if (feature instanceof MockAuditFeature) {
			setFeature((MockAuditFeature) feature);
		}
	}
}
