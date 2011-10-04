package fr.openwide.core.test.jpa.more.business.audit.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.business.audit.model.AbstractAudit;
import fr.openwide.core.jpa.more.business.audit.model.util.AbstractAuditAction;
import fr.openwide.core.jpa.more.business.audit.model.util.AbstractAuditFeature;

@Entity
public class MockAudit extends AbstractAudit {
	private static final long serialVersionUID = -8944353148532999908L;

	@ManyToOne
	private MockAuditAction action;

	@ManyToOne
	private MockAuditFeature feature;

	public MockAudit(String service, String method, GenericEntity<Integer, ?> context, GenericEntity<Integer, ?> subject,
			AbstractAuditFeature feature, AbstractAuditAction action, String message, GenericEntity<Integer, ?> object,
			GenericEntity<Integer, ?> secondaryObject) {
		this(new Date(), service, method, context, subject, feature, action, message, object, secondaryObject);
	}

	public MockAudit(Date date, String service, String method, GenericEntity<Integer, ?> context, GenericEntity<Integer, ?> subject,
			AbstractAuditFeature feature, AbstractAuditAction action, String message, GenericEntity<Integer, ?> object,
			GenericEntity<Integer, ?> secondaryObject) {
		super(date, service, method, context, subject, feature, action, message, object, secondaryObject);
	}

	@Override
	public MockAuditAction getAction() {
		return action;
	}

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
	public void setAction(AbstractAuditAction action) {
		if (action instanceof MockAuditAction) {
			setAction((MockAuditAction) action);
		}
	}

	@Override
	public void setFeature(AbstractAuditFeature feature) {
		if (feature instanceof MockAuditFeature) {
			setFeature((MockAuditFeature) feature);
		}
	}
}
