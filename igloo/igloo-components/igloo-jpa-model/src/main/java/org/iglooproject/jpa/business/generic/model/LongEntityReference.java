package org.iglooproject.jpa.business.generic.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.bindgen.Bindable;
import org.hibernate.annotations.JavaType;
import org.hibernate.annotations.JdbcType;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.type.descriptor.java.ClassJavaType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

/** This class allows to get rid of generics and ease entity reference usage. */
@Embeddable
@Bindable
@Access(AccessType.FIELD)
public final class LongEntityReference
    extends GenericEntityReference<Long, GenericEntity<Long, ?>> {

  private static final long serialVersionUID = -1385838799400769763L;

  @Column(nullable = true)
  @GenericField
  private Long id;

  @Column(nullable = true)
  @JavaType(ClassJavaType.class)
  @JdbcType(VarcharJdbcType.class)
  private Class<? extends GenericEntity<Long, ?>> type;

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

  // Pour Hibernate
  protected LongEntityReference() {}

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
