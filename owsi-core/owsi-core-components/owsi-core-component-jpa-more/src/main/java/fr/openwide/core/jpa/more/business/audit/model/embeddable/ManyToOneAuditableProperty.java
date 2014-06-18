package fr.openwide.core.jpa.more.business.audit.model.embeddable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

@Embeddable
public class ManyToOneAuditableProperty<T extends GenericEntity<?, ?>> extends AbstractAuditableProperty<T> {
	
	private static final long serialVersionUID = -3311981034473536730L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private T value;

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value; 
	}

}
