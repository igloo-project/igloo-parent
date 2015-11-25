package fr.openwide.core.jpa.more.business.property.model;

import java.io.Serializable;

import com.google.common.base.Preconditions;


public class CompositeProperty<T, A, B> implements Serializable {

	private static final long serialVersionUID = -4315027545450578079L;

	private final PropertyId<A> firstPropertyId;

	private final PropertyId<B> secondPropertyId;

	public CompositeProperty(final PropertyId<A> firstPropertyId, final PropertyId<B> secondPropertyId) {
		super();
		Preconditions.checkNotNull(firstPropertyId);
		Preconditions.checkNotNull(secondPropertyId);
		this.firstPropertyId = firstPropertyId;
		this.secondPropertyId = secondPropertyId;
	}

	public PropertyId<A> getFirstPropertyId() {
		return firstPropertyId;
	}

	public PropertyId<B> getSecondPropertyId() {
		return secondPropertyId;
	}

}
