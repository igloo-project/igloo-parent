package org.iglooproject.jpa.business.generic.model;

import com.google.common.base.Verify;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 241003 - Hibernate 6.6 not authorize @Embedded and @MappedSuperClass annotations. this class is
 * not an entity, annotation hibernate must be set on childens accessors
 */
public class GenericEntityReference<
        K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>>
    implements IReference<E>, Serializable {

  private static final long serialVersionUID = 1357434247523209721L;

  private /* final */ Class<? extends E> type;

  private /* final */ K id;

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

  @SuppressWarnings("unchecked")
  public GenericEntityReference(E entity) {
    Verify.verifyNotNull(entity, "The referenced entity must not be null");
    Verify.verify(!entity.isNew(), "The referenced entity must not be transient");
    setType((Class<? extends E>) GenericEntity.GET_CLASS_FUNCTION.apply(entity));
    setId(entity.getId());
  }

  public GenericEntityReference(Class<? extends E> entityClass, K entityId) {
    super();
    Verify.verifyNotNull(entityClass, "entityClass must not be null");
    Verify.verifyNotNull(entityId, "entityId must not be null");
    setType(entityClass);
    setId(entityId);
  }

  @Override
  public Class<? extends E> getType() {
    return type;
  }

  public void setType(Class<? extends E> type) {
    this.type = type;
  }

  @Override
  public K getId() {
    return id;
  }

  public void setId(K id) {
    this.id = id;
  }

  @SuppressWarnings("unchecked")
  protected static Class<? extends GenericEntity<?, ?>> getUpperEntityClass(
      Class<? extends GenericEntity<?, ?>> entityClass) {
    Class<?> currentClass = entityClass;
    while (currentClass != null && currentClass.getAnnotation(Entity.class) == null) {
      currentClass = currentClass.getSuperclass();
    }
    return (Class<? extends GenericEntity<?, ?>>) currentClass;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    /* Caution here: we really need an instanceof, not a this.getClass() == other.getClass()
     * because some subclasses may simply be workarounds (for instance HistoryEntityReference in JPA-More)
     */
    if (!(obj instanceof GenericEntityReference)) {
      return false;
    }
    GenericEntityReference<?, ? extends GenericEntity<?, ?>> other =
        (GenericEntityReference<?, ?>) obj;
    return new EqualsBuilder()
        .append(getId(), other.getId())
        .append(getUpperEntityClass(getType()), getUpperEntityClass(other.getType()))
        .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(getId()).append(getUpperEntityClass(getType())).build();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("class", getType())
        .append("id", getId())
        .build();
  }

  @Override
  @Transient
  public GenericEntityReference<K, E> asReference() {
    return this;
  }

  @Override
  @Transient
  public boolean matches(E referenceable) {
    return referenceable != null && equals(referenceable.asReference());
  }
}
