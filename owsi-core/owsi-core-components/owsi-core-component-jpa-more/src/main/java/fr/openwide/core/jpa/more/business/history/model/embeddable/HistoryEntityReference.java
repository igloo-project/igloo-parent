package fr.openwide.core.jpa.more.business.history.model.embeddable;

import javax.persistence.Embeddable;

import org.bindgen.Bindable;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;

@Embeddable
@Bindable
public final class HistoryEntityReference extends GenericEntityReference<Long, GenericEntity<Long,?>> {
	
	private static final long serialVersionUID = -1385838799400769763L;

	public static HistoryEntityReference from(GenericEntityReference<Long, ?> genericEntityReference) {
		return genericEntityReference == null ? null : new HistoryEntityReference(genericEntityReference);
	}
	
	protected HistoryEntityReference() { // Pour Hibernate
	}

	public HistoryEntityReference(Class<? extends GenericEntity<Long, ?>> entityClass, Long entityId) {
		super(entityClass, entityId);
	}

	public HistoryEntityReference(GenericEntity<Long, ?> entity) {
		super(entity);
	}

	public HistoryEntityReference(GenericEntityReference<Long, ? extends GenericEntity<Long, ?>> genericEntityReference) {
		super(genericEntityReference.getEntityClass(), genericEntityReference.getEntityId());
	}

}
