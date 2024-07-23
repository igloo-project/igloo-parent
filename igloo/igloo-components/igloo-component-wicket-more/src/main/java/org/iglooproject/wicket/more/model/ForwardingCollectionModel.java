package org.iglooproject.wicket.more.model;

import igloo.wicket.model.ICollectionModel;
import java.util.Collection;
import java.util.Iterator;
import org.apache.wicket.model.IModel;

public abstract class ForwardingCollectionModel<T, C extends Collection<T>>
    implements ICollectionModel<T, C> {

  private static final long serialVersionUID = 7663063141211550353L;

  protected abstract ICollectionModel<T, C> delegate();

  @Override
  public C getObject() {
    return delegate().getObject();
  }

  @Override
  public void setObject(C object) {
    delegate().setObject(object);
  }

  @Override
  public void detach() {
    delegate().detach();
  }

  @Override
  public Iterator<? extends IModel<T>> iterator(long offset, long limit) {
    return delegate().iterator(offset, limit);
  }

  @Override
  public long size() {
    return delegate().size();
  }

  @Override
  public void add(T item) {
    delegate().add(item);
  }

  @Override
  public void remove(T item) {
    delegate().remove(item);
  }

  @Override
  public void clear() {
    delegate().clear();
  }
}
