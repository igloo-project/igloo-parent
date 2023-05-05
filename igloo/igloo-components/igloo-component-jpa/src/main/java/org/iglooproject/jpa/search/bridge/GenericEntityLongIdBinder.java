package org.iglooproject.jpa.search.bridge;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

public class GenericEntityLongIdBinder implements ValueBridge<GenericEntity<Long, GenericEntity<Long,?>>, String> {

	@Override
	public String toIndexedValue(GenericEntity<Long, GenericEntity<Long, ?>> value, ValueBridgeToIndexedValueContext context) {
		if (value == null || value.getId() == null) {
			return null;
		}
		return value.getId().toString();
	}

}
