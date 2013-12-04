package fr.openwide.core.jpa.more.business.task.service;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.jpa.more.config.spring.JpaMoreTaskManagementConfig;
import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.spring.config.util.TaskQueueStartMode;

public class QueuedTaskHolderManagerImpl implements IQueuedTaskHolderManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueuedTaskHolderManagerImpl.class);

	private static final String THREAD_NAME = "Queued_Task_Holder_Executor";

	@Autowired
	private IQueuedTaskHolderService queuedTaskHolderService;

	@Autowired
	private IQueuedTaskHolderConsumer queuedTaskHolderConsumer;

	@Autowired
	@Qualifier(JpaMoreTaskManagementConfig.OBJECT_MAPPER_BEAN_NAME)
	private ObjectMapper queuedTaskHolderObjectMapper;

	@Autowired
	private CoreConfigurer configurer;
	
	private BlockingQueue<Long> queue;

	private Thread processConsumerThread;

	private boolean active = false;

	private boolean availableForAction = true;

	private int stopTimeout;
	
	@Override
	@PostConstruct
	public void init() {
		stopTimeout = configurer.getTaskStopTimeout();
		
		queue = new LinkedBlockingQueue<Long>();

		if (true) {
			queuedTaskHolderConsumer.setQueue(queue);
			if (TaskQueueStartMode.auto.equals(configurer.getTaskQueueStartMode())) {
				start();
			} else {
				LOGGER.warn("Task queue start configured in mode \"manual\".");
			}
		}
	}

	@Override
	public boolean isAvailableForAction() {
		return availableForAction;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public Integer getNumberOfWaitingTasks() {
		return queue.size();
	}

	@Override
	public void start() {
		synchronized (this) {
			if (!active) { 
				availableForAction = false;

				initQueueFromDatabase();

				active = true;
				startConsumer();

				availableForAction = true;
			}
		}
	}

	private void initQueueFromDatabase() {
		try {
			List<Long> taskIds = queuedTaskHolderService.initializeTasksAndListConsumable();
			for (Long taskId : taskIds) {
				boolean status = queue.offer(taskId);
				if (!status) {
					LOGGER.error("Unable to offer the task " + taskId + " to the queue");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while trying to init queue from database", e);
		}
	}

	private void startConsumer() {
		if (processConsumerThread == null) {
			processConsumerThread = new Thread(queuedTaskHolderConsumer, THREAD_NAME);
		}
		if (!queuedTaskHolderConsumer.isActive()) {
			queuedTaskHolderConsumer.start();
			processConsumerThread.start();
		}
	}

	@Override
	public void submit(AbstractTask task) {
		QueuedTaskHolder newQueuedTaskHolder = null;
		String serializedTask;
		try {
			serializedTask = queuedTaskHolderObjectMapper.writeValueAsString(task);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Serialized task: " + serializedTask);
			}

			newQueuedTaskHolder = new QueuedTaskHolder(task.getTaskName(), task.getTaskType(), serializedTask);

			queuedTaskHolderService.create(newQueuedTaskHolder);
		} catch (IOException e) {
			LOGGER.error("Error while trying to serialize task " + task, e);
		} catch (Exception e) {
			LOGGER.error("Error while creating and saving task " + task, e);
		}

		if (active) {
			boolean status = queue.offer(newQueuedTaskHolder.getId());
			if (!status) {
				LOGGER.error("Unable to offer the task " + newQueuedTaskHolder.getId() + " to the queue");
			}
		}
	}
	
	@Override
	public void reload(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException {
		QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);
		if (queuedTaskHolder != null) {
			queuedTaskHolder.setStatus(TaskStatus.TO_RUN);
			queuedTaskHolder.resetExecutionInformation();
			queuedTaskHolderService.update(queuedTaskHolder);
			
			if (active) {
				boolean status = queue.offer(queuedTaskHolder.getId());
				if (!status) {
					LOGGER.error("Unable to offer the task " + queuedTaskHolder.getId() + " to the queue");
				}
			}
		}
	}
	
	@Override
	public void cancel(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException {
		QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);
		if (queuedTaskHolder != null) {
			queuedTaskHolder.setStatus(TaskStatus.CANCELLED);
			queuedTaskHolderService.update(queuedTaskHolder);
		}
	}

	/**
	 * Warning: this destroy method is called twice, so all the related code has
	 * to be written with this constraint
	 * 
	 * @throws Exception
	 */
	@Override
	@PreDestroy
	public void destroy() {
		stop();
	}

	@Override
	public void stop() {
		/*
		 * stop method can be called twice, so we need to synchronize this block
		 */
		synchronized (this) {
			if (active) {
				availableForAction = false;
				active = false;

				/*
				 * signal the process consumer that it will be asked to stop in
				 * a few moment
				 */
				queuedTaskHolderConsumer.stop();

				/*
				 * if a queued task holder is currently active, we wait for it
				 */
				if (queuedTaskHolderConsumer.isWorking()) {
					long wait = 0;
					boolean interrupted = false;
					while ((wait < stopTimeout) && queuedTaskHolderConsumer.isWorking() && !interrupted) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							interrupted = true;
						}
						wait += 3000;
					}
				}
				if (processConsumerThread != null) {
					processConsumerThread.interrupt();
					processConsumerThread = null;
				}
				if (!queue.isEmpty()) {
					interruptQueueProcesses();
				}
				availableForAction = true;
			}
		}
	}

	private void interruptQueueProcesses() {
		List<Long> queuedTaskHolderIds = new LinkedList<Long>();
		queue.drainTo(queuedTaskHolderIds);

		for (Long queuedTaskHolderId : queuedTaskHolderIds) {
			QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);
			if (queuedTaskHolder != null) {
				try {
					queuedTaskHolder.setStatus(TaskStatus.INTERRUPTED);
					queuedTaskHolder.setEndDate(new Date());
					queuedTaskHolder.resetExecutionInformation();
					queuedTaskHolderService.update(queuedTaskHolder);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}