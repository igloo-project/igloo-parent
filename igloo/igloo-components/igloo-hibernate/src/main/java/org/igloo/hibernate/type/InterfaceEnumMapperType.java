package org.igloo.hibernate.type;

import java.util.Set;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;
import org.iglooproject.jpa.business.generic.model.IMappableInterface;

public class InterfaceEnumMapperType<T extends IMappableInterface> extends AbstractSingleColumnStandardBasicType<T> implements DiscriminatorType<T> {

	private static final long serialVersionUID = 3508687149135896486L;

	private final String typeName;

	public InterfaceEnumMapperType(String typeName, Class<T> _interface, Set<Class<? extends T>> enumTypes) {
		super(VarcharTypeDescriptor.INSTANCE, new InterfaceEnumMapperTypeDescriptor<T>(_interface, enumTypes));
		this.typeName = typeName;
	}

	@Override
	public T stringToObject(String xml) throws Exception {
		return fromString(xml);
	}

	@Override
	public String objectToSQLString(T value, Dialect dialect) throws Exception {
		return toString(value);
	}

	@Override
	public String getName() {
		return typeName;
	}
}
