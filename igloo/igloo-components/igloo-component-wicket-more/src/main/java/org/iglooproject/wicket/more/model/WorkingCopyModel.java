package org.iglooproject.wicket.more.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.model.IModel;

public class WorkingCopyModel<T> implements IModel<T> {
	
	private static final long serialVersionUID = -4049247716740595168L;

	private final IModel<T> reference;
	private final IModel<T> workingCopy;
	
	public static <T> WorkingCopyModel<T> of(IModel<T> reference, IModel<T> workingCopy) {
		return new WorkingCopyModel<>(reference, workingCopy);
	}
	
	public WorkingCopyModel(IModel<T> reference, IModel<T> workingCopy) {
		this.reference = reference;
		this.workingCopy = workingCopy;
		read();
	}

	@Override
	public boolean equals(Object obj) {
		return new EqualsBuilder().appendSuper(super.equals(obj)).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().appendSuper(super.hashCode()).hashCode();
	}

	@Override
	public void detach() {
		this.reference.detach();
		this.workingCopy.detach();
	}

	@Override
	public T getObject() {
		return workingCopy.getObject();
	}

	@Override
	public void setObject(T object) {
		workingCopy.setObject(object);
	}

	public void read() {
		try {
			workingCopy.setObject(reference.getObject());
		} catch (RuntimeException e) {
			throw new IllegalStateException("Exception while reading from " + tryToString(reference) + " to " + tryToString(workingCopy), e);
		}
	}

	public void write() {
		try {
			reference.setObject(workingCopy.getObject());
		} catch (RuntimeException e) {
			throw new IllegalStateException("Exception while writing from " + tryToString(workingCopy) + " to " + tryToString(reference), e);
		}
	}

	private String tryToString(IModel<?> model) {
		try {
			return String.valueOf(model);
		} catch (RuntimeException e) {
			return "<Unexpected exception while calling String.valueOf(model)>";
		}
	}

	public IModel<T> getReferenceModel() {
		return reference;
	}
}
