package org.iglooproject.jpa.more.business.history.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.bindgen.Bindable;
import org.hibernate.annotations.JavaType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.java.ClassJavaType;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;

/**
 * This class is a workaround: it's a subclass of GenericEntityReference with Long IDs which
 * prevents Bindgen from generating bad bindings due to generics.
 *
 * <p>For this reason, we <strong>do not</strong> override {@link #equals(Object)} and {@link
 * #hashCode()}.
 *
 * <p>Annotations Hibernate on accessors because of {@link GenericEntityReference} parent class
 * fields
 *
 * @see org.iglooproject.jpa.more.business.history.service.AbstractHistoryValueServiceImpl
 */
@Embeddable
@Bindable
public final class HistoryEventEntityReference
    extends GenericEntityReference<Long, GenericEntity<Long, ?>> {

  private static final long serialVersionUID = -1385838799400769763L;

  @Column(nullable = true)
  @JavaType(ClassJavaType.class)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private /* final */ Class<? extends GenericEntity<Long, ?>> type;

  @Column(nullable = true)
  @GenericField
  private /* final */ Long id;

  public static HistoryEventEntityReference from(
      GenericEntityReference<Long, ?> genericEntityReference) {
    return genericEntityReference == null
        ? null
        : new HistoryEventEntityReference(genericEntityReference);
  }

  // Pour Hibernate
  protected HistoryEventEntityReference() {}

  public HistoryEventEntityReference(
      Class<? extends GenericEntity<Long, ?>> entityClass, Long entityId) {
    super(entityClass, entityId);
  }

  public HistoryEventEntityReference(GenericEntity<Long, ?> entity) {
    super(entity);
  }

  public HistoryEventEntityReference(
      GenericEntityReference<Long, ? extends GenericEntity<Long, ?>> genericEntityReference) {
    super(genericEntityReference.getType(), genericEntityReference.getId());
  }

  @Override
  @Column(nullable = true)
  @GenericField
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  @Column(nullable = true)
  @JavaType(ClassJavaType.class)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  public Class<? extends GenericEntity<Long, ?>> getType() {
    return type;
  }

  @Override
  public void setType(Class<? extends GenericEntity<Long, ?>> type) {
    this.type = type;
  }
}
