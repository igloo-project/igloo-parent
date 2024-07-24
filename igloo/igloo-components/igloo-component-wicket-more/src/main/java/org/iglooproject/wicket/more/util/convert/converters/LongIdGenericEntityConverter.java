package org.iglooproject.wicket.more.util.convert.converters;

import java.util.Locale;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.spring.util.StringUtils;

public class LongIdGenericEntityConverter<E extends GenericEntity<Long, ?>>
    extends AbstractConverter<E> {

  private static final long serialVersionUID = -6119059653808653326L;

  private final Class<E> targetType;

  @SpringBean private IEntityService entityService;

  public LongIdGenericEntityConverter(Class<E> targetType) {
    this.targetType = targetType;
  }

  @Override
  public String convertToString(E value, Locale locale) {
    if (value == null) {
      return null;
    }
    return convertToString(value.getId());
  }

  @Override
  public E convertToObject(String value, Locale locale) {
    if (!StringUtils.hasText(value)) {
      return null;
    }
    if (entityService
        == null) { // Late initialization, so that we can use this class for static variables
      Injector.get().inject(this);
    }
    return entityService.getEntity(
        targetType, convertToId(value, locale)); // NOSONAR findbugs:NP_NULL_ON_SOME_PATH
    // false positive NP_NULL_ON_SOME_PATH as findbugs can't see that Injector.get().inject(this)
    // solves
    // entityService null value (NOTA : special path used when converter is a constant so that
    // service binding
    // cannot be done at construction time.
  }

  private String convertToString(Long value) {
    if (value == null) {
      return null;
    }
    return String.valueOf(value);
  }

  private Long convertToId(String value, Locale locale) {
    try {
      return Long.valueOf(value);
    } catch (NumberFormatException e) {
      throw newConversionException(
          e.getMessage(), value, locale); // NOSONAR : the Wicket API forces us to lose stacktrace
    }
  }

  @Override
  protected Class<E> getTargetType() {
    return targetType;
  }
}
