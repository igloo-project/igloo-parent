package org.iglooproject.jpa.hibernate.usertype;

import java.io.Serializable;

import com.google.common.base.Function;

import org.iglooproject.commons.util.functional.SerializableFunction;

/**
 * A value that must be stored as a primitive type, but has a strong business significance and must thus be
 * represented differently in business code.
 * 
 * @see AbstractImmutableMaterializedPrimitiveValueUserType
 * @see AbstractImmutableMaterializedStringValueUserType
 * @see AbstractImmutableMaterializedIntegerValueUserType
 */
public abstract class AbstractMaterializedPrimitiveValue<P extends Comparable<P>, T extends AbstractMaterializedPrimitiveValue<P, T>> implements Serializable, Comparable<T> {
	
	private static final long serialVersionUID = 1388663091843463782L;
	
	public static <P extends Comparable<P>> Function<AbstractMaterializedPrimitiveValue<P, ?>, P> materializedToPrimitive() {
		return new MaterializedToPrimitiveFunction<P>();
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
		
		// "value" may not be null (see constructor)
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
	
	private static class MaterializedToPrimitiveFunction<P extends Comparable<P>>
			implements SerializableFunction<AbstractMaterializedPrimitiveValue<P, ?>, P> {
		
		private static final long serialVersionUID = -4430088664760813685L;
		
		@Override
		public P apply(AbstractMaterializedPrimitiveValue<P, ?> input) {
			return input == null ? null : input.getValue();
		}
	}

	@Override
	public int compareTo(T other) {
		return value.compareTo(other.getValue());
	}

}
