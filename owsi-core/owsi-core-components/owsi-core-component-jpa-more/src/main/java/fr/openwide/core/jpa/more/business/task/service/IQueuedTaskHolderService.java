package fr.openwide.core.jpa.more.business.task.service;

import java.util.Date;
import java.util.List;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;

public interface IQueuedTaskHolderService extends IGenericEntityService<Long, QueuedTaskHolder> {

	Long count(Date since, TaskStatus... statuses);

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
