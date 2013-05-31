package fr.openwide.core.jpa.more.business.task.service;

import java.util.concurrent.BlockingQueue;


public interface IQueuedTaskHolderConsumer extends Runnable {

	void setQueue(BlockingQueue<Long> queue);
	
	boolean isActive();

	boolean isWorking();

	void stop();

	void start();
}
