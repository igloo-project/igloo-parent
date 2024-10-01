package org.iglooproject.jpa.business.generic.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import java.io.Serializable;
import org.hibernate.annotations.JavaType;
import org.hibernate.annotations.JdbcType;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.type.descriptor.java.ClassJavaType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

@Access(AccessType.FIELD)
public class GenericEntityReference<
        K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>>
    extends AbstractGenericEntityReference<K, E> {

  private static final long serialVersionUID = 1L;

  @Column(nullable = true)
  @JavaType(ClassJavaType.class)
  @JdbcType(VarcharJdbcType.class)
  private /* final */ Class<? extends E> type;

  @Column(nullable = true)
  @GenericField
  private /* final */ K id;

  @SuppressWarnings("unchecked")
  public GenericEntityReference(E entity) {
    super(entity);
  }

  public GenericEntityReference(Class<? extends E> entityClass, K entityId) {
    super(entityClass, entityId);
  }

  public static <K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>>
      GenericEntityReference<K, E> of(E entity) {
    return entity == null || entity.isNew() ? null : new GenericEntityReference<>(entity);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <E extends GenericEntity<?, ?>> GenericEntityReference<?, E> ofUnknownIdType(
      E entity) {
    return entity == null || entity.isNew() ? null : new GenericEntityReference(entity);
  }

  public static <K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>>
      GenericEntityReference<K, E> of(Class<? extends E> entityClass, K entityId) {
    return new GenericEntityReference<>(entityClass, entityId);
  }

  protected GenericEntityReference() {} // Pour Hibernate

  @Override
  public K getId() {
    return id;
  }

  public void setId(K id) {
    this.id = id;
  }

  @Override
  public Class<? extends E> getType() {
    return type;
  }

  public void setType(Class<? extends E> type) {
    this.type = type;
  }
}
