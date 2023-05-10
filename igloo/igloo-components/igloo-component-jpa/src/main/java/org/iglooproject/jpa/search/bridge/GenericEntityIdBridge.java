package org.iglooproject.jpa.search.bridge;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

/**
 * Warning: GenericEntity cannot be generic as Hibernate search introspection has to resolve completely
 * the generic type. Do not add <code>&lt;K, T&gt;</code> !
 */
@SuppressWarnings("rawtypes")
public class GenericEntityIdBridge implements ValueBridge<GenericEntity, String> { //NOSONAR

	@Override
	public String toIndexedValue(GenericEntity value, ValueBridgeToIndexedValueContext context) {
		if (value == null || value.getId() == null) {
			return null;
		}
		//TODO igloo-boot: dans Igloo 5 on mettais id == null ? "" avec le commentaire suivant :
		//The ID may be null if the FieldBridge is being used while building a query.
		// Est-ce qu'il faut le reprendre ? 
		return value.getId().toString();
	}

}
