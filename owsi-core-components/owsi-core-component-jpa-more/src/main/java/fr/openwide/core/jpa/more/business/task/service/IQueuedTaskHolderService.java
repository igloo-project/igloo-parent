package fr.openwide.core.jpa.more.business.task.service;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;

public interface IQueuedTaskHolderService extends IGenericEntityService<Long, QueuedTaskHolder> {

	QueuedTaskHolder getNextTaskForExecution(String taskType);
	
	QueuedTaskHolder getRandomStalledTask(String taskType, int executionTimeLimitInSeconds);
}
