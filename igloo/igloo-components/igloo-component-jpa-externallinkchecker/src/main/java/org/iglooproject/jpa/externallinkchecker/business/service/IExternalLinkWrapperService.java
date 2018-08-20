package org.iglooproject.jpa.externallinkchecker.business.service;

import java.util.Collection;
import java.util.List;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkStatus;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkWrapper;

public interface IExternalLinkWrapperService extends IGenericEntityService<Long, ExternalLinkWrapper> {
	
	List<ExternalLinkWrapper> listByIds(Collection<Long> ids);
	
	List<ExternalLinkWrapper> listActive();

	List<ExternalLinkWrapper> listNextCheckingBatch(int batchSize, int minDelayBetweenTwoChecks);

	void resetLinksFromIds(Collection<Long> ids) throws ServiceException, SecurityServiceException;

	void resetLinksFromStatuses(Collection<ExternalLinkStatus> statuses) throws ServiceException, SecurityServiceException;

	void resetLinksFromUrls(Collection<String> urls) throws ServiceException, SecurityServiceException;
}
