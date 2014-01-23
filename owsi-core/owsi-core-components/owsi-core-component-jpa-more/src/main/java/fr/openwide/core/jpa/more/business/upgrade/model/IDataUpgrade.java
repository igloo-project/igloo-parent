package fr.openwide.core.jpa.more.business.upgrade.model;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface IDataUpgrade {
	
	/**
	 * @return Le nom (unique) de cette mise à jour.
	 */
	String getName();
	
	void perform() throws ServiceException, SecurityServiceException;

}
