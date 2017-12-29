package org.iglooproject.showcase.core.business.task.service;

import java.util.List;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.showcase.core.business.task.model.search.TaskSearchQueryParameters;

public interface IShowcaseTaskService extends IGenericEntityService<Long, QueuedTaskHolder> {

	List<QueuedTaskHolder> search(TaskSearchQueryParameters searchParameters, Long count, Long first)
			throws ServiceException;

	long count(TaskSearchQueryParameters searchParameters) throws ServiceException;
}
