package org.iglooproject.jpa.search.bridge;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

/**
 * Warning: GenericEntity cannot be generic as Hibernate search introspection has to resolve completely
 * the generic type. Do not add <code>&lt;K, T&gt;</code> !
 */
public class GenericEntityIdBridge implements ValueBridge<GenericEntity, Long> { //NOSONAR

	@Override
	public Long toIndexedValue(GenericEntity value, ValueBridgeToIndexedValueContext context) {
		if (value == null || value.getId() == null) {
			return null;
		}
		return (Long) value.getId();
	}

	@Override
	public boolean isCompatibleWith(ValueBridge<?, ?> other) {
		return getClass().equals(other.getClass());
	}

}
