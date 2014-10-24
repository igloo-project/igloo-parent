package fr.openwide.core.jpa.more.business.task.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;
import fr.openwide.core.jpa.more.business.task.transaction.OpenEntityManagerWithNoTransactionTransactionTemplate;
import fr.openwide.core.jpa.more.business.task.transaction.TaskExecutionTransactionTemplateConfig;
import fr.openwide.core.jpa.more.business.task.util.TaskResult;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.jpa.more.config.spring.AbstractTaskManagementConfig;
import fr.openwide.core.jpa.util.EntityManagerUtils;

public abstract class AbstractTask implements Runnable, Serializable {
	private static final long serialVersionUID = 7734300264023051135L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);

	@Autowired
	protected IQueuedTaskHolderService queuedTaskHolderService;

	@Autowired
	@Qualifier(AbstractTaskManagementConfig.OBJECT_MAPPER_BEAN_NAME)
	private ObjectMapper queuedTaskHolderObjectMapper;
	
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private TransactionTemplate taskManagementTransactionTemplate;
	
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private TransactionTemplate taskExecutionTransactionTemplate;
	
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	protected Long queuedTaskHolderId;

	protected Date triggeringDate;

	protected String taskName;

	protected String taskType;

	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private TaskExecutionResult taskExecutionResult;

	protected AbstractTask() { }

	public AbstractTask(String taskName, ITaskTypeProvider taskTypeProvider, Date triggeringDate) {
		this(taskName, taskTypeProvider.getTaskType(), triggeringDate);
	}

	public AbstractTask(String taskName, String taskType, Date triggeringDate) {
		setTaskName(taskName);
		setTaskType(taskType);
		setTriggeringDate(triggeringDate);
	}
	
	/**
	 * @return The ID of the queue this task must be added in, or <code>null</code> for the default queue. 
	 */
	public IQueueId selectQueue() {
		return null;
	}

	@Autowired
	private void setTransactionManager(EntityManagerUtils entityManagerUtils, PlatformTransactionManager transactionManager) {
		// Le TransactionTemplate pour la gestion du cycle de vie doit forcément être défini comme cela,
		// pas besoin de pouvoir le redéfinir.
		DefaultTransactionAttribute defaultTransactionAttributes = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		taskManagementTransactionTemplate = new TransactionTemplate(transactionManager, defaultTransactionAttributes);
		
		// On donne la main sur l'initialisation du TransactionTemplate pour l'exécution de la tâche.
		taskExecutionTransactionTemplate = newTaskExecutionTransactionTemplate(
				entityManagerUtils, transactionManager);
	}
	
	/**
	 * Permet d'initialiser le TransactionManager utilisé pour l'exécution de la tâche.
	 * Dans la mesure du possible, surcharger plutôt {@link #getTaskExecutionTransactionTemplateConfig()}.
	 */
	protected TransactionTemplate newTaskExecutionTransactionTemplate(EntityManagerUtils entityManagerUtils,
			PlatformTransactionManager transactionManager) {
		TaskExecutionTransactionTemplateConfig config = getTaskExecutionTransactionTemplateConfig();
		TransactionTemplate taskExecutionTransactionTemplate;
		if (config.isTransactional()) {
			DefaultTransactionAttribute defaultTransactionAttributes = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			defaultTransactionAttributes.setReadOnly(config.isReadOnly());
			
			taskExecutionTransactionTemplate = new TransactionTemplate(transactionManager, defaultTransactionAttributes);
		} else {
			taskExecutionTransactionTemplate = new OpenEntityManagerWithNoTransactionTransactionTemplate(
					entityManagerUtils, transactionManager, config.isReadOnly());
		}
		return taskExecutionTransactionTemplate;
	}
	
	/**
	 * Permet de configurer le TransactionManager utilisé pour l'exécution de la tâche.
	 */
	protected TaskExecutionTransactionTemplateConfig getTaskExecutionTransactionTemplateConfig() {
		return new TaskExecutionTransactionTemplateConfig();
	}
	
	@Override
	public void run() {
		taskExecutionResult = taskManagementTransactionTemplate.execute(new TransactionCallback<TaskExecutionResult>() {
			@Override
			public TaskExecutionResult doInTransaction(TransactionStatus status) {
				try {
					QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);

					if (queuedTaskHolder == null) {
						throw new IllegalArgumentException("No task found with id " + queuedTaskHolderId);
					}

					queuedTaskHolder.setStartDate(new Date());
					queuedTaskHolder.setStatus(TaskStatus.RUNNING);
					queuedTaskHolderService.update(queuedTaskHolder);

					return null;
				} catch (Exception e) {
					status.setRollbackOnly();
					return TaskExecutionResult.failed(e);
				}
			}
		});

		if (taskExecutionResult != null && TaskResult.FATAL.equals(taskExecutionResult.getResult())) {
			taskManagementTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					try {
						QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);

						if (queuedTaskHolder == null) {
							LOGGER.error("An error has occured while executing task " + queuedTaskHolderId, taskExecutionResult);
							return;
						}
						
						LOGGER.error("An error has occured while executing task " + queuedTaskHolder, taskExecutionResult);
						
						endTask(queuedTaskHolder, onFailStatus(taskExecutionResult));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
			
			// Si on n'a pas pu passer en RUNNING, on arrête tout de suite.
			return;
		}

		taskExecutionResult = taskExecutionTransactionTemplate.execute(new TransactionCallback<TaskExecutionResult>() {
			@Override
			public TaskExecutionResult doInTransaction(TransactionStatus status) {
				try {
					// Le résultat peut contenir une exception métier, interceptée dans doTask. On n'est pas obligé
					// de la propager, la tâche passera en erreur et dans onFailStatus().
					TaskExecutionResult executionResult = doTask();
					Asserts.notNull(executionResult, "executionResult");
					
					// Si l'execution de la tâche a intercepté une exception métier dans doTask sans la propager
					// pour conserver le rapport, on fait un rollback sur la transaction.
					if (TaskResult.FATAL.equals(executionResult.getResult())) {
						status.setRollbackOnly();
					}
					
					return executionResult;
				} catch (Exception e) {
					status.setRollbackOnly();
					return TaskExecutionResult.failed(e);
				}
			}
		});
		
		// Ne devrait pas arriver mais on vérifie, au cas où.
		if (taskExecutionResult == null) {
			throw new RuntimeException();
		}
		
		if (!TaskResult.FATAL.equals(taskExecutionResult.getResult())) {
			// Cas du succès
			taskManagementTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					try {
						QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);
						endTask(queuedTaskHolder, TaskStatus.COMPLETED);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		} else {
			// Cas de l'erreur
			taskManagementTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					try {
						QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);
						
						LOGGER.error("An error has occured while executing task " + queuedTaskHolder, taskExecutionResult.getStackTrace());
						
						endTask(queuedTaskHolder, onFailStatus(taskExecutionResult));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
		
		taskExecutionResult = null;
	}

	private void endTask(QueuedTaskHolder queuedTaskHolder, TaskStatus endingStatus)
			throws JsonProcessingException, ServiceException, SecurityServiceException {
		queuedTaskHolder.setStatus(endingStatus);
		queuedTaskHolder.setEndDate(new Date());
		queuedTaskHolder.updateExecutionInformation(taskExecutionResult, queuedTaskHolderObjectMapper);
		queuedTaskHolderService.update(queuedTaskHolder);
	}

	/**
	 * L'exécution de la tâche à proprement parler.
	 * @return Le résultat de l'exécution de la tâche, obligatoire.
	 * @throws Exception Exception non gérée par le code métier, perd complètement le BatchReport
	 */
	protected abstract TaskExecutionResult doTask() throws Exception;

	protected TransactionTemplate getTaskManagementTransactionTemplate() {
		return taskManagementTransactionTemplate;
	}

	protected TransactionTemplate getTaskExecutionTransactionTemplate() {
		return taskExecutionTransactionTemplate;
	}

	public Long getQueuedTaskHolderId() {
		return queuedTaskHolderId;
	}

	public void setQueuedTaskHolderId(Long queuedTaskHolderId) {
		this.queuedTaskHolderId = queuedTaskHolderId;
	}

	public Date getTriggeringDate() {
		return CloneUtils.clone(triggeringDate);
	}

	public void setTriggeringDate(Date triggeringDate) {
		this.triggeringDate = CloneUtils.clone(triggeringDate);
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * Permet principalement de définir si une tâche n'ayant pas abouti doit être passée au statut
	 * CANCELLED (ne se relance pas automatiquement si on redémarre la file) ou FAILED.
	 * 
	 * @param executionResult permet de choisir le statut en fonction du résultat d'exécution
	 * (ex : CANCELLED si exception métier, FAILED si exception autre).
	 */
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	public TaskStatus onFailStatus(TaskExecutionResult executionResult) {
		return TaskStatus.FAILED;
	}
}
