package org.iglooproject.spring.property.service;

import java.util.Locale;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.exception.PropertyServiceIncompleteRegistrationException;
import org.iglooproject.spring.property.model.IMutablePropertyValueMap;
import org.iglooproject.spring.property.model.MutablePropertyId;
import org.iglooproject.spring.property.model.PropertyId;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

public interface IPropertyService {

  /**
   * @param propertyId The ID of the property whose value is to be retrieved.
   * @return The current value of this property
   * @throws PropertyServiceIncompleteRegistrationException If the property was not properly {@link
   *     IPropertyRegistry registered}.
   */
  @NullUnmarked
  <T> T get(PropertyId<T> propertyId);

  /**
   * Alter the value of a property.
   *
   * @param propertyId The ID of the property whose value is to be set.
   * @param value The value the given property should be set to.
   * @throws PropertyServiceIncompleteRegistrationException If the property was not properly {@link
   *     IPropertyRegistry registered}.
   * @throws ServiceException If an error occurred in the underlying data store.
   * @throws SecurityServiceException If an error occurred in the underlying data store.
   */
  <T> void set(MutablePropertyId<T> propertyId, @Nullable T value)
      throws ServiceException, SecurityServiceException;

  /**
   * Alter the value of several properties in a single transaction.
   *
   * @param valueMap The ID => value map.
   * @throws PropertyServiceIncompleteRegistrationException If one property was not properly {@link
   *     IPropertyRegistry registered}.
   * @throws ServiceException If an error occurred in the underlying data store.
   * @throws SecurityServiceException If an error occurred in the underlying data store.
   */
  void setAll(IMutablePropertyValueMap valueMap) throws ServiceException, SecurityServiceException;

  /**
   * Alter the value of a property.
   *
   * @param propertyId The ID of the property whose value is to be set.
   * @param valueAsString The value as String the given property should be set to.
   * @throws PropertyServiceIncompleteRegistrationException If the property was not properly {@link
   *     IPropertyRegistry registered}.
   * @throws ServiceException If an error occurred in the underlying data store.
   * @throws SecurityServiceException If an error occurred in the underlying data store.
   */
  <T> void setAsString(MutablePropertyId<T> propertyId, @Nullable String valueAsString)
      throws ServiceException, SecurityServiceException;

  /**
   * @param propertyId The ID of the property whose value is to be retrieved.
   * @return The current value of this property, as a raw String exactly as it is stored in the
   *     underlying data store.
   * @throws PropertyServiceIncompleteRegistrationException If the property was not properly {@link
   *     IPropertyRegistry registered}.
   */
  <T> @Nullable String getAsString(PropertyId<T> propertyId);

  boolean isConfigurationTypeDevelopment();

  Locale toAvailableLocale(@Nullable Locale locale);
}
