package fr.openwide.core.jpa.more.business.task.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
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
		 * signal the process consumer that it will be asked to stop in
		 * a few moment
		 */
		active.set(false);
		
		try {
			/*
			 * if a queued task holder is currently active, we wait for it
			 */
			if (isWorking()) {
				long wait = 0;
				boolean interrupted = false;
				while ((wait < stopTimeout) && isWorking() && !interrupted) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						interrupted = true;
					}
					wait += 3000;
				}
			}
		} finally {
			if (thread != null) {
				thread.interrupt();
				thread = null;
				working.set(false);
			}
		}
	}

	private void consume(QueuedTaskHolder queuedTaskHolder) throws InterruptedException {
		try {
			AbstractTask runnableTask = queuedTaskHolderObjectMapper.readValue(queuedTaskHolder.getSerializedTask(), AbstractTask.class);
			runnableTask.setQueuedTaskHolderId(queuedTaskHolder.getId());
			SpringBeanUtils.autowireBean(applicationContext, runnableTask);
			runnableTask.run();
		} catch (IOException e) {
			LOGGER.error("Error while trying to deserialize task " + queuedTaskHolder, e);
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
					// We are in a different transaction so we need to check that the 
					// transaction which created the queued task holder has already been commited.
					// If not, we sleep for a while and we try again
					int counter = 0;
					while(active.get() && !Thread.currentThread().isInterrupted() && counter < 4) {
						queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);
						if(queuedTaskHolder != null) {
							consume(queuedTaskHolder);
							break;
						} else {
							Thread.sleep((2 + counter * 10) * 1000L);
							counter ++;
						}
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
			} catch (InterruptedException ex) {
				/*
				 * interrupt() method is null aware
				 */
				if (queuedTaskHolder != null) {
					queuedTaskHolder.setStatus(TaskStatus.INTERRUPTED);
					queuedTaskHolder.setEndDate(new Date());
					queuedTaskHolder.resetExecutionInformation();
				}
				
				working.set(false);
				
				return;
			}
		}
	}
}