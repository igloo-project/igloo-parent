package org.iglooproject.jpa.more.business.history.hibernate;

import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.sql.DdlType;

public class HistoryEntityReferenceDdlType implements DdlType {
  private static final long serialVersionUID = 1345434531879297143L;

  @Override
  public int getSqlTypeCode() {
    return HistoryEntityReferenceTypeJdbcType.SQL_TYPE_CODE;
  }

  @Override
  public String getRawTypeName() {
    return HistoryEntityReferenceTypeJdbcType.ENUM_TYPE_NAME;
  }

  @Override
  public String getTypeName(Long size, Integer precision, Integer scale) {
    return HistoryEntityReferenceTypeJdbcType.ENUM_TYPE_NAME;
  }

  @Override
  public String getCastTypeName(JdbcType jdbcType, JavaType<?> javaType) {
    return HistoryEntityReferenceTypeJdbcType.ENUM_TYPE_NAME;
  }

  @Override
  public String getCastTypeName(
      JdbcType jdbcType, JavaType<?> javaType, Long length, Integer precision, Integer scale) {
    return HistoryEntityReferenceTypeJdbcType.ENUM_TYPE_NAME;
  }
}
