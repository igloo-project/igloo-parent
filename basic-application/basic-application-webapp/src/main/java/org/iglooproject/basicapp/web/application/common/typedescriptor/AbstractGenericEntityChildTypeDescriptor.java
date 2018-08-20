package org.iglooproject.basicapp.web.application.common.typedescriptor;

import java.io.Serializable;

import org.iglooproject.jpa.business.generic.model.GenericEntity;

public abstract class AbstractGenericEntityChildTypeDescriptor<T extends AbstractGenericEntityTypeDescriptor<?, E>, E extends GenericEntity<?, ?>>
		implements Serializable {

	private static final long serialVersionUID = -7148614000136537502L;

	protected final T typeDescriptor;

	protected AbstractGenericEntityChildTypeDescriptor(T typeDescriptor) {
		this.typeDescriptor = typeDescriptor;
	}

	public T getTypeDescriptor() {
		return typeDescriptor;
	}

	protected abstract Object readResolve();
}
