package fr.openwide.core.jpa.more.business.link.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.more.business.link.dao.IExternalLinkWrapperDao;
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
	public List<ExternalLinkWrapper> listActive() {
		return dao.listActive();
	}
}