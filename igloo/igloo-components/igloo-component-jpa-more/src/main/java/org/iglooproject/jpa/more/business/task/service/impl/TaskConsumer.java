package org.iglooproject.jpa.more.business.task.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import org.iglooproject.infinispan.model.ILock;
import org.iglooproject.infinispan.model.IPriorityQueue;
import org.iglooproject.spring.util.SpringBeanUtils;

public final class TaskConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskConsumer.class);

	private static final String THREAD_NAME_FORMAT = "TaskConsumer-%1$s-%2$s";

	@Autowired
	private ApplicationContext applicationContext;

	private final TaskQueue queue;
	
	private final int threadIdForThisQueue;
	
	private ConsumerThread thread;

	private final ILock lock;
	private final IPriorityQueue priorityQueue;

	public TaskConsumer(TaskQueue queue, int threadIdForThisQueue) {
		this(queue, threadIdForThisQueue, null, null);
	}

	public TaskConsumer(TaskQueue queue, int threadIdForThisQueue, ILock lock, IPriorityQueue priorityQueue) {
		Assert.notNull(queue, "[Assertion failed] - this argument is required; it must not be null");
		this.queue = queue;
		this.threadIdForThisQueue = threadIdForThisQueue;
		this.lock = lock;
		this.priorityQueue = priorityQueue;
	}

	public TaskQueue getQueue() {
		return queue;
	}

	public void start() {
		start(0L);
	}

	/**
	 * @param startDelay A length of time the consumer thread will wait before its first access to the task queue.
	 */
	public synchronized void start(long startDelay) {
		if (thread == null || !thread.isAlive()) { // synchronized access
			thread = null;
			String id = String.format(THREAD_NAME_FORMAT, queue.getId(), threadIdForThisQueue);
			if (lock != null) {
				thread = new ConsumerInfinispanAwareThread(
						id,
						startDelay,
						queue,
						lock, priorityQueue);
			} else {
				thread = new ConsumerThread(
						id,
						startDelay,
						queue
				);
			}
			SpringBeanUtils.autowireBean(applicationContext, thread);
			thread.start();
		}
	}

	public synchronized void stop(long stopTimeout) {
		if (thread != null) { // synchronized access
			thread.stop(stopTimeout);
		}
	}

	public synchronized void joinThread() throws InterruptedException {
		if (thread != null) {
			thread.join();
		} else {
			LOGGER.warn("joinThread called as thread is not present");
		}
	}
	
	public boolean isWorking() {
		return thread != null && thread.isWorking();
	}
	
	public boolean isActive(){
		return thread != null && thread.isActive();
	}

	public String getName() {
		return thread != null ? thread.getName() : "ABSENT";
	}
}