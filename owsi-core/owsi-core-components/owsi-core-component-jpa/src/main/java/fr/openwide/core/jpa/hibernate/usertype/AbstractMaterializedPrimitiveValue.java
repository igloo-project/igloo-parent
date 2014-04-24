package fr.openwide.core.jpa.hibernate.usertype;

import java.io.Serializable;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.SerializableFunction;

public abstract class AbstractMaterializedPrimitiveValue<P, T extends AbstractMaterializedPrimitiveValue<P, T>> implements Serializable {
	
	private static final long serialVersionUID = 1388663091843463782L;
	
	public static <P, T extends AbstractMaterializedPrimitiveValue<P, T>> Function<T, P> materializedToPrimitive() {
		return new MaterializedToPrimitiveFunction<P, T>();
	}
	
	private final P value;
	
	protected AbstractMaterializedPrimitiveValue(P value) {
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
	
	public final P getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	private static class MaterializedToPrimitiveFunction<P, T extends AbstractMaterializedPrimitiveValue<P, T>>
			implements SerializableFunction<T, P> {
		
		private static final long serialVersionUID = -4430088664760813685L;
		
		@Override
		public P apply(T input) {
			return input.getValue();
		}
	}

}
