package org.iglooproject.jpa.more.business.history.dto;

import java.util.Optional;
import org.iglooproject.jpa.more.business.history.model.embeddable.IHistoryValue;
import org.iglooproject.jpa.more.rendering.IRenderable;

/** Conversion of history values and referenced entities into display DTOs. */
public interface IHistoryRenderableService {

  /**
   * Converts a history value into a display DTO: label stored when the history was logged and, if
   * the referenced entity still exists and has an {@link IHistoryRenderableMapper}, its renderable
   * DTO.
   */
  IHistoryValueDto toHistoryValueDto(IHistoryValue value);

  /** Converts an entity into a renderable DTO, using its {@link IHistoryRenderableMapper}. */
  Optional<IRenderable> toRenderable(Object entity);
}
