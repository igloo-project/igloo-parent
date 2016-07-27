package fr.openwide.core.jpa.more.business.task.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Longs;

import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;
import fr.openwide.core.jpa.more.config.spring.AbstractTaskManagementConfig;
import fr.openwide.core.jpa.util.EntityManagerUtils;
import fr.openwide.core.spring.util.SpringBeanUtils;

public final class TaskConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskConsumer.class);

	private static final String THREAD_NAME_FORMAT = "TaskConsumer-%1$s-%2$s";

	private static final long MAX_STOP_TIMEOUT_WAIT_INCREMENT_MS = 100L;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private IQueuedTaskHolderService queuedTaskHolderService;

	@Autowired
	@Qualifier(AbstractTaskManagementConfig.OBJECT_MAPPER_BEAN_NAME)
	private ObjectMapper queuedTaskHolderObjectMapper;

	@Autowired
	private EntityManagerUtils entityManagerUtils;

	private final TaskQueue queue;
	
	private final int threadIdForThisQueue;
	
	private ConsumerThread thread;

	public TaskConsumer(TaskQueue queue, int threadIdForThisQueue) {
		super();
		Assert.notNull(queue);
		this.queue = queue;
		this.threadIdForThisQueue = threadIdForThisQueue;
	}

	public void start() {
		start(0L);
	}

	/**
	 * @param startDelay A length of time the consumer thread will wait before its first access to the task queue.
	 */
	public synchronized void start(long startDelay) {
		if (thread == null || !thread.isAlive()) { // synchronized access
			thread = new ConsumerThread(
					String.format(THREAD_NAME_FORMAT, queue.getId(), threadIdForThisQueue),
					startDelay
			);
			thread.start();
		}
	}

	public synchronized void stop(long stopTimeout) {
		if (thread != null) { // synchronized access
			thread.stop(stopTimeout);
			thread = null;
		}
	}
	
	/**
	 * A consumer thread with the ability to try stopping gracefully.
	 * @see #stop(int)
	 */
	private class ConsumerThread extends Thread {
		/**
		 * A flag indicating whether new tasks should be consumed.
		 */
		private volatile boolean active = false;
		/**
		 * A flag indicating whether the thread is currently executing a task.
		 * <p>Differs from <code>isAlive()</code> in that <code>isAlive()</code> returns true even if the thread is
		 * only waiting for a task to be offered in the queue.
		 */
		private volatile boolean working = false;
		
		private final long startDelay;

		public ConsumerThread(String name, long startDelay) {
			super(name);
			this.startDelay = startDelay;
		}
		
		@Override
		public synchronized void start() {
			active = true;
			try {
				super.start();
			} catch (RuntimeException e) {
				active = false;
				throw e;
			}
		}

		public void stop(long stopTimeout) {
			/*
			 * Signal the run() method that it should not consume any more tasks.
			 */
			active = false;
			
			try {
				/*
				 * If a task is currently being handled, we wait for it to complete within the given time limit.
				 */
				long timeRemaining = stopTimeout;
				while (timeRemaining > 0 && working) {
					/*
					 * Wait for small durations of time, so that we'll stop waiting as
					 * soon as the consumer thread stops even if the timeout is huge.
					 */
					long step = Longs.min(MAX_STOP_TIMEOUT_WAIT_INCREMENT_MS, timeRemaining);
					Thread.sleep(step); // NOSONAR findbugs:SWL_SLEEP_WITH_LOCK_HELD
					/*
					 * Sleep in synchronized method does not harm because there is no
					 * high concurrency on this method (we simply don't want concurrent
					 * execution)
					 */
					timeRemaining -= step;
				}
			} catch (InterruptedException e) {
				/*
				 * The current thread (the one waiting for the consumer thread) was interrupted
				 * Just put back the interrupt marker on the current thread before we interrupt the consumer thread
				 */
				Thread.currentThread().interrupt();
			} finally {
				/*
				 * If the current thread (the one waiting for the consumer thread) was interrupted, or if the timeout
				 * was reached when waiting for the task to complete, or if the task completed in time, we order the
				 * consumer thread to stop ASAP.
				 */
				this.interrupt();
			}
		}
		
		@Override
		public void run() {
			try {
				if (startDelay > 0) {
					Thread.sleep(startDelay);
				}
				/*
				 * condition: permits thread to finish gracefully (stop was
				 * signaled, last taken element had been consumed, we can
				 * stop without any other action)
				 */
				while (active && !Thread.currentThread().isInterrupted()) {
					Long queuedTaskHolderId = queue.take();
					this.working = true;
					try {
						entityManagerUtils.openEntityManager();
						try {
							tryConsumeTask(queuedTaskHolderId);
						} finally {
							entityManagerUtils.closeEntityManager();
						}
					} finally {
						this.working = false;
					}
				}
			} catch (InterruptedException ignored) {
				// Do nothing, just stop taking tasks
			}
		}
		
		/**
		 * TOTALLY safe, NEVER throws any exception.
		 */
		private void tryConsumeTask(Long queuedTaskHolderId) {
			QueuedTaskHolder queuedTaskHolder;
			try {
				queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);
			} catch (RuntimeException e) {
				LOGGER.error("Error while trying to fetch a task from database before run (holder: " + queuedTaskHolderId + ").", e);
				return;
			}
			
			AbstractTask runnableTask;
			try {
				runnableTask = queuedTaskHolderObjectMapper.readValue(queuedTaskHolder.getSerializedTask(), AbstractTask.class);
			} catch (RuntimeException | IOException e) {
				LOGGER.error("Error while trying to deserialize a task before run (holder: " + queuedTaskHolder + ").", e);
				return;
			}

			try {
				runnableTask.setQueuedTaskHolderId(queuedTaskHolder.getId());
				SpringBeanUtils.autowireBean(applicationContext, runnableTask);
			} catch (RuntimeException e) {
				LOGGER.error("Error while trying to initialize a task before run (holder: " + queuedTaskHolder + ").", e);
				return;
			}
			
			try {
				runnableTask.run();
			} catch (RuntimeException e) {
				LOGGER.error("Error while trying to consume a task (holder: " + queuedTaskHolder + "); the task holder was probably left in a stale state.", e);
				return;
			}
		}
	}
}