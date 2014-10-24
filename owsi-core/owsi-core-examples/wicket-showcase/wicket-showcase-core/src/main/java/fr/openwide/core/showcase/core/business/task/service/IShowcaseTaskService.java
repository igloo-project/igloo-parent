package fr.openwide.core.showcase.core.business.task.service;

import java.util.List;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.showcase.core.business.task.model.search.TaskSearchQueryParameters;

public interface IShowcaseTaskService extends IGenericEntityService<Long, QueuedTaskHolder> {

	List<QueuedTaskHolder> search(TaskSearchQueryParameters searchParameters, Long count, Long first)
			throws ServiceException;

	long count(TaskSearchQueryParameters searchParameters) throws ServiceException;
}
