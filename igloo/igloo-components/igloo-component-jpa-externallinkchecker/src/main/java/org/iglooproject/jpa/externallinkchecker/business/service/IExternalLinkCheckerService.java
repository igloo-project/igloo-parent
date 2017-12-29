package org.iglooproject.jpa.externallinkchecker.business.service;

import io.mola.galimatias.URL;

import java.util.Collection;
import java.util.regex.Pattern;

import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkWrapper;

public interface IExternalLinkCheckerService extends ITransactionalAspectAwareService {
	
	void checkBatch() throws ServiceException, SecurityServiceException;
	
	void checkLink(ExternalLinkWrapper link) throws ServiceException, SecurityServiceException;

	void checkLinksWithSameUrl(URL url, Collection<ExternalLinkWrapper> links) throws ServiceException,
			SecurityServiceException;

	void addIgnorePattern(Pattern ignorePattern);

}
