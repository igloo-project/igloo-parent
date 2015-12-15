package fr.openwide.core.wicket.more.model.threadsafe.impl;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import fr.openwide.core.wicket.more.model.threadsafe.SessionThreadSafeDerivedSerializableStateLoadableDetachableModel;
import fr.openwide.core.wicket.more.model.threadsafe.SessionThreadSafeSimpleLoadableDetachableModel;

/**
 * An alternative implementation of {@link LoadableDetachableModel} that is thread-safe, and may thus be used in multiple request cycles at the same time.
 * <p>This class should be used as an abstract base class for loadable/detachable models when the models are stored in a global object, such as the {@link Session wicket session}.
 * <p>Two subclasses are provided to ease implementation : {@link SessionThreadSafeSimpleLoadableDetachableModel} (simple use) and {@link SessionThreadSafeDerivedSerializableStateLoadableDetachableModel} (advanced use).
 *
 * @param <T> The type of the model object.
 */
public abstract class AbstractThreadSafeLoadableDetachableModel<T, TThreadContext extends LoadableDetachableModelThreadContext<T>>
		implements IModel<T> {
	
	private static final long serialVersionUID = 6859907414385876596L;

	/**
	 * The loading context, local to each thread.
	 * <p>Will be null if the model is not currently attached.
	 */
	private transient ThreadLocal<TThreadContext> threadLocal = new ThreadLocal<TThreadContext>();
	
	public AbstractThreadSafeLoadableDetachableModel() { }

	public AbstractThreadSafeLoadableDetachableModel(T object) {
		setObject(object);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		threadLocal = new ThreadLocal<>();
	}
	
	protected abstract TThreadContext newThreadContext();
	
	@Override
	public final T getObject() {
		TThreadContext threadContext = threadLocal.get();
		if (threadContext == null) { // If not attached yet
			threadContext = newThreadContext();
			threadLocal.set(threadContext); // Attach model
			threadContext.setTransientModelObject(load(threadContext)); // Populate model value
		}
		return threadContext.getTransientModelObject();
	}

	@Override
	public final void setObject(T object) {
		TThreadContext threadContext = threadLocal.get();
		if (threadContext == null) { // If not attached yet
			threadContext = newThreadContext();
			threadLocal.set(threadContext); // Attach model
		}
		threadContext.setTransientModelObject(wrap(object)); // Populate model value
		onSetObject(threadContext);
	}
	
	/**
	 * Updates the threadContext as needed.
	 */
	protected abstract void onSetObject(TThreadContext threadContext);

	/**
	 * Loads the model object value from the implementation-defined data source, updating the threadContext as needed.
	 */
	protected abstract T load(TThreadContext threadContext);

	/**
	 * Wraps the model object value when the model is {@link #setObject(Object) set} , if needed.
	 * <p>This method is not called when the model object value is obtained using {@link #load()}.
	 */
	protected T wrap(T object) {
		return object;
	}
	
	@Override
	public final void detach() {
		try {
			TThreadContext threadContext = threadLocal.get();
			if (threadContext != null) { // If attached
				onDetach(threadContext);
			} else {
				onDetachDetached();
			}
		} finally {
			threadLocal.remove();
		}
	}

	protected abstract void onDetach(TThreadContext threadContext);
	
	/**
	 * Perform any necessary adjustment on implementation-defined detached data when detach() is called, but the
	 * model is already detached.
	 */
	protected abstract void onDetachDetached();

}
