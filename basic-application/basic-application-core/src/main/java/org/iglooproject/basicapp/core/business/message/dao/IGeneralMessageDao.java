package org.iglooproject.basicapp.core.business.message.dao;

import java.util.Date;
import java.util.List;

import org.iglooproject.basicapp.core.business.message.model.GeneralMessage;
import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;

public interface IGeneralMessageDao extends IGenericEntityDao<Long, GeneralMessage> {

	public List<GeneralMessage> listActiveMessages();

	public Date getMostRecentPublicationStartDate();

}
