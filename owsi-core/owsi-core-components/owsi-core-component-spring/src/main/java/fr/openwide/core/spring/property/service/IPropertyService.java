package fr.openwide.core.spring.property.service;

import java.util.List;
import java.util.Locale;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.property.model.MutablePropertyId;
import fr.openwide.core.spring.property.model.PropertyId;

public interface IPropertyService {

	<T> T get(PropertyId<T> propertyId);

	<T> void set(MutablePropertyId<T> propertyId, T value) throws ServiceException, SecurityServiceException;

	<T> String getAsString(PropertyId<T> propertyId);

	List<PropertyId<?>> listRegisteredPropertyIds();

	void clean();

	boolean isConfigurationTypeDevelopment();
	Locale toAvailableLocale(Locale locale);

}
