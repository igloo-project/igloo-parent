package org.iglooproject.jpa.externallinkchecker.business.dao;

import java.util.Collection;
import java.util.List;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkStatus;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkWrapper;

public interface IExternalLinkWrapperDao extends IGenericEntityDao<Long, ExternalLinkWrapper> {
	
	List<ExternalLinkWrapper> listByIds(Collection<Long> ids);

	List<ExternalLinkWrapper> listActive();

	/**
	 * @return A list of ExternalLinkWrappers containing at most <code>batchSize</code> urls.
	 * The status of the returned ExternalLinkWrapper is not in {@link ExternalLinkStatus#INACTIVES}. 
	 */
	List<ExternalLinkWrapper> listNextCheckingBatch(int batchSize, int minDelayBetweenTwoChecks);

	List<String> listUrlsFromIds(Collection<Long> ids);

	List<String> listUrlsFromStatuses(Collection<ExternalLinkStatus> statuses);

	List<ExternalLinkWrapper> listFromUrls(Collection<String> urls);

}
