package fr.openwide.core.jpa.more.business.property.dao;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface IMutablePropertyDao {

	String get(String key);

	void set(String key, String value) throws ServiceException, SecurityServiceException;

	void clean();

}
