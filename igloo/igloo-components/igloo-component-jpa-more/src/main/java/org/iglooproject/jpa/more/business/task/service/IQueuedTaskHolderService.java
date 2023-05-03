package org.iglooproject.jpa.more.business.task.service;

import java.time.Instant;
import java.util.List;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;

public interface IQueuedTaskHolderService extends IGenericEntityService<Long, QueuedTaskHolder> {

	Long count(Instant since, TaskStatus... statuses);

	Long count(TaskStatus... statuses);

	QueuedTaskHolder getNextTaskForExecution(String taskType);

	QueuedTaskHolder getRandomStalledTask(String taskType, int executionTimeLimitInSeconds);

	List<String> listTypes();

	boolean isReloadable(QueuedTaskHolder task);

	boolean isCancellable(QueuedTaskHolder task);

	/**
	 * Gets the tasks that may be run
	 * @param queueId
	 * @return
	 * @throws ServiceException
	 * @throws SecurityServiceException
	 */
	List<QueuedTaskHolder> getListConsumable(String queueId) throws ServiceException, SecurityServiceException;
}
