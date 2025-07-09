package org.iglooproject.jpa.more.business.history.model.embeddable;

import org.bindgen.Bindable;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.more.business.history.hibernate.HistoryEntityReferenceTypeContributor;
import org.iglooproject.jpa.more.business.history.hibernate.composite.AbstractHistoryValueCompositeType;

/**
 * {@link HistoryEntityReference} dedicated to historylog optimization. <code>type</code> field may
 * be written as text or enum, based on {@link AbstractHistoryValueCompositeType} implementation.
 *
 * @see HistoryEntityReferenceTypeContributor
 */
@Bindable
public final class HistoryEntityReference
    extends GenericEntityReference<Long, GenericEntity<Long, ?>> {

  private static final long serialVersionUID = -1385838799400769763L;

  private /* final */ Class<? extends GenericEntity<Long, ?>> type;

  private /* final */ Long id;

  public static HistoryEntityReference from(
      GenericEntityReference<Long, ?> genericEntityReference) {
    return genericEntityReference == null
        ? null
        : new HistoryEntityReference(genericEntityReference);
  }

  // Pour Hibernate
  protected HistoryEntityReference() {}

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

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public Class<? extends GenericEntity<Long, ?>> getType() {
    return type;
  }

  @Override
  public void setType(Class<? extends GenericEntity<Long, ?>> type) {
    this.type = type;
  }
}
