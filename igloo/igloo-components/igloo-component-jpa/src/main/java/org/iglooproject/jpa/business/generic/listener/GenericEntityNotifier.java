package org.iglooproject.jpa.business.generic.listener;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public class GenericEntityNotifier<
    K extends Serializable & Comparable<K>,
    E extends GenericEntity<K, E>,
    L extends GenericEntityListener<K, E>> {

  protected Set<L> listeners = new HashSet<>();

  public void subscribe(L listener) {
    this.listeners.add(listener);
  }

  public void unsubscribe(L listener) {
    if (this.listeners.contains(listener)) {
      this.listeners.remove(listener);
    }
  }

  public void onCreate(E entity) throws ServiceException, SecurityServiceException {
    for (L listener : listeners) {
      listener.onCreate(entity);
    }
  }

  public void onUpdate(E entity) throws ServiceException, SecurityServiceException {
    for (L listener : listeners) {
      listener.onUpdate(entity);
    }
  }

  public void onDelete(E entity) throws ServiceException, SecurityServiceException {
    for (L listener : listeners) {
      listener.onDelete(entity);
    }
  }
}
