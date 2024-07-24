package org.iglooproject.wicket.more.model.threadsafe;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.wicket.more.model.threadsafe.impl.AbstractThreadSafeLoadableDetachableModel;
import org.iglooproject.wicket.more.model.threadsafe.impl.LoadableDetachableModelThreadContext;

/**
 * An implementation of {@link LoadableDetachableModel} that is thread-safe, and may thus be used in
 * multiple request cycles at the same time.
 *
 * <p>When in doubt, use this class as a basis for your thread-safe loadable/detachable model.
 *
 * <p>This class should be used when :
 *
 * <ul>
 *   <li>the model is stored in a global object, such as the {@link Session wicket session}
 *   <li>the serializable state of this loadable/detachable model is empty (no non-transient member)
 *       or not directly related to the model object (for instance, it is contained in a member
 *       whose type is {@link IModel}).
 * </ul>
 *
 * @param <T> The type of the model object.
 */
public abstract class SessionThreadSafeSimpleLoadableDetachableModel<T>
    extends AbstractThreadSafeLoadableDetachableModel<
        T, SessionThreadSafeSimpleLoadableDetachableModel<T>.ThreadContextImpl> {

  private static final long serialVersionUID = 6859907414385876596L;

  protected class ThreadContextImpl extends LoadableDetachableModelThreadContext<T> {}

  public SessionThreadSafeSimpleLoadableDetachableModel() {}

  public SessionThreadSafeSimpleLoadableDetachableModel(T object) {
    setObject(object);
  }

  @Override
  protected final ThreadContextImpl newThreadContext() {
    return new ThreadContextImpl();
  }

  @Override
  protected final T load(ThreadContextImpl threadContext) {
    return load();
  }

  /** Loads the model object value from the implementation-defined data source. */
  protected abstract T load();

  @Override
  protected final void onSetObject(ThreadContextImpl threadContext) {
    save(threadContext.getTransientModelObject());
  }

  protected void save(T object) {
    // Does nothing by default
  }

  @Override
  protected final void onDetach(ThreadContextImpl threadContext) {
    onDetach();
  }

  protected void onDetach() {
    // Does nothing by default
  }

  @Override
  protected void onDetachDetached() {
    // Does nothing by default
  }
}
