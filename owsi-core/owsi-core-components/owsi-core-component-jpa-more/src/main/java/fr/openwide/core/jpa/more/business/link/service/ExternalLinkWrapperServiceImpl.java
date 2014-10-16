package fr.openwide.core.jpa.more.business.link.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.link.dao.IExternalLinkWrapperDao;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkStatus;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;

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
	public List<ExternalLinkWrapper> listNextCheckingBatch(int batchSize) {
		return dao.listNextCheckingBatch(batchSize);
	}
	
	@Override
	public Set<ExternalLinkWrapper> resetLinksFromIds(Collection<Long> ids) throws ServiceException, SecurityServiceException {
		return resetLinksFromUrls(dao.listUrlsFromIds(ids));
	}
	
	@Override
	public Set<ExternalLinkWrapper> resetLinksFromStatuses(Collection<ExternalLinkStatus> statuses)
			throws ServiceException, SecurityServiceException {
		return resetLinksFromUrls(dao.listUrlsFromStatuses(statuses));
	}
	
	@Override
	public Set<ExternalLinkWrapper> resetLinksFromUrls(Collection<String> urls) throws ServiceException, SecurityServiceException {
		Set<ExternalLinkWrapper> resetLinks = Sets.newHashSet();
		for (ExternalLinkWrapper wrapper : dao.listFromUrls(urls)) {
			wrapper.resetStatus();
			update(wrapper);
			resetLinks.add(wrapper);
		}
		return resetLinks;
	}
}