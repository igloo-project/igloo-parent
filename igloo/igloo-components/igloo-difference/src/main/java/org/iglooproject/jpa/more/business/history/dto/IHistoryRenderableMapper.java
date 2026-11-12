package org.iglooproject.jpa.more.business.history.dto;

import java.io.Serializable;
import org.iglooproject.jpa.more.rendering.IRenderable;

/**
 * Mapper converting an entity referenced by the history into a renderable DTO.
 *
 * <p>Meant to be implemented by the mapper of the entity (e.g. a MapStruct mapper), so that the
 * {@link IHistoryRenderableService} implementation can find it. When displaying the history, the
 * referenced entity is reloaded then converted into its renderable DTO. An entity without mapper,
 * or deleted, is displayed with the label stored when the history was logged.
 *
 * @param <E> entity
 * @param <D> renderable DTO
 */
public interface IHistoryRenderableMapper<E, D extends IRenderable & Serializable> {

  D map(E entity);
}
