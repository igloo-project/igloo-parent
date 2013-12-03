package fr.openwide.core.wicket.more.model.threadsafe;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.wicket.Session;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.lang.Objects;

import fr.openwide.core.wicket.more.model.threadsafe.impl.AbstractThreadSafeLoadableDetachableModel;
import fr.openwide.core.wicket.more.model.threadsafe.impl.LoadableDetachableModelThreadContext;

/**
 * An implementation of {@link LoadableDetachableModel} that is thread-safe, and may thus be used in multiple request cycles at the same time.
 * <p>This should be used mainly as an abstract base class for entity-related loadable/detachable models (such as {@link SessionThreadSafeGenericEntityModel}).
 * When in doubt, prefer {@link SessionThreadSafeSimpleLoadableDetachableModel} as an abstract base class for your custom thread-safe loadable/detachable model.
 * <p>This class may be used as a basis for loadable/detachable models when :
 * <ul>
 * 	<li>the model is stored in a global object, such as the {@link Session wicket session}
 * 	<li>the serializable state of this loadable/detachable model derives directly from the model object (see {@link #load(Serializable)} and {@link #makeSerializable(Object)})
 * </ul>
 * 
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
 * after the last call to {@link #setObject(Object)}. It also ensures that threads that do <em>not</em> change the model object won't write it to the shared context. 
 *
 * @param <T> The type of the model object.
 * @param <S> The type of the object that is serialized in stead of the model object.
 */
public abstract class SessionThreadSafeDerivedSerializableStateLoadableDetachableModel<T, S extends Serializable>
		extends AbstractThreadSafeLoadableDetachableModel<T, SessionThreadSafeDerivedSerializableStateLoadableDetachableModel<T,S>.ThreadContextImpl> {
	
	private static final long serialVersionUID = 6859907414385876596L;

	protected class ThreadContextImpl extends LoadableDetachableModelThreadContext<T> {
		private S sharedSerializableStateOnLastLoad = null;
	}
	
	/**
	 * The serializable form of the model object, shared among all threads.
	 */
	private final AtomicReference<S> sharedSerializableState = new AtomicReference<S>();
	
	public SessionThreadSafeDerivedSerializableStateLoadableDetachableModel() { }

	public SessionThreadSafeDerivedSerializableStateLoadableDetachableModel(S serializableObject) {
		sharedSerializableState.set(serializableObject);
	}

	public SessionThreadSafeDerivedSerializableStateLoadableDetachableModel(T object) {
		setObject(object);
	}
	
	@Override
	protected final ThreadContextImpl newThreadContext() {
		return new ThreadContextImpl();
	}
	
	@Override
	protected final T load(ThreadContextImpl threadContext) {
		threadContext.sharedSerializableStateOnLastLoad = sharedSerializableState.get();
		return load(threadContext.sharedSerializableStateOnLastLoad);
	}

	/**
	 * Loads the model object value from the implementation-defined data source, using given {@link #makeSerializable(Object) serializable state}.
	 * @param serializableState The serializable state that was returned by {@link #makeSerializable(Object)}.
	 * @see #makeSerializable(T)
	 */
	protected abstract T load(S serializableState);
	
	@Override
	protected final void onSetObject(ThreadContextImpl threadContext) {
		S serializableObject = makeSerializable(threadContext.getTransientModelObject());
		sharedSerializableState.set(serializableObject);
		threadContext.sharedSerializableStateOnLastLoad = serializableObject;
		onSetObject(threadContext.getTransientModelObject());
	}
	
	protected void onSetObject(T object) {
		// Does nothing by default
	}
	
	/**
	 * Creates the serializable state from the given object.
	 * @return A serializable object that will be used in {@link #load(S)} to retrieve the object when it is not available anymore.
	 * @see #load(S)
	 */
	protected abstract S makeSerializable(T currentObject);
	
	@Override
	protected final void onDetach(ThreadContextImpl threadContext) {
		// Write to the shared context ONLY if a change occurred in this thread.
		// This prevents the model to overwrite a serializable object that has been set in another thread if there was no change in this thread.
		S attachedObjectSerializableContext = makeSerializable(threadContext.getTransientModelObject());
		if (!Objects.equal(attachedObjectSerializableContext, threadContext.sharedSerializableStateOnLastLoad)) {
			sharedSerializableState.set(attachedObjectSerializableContext);
		}
		onDetach();
	}
	
	protected void onDetach() {
	}

}
