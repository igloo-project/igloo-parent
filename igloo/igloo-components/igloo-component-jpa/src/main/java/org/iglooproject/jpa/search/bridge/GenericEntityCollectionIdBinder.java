package org.iglooproject.jpa.search.bridge;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

public class GenericEntityCollectionIdBinder<K extends Comparable<K> & Serializable, C extends Collection<GenericEntity<K, GenericEntity<K,?>>>> implements ValueBridge<C, K> {

	@Override
	public K toIndexedValue(C value, ValueBridgeToIndexedValueContext context) {
		for (GenericEntity<K, GenericEntity<K, ?>> entity : value) {
			
		}
		return null;
	}

}

