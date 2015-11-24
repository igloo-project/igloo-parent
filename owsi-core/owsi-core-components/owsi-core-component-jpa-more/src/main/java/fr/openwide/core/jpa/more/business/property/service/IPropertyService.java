package fr.openwide.core.jpa.more.business.property.service;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.more.business.property.model.MutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.PropertyId;

public interface IPropertyService extends ITransactionalAspectAwareService {

	<T> T get(PropertyId<T> propertyId);

	<T> void set(MutablePropertyId<T> propertyId, T value);

}
