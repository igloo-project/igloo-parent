package fr.openwide.core.jpa.more.business.task.service;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.AbstractTask;


public interface IQueuedTaskHolderManager {
	
	String DEFAULT_QUEUE_ID = "&__DEFAULT__DO_NOT_USE_THIS_ID_FOR_CUSTOM_QUEUES__&";

	void init();
	
	boolean isAvailableForAction();

	boolean isActive();

	int getNumberOfWaitingTasks();
	
	void start();
	
	void submit(AbstractTask task) throws ServiceException;
	
	void reload(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException;
	
	void cancel(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException;
	
	void destroy();
	
	void stop();

}
