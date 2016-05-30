package fr.openwide.core.jpa.more.business.history.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.jpa.hibernate.usertype.StringClobType;

public class FieldPathType implements UserType {
	
	private final UserType delegateType = new StringClobType();

	@Override
	public int[] sqlTypes() {
		return delegateType.sqlTypes();
	}
	
	@Override
	public Class<FieldPath> returnedClass() {
		return FieldPath.class;
	}

	@Override
	@SuppressFBWarnings("squid:S1201")
	public boolean equals(Object x, Object y) throws HibernateException { // NOSONAR
		return (x == y) || (x != null && y != null && x.equals(y)); // NOSONAR
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		String value = (String) delegateType.nullSafeGet(rs, names, session, owner);
		if (value == null) {
			return null;
		} else {
			return instantiate(value);
		}
	}

	private Object instantiate(String value) {
		return FieldPath.fromString(value);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) 
			throws HibernateException, SQLException {
		if (value == null) {
			delegateType.nullSafeSet(st, null, index, session);
		} else {
			delegateType.nullSafeSet(st, ((FieldPath)value).toString(), index, session);
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value; // type is immutable
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value; // type is immutable
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached; // type is immutable
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original; // type is immutable
	}

}
