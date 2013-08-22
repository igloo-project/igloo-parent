package fr.openwide.core.jpa.more.business.task.service;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.AbstractTask;


public interface IQueuedTaskHolderManager {

	void init();
	
	boolean isAvailableForAction();

	boolean isActive();

	Integer getNumberOfWaitingTasks();
	
	void start();
	
	void submit(AbstractTask task);
	
	void reload(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException;
	
	void destroy();
	
	void stop();
}
