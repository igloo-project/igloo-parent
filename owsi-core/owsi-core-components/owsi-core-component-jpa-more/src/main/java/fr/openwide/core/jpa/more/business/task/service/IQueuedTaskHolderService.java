package fr.openwide.core.jpa.more.business.task.service;

import java.util.Date;
import java.util.List;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.search.QueuedTaskHolderSearchQueryParameters;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;

public interface IQueuedTaskHolderService extends IGenericEntityService<Long, QueuedTaskHolder> {

	List<QueuedTaskHolder> search(QueuedTaskHolderSearchQueryParameters searchParams, Long limit, Long offset)
			throws ServiceException;

	int count(QueuedTaskHolderSearchQueryParameters searchParams) throws ServiceException;

	Long count(Date since, TaskStatus... statuses);

	Long count(TaskStatus... statuses);
	
	QueuedTaskHolder getNextTaskForExecution(String taskType);

	QueuedTaskHolder getRandomStalledTask(String taskType, int executionTimeLimitInSeconds);

	List<QueuedTaskHolder> listConsumable();
}
