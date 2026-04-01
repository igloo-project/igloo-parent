package basicapp.front.pagelink;

import basicapp.front.pagelink.base.IPageLinkDescriptor;
import basicapp.front.pagelink.dto.IPageLinkDataDto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.apache.wicket.Page;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;

public class PageLinkDescriptorBuilder<P extends Page, D extends IPageLinkDataDto> {

  private final Class<P> pageClass;
  private final SerializableSupplier2<D> dataDtoFactory;
  private final List<MappingEntry<D>> mappings = new ArrayList<>();
  private SerializableFunction2<D, String> permissionProvider = dto -> null;

  private PageLinkDescriptorBuilder(Class<P> pageClass, SerializableSupplier2<D> dataDtoFactory) {
    this.pageClass = pageClass;
    this.dataDtoFactory = dataDtoFactory;
  }

  public static <P extends Page, D extends IPageLinkDataDto> PageLinkDescriptorBuilder<P, D> create(
      Class<P> pageClass, SerializableSupplier2<D> dtoFactory) {
    return new PageLinkDescriptorBuilder<>(pageClass, dtoFactory);
  }

  public PageLinkDescriptorBuilder<P, D> mapping(Consumer<MappingRegistry<D>> configurer) {
    configurer.accept(new MappingRegistry<>(this.mappings));
    return this;
  }

  public PageLinkDescriptorBuilder<P, D> permission(
      SerializableFunction2<D, String> permissionProvider) {
    this.permissionProvider = Objects.requireNonNull(permissionProvider);
    return this;
  }

  public PageLinkDescriptorBuilder<P, D> permission(String permission) {
    return permission(linkDto -> permission);
  }

  public IPageLinkDescriptor<P, D> build() {
    return new PageLinkDescriptorImpl<>(pageClass, dataDtoFactory, mappings, permissionProvider);
  }

  public class MappingRegistry<T> {
    private final List<PageLinkDescriptorBuilder.MappingEntry<T>> registry;

    public MappingRegistry(List<PageLinkDescriptorBuilder.MappingEntry<T>> registry) {
      this.registry = registry;
    }

    public MappingRegistry<T> map(SerializableConsumer<ArgumentBuilder<T>> configurer) {
      ArgumentBuilder<T> builder = new ArgumentBuilder<>();
      configurer.accept(builder);
      registry.add(builder.build());
      return this;
    }

    public MappingRegistry<T> renderInUrl(
        String paramName, SerializableFunction2<T, String> serializer) {
      registry.add(
          new PageLinkDescriptorBuilder.MappingEntry<>(paramName, serializer, null, false, false));
      return this;
    }
  }

  public class ArgumentBuilder<T> {
    private String name;
    private SerializableFunction2<T, String> serializer;
    private SerializableBiConsumer<T, String> deserializer;
    private boolean required = false;
    private boolean extractable = true;

    public ArgumentBuilder<T> name(String name) {
      this.name = name;
      return this;
    }

    private <V> ArgumentBuilder<T> mapType(
        SerializableFunction2<T, V> getter,
        SerializableBiConsumer<T, V> setter,
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

    public ArgumentBuilder<T> mapString(
        SerializableFunction2<T, String> getter, SerializableBiConsumer<T, String> setter) {
      return mapType(getter, setter, Functions2.identity(), Functions2.identity());
    }

    public ArgumentBuilder<T> mapLong(
        SerializableFunction2<T, Long> getter, SerializableBiConsumer<T, Long> setter) {
      return mapType(getter, setter, String::valueOf, Long::valueOf);
    }

    public ArgumentBuilder<T> mapInteger(
        SerializableFunction2<T, Integer> getter, SerializableBiConsumer<T, Integer> setter) {
      return mapType(getter, setter, String::valueOf, Integer::valueOf);
    }

    public <E extends Enum<E>> ArgumentBuilder<T> mapEnum(
        SerializableFunction2<T, E> getter,
        SerializableBiConsumer<T, E> setter,
        Class<E> enumClass) {
      return mapType(getter, setter, Enum::name, str -> Enum.valueOf(enumClass, str));
    }

    public ArgumentBuilder<T> required() {
      this.required = true;
      return this;
    }

    PageLinkDescriptorBuilder.MappingEntry<T> build() {
      if (name == null || serializer == null) {
        throw new IllegalStateException("Name and serializer required");
      }
      return new PageLinkDescriptorBuilder.MappingEntry<>(
          name, serializer, deserializer, extractable, required);
    }
  }

  public record MappingEntry<T>(
      String paramName,
      SerializableFunction2<T, String> serializer,
      SerializableBiConsumer<T, String> deserializer,
      boolean extractable,
      boolean required)
      implements Serializable {
    private static final long serialVersionUID = 1L;
  }
}
