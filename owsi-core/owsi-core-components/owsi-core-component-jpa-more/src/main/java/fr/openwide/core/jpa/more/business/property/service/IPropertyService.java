package fr.openwide.core.jpa.more.business.property.service;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.property.model.MutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.PropertyId;

public interface IPropertyService {

	<T> T get(PropertyId<T> propertyId);

	<T> void set(MutablePropertyId<T> propertyId, T value) throws ServiceException, SecurityServiceException;

	void clean();

}
