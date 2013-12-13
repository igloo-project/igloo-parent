package fr.openwide.core.jpa.more.business.task.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.IQueueId;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.service.impl.TaskConsumer;
import fr.openwide.core.jpa.more.business.task.service.impl.TaskQueue;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.jpa.more.config.spring.JpaMoreTaskManagementConfig;
import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.spring.config.util.TaskQueueStartMode;
import fr.openwide.core.spring.util.SpringBeanUtils;

public class QueuedTaskHolderManagerImpl implements IQueuedTaskHolderManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueuedTaskHolderManagerImpl.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private IQueuedTaskHolderService queuedTaskHolderService;

	@Autowired
	@Qualifier(JpaMoreTaskManagementConfig.OBJECT_MAPPER_BEAN_NAME)
	private ObjectMapper queuedTaskHolderObjectMapper;

	@Autowired
	private CoreConfigurer configurer;
	
	private int stopTimeout;
	
	private final Multimap<TaskQueue, TaskConsumer> consumersByQueue
			= Multimaps.newListMultimap(new HashMap<TaskQueue, Collection<TaskConsumer>>(), new Supplier<List<TaskConsumer>>() {
				@Override
				public List<TaskConsumer> get() {
					return Lists.newArrayList();
				}
			});
	
	private final Map<String, TaskQueue> queuesById = Maps.newHashMap();
	
	private TaskQueue defaultQueue;

	private AtomicBoolean active = new AtomicBoolean(false);

	private AtomicBoolean availableForAction = new AtomicBoolean(true);
	
	@Override
	@PostConstruct
	public void init() {
		stopTimeout = configurer.getTaskStopTimeout();
		
		initQueues();
		
		if (TaskQueueStartMode.auto.equals(configurer.getTaskQueueStartMode())) {
			start();
		} else {
			LOGGER.warn("Task queue start configured in \"manual\" mode.");
		}
	}
	
	private final void initQueues() {
		queuesById.clear();
		consumersByQueue.clear();
		
		defaultQueue = initQueue(DEFAULT_QUEUE_ID, 1);
		
		Collection<String> queueIds = ImmutableSet.copyOf(configurer.getTaskQueueIds());
		for (String queueId : queueIds) {
			Assert.state(!DEFAULT_QUEUE_ID.equals(queueId), "Queue ID '" + DEFAULT_QUEUE_ID + "' is reserved for implementation purposes. Please choose another ID.");
			int numberOfThreads = configurer.getTaskQueueNumberOfThreads(queueId);
			if (numberOfThreads < 1) {
				LOGGER.warn("Number of threads for queue '{}' is set to an invalid value ({}); defaulting to 1", queueId, numberOfThreads);
				numberOfThreads = 1;
			}
			initQueue(queueId, numberOfThreads);
		}
	}
	
	private TaskQueue initQueue(String queueId, int numberOfThreads) {
		TaskQueue queue = new TaskQueue(queueId);
		for (int i = 0 ; i < numberOfThreads ; ++i) {
			TaskConsumer consumer = new TaskConsumer(queue, i);
			SpringBeanUtils.autowireBean(applicationContext, consumer);
			consumersByQueue.put(queue, consumer);
		}
		queuesById.put(queueId, queue);
		return queue;
	}
	
	private String getQueueId(AbstractTask task) {
		IQueueId queueId = task.selectQueue();
		String queueIdString = queueId == null ? null : queueId.getUniqueStringId();
		return queueIdString;
	}
	
	private TaskQueue getQueue(String queueId) {
		if (queueId == null) {
			return defaultQueue;
		}
		
		TaskQueue queue = queuesById.get(queueId);
		if (queue == null) {
			LOGGER.warn("Queue ID '{}' is unknown ; falling back to default queue", queueId);
			queue = defaultQueue;
		}
		return queue;
	}

	@Override
	public boolean isAvailableForAction() {
		return availableForAction.get();
	}

	@Override
	public boolean isActive() {
		return active.get();
	}

	@Override
	public int getNumberOfWaitingTasks() {
		int total = 0;
		for (TaskQueue queue : queuesById.values()) {
			total += queue.size();
		}
		return total;
	}

	@Override
	public synchronized void start() {
		if (!active.get()) {
			availableForAction.set(false);
			
			try {
				initQueuesFromDatabase();
				for (TaskConsumer consumer : consumersByQueue.values()) {
					consumer.start();
				}
			} finally {
				active.set(true);
				availableForAction.set(true);
			}
		}
	}

	private void initQueuesFromDatabase() {
		for (TaskQueue queue : queuesById.values()) {
			try {
				List<Long> taskIds = queuedTaskHolderService.initializeTasksAndListConsumable(queue.getId());
				for (Long taskId : taskIds) {
					boolean status = queue.offer(taskId);
					if (!status) {
						LOGGER.error("Unable to offer the task " + taskId + " to the queue");
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error while trying to init queue " + queue + " from database", e);
			}
		}
	}

	@Override
	public void submit(AbstractTask task) throws ServiceException {
		QueuedTaskHolder newQueuedTaskHolder = null;
		String serializedTask;
		String selectedQueueId = getQueueId(task);
		TaskQueue selectedQueue = getQueue(selectedQueueId);
		
		try {
			serializedTask = queuedTaskHolderObjectMapper.writeValueAsString(task);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Serialized task: " + serializedTask);
			}
			
			newQueuedTaskHolder = new QueuedTaskHolder(
					task.getTaskName(), selectedQueueId /* May differ from selectedQueue.getId() */,
					task.getTaskType(), serializedTask
			);
			queuedTaskHolderService.create(newQueuedTaskHolder);
		} catch (IOException e) {
			throw new ServiceException("Error while trying to serialize task " + task, e);
		} catch (Exception e) {
			throw new ServiceException("Error while creating and saving task " + task, e);
		}
		
		if (active.get()) {
			boolean status = selectedQueue.offer(newQueuedTaskHolder.getId());
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
			
			TaskQueue queue = getQueue(queuedTaskHolder.getQueueId());
			
			if (active.get()) {
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
	public synchronized void stop() {
		if (active.get()) {
			availableForAction.set(false);
			
			try {
				// TODO YRO gérer le timeout plus intelligemment
				for (TaskQueue queue : queuesById.values()) {
					for (TaskConsumer consumer : consumersByQueue.get(queue)) {
						try {
							consumer.stop(stopTimeout);
						} catch (Exception e) {
							LOGGER.error("Error while trying to stop consumer " + consumer, e);
						}
					}
					interruptQueueProcesses(queue);
				}
			} finally {
				active.set(false);
				availableForAction.set(true);
			}
		}
	}

	private void interruptQueueProcesses(TaskQueue queue) {
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