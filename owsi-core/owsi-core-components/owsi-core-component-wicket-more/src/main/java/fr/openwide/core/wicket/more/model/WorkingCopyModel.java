package fr.openwide.core.wicket.more.model;

import org.apache.wicket.model.IModel;

public class WorkingCopyModel<T> implements IModel<T> {
	
	private static final long serialVersionUID = -4049247716740595168L;

	private final IModel<T> reference;
	private final IModel<T> workingCopy;
	
	public static <T> WorkingCopyModel<T> of(IModel<T> reference, IModel<T> workingCopy) {
		return new WorkingCopyModel<T>(reference, workingCopy);
	}
	
	public WorkingCopyModel(IModel<T> reference, IModel<T> workingCopy) {
		this.reference = reference;
		this.workingCopy = workingCopy;
		read();
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
		workingCopy.setObject(reference.getObject());
	}

	public void write() {
		reference.setObject(workingCopy.getObject());
	}

	public IModel<T> getReferenceModel() {
		return reference;
	}
}
