package org.iglooproject.jpa.more.business.history.hibernate.composite;

import jakarta.persistence.Column;
import org.hibernate.annotations.JavaType;
import org.hibernate.annotations.JdbcType;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.type.descriptor.java.ClassJavaType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.hibernate.HistoryEntityReferenceTypeJdbcType;

/**
 * Use old-style varchar storing for <code>type</code> field.
 *
 * @see HistoryEntityReferenceTypeJdbcType
 */
public class HistoryEntityReferenceLegacyCompositeType extends AbstractHistoryValueCompositeType {

  @Override
  public Class<?> embeddable() {
    return HistoryEntityReferenceEnumEmbeddable.class;
  }

  public static class HistoryEntityReferenceEnumEmbeddable {

    @Column(nullable = true)
    @JavaType(ClassJavaType.class)
    @JdbcType(VarcharJdbcType.class)
    private /* final */ Class<? extends GenericEntity<Long, ?>> type;

    @Column(nullable = true)
    @GenericField
    private /* final */ Long id;
  }
}
