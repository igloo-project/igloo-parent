package fr.openwide.core.jpa.business.generic.listener;

import java.io.Serializable;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface GenericEntityListener<K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>> {
	
	void onCreate(E entity) throws ServiceException, SecurityServiceException;
	
	void onUpdate(E entity) throws ServiceException, SecurityServiceException;
	
	void onDelete(E entity) throws ServiceException, SecurityServiceException;
	
}
