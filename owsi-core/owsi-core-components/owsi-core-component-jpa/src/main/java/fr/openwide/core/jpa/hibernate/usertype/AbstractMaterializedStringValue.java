package fr.openwide.core.jpa.hibernate.usertype;

import java.io.Serializable;

public abstract class AbstractMaterializedStringValue<T extends AbstractMaterializedStringValue<T>> implements Serializable {
	
	private static final long serialVersionUID = -798806400623732408L;
	
	private final String value;
	
	protected AbstractMaterializedStringValue(String value) {
		if (value == null) {
			throw new IllegalArgumentException("value must not be null");
		}
		this.value = value;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (!(getClass().equals(object.getClass()))) {
			return false;
		}

		@SuppressWarnings("unchecked")
		T other = (T) object;
		return value.equals(other.getValue());
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public String toString() {
		return value;
	}

}
