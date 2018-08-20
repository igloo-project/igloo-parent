package org.iglooproject.jpa.externallinkchecker.business.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.externallinkchecker.business.dao.IExternalLinkWrapperDao;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkStatus;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkWrapper;

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