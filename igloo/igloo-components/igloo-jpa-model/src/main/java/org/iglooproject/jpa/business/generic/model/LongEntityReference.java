package org.iglooproject.jpa.business.generic.model;

import javax.persistence.Embeddable;
import org.bindgen.Bindable;

/** This class allows to get rid of generics and ease entity reference usage. */
@Embeddable
@Bindable
public final class LongEntityReference
    extends GenericEntityReference<Long, GenericEntity<Long, ?>> {

  private static final long serialVersionUID = -1385838799400769763L;

  public static <E extends GenericEntity<Long, ?>> LongEntityReference ofLongEntity(E entity) {
    return entity == null || entity.isNew() ? null : new LongEntityReference(entity);
  }

  public static <E extends GenericEntity<Long, ?>> LongEntityReference of(
      Class<? extends E> entityClass, Long entityId) {
    return new LongEntityReference(entityClass, entityId);
  }

  public static LongEntityReference from(GenericEntityReference<Long, ?> genericEntityReference) {
    return genericEntityReference == null ? null : new LongEntityReference(genericEntityReference);
  }

  protected LongEntityReference() { // Pour Hibernate
  }

  public LongEntityReference(Class<? extends GenericEntity<Long, ?>> entityClass, Long entityId) {
    super(entityClass, entityId);
  }

  public LongEntityReference(GenericEntity<Long, ?> entity) {
    super(entity);
  }

  public LongEntityReference(
      GenericEntityReference<Long, ? extends GenericEntity<Long, ?>> genericEntityReference) {
    super(genericEntityReference.getType(), genericEntityReference.getId());
  }
}
