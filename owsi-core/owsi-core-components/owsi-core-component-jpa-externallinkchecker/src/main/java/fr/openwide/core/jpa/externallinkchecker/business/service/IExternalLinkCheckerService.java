package fr.openwide.core.jpa.externallinkchecker.business.service;

import io.mola.galimatias.URL;

import java.util.Collection;
import java.util.regex.Pattern;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.externallinkchecker.business.model.ExternalLinkWrapper;

public interface IExternalLinkCheckerService extends ITransactionalAspectAwareService {
	
	void checkBatch() throws ServiceException, SecurityServiceException;
	
	void checkLink(ExternalLinkWrapper link) throws ServiceException, SecurityServiceException;

	void checkLinksWithSameUrl(URL url, Collection<ExternalLinkWrapper> links) throws ServiceException,
			SecurityServiceException;

	void addIgnorePattern(Pattern ignorePattern);

}
