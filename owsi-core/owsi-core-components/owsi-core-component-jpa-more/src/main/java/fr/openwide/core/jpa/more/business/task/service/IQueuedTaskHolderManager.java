package fr.openwide.core.jpa.more.business.task.service;

import fr.openwide.core.jpa.more.business.task.model.AbstractTask;


public interface IQueuedTaskHolderManager {

	void init();
	
	boolean isAvailableForAction();

	boolean isActive();

	Integer getNumberOfWaitingTaks();
	
	void start();
	
	void submit(AbstractTask task);
	
	void destroy();
	
	void stop();
}
