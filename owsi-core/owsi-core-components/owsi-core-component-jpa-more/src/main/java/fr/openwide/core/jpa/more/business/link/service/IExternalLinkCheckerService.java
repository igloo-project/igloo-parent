package fr.openwide.core.jpa.more.business.link.service;

import java.util.Collection;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;

public interface IExternalLinkCheckerService extends ITransactionalAspectAwareService {
	
	void checkBatch() throws ServiceException, SecurityServiceException;
	
	void checkLink(ExternalLinkWrapper link) throws ServiceException, SecurityServiceException;

	void checkLinksWithSameUrl(String url, Collection<ExternalLinkWrapper> link) throws ServiceException, SecurityServiceException;
	
}
