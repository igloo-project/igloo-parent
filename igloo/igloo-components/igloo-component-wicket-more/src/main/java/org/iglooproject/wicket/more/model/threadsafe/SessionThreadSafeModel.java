package org.iglooproject.wicket.more.model.threadsafe;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * An alternative implementation of {@link Model} that is thread-safe, and may thus be used in multiple request cycles at the same time.
 * <p>This class should be used when :
 * <ul>
 * 	<li>the model is stored in a global object, such as the {@link Session wicket session}
 * 	<li>the model need not maintain a cache (on contrary to loadable/detachable models such as {@link SessionThreadSafeSimpleLoadableDetachableModel} or {@link SessionThreadSafeDerivedSerializableStateLoadableDetachableModel}).
 * </ul>
 * <p><strong>WARNING:</strong> Access to members of the model object is obviously not secured by this model in any way, so writing to the model
 * object is <strong>NOT</strong> thread-safe, unless the object implements thread safety.
 */
public class SessionThreadSafeModel<T extends Serializable> implements IModel<T> {
	
	private static final long serialVersionUID = 4954375223101982404L;
	
	private final AtomicReference<T> objectReference = new AtomicReference<>();

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
