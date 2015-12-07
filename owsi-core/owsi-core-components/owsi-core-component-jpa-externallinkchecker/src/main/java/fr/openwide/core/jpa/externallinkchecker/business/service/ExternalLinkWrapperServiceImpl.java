package fr.openwide.core.jpa.externallinkchecker.business.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.externallinkchecker.business.dao.IExternalLinkWrapperDao;
import fr.openwide.core.jpa.externallinkchecker.business.model.ExternalLinkStatus;
import fr.openwide.core.jpa.externallinkchecker.business.model.ExternalLinkWrapper;

@Service("externalLinkWrapperService")
public class ExternalLinkWrapperServiceImpl extends GenericEntityServiceImpl<Long, ExternalLinkWrapper> implements IExternalLinkWrapperService {
	
	private IExternalLinkWrapperDao dao;
	
	@Autowired
	public ExternalLinkWrapperServiceImpl(IExternalLinkWrapperDao externalLinkWrapperDao) {
		super(externalLinkWrapperDao);
		
		this.dao = externalLinkWrapperDao;
	}
	
	@Override
	public List<ExternalLinkWrapper> listByIds(Collection<Long> ids) {
		return dao.listByIds(ids);
	}

	@Override
	public List<ExternalLinkWrapper> listActive() {
		return dao.listActive();
	}
	
	@Override
	public List<ExternalLinkWrapper> listNextCheckingBatch(int batchSize, int minDelayBetweenTwoChecks) {
		return dao.listNextCheckingBatch(batchSize, minDelayBetweenTwoChecks);
	}
	
	@Override
	public void resetLinksFromIds(Collection<Long> ids) throws ServiceException, SecurityServiceException {
		resetLinksFromUrls(dao.listUrlsFromIds(ids));
	}
	
	@Override
	public void resetLinksFromStatuses(Collection<ExternalLinkStatus> statuses)
			throws ServiceException, SecurityServiceException {
		resetLinksFromUrls(dao.listUrlsFromStatuses(statuses));
	}
	
	@Override
	public void resetLinksFromUrls(Collection<String> urls) throws ServiceException, SecurityServiceException {
		for (ExternalLinkWrapper wrapper : dao.listFromUrls(urls)) {
			wrapper.resetStatus();
			update(wrapper);
		}
	}
}