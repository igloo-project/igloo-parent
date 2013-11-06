package fr.openwide.core.jpa.more.business.link.service;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;

public interface IExternalLinkCheckerService extends ITransactionalAspectAwareService {
	
	void checkAllLinks() throws ServiceException, SecurityServiceException;
	
	void checkLink(ExternalLinkWrapper link) throws ServiceException, SecurityServiceException;
}
