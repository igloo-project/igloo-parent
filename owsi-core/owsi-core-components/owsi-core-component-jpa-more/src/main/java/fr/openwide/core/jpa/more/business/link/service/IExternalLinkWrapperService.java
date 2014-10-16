package fr.openwide.core.jpa.more.business.link.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkStatus;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;

public interface IExternalLinkWrapperService extends IGenericEntityService<Long, ExternalLinkWrapper> {
	
	List<ExternalLinkWrapper> listByIds(Collection<Long> ids);
	
	List<ExternalLinkWrapper> listActive();

	List<ExternalLinkWrapper> listNextCheckingBatch(int batchSize);

	Set<ExternalLinkWrapper> resetLinksFromIds(Collection<Long> ids) throws ServiceException, SecurityServiceException;

	Set<ExternalLinkWrapper> resetLinksFromStatuses(Collection<ExternalLinkStatus> statuses)
			throws ServiceException, SecurityServiceException;

	Set<ExternalLinkWrapper> resetLinksFromUrls(Collection<String> urls) throws ServiceException, SecurityServiceException;
}
