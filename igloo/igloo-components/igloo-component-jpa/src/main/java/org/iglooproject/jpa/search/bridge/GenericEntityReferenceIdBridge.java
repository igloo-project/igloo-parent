package org.iglooproject.jpa.search.bridge;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;

/**
 * Warning: GenericEntityReference cannot be generic as Hibernate search introspection has to resolve completely
 * the generic type. Do not add <code>&lt;K, T&gt;</code> !
 */
@SuppressWarnings("rawtypes")
public class GenericEntityReferenceIdBridge implements ValueBridge<GenericEntityReference, String> { //NOSONAR

	@Override
	public String toIndexedValue(GenericEntityReference value, ValueBridgeToIndexedValueContext context) {
		if (value == null || value.getId() == null || value.getType() == null) {
			return null;
		}
		return value.getType().getCanonicalName()+ "|" + value.getId().toString();
	}

	@Override
	public boolean isCompatibleWith(ValueBridge<?, ?> other) {
		return getClass().equals(other.getClass());
	}

}
