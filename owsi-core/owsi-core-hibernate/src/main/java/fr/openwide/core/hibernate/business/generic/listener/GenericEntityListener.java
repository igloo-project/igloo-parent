package fr.openwide.core.hibernate.business.generic.listener;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;

public interface GenericEntityListener<T extends GenericEntity<?>> {
	
	void onCreate(T entity) throws ServiceException, SecurityServiceException;
	
	void onUpdate(T entity) throws ServiceException, SecurityServiceException;
	
	void onDelete(T entity) throws ServiceException, SecurityServiceException;
	
}
