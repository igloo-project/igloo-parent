package org.iglooproject.jpa.more.business.history.hibernate;

import static org.hibernate.type.SqlTypes.OTHER;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.hibernate.HibernateException;
import org.hibernate.mapping.BasicValue;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.AdjustableJdbcType;
import org.hibernate.type.descriptor.jdbc.BasicBinder;
import org.hibernate.type.descriptor.jdbc.BasicExtractor;
import org.hibernate.type.descriptor.jdbc.JdbcLiteralFormatter;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEntityReference;

/**
 * Custom {@link JdbcType} to manage {@link HistoryEntityReference#getType()} as an enum. It allows
 * to optimize table storage size.
 */
public class HistoryEntityReferenceTypeJdbcType implements AdjustableJdbcType {

  private static final long serialVersionUID = 1L;

  public static final String ENUM_TYPE_NAME = "historylog_reference_type";
  public static final int SQL_TYPE_CODE = 6004;

  @SuppressWarnings("rawtypes")
  private Map<String, Class> valueToClass;

  @SuppressWarnings("rawtypes")
  private Map<Class, String> classToValue;

  public HistoryEntityReferenceTypeJdbcType() {
    this.valueToClass = null;
    this.classToValue = null;
  }

  public HistoryEntityReferenceTypeJdbcType(@SuppressWarnings("rawtypes") List<Class> classes) {
    setupTypes(classes);
  }

  public void setupTypes(@SuppressWarnings("rawtypes") List<Class> classes) {
    if (valueToClass != null || classToValue != null) {
      throw new IllegalStateException("Type setting can be called only once.");
    }
    valueToClass =
        classes.stream().collect(Collectors.toMap(Class::getSimpleName, Function.identity()));
    classToValue =
        classes.stream().collect(Collectors.toMap(Function.identity(), Class::getSimpleName));
  }

  @Override
  public int getJdbcTypeCode() {
    return OTHER;
  }

  @Override
  public int getDefaultSqlTypeCode() {
    return SQL_TYPE_CODE;
  }

  @Override
  public <T> JdbcLiteralFormatter<T> getJdbcLiteralFormatter(JavaType<T> javaType) {
    return (appender, value, dialect, wrapperOptions) ->
        appender.appendSql("'" + value + "'::" + ENUM_TYPE_NAME);
  }

  @Override
  public <X> ValueBinder<X> getBinder(JavaType<X> javaType) {
    return new BasicBinder<>(javaType, this) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
          throws SQLException {
        @SuppressWarnings("rawtypes")
        final Class casted = javaType.unwrap(value, Class.class, options);
        st.setObject(index, fromClass(casted), Types.OTHER);
      }

      @Override
      protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
          throws SQLException {
        @SuppressWarnings("rawtypes")
        final Class casted = javaType.unwrap(value, Class.class, options);
        st.setObject(name, fromClass(casted), Types.OTHER);
      }
    };
  }

  @Override
  public <X> ValueExtractor<X> getExtractor(JavaType<X> javaType) {
    return new BasicExtractor<>(javaType, this) {
      private static final long serialVersionUID = 1L;

      @Override
      protected X doExtract(ResultSet rs, int paramIndex, WrapperOptions options)
          throws SQLException {
        final String string = rs.getString(paramIndex);
        return javaType.wrap(fromString(string), options);
      }

      @Override
      protected X doExtract(CallableStatement statement, int index, WrapperOptions options)
          throws SQLException {
        final String string = statement.getString(index);
        return javaType.wrap(fromString(string), options);
      }

      @Override
      protected X doExtract(CallableStatement statement, String name, WrapperOptions options)
          throws SQLException {
        final String string = statement.getString(name);
        return javaType.wrap(fromString(string), options);
      }
    };
  }

  /** Map a string value to a {@link Class} (database to entity). Null maps to null. */
  @SuppressWarnings("rawtypes")
  protected Class fromString(String value) {
    if (value == null) {
      return null;
    }
    Class clazz = valueToClass.get(value);
    if (clazz == null) {
      throw new HibernateException("Class not found for type " + value);
    }
    return clazz;
  }

  /** Map a {@link Class} value to the excepted type (entity to database). Null maps to null. */
  protected String fromClass(@SuppressWarnings("rawtypes") Class clazz) {
    if (clazz == null) {
      return null;
    }
    String value = classToValue.get(clazz);
    if (value == null) {
      throw new HibernateException("Class " + clazz.getName() + " is not a detected entity type");
    }
    return value;
  }

  @Override
  public JdbcType resolveIndicatedType(JdbcTypeIndicators indicators, JavaType<?> domainJtd) {
    if (indicators instanceof BasicValue) {}

    return new VarcharJdbcType();
  }
}
