package org.iglooproject.jpa.more.business.task.service;

import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.START_MODE;
import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.STOP_TIMEOUT;
import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.queueNumberOfThreads;
import static org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds.queueStartDelay;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.iglooproject.infinispan.model.IAttribution;
import org.iglooproject.infinispan.model.SimpleLock;
import org.iglooproject.infinispan.model.SimpleRole;
import org.iglooproject.infinispan.model.impl.Attribution;
import org.iglooproject.infinispan.model.impl.LockRequest;
import org.iglooproject.infinispan.service.IInfinispanClusterService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.task.event.QueuedTaskFinishedEvent;
import org.iglooproject.jpa.more.business.task.model.AbstractTask;
import org.iglooproject.jpa.more.business.task.model.IInfinispanQueue;
import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.service.impl.TaskConsumer;
import org.iglooproject.jpa.more.business.task.service.impl.TaskQueue;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.jpa.more.config.spring.AbstractTaskManagementConfig;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import org.iglooproject.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;
import org.iglooproject.spring.config.util.TaskQueueStartMode;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.SpringBeanUtils;
	
public class QueuedTaskHolderManagerImpl implements IQueuedTaskHolderManager, ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueuedTaskHolderManagerImpl.class);
	private static final String CACHE_KEY_TASK_ID = "QueuedTaskHolderManagerImpl##taskIds";
	private static final String INIT_QUEUES_EXECUTOR_NAME = "QueueTaskHolder#initQueuesFromDatabase";

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private IQueuedTaskHolderService queuedTaskHolderService;

	@Autowired
	@Qualifier(AbstractTaskManagementConfig.OBJECT_MAPPER_BEAN_NAME)
	private ObjectMapper queuedTaskHolderObjectMapper;

	@Autowired
	private IPropertyService propertyService;

	@Autowired
	private ITransactionSynchronizationTaskManagerService synchronizationManager;

	@Autowired(required = false)
	private IInfinispanClusterService infinispanClusterService;

	@Resource
	private Collection<? extends IQueueId> queueIds;

	private int stopTimeout;

	private ScheduledThreadPoolExecutor initQueuesFromDatabaseExecutor;

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
	public void onApplicationEvent(ContextRefreshedEvent event) {
		/* 
		 * onApplicationEvent is called for every context initialization, including potential child contexts.
		 * We avoid starting queues multiple times, by calling init() only on the root application context initialization.
		 */
		if (event != null && event.getSource() != null
				&& AbstractApplicationContext.class.isAssignableFrom(event.getSource().getClass())
				&& ((AbstractApplicationContext) event.getSource()).getParent() == null) {
			init();
		}
	}

	private void init() {
		stopTimeout = propertyService.get(STOP_TIMEOUT);

		initQueues();
		if (TaskQueueStartMode.auto.equals(propertyService.get(START_MODE))) {
			start();
		} else {
			LOGGER.warn("Task queue start configured in \"manual\" mode.");
		}
	}

	private final void initQueues() {
		Collection<IQueueId> queueIdsAsStrings = Lists.newArrayList();
		
		// Sanity checks
		for (IQueueId queueId : queueIds) {
			String queueIdAsString = queueId.getUniqueStringId();
			Assert.state(!IQueueId.RESERVED_DEFAULT_QUEUE_ID_STRING.equals(queueIdAsString),
					"Queue ID '" + IQueueId.RESERVED_DEFAULT_QUEUE_ID_STRING + "' is reserved for implementation purposes. Please choose another ID.");
			Assert.state(!queueIdsAsStrings.contains(queueIdAsString), "Queue ID '" + queueIdAsString + "' was defined more than once. Queue IDs must be unique.");
			queueIdsAsStrings.add(queueId);
		}

		defaultQueue = initQueue(IQueueId.RESERVED_DEFAULT_QUEUE_ID_STRING, 1);
		for (IQueueId queueId : queueIdsAsStrings) {
			int numberOfThreads = propertyService.get(queueNumberOfThreads(queueId));
			if (numberOfThreads < 1) {
				LOGGER.warn("Number of threads for queue '{}' is set to an invalid value ({}); defaulting to 1", queueId.getUniqueStringId(), numberOfThreads);
				numberOfThreads = 1;
			}
			initQueue(queueId, numberOfThreads);
		}
	}

	private TaskQueue initQueue(IQueueId queueId, int numberOfThreads) {
		TaskQueue queue = new TaskQueue(queueId.getUniqueStringId());
		for (int i = 0 ; i < numberOfThreads ; ++i) {
			TaskConsumer consumer;
			if (infinispanClusterService != null
					&& queueId instanceof IInfinispanQueue
					&& ((IInfinispanQueue) queueId).handleInfinispan()) {
				IInfinispanQueue infinispanQueue = (IInfinispanQueue) queueId;
				if (numberOfThreads != 1) {
					throw new IllegalStateException("If you want to manage infinispan in queue, you must use only one thread");
				}
				consumer = new TaskConsumer(queue, i, infinispanQueue.getLock(), infinispanQueue.getPriorityQueue());
			} else {
				consumer = new TaskConsumer(queue, i);
			}
			SpringBeanUtils.autowireBean(applicationContext, consumer);
			consumersByQueue.put(queue, consumer);
		}
		queuesById.put(queueId.getUniqueStringId(), queue);
		return queue;
	}

	private String selectQueue(AbstractTask task) {
		SpringBeanUtils.autowireBean(applicationContext, task);
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
	public boolean isTaskQueueActive(String queueId){
		TaskQueue queue = queuesById.get(queueId);
		Collection<TaskConsumer> consumers= consumersByQueue.get(queue);
		if(consumers.isEmpty()){
			return false;
		}
		return consumers.iterator().next().isActive();
	}

	@Override
	public int getNumberOfTaskConsumer(String queueId){
		TaskQueue queue = queuesById.get(queueId);
		return consumersByQueue.get(queue).size();
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
	public int getNumberOfWaitingTasks(String queueId){
		try {
			return queuesById.get(queueId).size();
		} catch (NullPointerException e) {
			return -1;
		}
	}

	@Override
	public int getNumberOfRunningTasks() {
		int total = 0;
		for (TaskQueue queue : queuesById.values()) {
			total += getNumberOfRunningTasks(queue.getId());
		}
		return total;
	}

	@Override
	public int getNumberOfRunningTasks(String queueId){
		int total = 0;
		TaskQueue queue = queuesById.get(queueId);
		Collection<TaskConsumer> consumers = consumersByQueue.get(queue);
		for (TaskConsumer consumer : consumers) {
			if (consumer.isWorking()) {
				total++;
			}
		}
		return total; 
	}

	@Override
	public Collection<IQueueId> getQueueIds() {
		return Collections.unmodifiableCollection(queueIds);
	}

	@Override
	public void start() {
		startConsumers();
	}

	protected synchronized void startConsumers() {
		if (!active.get()) {
			availableForAction.set(false);
			
			// load infinispan backend if needed
			getCache();
			
			if (infinispanClusterService  != null) {
				// Init the recurrent executor for initQueues.
				// Must be used only in a cluster context
				initializeDatabaseExecutor();
			}
			
			try {
				initQueuesFromDatabase();
				for (TaskConsumer consumer : consumersByQueue.values()) {
					Long configurationStartDelay = getStartDelay(consumer.getQueue().getId());
					consumer.start(configurationStartDelay);
				}
			} finally {
				active.set(true);
				availableForAction.set(true);
			}
		}
	}

	/**
	 *  The length of time the consumer threads will wait before their first access to their task queue.
	 */
	protected Long getStartDelay(String queueId) {
		return propertyService.get(queueStartDelay(queueId));
	}

	/**
	 * Initialize queues with the tasks to be run from the database
	 */
	private void initQueuesFromDatabase() {
		// NOTE : infinispanClusterService is initialized @PostConstruct, so infinispan subsystem is initialized at
		// this time.
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for (TaskQueue queue : queuesById.values()) {
					try {
						// Gets the tasks that can be run from DB
						List<QueuedTaskHolder> tasks = queuedTaskHolderService.getListConsumable(queue.getId());
						
						if (queue == defaultQueue) {
							tasks.addAll(queuedTaskHolderService.getListConsumable(null));
						}
						
						if (! tasks.isEmpty()) {
							// remove already loaded keys
							if (infinispanClusterService != null) {
								tasks.removeIf(t -> getCache().keySet().contains(t.getId()));
							}
							LOGGER.warn("Reload {} tasks in QueuedTaskHolderManager", tasks.size());
							// Gets tasks  Id
							List<Long> taskIds = tasks.stream().map(QueuedTaskHolder::getId).collect(Collectors.toList());
							LOGGER.warn("Reloaded tasks: {}", Joiner.on(",").join(taskIds));

							// Initialize and submit the task to the queue
							tasks.forEach(t -> queueOffer(queue, t));
						}
						
					} catch (RuntimeException | ServiceException | SecurityServiceException e) {
						LOGGER.error("Error while trying to init queue " + queue + " from database", e);
					}
				}
			}
		};
		if (infinispanClusterService != null) {
			LockRequest lockRequest = LockRequest.with(
					SimpleRole.from(INIT_QUEUES_EXECUTOR_NAME),
					SimpleLock.from(INIT_QUEUES_EXECUTOR_NAME, "QueueTaskHolder")
			);
			try {
				infinispanClusterService.doIfRoleWithLock(lockRequest, runnable);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
		} else {
			runnable.run();
		}
	}

	@Override
	public final QueuedTaskHolder submit(AbstractTask task) throws ServiceException {
		QueuedTaskHolder newQueuedTaskHolder = null;
		final String selectedQueueId;
		
		try {
			String serializedTask;
			selectedQueueId = selectQueue(task);
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
		} catch (RuntimeException | ServiceException | SecurityServiceException e) {
			throw new ServiceException("Error while creating and saving task " + task, e);
		}
		
		final Long newQueuedTaskHolderId = newQueuedTaskHolder.getId();
		synchronizationManager.push(new ITransactionSynchronizationAfterCommitTask() {
			@Override
			public void run() throws Exception {
				doSubmit(selectedQueueId, newQueuedTaskHolderId);
			}
		});
		
		return newQueuedTaskHolder;
	}

	protected void doSubmit(String queueId, Long newQueuedTaskHolderId) throws ServiceException {
		if (active.get()) {
			TaskQueue selectedQueue = getQueue(queueId);
			queueOffer(selectedQueue, newQueuedTaskHolderId);
		}
	}

	@Override
	public void reload(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException {
		QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);
		if (queuedTaskHolder != null) {
			TaskQueue queue = getQueue(queuedTaskHolder.getQueueId());
			if (active.get()) {
				queueOffer(queue, queuedTaskHolder);
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
	@PreDestroy
	private void destroy() {
		stop();
	}

	@Override
	public synchronized void stop() {
		if (active.get()) {
			availableForAction.set(false);
			
			try {
				// TODO YRO gérer le timeout plus intelligemment
				
				// send interrupt to all threads
				// if working, wait timeout before asking interrupt
				if (initQueuesFromDatabaseExecutor != null) {
					initQueuesFromDatabaseExecutor.shutdownNow(); // remaining tasks are not important
				}
				for (TaskQueue queue : queuesById.values()) {
					for (TaskConsumer consumer : consumersByQueue.get(queue)) {
						try {
							LOGGER.info("Stopping {}", queue.getId());
							consumer.stop(stopTimeout);
						} catch (RuntimeException e) {
							LOGGER.error("Error while trying to stop consumer {}", consumer.getName(), e);
						}
					}
				}
				
				// wait for effective shutdown on each thread and executor
				for (TaskQueue queue : queuesById.values()) {
					for (TaskConsumer consumer : consumersByQueue.get(queue)) {
						while (true) {
							try {
								LOGGER.info("Waiting for {} stop", consumer.getName());
								consumer.joinThread();
								LOGGER.info("{} stopped", consumer.getName());
								break;
							} catch (InterruptedException e) {
								LOGGER.warn("Interrupted waiting for {} stop", consumer.getName());
							}
						}
					}
				}
				if (initQueuesFromDatabaseExecutor != null) {
					while (true) {
						try {
							LOGGER.info("Waiting for {} stop", INIT_QUEUES_EXECUTOR_NAME);
							initQueuesFromDatabaseExecutor.awaitTermination(1, TimeUnit.HOURS); // forever
							LOGGER.info("{} stopped", INIT_QUEUES_EXECUTOR_NAME);
							break;
						} catch (InterruptedException e) {
							LOGGER.warn("Interrupted waiting for {} stop", INIT_QUEUES_EXECUTOR_NAME);
						}
					}
				}
				
				// mark tasks as interrupted
				for (TaskQueue queue : queuesById.values()) {
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
				} catch (RuntimeException | ServiceException | SecurityServiceException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * Cluster-safe offer a task in a queue ; if task is already loaded on the cluster, we don't load it.
	 * 
	 * @param queue
	 * @param taskId
	 * @return true if task is added to the queue
	 */
	private boolean queueOffer(TaskQueue queue, Long taskId) {
		// check if task is already in a queue
		if (getCache() != null) {
			IAttribution previous = getCache().putIfAbsent(taskId, Attribution.from(infinispanClusterService.getLocalAddress(), new Date()));
			if (previous != null) {
				// if already loaded, stop processing
				LOGGER.warn("Task {} already loaded in cluster and ignored", taskId);
				return false;
			}
		}
		// else offer in queue
		boolean status = queue.offer(taskId);
		if (!status) {
			LOGGER.error("Unable to offer the task " + taskId + " to the queue");
		}
		return status;
	}
	
	/**
	 * Cluster-safe offer a task in a queue ; if task is already loaded on the cluster, we don't load it.
	 * Reinit the task information before doing it
	 * @param queue
	 * @param task
	 * @return
	 */
	private boolean queueOffer(TaskQueue queue, QueuedTaskHolder task) {
		task.setStatus(TaskStatus.TO_RUN);
		task.resetExecutionInformation();
		return queueOffer(queue, task.getId());
	}

	/**
	 * Check if cache exists and returns it ; null if infinispan is disabled
	 */
	private Cache<Long, IAttribution> getCache() {
		if (infinispanClusterService != null) {
			if (!infinispanClusterService.getCacheManager().cacheExists(CACHE_KEY_TASK_ID)) {
				infinispanClusterService.getCacheManager().getCache(CACHE_KEY_TASK_ID);
			}
			return infinispanClusterService.getCacheManager().getCache();
		}
		return null;
	}

	/**
	 * Initialize the executor that will submit the tasks from DB every 1 minute
	 * 
	 * This method MUST NOT be called if cache is disabled
	 */
	private synchronized void initializeDatabaseExecutor() {
		if (infinispanClusterService == null) {
			throw new IllegalStateException("This code must not be called as infinispan is not enabled");
		}
		initQueuesFromDatabaseExecutor = new ScheduledThreadPoolExecutor(1,
					new ThreadFactoryBuilder()
						.setDaemon(true)
						.setNameFormat(String.format("%s-%%d", INIT_QUEUES_EXECUTOR_NAME)) // %%d is an escaped %d
						.build());
		initQueuesFromDatabaseExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		initQueuesFromDatabaseExecutor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		initQueuesFromDatabaseExecutor.prestartAllCoreThreads();
		initQueuesFromDatabaseExecutor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				initQueuesFromDatabase();
			}
		}, 1, 1, TimeUnit.MINUTES);
	}

	@Override
	public void onTaskFinish(Long taskId) {
		if (getCache() != null) {
			IAttribution previous = getCache().remove(taskId);
			if (previous == null) {
				// if already loaded, stop processing
				LOGGER.warn("Task {} finished but not in cache map", taskId);
			}
			if (previous != null && ! previous.match(infinispanClusterService.getLocalAddress())) {
				LOGGER.warn("Task {} finished but attribution does not match", taskId);
			}
		}
		eventPublisher.publishEvent(new QueuedTaskFinishedEvent(taskId));
	}
}