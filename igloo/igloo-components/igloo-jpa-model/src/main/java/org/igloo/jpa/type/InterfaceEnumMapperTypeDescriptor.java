package org.igloo.jpa.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.iglooproject.jpa.business.generic.model.IMappableInterface;

public class InterfaceEnumMapperTypeDescriptor<T extends IMappableInterface>
    extends AbstractTypeDescriptor<T> {

  private static final long serialVersionUID = 3446155631478715197L;

  private final Map<String, T> mapping;

  @SuppressWarnings({"unchecked", "rawtypes"})
  protected InterfaceEnumMapperTypeDescriptor(
      Class<T> _interface, Set<Class<? extends T>> enumTypes) {
    super(_interface);
    Map<String, T> builder = new HashMap<>();
    for (Class<? extends T> enumType : enumTypes) {
      // if there is a class cast problem, it'll be triggered at start time
      // builder refuses duplicates key, so we are protected against key reuse
      // (Class<Enum>) (Class<?>) and (Iterable<Enum>) are needed outside eclipse
      for (Enum type : (Iterable<Enum>) EnumSet.allOf((Class<Enum>) (Class<?>) enumType)) {
        builder.put(type.name(), (T) type);
      }
    }
    mapping = Map.copyOf(builder);
  }

  @Override
  public T fromString(String string) {
    if (string == null) {
      return null;
    }
    return Optional.ofNullable(mapping.get(string)).orElseThrow();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <X> X unwrap(T value, Class<X> type, WrapperOptions options) {
    if (value == null) {
      return null;
    }
    if (getJavaType().isAssignableFrom(type)) {
      return (X) value;
    }
    if (String.class.isAssignableFrom(type)) {
      return (X) value.getName();
    }
    throw unknownUnwrap(type);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <X> T wrap(X value, WrapperOptions options) {
    if (value == null) {
      return null;
    }
    if (String.class.isInstance(value)) {
      return fromString((String) value);
    }
    if (getJavaType().isInstance(value)) {
      return (T) value;
    }
    throw unknownWrap(value.getClass());
  }
}
