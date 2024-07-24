package org.iglooproject.jpa.more.business.history.model.embeddable;

import jakarta.persistence.Embeddable;
import org.bindgen.Bindable;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;

/**
 * This class is a workaround: it's a subclass of GenericEntityReference with Long IDs which
 * prevents Bindgen from generating bad bindings due to generics.
 *
 * <p>For this reason, we <strong>do not</strong> override {@link #equals(Object)} and {@link
 * #hashCode()}.
 *
 * @see org.iglooproject.jpa.more.business.history.service.AbstractHistoryValueServiceImpl
 */
@Embeddable
@Bindable
public final class HistoryEntityReference
    extends GenericEntityReference<Long, GenericEntity<Long, ?>> {

  private static final long serialVersionUID = -1385838799400769763L;

  public static HistoryEntityReference from(
      GenericEntityReference<Long, ?> genericEntityReference) {
    return genericEntityReference == null
        ? null
        : new HistoryEntityReference(genericEntityReference);
  }

  protected HistoryEntityReference() { // Pour Hibernate
  }

  public HistoryEntityReference(
      Class<? extends GenericEntity<Long, ?>> entityClass, Long entityId) {
    super(entityClass, entityId);
  }

  public HistoryEntityReference(GenericEntity<Long, ?> entity) {
    super(entity);
  }

  public HistoryEntityReference(
      GenericEntityReference<Long, ? extends GenericEntity<Long, ?>> genericEntityReference) {
    super(genericEntityReference.getType(), genericEntityReference.getId());
  }
}
