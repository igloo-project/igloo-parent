package fr.openwide.core.jpa.search.bridge;

import org.hibernate.search.bridge.FieldBridge;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public abstract class AbstractGenericEntityIdFieldBridge implements FieldBridge {
	
	public Long objectToLong(Object object) {
		if (object == null) {
			return null;
		}
		if (!(object instanceof GenericEntity)) {
			throw new IllegalArgumentException("This FieldBridge only supports GenericEntity properties.");
		}
		@SuppressWarnings("unchecked")
		GenericEntity<Long, ?> entity = (GenericEntity<Long, ?>) object;
		return entity.getId();
	}
	
}