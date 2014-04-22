package fr.openwide.core.jpa.hibernate.usertype;

import org.hibernate.type.IntegerType;

public abstract class AbstractImmutableMaterializedIntegerValueUserType<T extends AbstractMaterializedPrimitiveValue<Integer, T>>
		extends AbstractImmutableMaterializedPrimitiveValueUserType<Integer, T> {
	
	public AbstractImmutableMaterializedIntegerValueUserType() {
		super(new IntegerType());
	}

}
