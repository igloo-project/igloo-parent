package org.iglooproject.basicapp.core.business.message.service;

import java.util.Date;
import java.util.List;

import org.iglooproject.basicapp.core.business.message.model.GeneralMessage;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;

public interface IGeneralMessageService extends IGenericEntityService<Long, GeneralMessage> {

	public List<GeneralMessage> listActiveMessages();

	public boolean newMessages();

	public Date getMostRecentPublicationStartDate();
}
