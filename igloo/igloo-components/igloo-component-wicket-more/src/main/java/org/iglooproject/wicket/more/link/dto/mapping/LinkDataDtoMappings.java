package org.iglooproject.wicket.more.link.dto.mapping;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.dto.exception.PageLinkExtractionException;

/**
 * Built mappings between a link data DTO and the page parameters: page parameters generation and
 * extraction, shared by the page and resource link descriptors.
 */
public final class LinkDataDtoMappings<D> implements Serializable {

  private static final long serialVersionUID = 1L;

  private final List<LinkDataDtoMappingEntry<D>> entries;

  LinkDataDtoMappings(List<LinkDataDtoMappingEntry<D>> entries) {
    this.entries = List.copyOf(entries);
  }

  /**
   * @return {@code true} if every required parameter has a value for the given DTO.
   */
  public boolean hasRequiredValues(D dataDto) {
    for (var entry : entries) {
      if (entry.required() && StringUtils.isBlank(entry.serializer().apply(dataDto))) {
        return false;
      }
    }
    return true;
  }

  public PageParameters toPageParameters(D dataDto) {
    PageParameters pageParameters = new PageParameters();
    for (var entry : entries) {
      String value = entry.serializer().apply(dataDto);
      if (value != null) {
        pageParameters.add(entry.paramName(), value);
      }
    }
    return pageParameters;
  }

  /**
   * Sets the extractable parameters on the given DTO, then checks that every required parameter has
   * a value.
   */
  public void extract(D dataDto, PageParameters parameters) throws PageLinkExtractionException {
    for (var entry : entries) {
      if (entry.extractable() && entry.deserializer() != null) {
        String stringValue = parameters.get(entry.paramName()).toString(null);

        if (entry.required() && StringUtils.isBlank(stringValue)) {
          throw new PageLinkExtractionException(
              String.format("Missing required parameter '%s'.", entry.paramName()));
        }

        if (StringUtils.isNotBlank(stringValue)) {
          try {
            entry.deserializer().accept(dataDto, stringValue);
          } catch (Exception e) {
            throw new PageLinkExtractionException(
                String.format(
                    "Invalid value '%s' for parameter '%s'.", stringValue, entry.paramName()),
                e);
          }
        }
      }
    }

    for (var entry : entries) {
      if (entry.required() && StringUtils.isBlank(entry.serializer().apply(dataDto))) {
        throw new PageLinkExtractionException(
            String.format("Missing required parameter '%s'.", entry.paramName()));
      }
    }
  }
}
