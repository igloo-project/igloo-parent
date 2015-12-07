package fr.openwide.core.jpa.externallinkchecker.business.service;

import java.util.Collection;
import java.util.List;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.externallinkchecker.business.model.ExternalLinkStatus;
import fr.openwide.core.jpa.externallinkchecker.business.model.ExternalLinkWrapper;

public interface IExternalLinkWrapperService extends IGenericEntityService<Long, ExternalLinkWrapper> {
	
	List<ExternalLinkWrapper> listByIds(Collection<Long> ids);
	
	List<ExternalLinkWrapper> listActive();

	List<ExternalLinkWrapper> listNextCheckingBatch(int batchSize, int minDelayBetweenTwoChecks);

	void resetLinksFromIds(Collection<Long> ids) throws ServiceException, SecurityServiceException;

	void resetLinksFromStatuses(Collection<ExternalLinkStatus> statuses) throws ServiceException, SecurityServiceException;

	void resetLinksFromUrls(Collection<String> urls) throws ServiceException, SecurityServiceException;
}
