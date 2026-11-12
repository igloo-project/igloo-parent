package org.iglooproject.jpa.more.business.history.dto;

import java.io.Serializable;
import org.iglooproject.jpa.more.rendering.IRenderable;

/** Display DTO of a history value. */
public interface IHistoryValueDto extends Serializable {

  /**
   * @return the label stored when the history was logged.
   */
  String getValue();

  /**
   * @return the renderable DTO of the referenced entity, if any; {@code null} by default.
   */
  default IRenderable getRenderable() {
    return null;
  }
}
