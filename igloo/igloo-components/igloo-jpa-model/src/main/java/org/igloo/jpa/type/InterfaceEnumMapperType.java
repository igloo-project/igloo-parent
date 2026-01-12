package org.igloo.jpa.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.usertype.UserType;
import org.iglooproject.jpa.business.generic.model.IMappableInterface;

public class InterfaceEnumMapperType<T extends IMappableInterface & Serializable>
    implements UserType<T> {

  private final Class<T> type;

  private final Map<String, T> mapping;

  public InterfaceEnumMapperType(Class<T> type, Set<Class<? extends T>> enumTypes) {
    this.type = type;
    Map<String, T> builder = new HashMap<>();
    for (Class<? extends T> enumType : enumTypes) {
      // if there is a class cast problem, it'll be triggered at start time
      // builder refuses duplicates key, so we are protected against key reuse
      // (Class<Enum>) (Class<?>) and (Iterable<Enum>) are needed outside eclipse
      for (Enum enumValue : (Iterable<Enum>) EnumSet.allOf((Class<Enum>) (Class<?>) enumType)) {
        builder.put(enumValue.name(), (T) enumValue);
      }
    }
    mapping = Map.copyOf(builder);
  }

  private T instantiate(String columnValue) {
    return mapping.get(columnValue);
  }

  @Override
  public int getSqlType() {
    return SqlTypes.VARCHAR;
  }

  @Override
  public Class<T> returnedClass() {
    return type;
  }

  @Override
  public int hashCode(T x) {
    return x.hashCode();
  }

  @Override
  public T nullSafeGet(ResultSet rs, int position, WrapperOptions options) throws SQLException {
    String columnValue = rs.getString(position);
    if (rs.wasNull()) {
      return null;
    }
    return instantiate(columnValue);
  }

  @Override
  public void nullSafeSet(PreparedStatement st, T value, int index, WrapperOptions session)
      throws SQLException {
    if (value == null) {
      // postgresql only handle VARCHAR/LONGVARCHAR
      st.setNull(index, SqlTypes.VARCHAR);
    } else {
      st.setString(index, value.getName());
    }
  }

  @Override
  public T deepCopy(T value) {
    return value; // type is immutable
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(T value) {
    return value; // type is immutable
  }

  @Override
  public T assemble(Serializable cached, Object owner) {
    return (T) cached; // type is immutable
  }
}
