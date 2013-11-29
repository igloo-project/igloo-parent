package fr.openwide.core.wicket.more.model.threadsafe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.lang.Objects;

/**
 * An alternative implementation of {@link LoadableDetachableModel} that is thread-safe, and may thus be used in multiple request cycles at the same time.
 * <p>This class should be used when loadable-detachable models are needed in a global object, such as the {@link Session wicket session}.
 * <p><strong>WARNING:</strong> user willing to make changes on this model (calls to {@link #setObject(Object)}) from multiple request cycles
 * should be advised that only the changes from the call to {@link #setObject(Object)} will be kept, except if the said object experienced changes affecting
 * its {@link #makeSerializable(Object) serialized form} afterwards. In this last case, the actual effect is well-defined, but quite complex and should probably not be relied on.
 * 
 * <p>Actual changes to the stored serialized form of the model object will happen in the following situations :<ul>
 * <li>{@link #ThreadSafeLoadableDetachableModel(T)} is called
 * <li>{@link #setObject(T)} is called
 * <li>{@link #detach()} is called AND the model is attached AND the object returned by {@link #makeSerializable(T)} is not {@link #equals(Object) equal} to
 * the one that was returned by {@code makeSerializable(currentModelObject)} on the last call to {@link #ThreadSafeLoadableDetachableModel(Object)},
 * {@link #setObject(Object)} or {@link #getObject()} (whichever happened last).
 * </ul>
 * 
 * The last criteria ensures that changes to the model object impacting its serializable form will not be lost, even if they happen
 * after the last call to {@link #setObject(Object)}. It also ensures that, in threads that
 *
 * @param <T> The type of the model object.
 * @param <S> The type of the object that is serialized in stead of the model object.
 */
public abstract class SessionThreadSafeLoadableDetachableModel<T, S extends Serializable> implements IModel<T> {
	
	private static final long serialVersionUID = 6859907414385876596L;

	private class ThreadContext {
		private boolean attached = false;
		private T transientModelObject = null;
		private S sharedSerializableObjectOnLastLoad = null;
	}
	
	private class ThreadContextThreadLocal extends ThreadLocal<ThreadContext> {
		@Override
		protected ThreadContext initialValue() {
			return new ThreadContext();
		}
	}
	
	private transient ThreadLocal<ThreadContext> threadLocal = new ThreadContextThreadLocal();
	
	private final AtomicReference<S> sharedSerializableObject = new AtomicReference<S>();
	
	public SessionThreadSafeLoadableDetachableModel() { }

	public SessionThreadSafeLoadableDetachableModel(T object) {
		setObject(object);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		threadLocal = new ThreadContextThreadLocal();
	}
	
	@Override
	public final T getObject() {
		ThreadContext threadContext = threadLocal.get();
		if (!threadContext.attached) {
			threadContext.sharedSerializableObjectOnLastLoad = sharedSerializableObject.get();
			threadContext.transientModelObject = load(threadContext.sharedSerializableObjectOnLastLoad);
			threadContext.attached = true;
		}
		return threadContext.transientModelObject;
	}
	
	@Override
	public final void setObject(T object) {
		ThreadContext threadContext = threadLocal.get();
		S serializableObject = makeSerializable(object);
		sharedSerializableObject.set(serializableObject);
		threadContext.sharedSerializableObjectOnLastLoad = serializableObject;
		threadContext.transientModelObject = object;
		threadContext.attached = true;
	}

	/**
	 * Loads the model object value from the implementation-defined data source, using given {@link #makeSerializable(Object) serializable data}.
	 * @param serializableObject The serializable object that was returned by {@link #makeSerializable(Object)}.
	 * @see #makeSerializable(T)
	 */
	protected abstract T load(S serializableObject);
	
	/**
	 * Creates a serializable object from the given object.
	 * @return A serializable object that will be used in {@link #load(S)} to retrieve the object when it is not available anymore.
	 * @see #load(S)
	 */
	protected abstract S makeSerializable(T currentObject);
	
	@Override
	public final void detach() {
		try {
			ThreadContext threadContext = threadLocal.get();
			if (threadContext.attached) {
				// Prevents the model to overwrite a serializable object that has been set in another thread if there was no change in this thread.
				S attachedObjectSerializableContext = makeSerializable(threadContext.transientModelObject);
				if (!Objects.equal(attachedObjectSerializableContext, threadContext.sharedSerializableObjectOnLastLoad)) {
					sharedSerializableObject.set(attachedObjectSerializableContext);
				}
				// We don't care about updating the thread context : it will be removed right after this
			}
		} finally {
			threadLocal.remove();
		}
	}

}
