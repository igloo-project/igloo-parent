package org.iglooproject.wicket.more.link.dto.mapping;

import java.io.Serializable;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.iglooproject.functional.SerializableFunction2;

/**
 * Mapping between a link data DTO and one page parameter.
 *
 * @param paramName the page parameter name
 * @param serializer DTO to parameter value
 * @param deserializer parameter value to DTO, {@code null} if the parameter is not extracted
 * @param extractable whether the parameter is extracted from the page parameters
 * @param required whether the parameter is required (link generation and extraction)
 */
public record LinkDataDtoMappingEntry<D>(
    String paramName,
    SerializableFunction2<D, String> serializer,
    SerializableBiConsumer<D, String> deserializer,
    boolean extractable,
    boolean required)
    implements Serializable {
  private static final long serialVersionUID = 1L;
}
