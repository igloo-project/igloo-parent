package org.iglooproject.jpa.business.generic.model;

import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

public class GenericEntityCollectionReference<
        K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>>
    implements Serializable {

  private static final long serialVersionUID = 1357434247523209721L;

  @Column(nullable = true)
  private final Class<? extends E> entityClass;

  @Column(nullable = true)
  @GenericField
  private final List<K> entityIdList;

  public static <K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>>
      GenericEntityCollectionReference<K, E> of(
          Class<E> entityClass, Collection<? extends E> entityCollection) {
    List<K> entityIdCollection = Lists.newArrayListWithExpectedSize(entityCollection.size());
    for (E entity : entityCollection) {
      Verify.verify(!entity.isNew(), "None of the referenced entities must be transient");
      entityIdCollection.add(entity.getId());
    }
    return new GenericEntityCollectionReference<>(entityClass, entityIdCollection);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <E extends GenericEntity<?, ?>>
      GenericEntityCollectionReference<?, E> ofUnknownIdType(
          Class<E> entityClass, Collection<? extends E> entityCollection) {
    List<Object> entityIdCollection = Lists.newArrayListWithExpectedSize(entityCollection.size());
    for (E entity : entityCollection) {
      Verify.verify(!entity.isNew(), "None of the referenced entities must be transient");
      entityIdCollection.add(entity.getId());
    }
    return new GenericEntityCollectionReference(entityClass, entityIdCollection);
  }

  public GenericEntityCollectionReference(E object) {
    this(GenericEntityReference.of(object));
  }

  public GenericEntityCollectionReference(GenericEntityReference<K, E> reference) {
    this(reference.getType(), List.of(reference.getId()));
  }

  public GenericEntityCollectionReference(Class<? extends E> entityClass) {
    this(entityClass, List.of());
  }

  public GenericEntityCollectionReference(
      Class<? extends E> entityClass, Collection<K> entityIdCollection) {
    super();
    Verify.verifyNotNull(entityClass, "entityClass must not be null");
    Verify.verifyNotNull(entityIdCollection, "entityIdCollection must not be null");
    this.entityClass = entityClass;
    this.entityIdList = Collections.unmodifiableList(Lists.newArrayList(entityIdCollection));
  }

  public Class<? extends E> getEntityClass() {
    return entityClass;
  }

  public List<K> getEntityIdList() {
    return entityIdList;
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
    if (!(obj instanceof GenericEntityCollectionReference)) {
      return false;
    }
    GenericEntityCollectionReference<?, ? extends GenericEntity<?, ?>> other =
        (GenericEntityCollectionReference<?, ?>) obj;
    return new EqualsBuilder()
        .append(getEntityIdList(), other.getEntityIdList())
        .append(getUpperEntityClass(getEntityClass()), getUpperEntityClass(other.getEntityClass()))
        .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getEntityIdList())
        .append(getUpperEntityClass(getEntityClass()))
        .build();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(ToStringStyle.SHORT_PREFIX_STYLE)
        .append("class", getEntityClass())
        .append("id", getEntityIdList())
        .build();
  }

  public GenericEntityCollectionReference<K, E> subset(int offset, int limit) {
    if (offset < 0) {
      throw new IllegalArgumentException("offset = " + offset);
    }
    if (limit < 0) {
      throw new IllegalArgumentException("limit = " + limit);
    }

    int size = entityIdList.size();
    if (offset >= size) {
      return new GenericEntityCollectionReference<>(getEntityClass(), Collections.<K>emptyList());
    } else {
      int toIndex = Math.min(size, offset + limit);
      List<K> sublist = entityIdList.subList(offset, toIndex);
      return new GenericEntityCollectionReference<>(getEntityClass(), sublist);
    }
  }
}
