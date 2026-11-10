package org.iglooproject.wicket.more.link.dto.mapping;

import java.util.ArrayList;
import java.util.List;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.iglooproject.functional.SerializableFunction2;

/**
 * Mapping DSL between a link data DTO and the page parameters, shared by the page and resource link
 * descriptor builders.
 */
public class LinkDataDtoMappingRegistry<D> {

  private final List<LinkDataDtoMappingEntry<D>> entries = new ArrayList<>();

  public LinkDataDtoMappingRegistry<D> map(
      SerializableConsumer<LinkDataDtoArgumentBuilder<D>> configurer) {
    LinkDataDtoArgumentBuilder<D> builder = new LinkDataDtoArgumentBuilder<>();
    configurer.accept(builder);
    entries.add(builder.build());
    return this;
  }

  /** Parameter rendered in the URL, but never extracted. */
  public LinkDataDtoMappingRegistry<D> renderInUrl(
      String paramName, SerializableFunction2<D, String> serializer) {
    entries.add(new LinkDataDtoMappingEntry<>(paramName, serializer, null, false, false));
    return this;
  }

  public LinkDataDtoMappings<D> build() {
    return new LinkDataDtoMappings<>(entries);
  }
}
