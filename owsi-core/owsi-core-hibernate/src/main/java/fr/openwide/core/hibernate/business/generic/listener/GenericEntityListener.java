package fr.openwide.core.hibernate.business.generic.listener;

import java.io.Serializable;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;

public interface GenericEntityListener<K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>> {
	
	void onCreate(E entity) throws ServiceException, SecurityServiceException;
	
	void onUpdate(E entity) throws ServiceException, SecurityServiceException;
	
	void onDelete(E entity) throws ServiceException, SecurityServiceException;
	
}
