package org.iglooproject.basicapp.core.business.message.service;

import java.util.Date;
import java.util.List;

import org.iglooproject.basicapp.core.business.history.service.IHistoryEventSummaryService;
import org.iglooproject.basicapp.core.business.message.dao.IGeneralMessageDao;
import org.iglooproject.basicapp.core.business.message.model.GeneralMessage;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralMessageServiceImpl extends GenericEntityServiceImpl<Long, GeneralMessage> implements IGeneralMessageService {

	private IGeneralMessageDao dao;

	@Autowired
	private IHistoryEventSummaryService historyEventSummaryService;

	public GeneralMessageServiceImpl(IGeneralMessageDao dao) {
		super(dao);
		this.dao = dao;
	}

	@Override
	protected void createEntity(GeneralMessage entity) throws ServiceException, SecurityServiceException {
		historyEventSummaryService.refresh(entity.getCreation());
		historyEventSummaryService.refresh(entity.getModification());
		super.createEntity(entity);
	}

	@Override
	protected void updateEntity(GeneralMessage entity) throws ServiceException, SecurityServiceException {
		historyEventSummaryService.refresh(entity.getModification());
		super.updateEntity(entity);
	}

	@Override
	public List<GeneralMessage> listActiveMessages() {
		return dao.listActiveMessages();
	}

	@Override
	public boolean newMessages() {
		return !listActiveMessages().isEmpty();
	}

	@Override
	public Date getMostRecentPublicationStartDate() {
		return dao.getMostRecentPublicationStartDate();
	}

}
