package fr.openwide.core.jpa.more.business.audit.model.embeddable;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

@Embeddable
public class BasicAuditableProperty<T extends Serializable> extends AbstractAuditableProperty<T> {
	
	private static final long serialVersionUID = -6116430801238809098L;
	
	@Basic
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
