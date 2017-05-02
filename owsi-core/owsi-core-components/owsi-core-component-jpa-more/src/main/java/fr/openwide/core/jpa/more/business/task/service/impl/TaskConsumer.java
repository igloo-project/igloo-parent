package fr.openwide.core.jpa.more.business.task.service.impl;

import org.springframework.util.Assert;

import fr.openwide.core.infinispan.model.ILock;
import fr.openwide.core.infinispan.model.IPriorityQueue;

public final class TaskConsumer {

	private static final String THREAD_NAME_FORMAT = "TaskConsumer-%1$s-%2$s";

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
			String id = String.format(THREAD_NAME_FORMAT, queue.getId(), threadIdForThisQueue);
			if (lock != null) {
				thread = new ConsumerThread(
						id,
						startDelay,
						queue
				);
			} else {
				thread = new ConsumerInfinispanAwareThread(
						id,
						startDelay,
						queue,
						lock, priorityQueue);
			}
			thread.start();
		}
	}

	public synchronized void stop(long stopTimeout) {
		if (thread != null) { // synchronized access
			thread.stop(stopTimeout);
			thread = null;
		}
	}
	
	public boolean isWorking() {
		return thread != null && thread.isWorking();
	}
}
