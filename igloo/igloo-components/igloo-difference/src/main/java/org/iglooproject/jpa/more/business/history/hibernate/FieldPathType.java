package org.iglooproject.jpa.more.business.history.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;
import org.iglooproject.commons.util.fieldpath.FieldPath;

public class FieldPathType implements UserType<FieldPath> {

	@Override
	public int getSqlType() {
		// postgresql translate this type to text
		return SqlTypes.LONG32VARCHAR;
	}
	
	@Override
	public Class<FieldPath> returnedClass() {
		return FieldPath.class;
	}

	@Override
	public boolean equals(FieldPath x, FieldPath y) {
		return (x == y) || (x != null && y != null && x.equals(y));
	}

	@Override
	public int hashCode(FieldPath x) {
		return x.hashCode();
	}

	@Override
	public FieldPath nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
			throws SQLException {
		String columnValue = rs.getString(position);
		if (rs.wasNull()) {
			return null;
		}
		return FieldPath.fromString(columnValue);
	}
	
	@Override
	public void nullSafeSet(PreparedStatement st, FieldPath value, int index, SharedSessionContractImplementor session)
			throws SQLException {
		if (value == null) {
			// postgresql only handle VARCHAR/LONGVARCHAR
			st.setNull(index, SqlTypes.LONGVARCHAR);
		} else {
			st.setString(index, value.toString());
		}
	}

	@Override
	public FieldPath deepCopy(FieldPath value) {
		return value; // type is immutable
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(FieldPath value) {
		return value; // type is immutable
	}

	@Override
	public FieldPath assemble(Serializable cached, Object owner) throws HibernateException {
		return (FieldPath) cached; // type is immutable
	}

}
