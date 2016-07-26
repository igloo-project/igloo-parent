package fr.openwide.core.jpa.more.business.task.service.impl;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

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
	
	private Thread thread;

	private AtomicBoolean active = new AtomicBoolean(false);

	private AtomicBoolean working = new AtomicBoolean(false);

	public TaskConsumer(TaskQueue queue, int threadIdForThisQueue) {
		super();
		Assert.notNull(queue);
		this.queue = queue;
		this.threadIdForThisQueue = threadIdForThisQueue;
	}

	public boolean isWorking() {
		return working.get();
	}

	public synchronized void start() {
		if (!active.get()) {
			if (thread == null) {
				thread = new Thread(new ConsumerRunnable(), String.format(THREAD_NAME_FORMAT, queue.getId(), threadIdForThisQueue));
			}
			active.set(true);
			thread.start();
		}
	}

	public synchronized void stop(int stopTimeout) {
		/*
		 * Signal the process consumer that it will be asked to stop in
		 * a few moment
		 */
		active.set(false);
		
		try {
			/*
			 * If a queued task holder is currently active, we wait for it
			 */
			long timeRemaining = stopTimeout;
			while (timeRemaining > 0 && isWorking()) {
				/*
				 * Wait for small durations of time, so that we'll stop waiting as
				 * soon as the working thread stops even if the timeout is huge.
				 */
				long step = Longs.min(100L, timeRemaining);
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
			 * The current thread (the one waiting for the working thread) was interrupted
			 * Just put back the interrupt marker on the current thread before we interrupt the working thread
			 */
			Thread.currentThread().interrupt();
		} finally {
			if (thread != null) {
				thread.interrupt();
				thread = null;
				working.set(false);
			}
		}
	}
	
	private class ConsumerRunnable implements Runnable {
		@Override
		public void run() {
			Long queuedTaskHolderId = null;
			QueuedTaskHolder queuedTaskHolder = null;
			
			try {
				/*
				 * Needed because server startup can hangs during bean initialization at these places :
				 * org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinitionNames()
				 * org.springframework.beans.factory.support.DefaultSingletonBeanRegistry (somewhere)
				 * By delaying the queue startup, no hang
				 */
				Thread.sleep(10000);
				/*
				 * condition: permits thread to finish gracefully (stop was
				 * signaled, last taken element had been consumed, we can
				 * stop without any other action)
				 */
				while (active.get() && !Thread.currentThread().isInterrupted()) {
					queuedTaskHolderId = queue.take();
					working.set(true);
					
					entityManagerUtils.openEntityManager();
					queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);
					if(queuedTaskHolder != null) {
						tryConsumeTask(queuedTaskHolder);
					}
					entityManagerUtils.closeEntityManager();
					/*
					 * dereferencing queued task holder else it will be marked
					 * interrupted by catch block if InterruptedException is
					 * thrown during the next take()
					 */
					queuedTaskHolder = null;
					working.set(false);
				}
			} catch (InterruptedException ignored) {
				// Do nothing, just stop taking tasks
			} finally {
				working.set(false);
			}
		}
		
		/**
		 * TOTALLY safe, NEVER throws any exception.
		 */
		private void tryConsumeTask(QueuedTaskHolder queuedTaskHolder) {
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
			}
		}
	}
}