package org.iglooproject.jpa.config.spring.convert.converter;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

/**
 * Converts a GenericEntity to a String. Only matches if the target GenericEntity key type can be
 * converted to String.class.
 */
public class GenericEntityToStringSpringConverter implements ConditionalGenericConverter {

  private final ConversionService conversionService;

  public GenericEntityToStringSpringConverter(ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Collections.singleton(new ConvertiblePair(GenericEntity.class, String.class));
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    /*
     * NOTE: for the official javac to infer types properly, the GenericEntity below must be left as a raw type.
     * Eclipse JDT works fine without this hack.
     */
    return canConvertKey(
        (Class<? extends GenericEntity>) sourceType.getType(), targetType.getType());
  }

  private <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
      boolean canConvertKey(Class<E> sourceType, Class<?> targetType) {
    Class<K> keyType = GenericEntityTypeResolver.resolveKeyTypeParameter(sourceType);

    if (keyType != null) {
      return this.conversionService.canConvert(keyType, targetType);
    } else {
      return false;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    if (source == null) {
      return null;
    }

    return convertKey((GenericEntity<?, ?>) source, (Class<? extends String>) targetType.getType());
  }

  private <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> String convertKey(
      E source, Class<? extends String> targetType) {
    K id = source.getId();
    if (id == null) {
      throw new RuntimeException("The entity id was null");
    } else {
      return conversionService.convert(id, targetType);
    }
  }
}
