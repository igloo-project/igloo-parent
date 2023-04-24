package org.igloo.jpa.type;

import java.util.Set;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import org.iglooproject.jpa.business.generic.model.IMappableInterface;

public class InterfaceEnumMapperType<T extends IMappableInterface> extends AbstractSingleColumnStandardBasicType<T> implements Type {

	private static final long serialVersionUID = 3508687149135896486L;

	public InterfaceEnumMapperType(Class<T> _interface, Set<Class<? extends T>> enumTypes) {
		super(VarcharJdbcType.INSTANCE, new InterfaceEnumMapperTypeDescriptor<>(_interface, enumTypes));
	}


	@Override
	public String getName() {
		return getJavaType().getName();
	}
}
