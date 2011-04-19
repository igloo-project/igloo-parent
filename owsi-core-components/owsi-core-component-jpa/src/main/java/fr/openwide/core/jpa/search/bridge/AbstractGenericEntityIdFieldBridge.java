package fr.openwide.core.jpa.search.bridge;

import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.StringBridge;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public abstract class AbstractGenericEntityIdFieldBridge implements FieldBridge, StringBridge {
	
	@Override
	public String objectToString(Object object) {
		if (object == null) {
			return null;
		}
		if (!(object instanceof GenericEntity)) {
			throw new IllegalArgumentException("This FieldBridge only supports GenericEntity properties.");
		}
		GenericEntity<?, ?> entity = (GenericEntity<?, ?>) object;
		return entity.getId().toString();
	}
	
}