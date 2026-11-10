package org.iglooproject.wicket.more.link.dto.mapping;

import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.SerializableFunction2;

/** Builds the mapping between a link data DTO property and a page parameter. */
public class LinkDataDtoArgumentBuilder<D> {
  private String name;
  private SerializableFunction2<D, String> serializer;
  private SerializableBiConsumer<D, String> deserializer;
  private boolean required = false;

  LinkDataDtoArgumentBuilder() {}

  public LinkDataDtoArgumentBuilder<D> name(String name) {
    this.name = name;
    return this;
  }

  public <V> LinkDataDtoArgumentBuilder<D> mapType(
      SerializableFunction2<D, V> getter,
      SerializableBiConsumer<D, V> setter,
      SerializableFunction2<V, String> toString,
      SerializableFunction2<String, V> fromString) {
    this.serializer =
        dto -> {
          V val = getter.apply(dto);
          return val != null ? toString.apply(val) : null;
        };
    this.deserializer =
        (dto, str) -> {
          if (str != null && !str.isBlank()) {
            setter.accept(dto, fromString.apply(str));
          }
        };
    return this;
  }

  public LinkDataDtoArgumentBuilder<D> mapString(
      SerializableFunction2<D, String> getter, SerializableBiConsumer<D, String> setter) {
    return mapType(getter, setter, Functions2.identity(), Functions2.identity());
  }

  public LinkDataDtoArgumentBuilder<D> mapLong(
      SerializableFunction2<D, Long> getter, SerializableBiConsumer<D, Long> setter) {
    return mapType(getter, setter, String::valueOf, Long::valueOf);
  }

  public LinkDataDtoArgumentBuilder<D> mapInteger(
      SerializableFunction2<D, Integer> getter, SerializableBiConsumer<D, Integer> setter) {
    return mapType(getter, setter, String::valueOf, Integer::valueOf);
  }

  public <E extends Enum<E>> LinkDataDtoArgumentBuilder<D> mapEnum(
      SerializableFunction2<D, E> getter, SerializableBiConsumer<D, E> setter, Class<E> enumClass) {
    return mapType(getter, setter, Enum::name, str -> Enum.valueOf(enumClass, str));
  }

  public LinkDataDtoArgumentBuilder<D> required() {
    this.required = true;
    return this;
  }

  LinkDataDtoMappingEntry<D> build() {
    if (name == null || serializer == null) {
      throw new IllegalStateException("Name and serializer required");
    }
    return new LinkDataDtoMappingEntry<>(name, serializer, deserializer, true, required);
  }
}
