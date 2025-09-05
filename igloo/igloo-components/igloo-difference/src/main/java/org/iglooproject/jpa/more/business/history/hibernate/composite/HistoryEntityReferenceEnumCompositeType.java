package org.iglooproject.jpa.more.business.history.hibernate.composite;

import jakarta.persistence.Column;
import org.hibernate.annotations.JavaType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.type.descriptor.java.ClassJavaType;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.hibernate.HistoryEntityReferenceTypeJdbcType;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEntityReference;

/**
 * Use an optimized class to varchar for {@link HistoryEntityReference#getType()}
 *
 * @see HistoryEntityReferenceTypeJdbcType
 */
public class HistoryEntityReferenceEnumCompositeType extends AbstractHistoryValueCompositeType {

  @Override
  public Class<?> embeddable() {
    return HistoryEntityReferenceEnumEmbeddable.class;
  }

  public static class HistoryEntityReferenceEnumEmbeddable {

    @Column(nullable = true)
    @JavaType(ClassJavaType.class)
    @JdbcTypeCode(HistoryEntityReferenceTypeJdbcType.SQL_TYPE_CODE)
    private /* final */ Class<? extends GenericEntity<Long, ?>> type;

    @Column(nullable = true)
    @GenericField
    private /* final */ Long id;
  }
}
