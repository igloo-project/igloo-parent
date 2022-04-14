package org.igloo.jpa.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.jdbc.Size;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.BasicType;
import org.hibernate.type.ForeignKeyDirection;
import org.hibernate.type.Type;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;

public class GenericEntityReferenceType implements BasicType {

	private static final long serialVersionUID = -3635079322937387285L;

	private int[] sqlTypes() {
		return new int[] { Types.VARCHAR, Types.BIGINT };
	}
	
	@SuppressWarnings("rawtypes")
	private Class returnedClass() {
		return GenericEntityReference.class;
	}
	
	private boolean equals(Object x, Object y) throws HibernateException {
		return Objects.equals(x, y);
	}
	
	private int hashCode(Object x) throws HibernateException {
		return Objects.hashCode(x);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		String className = rs.getString(names[0]);
		Long id = rs.getLong(names[1]);
		if (rs.wasNull()) {
			id = null;
		}
		if (className == null && id == null) {
			return null;
		} else if (className != null && id != null) {
			try {
				return GenericEntityReference.of((Class) Class.forName(className), id);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(String.format("className %s cannot be bound to an effective class", className), e);
			}
		} else {
			throw new IllegalStateException(String.format("className %s and id %d must be both null or non null", className, id));
		}
	}
	
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if (value == null) {
			st.setString(index, null);
			st.setNull(index + 1, Types.BIGINT);
		} else {
			@SuppressWarnings("unchecked")
			GenericEntityReference<Long, ?> ref = (GenericEntityReference<Long, ?>) value;
			st.setString(index, ref.getClass().getName());
			st.setLong(index + 1, ref.getId());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object deepCopy(Object value) throws HibernateException {
		GenericEntityReference<Long, ?> ref = (GenericEntityReference<Long, ?>) value;
		return GenericEntityReference.of((Class) ref.getClass(), ref.getId());
	}
	@Override
	public boolean isMutable() {
		return false;
	}
	
	private Serializable disassemble(Object value) throws HibernateException {
		return (GenericEntityReference<?, ?>) value;
	}
	
	private Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}
	
	private Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@Override
	public boolean isAssociationType() {
		return false;
	}

	@Override
	public boolean isCollectionType() {
		return false;
	}

	@Override
	public boolean isEntityType() {
		return false;
	}

	@Override
	public boolean isAnyType() {
		return false;
	}

	@Override
	public boolean isComponentType() {
		return false;
	}

	@Override
	public int getColumnSpan(Mapping mapping) throws MappingException {
		return 2;
	}

	@Override
	public int[] sqlTypes(Mapping mapping) throws MappingException {
		return sqlTypes();
	}

	@Override
	public Size[] dictatedSizes(Mapping mapping) throws MappingException {
		return new Size[] { new Size(), new Size() };
	}

	@Override
	public Size[] defaultSizes(Mapping mapping) throws MappingException {
		return dictatedSizes(mapping);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getReturnedClass() {
		return returnedClass();
	}

	@Override
	public boolean isSame(Object x, Object y) throws HibernateException {
		return equals(x, y);
	}

	@Override
	public boolean isEqual(Object x, Object y) throws HibernateException {
		return isEqual(x, y);
	}

	@Override
	public boolean isEqual(Object x, Object y, SessionFactoryImplementor factory) throws HibernateException {
		return isEqual(x, y);
	}

	@Override
	public int getHashCode(Object x) throws HibernateException {
		return hashCode(x);
	}

	@Override
	public int getHashCode(Object x, SessionFactoryImplementor factory) throws HibernateException {
		return hashCode(x);
	}

	@Override
	public int compare(Object x, Object y) {
		throw new IllegalStateException("Not implemented");
	}

	@Override
	public boolean isDirty(Object old, Object current, SharedSessionContractImplementor session)
			throws HibernateException {
		return false;
	}

	@Override
	public boolean isDirty(Object oldState, Object currentState, boolean[] checkable,
			SharedSessionContractImplementor session) throws HibernateException {
		return checkable[0] && checkable[1] && isDirty(oldState, currentState, session);
	}

	@Override
	public boolean isModified(Object dbState, Object currentState, boolean[] checkable,
			SharedSessionContractImplementor session) throws HibernateException {
		return isDirty(dbState, currentState, checkable, session);
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String name, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		throw new IllegalStateException("Not allowed; we expect multiple columns");
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, boolean[] settable,
			SharedSessionContractImplementor session) throws HibernateException, SQLException {
		nullSafeSet(st, value, index, session);
	}

	@Override
	public String toLoggableString(Object value, SessionFactoryImplementor factory) throws HibernateException {
		return value != null ? value.toString() : "<null>";
	}

	@Override
	public String getName() {
		return GenericEntityReferenceType.class.getName();
	}

	@Override
	public Object deepCopy(Object value, SessionFactoryImplementor factory) throws HibernateException {
		return deepCopy(value);
	}

	@Override
	public Serializable disassemble(Object value, SharedSessionContractImplementor session, Object owner)
			throws HibernateException {
		return disassemble(value);
	}

	@Override
	public Object assemble(Serializable cached, SharedSessionContractImplementor session, Object owner)
			throws HibernateException {
		return assemble(cached, owner);
	}

	@Override
	public void beforeAssemble(Serializable cached, SharedSessionContractImplementor session) {
		// NOSONAR
	}

	@Override
	public Object hydrate(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		return nullSafeGet(rs, getName(), session, owner);
	}

	@Override
	public Object resolve(Object value, SharedSessionContractImplementor session, Object owner)
			throws HibernateException {
		throw new IllegalStateException("Not allowed; not a relation/collection");
	}

	@Override
	public Object semiResolve(Object value, SharedSessionContractImplementor session, Object owner)
			throws HibernateException {
		return value;
	}

	@Override
	public Type getSemiResolvedType(SessionFactoryImplementor factory) {
		return this;
	}

	@Override
	public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner,
			@SuppressWarnings("rawtypes") Map copyCache) throws HibernateException {
		return replace(original, target, owner);
	}

	@Override
	public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner,
			@SuppressWarnings("rawtypes") Map copyCache, ForeignKeyDirection foreignKeyDirection) throws HibernateException {
		return replace(original, target, owner);
	}

	@Override
	public boolean[] toColumnNullness(Object value, Mapping mapping) {
		if (value != null) {
			return new boolean[] { false, false };
		}
		return new boolean [] { true, true };
	}

	@Override
	public String[] getRegistrationKeys() {
		return new String[0];
	}

}
