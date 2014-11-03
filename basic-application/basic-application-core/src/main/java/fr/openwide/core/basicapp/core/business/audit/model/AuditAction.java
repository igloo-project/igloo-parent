package fr.openwide.core.basicapp.core.business.audit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.bindgen.Bindable;

import fr.openwide.core.jpa.more.business.audit.model.util.AbstractAuditAction;

@Entity
@Bindable
public class AuditAction extends AbstractAuditAction {

	private static final long serialVersionUID = -1405168262903524119L;

	private static final int POSITION_DEFAULT = 0;

	@Column(unique = true, nullable = false)
	@Enumerated(EnumType.STRING)
	private AuditActionType type;

	protected AuditAction() {
		super();
	}

	public AuditAction(String label, AuditActionType type) {
		super(label, POSITION_DEFAULT);

		this.type = type;
	}

	public AuditActionType getType() {
		return type;
	}

	public void setType(AuditActionType type) {
		this.type = type;
	}

}
