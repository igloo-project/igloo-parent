package org.iglooproject.jpa.hibernate.usertype;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.usertype.UserType;

/**
 * A Hibernate {@link UserType} for {@link AbstractMaterializedPrimitiveValue} stored as text.
 *
 * <p><strong>CAUTION</strong>: in order for this {@link UserType} to be used by Hibernate, you must
 * either register it as the default for your {@link AbstractMaterializedPrimitiveValue} by
 * configuring your type contributor or declaring your field with {@link Type} annotation.
 *
 * @see AbstractMaterializedPrimitiveValue
 */
public abstract class AbstractImmutableMaterializedBigDecimalValueUserType<
        T extends AbstractMaterializedPrimitiveValue<BigDecimal, T>>
    implements UserType<T> {

  @Override
  public int getSqlType() {
    return SqlTypes.NUMERIC;
  }

  @Override
  public abstract Class<T> returnedClass();

  protected abstract T instantiate(BigDecimal value);

  @Override
  public boolean equals(T x, T y) {
    return (x == y) || (x != null && y != null && x.equals(y));
  }

  @Override
  public int hashCode(T x) {
    return x.hashCode();
  }

  @Override
  public T nullSafeGet(ResultSet rs, int position, WrapperOptions options) throws SQLException {
    BigDecimal columnValue = rs.getBigDecimal(position);
    if (rs.wasNull()) {
      return null;
    }
    return instantiate(columnValue);
  }

  @Override
  public void nullSafeSet(PreparedStatement st, T value, int index, WrapperOptions options)
      throws SQLException {
    if (value == null) {
      st.setNull(index, getSqlType());
    } else {
      st.setBigDecimal(index, value.getValue());
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

  @SuppressWarnings("unchecked")
  @Override
  public T assemble(Serializable cached, Object owner) throws HibernateException {
    return (T) cached; // type is immutable
  }
}
