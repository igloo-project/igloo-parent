package fr.openwide.core.jpa.more.business.link.dao;

import java.util.Collection;
import java.util.List;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkStatus;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;

public interface IExternalLinkWrapperDao extends IGenericEntityDao<Long, ExternalLinkWrapper> {
	
	List<ExternalLinkWrapper> listByIds(Collection<Long> ids);

	List<ExternalLinkWrapper> listActive();

	/**
	 * @return A list of ExternalLinkWrappers containing at most <code>batchSize</code> urls.
	 * The status of the returned ExternalLinkWrapper is anything but {@link ExternalLinkStatus#DEAD_LINK} or {@link ExternalLinkStatus#DELETED}. 
	 */
	List<ExternalLinkWrapper> listNextCheckingBatch(int batchSize);

}
