package fr.openwide.core.wicket.more.model.threadsafe;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.wicket.model.IModel;

public class SessionThreadSafeModel<T extends Serializable> implements IModel<T> {
	
	private static final long serialVersionUID = 4954375223101982404L;
	
	private final AtomicReference<T> objectReference = new AtomicReference<T>();

	public SessionThreadSafeModel() { }
	
	public SessionThreadSafeModel(T object) {
		setObject(object);
	}

	@Override
	public T getObject() {
		return objectReference.get();
	}

	@Override
	public void setObject(T object) {
		objectReference.set(object);
	}

	@Override
	public void detach() {
		// Nothing to do
	}

}
