package fr.openwide.core.jpa.more.business.task.service;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.jpa.more.config.spring.JpaMoreTaskManagementConfig;
import fr.openwide.core.jpa.util.EntityManagerUtils;
import fr.openwide.core.spring.util.SpringBeanUtils;

public class QueuedTaskHolderConsumerImpl implements IQueuedTaskHolderConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueuedTaskHolderConsumerImpl.class);

	private BlockingQueue<Long> queue;

	private boolean active = false;

	private AtomicBoolean working = new AtomicBoolean(false);

	@Autowired
	private IQueuedTaskHolderService queuedTaskHolderService;

	@Autowired
	@Qualifier(JpaMoreTaskManagementConfig.OBJECT_MAPPER_BEAN_NAME)
	private ObjectMapper queuedTaskHolderObjectMapper;

	@Autowired
	private EntityManagerUtils entityManagerUtils;

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void setQueue(BlockingQueue<Long> queue) {
		this.queue = queue;
	}

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
			while (active && !Thread.currentThread().isInterrupted()) {
				queuedTaskHolderId = queue.take();
				working.set(true);
				
				entityManagerUtils.openEntityManager();
				// We are in a different transaction so we need to check that the 
				// transaction which created the queued task holder has already been commited.
				// If not, we sleep for a while and we try again
				int counter = 0;
				while(active && !Thread.currentThread().isInterrupted() && counter < 4) {
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

	protected void consume(QueuedTaskHolder queuedTaskHolder) throws InterruptedException {
		try {
			AbstractTask runnableTask = queuedTaskHolderObjectMapper.readValue(queuedTaskHolder.getSerializedTask(), AbstractTask.class);
			runnableTask.setQueuedTaskHolderId(queuedTaskHolder.getId());
			SpringBeanUtils.autowireBean(applicationContext, runnableTask);
			runnableTask.run();
		} catch (IOException e) {
			LOGGER.error("Error while trying to deserialize task " + queuedTaskHolder, e);
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public boolean isWorking() {
		return working.get();
	}

	@Override
	public void stop() {
		active = false;
	}

	@Override
	public void start() {
		active = true;
	}
}