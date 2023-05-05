package org.iglooproject.jpa.more.business.history.search;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEntityReference;

public class HistoryEntityReferenceBridge implements ValueBridge<HistoryEntityReference, String>{

	@Override
	public String toIndexedValue(HistoryEntityReference value, ValueBridgeToIndexedValueContext context) {
		return value == null ? null : value.getId().toString();
	}

}
