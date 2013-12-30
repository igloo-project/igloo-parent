package fr.openwide.core.jpa.more.business.task.service;

import java.util.Collection;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.IQueueId;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;


public interface IQueuedTaskHolderManager {
	
	boolean isAvailableForAction();

	boolean isActive();

	int getNumberOfWaitingTasks();

	Collection<IQueueId> getQueueIds();
	
	void start();
	
	void stop();
	
	QueuedTaskHolder submit(AbstractTask task) throws ServiceException;
	
	void reload(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException;
	
	void cancel(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException;

}
