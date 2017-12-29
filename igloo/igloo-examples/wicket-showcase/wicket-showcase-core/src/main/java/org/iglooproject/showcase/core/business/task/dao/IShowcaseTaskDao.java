package org.iglooproject.showcase.core.business.task.dao;

import java.util.List;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.showcase.core.business.task.model.search.TaskSearchQueryParameters;

public interface IShowcaseTaskDao extends IGenericEntityDao<Long, QueuedTaskHolder> {

	List<QueuedTaskHolder> search(TaskSearchQueryParameters searchParameters, Long limit, Long offset)
			throws ServiceException;

	long count(TaskSearchQueryParameters searchParameters) throws ServiceException;

}
