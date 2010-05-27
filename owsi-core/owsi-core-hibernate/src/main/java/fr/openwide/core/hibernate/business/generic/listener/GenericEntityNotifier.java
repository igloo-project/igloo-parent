package fr.openwide.core.hibernate.business.generic.listener;

import java.util.HashSet;
import java.util.Set;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;

public class GenericEntityNotifier<T extends GenericEntity<?>, U extends GenericEntityListener<T>> {

	protected Set<U> listeners = new HashSet<U>();

	public void subscribe(U listener) {
		this.listeners.add(listener);
	}

	public void unsubscribe(U listener) {
		if (this.listeners.contains(listener)) {
			this.listeners.remove(listener);
		}
	}
	
	public void onCreate(T entity) throws ServiceException, SecurityServiceException {
		for (U listener : listeners) {
			listener.onCreate(entity);
		}
	}
	
	public void onUpdate(T entity) throws ServiceException, SecurityServiceException {
		for (U listener : listeners) {
			listener.onUpdate(entity);
		}
	}
	
	public void onDelete(T entity) throws ServiceException, SecurityServiceException {
		for (U listener : listeners) {
			listener.onDelete(entity);
		}
	}

}
