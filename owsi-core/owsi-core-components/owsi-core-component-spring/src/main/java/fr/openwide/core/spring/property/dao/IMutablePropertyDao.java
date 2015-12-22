package fr.openwide.core.spring.property.dao;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface IMutablePropertyDao {

	String getInTransaction(String key);

	void setInTransaction(String key, String value) throws ServiceException, SecurityServiceException;

	void cleanInTransaction();

}
