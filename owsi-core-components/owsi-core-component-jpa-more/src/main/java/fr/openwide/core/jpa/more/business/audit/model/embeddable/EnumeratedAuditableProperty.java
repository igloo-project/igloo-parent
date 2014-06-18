package fr.openwide.core.jpa.more.business.audit.model.embeddable;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.bindgen.Bindable;

@Embeddable
@Bindable
public class EnumeratedAuditableProperty<T extends Enum<?>> extends AbstractAuditableProperty<T> {
	
	private static final long serialVersionUID = 417422548747613975L;
	
	@Basic
	@Enumerated(EnumType.STRING)
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
