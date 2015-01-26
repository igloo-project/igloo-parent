package fr.openwide.core.basicapp.core.business.audit.model;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.business.audit.model.AbstractAudit;
import fr.openwide.core.jpa.more.business.audit.model.util.AbstractAuditAction;
import fr.openwide.core.jpa.more.business.audit.model.util.AbstractAuditFeature;
import fr.openwide.core.jpa.search.bridge.GenericEntityIdFieldBridge;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;

@Entity
@Bindable
@Cacheable
@Indexed
public class Audit extends AbstractAudit {

	private static final long serialVersionUID = -1146280203615151992L;

	@ManyToOne
	@Field(bridge = @FieldBridge(impl = GenericEntityIdFieldBridge.class), analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private AuditAction action;

	protected Audit() {
	}
	
	public Audit(Date date, String service, String method, GenericEntity<Long, ?> context, User subject,
			GenericEntity<Long, ?> object, GenericEntity<Long, ?> secondaryObject, AuditAction action, String message) {
		super(date, service, method, context, subject, null, action, message, object, secondaryObject);
	}

	@Override
	public AuditAction getAction() {
		return action;
	}

	private void setAction(AuditAction action) {
		this.action = action;
	}
	
	@Override
	public void setAction(AbstractAuditAction action) {
		setAction((AuditAction) action);
	}

	@Override
	@Deprecated
	public AbstractAuditFeature getFeature() {
		return null;
	}

	@Override
	@Deprecated
	public void setFeature(AbstractAuditFeature feature) {
	}

}
