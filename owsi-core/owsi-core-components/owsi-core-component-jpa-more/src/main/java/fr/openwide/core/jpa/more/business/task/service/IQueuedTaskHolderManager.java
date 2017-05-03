package fr.openwide.core.jpa.more.business.task.service;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.IQueueId;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;


public interface IQueuedTaskHolderManager {
	
	boolean isAvailableForAction();

	boolean isActive();

	int getNumberOfWaitingTasks();

	int getNumberOfRunningTasks();

	Collection<IQueueId> getQueueIds();
	
	void start();
	
	void stop();
	
	@Transactional
	QueuedTaskHolder submit(AbstractTask task) throws ServiceException;

	@Transactional
	void reload(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException;

	@Transactional
	void cancel(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException;

	void onTaskFinish(Long id);

}
