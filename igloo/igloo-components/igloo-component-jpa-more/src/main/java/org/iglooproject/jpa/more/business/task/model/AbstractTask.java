package org.iglooproject.jpa.more.business.task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderService;
import org.iglooproject.jpa.more.business.task.transaction.OpenEntityManagerWithNoTransactionTransactionTemplate;
import org.iglooproject.jpa.more.business.task.transaction.TaskExecutionTransactionTemplateConfig;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.jpa.more.config.spring.AbstractTaskManagementConfig;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class AbstractTask implements Runnable, Serializable {
  private static final long serialVersionUID = 7734300264023051135L;

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);

  @Autowired protected IQueuedTaskHolderService queuedTaskHolderService;

  @Autowired
  @Qualifier(AbstractTaskManagementConfig.OBJECT_MAPPER_BEAN_NAME)
  private ObjectMapper queuedTaskHolderObjectMapper;

  @JsonIgnore private TransactionTemplate taskManagementTransactionTemplate;

  @JsonIgnore private TransactionTemplate taskExecutionTransactionTemplate;

  @JsonIgnore protected Long queuedTaskHolderId;

  protected Date triggeringDate;

  protected String taskName;

  protected String taskType;

  @JsonIgnore private TaskExecutionResult taskExecutionResult;

  protected AbstractTask() {}

  public AbstractTask(String taskName, ITaskTypeProvider taskTypeProvider, Date triggeringDate) {
    this(taskName, taskTypeProvider.getTaskType(), triggeringDate);
  }

  public AbstractTask(String taskName, String taskType, Date triggeringDate) {
    setTaskName(taskName);
    setTaskType(taskType);
    setTriggeringDate(triggeringDate);
  }

  /**
   * @return The ID of the queue this task must be added in, or <code>null</code> for the default
   *     queue.
   */
  public IQueueId selectQueue() {
    return null;
  }

  @Autowired
  private void setTransactionManager(
      EntityManagerUtils entityManagerUtils, PlatformTransactionManager transactionManager) {
    // Le TransactionTemplate pour la gestion du cycle de vie doit forcément être défini comme cela,
    // pas besoin de pouvoir le redéfinir.
    DefaultTransactionAttribute defaultTransactionAttributes =
        new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    taskManagementTransactionTemplate =
        new TransactionTemplate(transactionManager, defaultTransactionAttributes);

    // On donne la main sur l'initialisation du TransactionTemplate pour l'exécution de la tâche.
    taskExecutionTransactionTemplate =
        newTaskExecutionTransactionTemplate(entityManagerUtils, transactionManager);
  }

  /**
   * Permet d'initialiser le TransactionManager utilisé pour l'exécution de la tâche. Dans la mesure
   * du possible, surcharger plutôt {@link #getTaskExecutionTransactionTemplateConfig()}.
   */
  protected TransactionTemplate newTaskExecutionTransactionTemplate(
      EntityManagerUtils entityManagerUtils, PlatformTransactionManager transactionManager) {
    TaskExecutionTransactionTemplateConfig config = getTaskExecutionTransactionTemplateConfig();
    TransactionTemplate taskExecutionTransactionTemplate;
    if (config.isTransactional()) {
      DefaultTransactionAttribute defaultTransactionAttributes =
          new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
      defaultTransactionAttributes.setReadOnly(config.isReadOnly());

      taskExecutionTransactionTemplate =
          new TransactionTemplate(transactionManager, defaultTransactionAttributes);
    } else {
      taskExecutionTransactionTemplate =
          new OpenEntityManagerWithNoTransactionTransactionTemplate(
              entityManagerUtils, transactionManager, config.isReadOnly());
    }
    return taskExecutionTransactionTemplate;
  }

  /** Permet de configurer le TransactionManager utilisé pour l'exécution de la tâche. */
  protected TaskExecutionTransactionTemplateConfig getTaskExecutionTransactionTemplateConfig() {
    return new TaskExecutionTransactionTemplateConfig();
  }

  /**
   * This method is final. If you need to customize its behavior, check out one of the onXX()
   * methods.
   *
   * @see #onBeforeStart()
   * @see #onBeforeExecute()
   * @see #onAfterExecute(TaskExecutionResult, TaskStatus)
   */
  @Override
  public final void run() {
    /*
     * Stores the "this thread was interrupted" information.
     * For some reason, the Thread.isInterrupted() flag is not properly preserved after using
     * a transaction template. Maybe some code does a Thread.sleep, then catches InterruptedException but never
     * resets the flag. Anyway, using this mutable boolean works around the problem.
     */
    final MutableBoolean interruptedFlag = new MutableBoolean(false);
    try {
      onBeforeStart();

      taskExecutionResult =
          taskManagementTransactionTemplate.execute(
              new TransactionCallback<TaskExecutionResult>() {
                @Override
                public TaskExecutionResult doInTransaction(TransactionStatus status) {
                  try {
                    QueuedTaskHolder queuedTaskHolder =
                        queuedTaskHolderService.getById(queuedTaskHolderId);

                    if (queuedTaskHolder == null) {
                      throw new IllegalArgumentException(
                          "No task found with id " + queuedTaskHolderId);
                    }

                    queuedTaskHolder.setStartDate(new Date());
                    queuedTaskHolder.setStatus(TaskStatus.RUNNING);
                    queuedTaskHolderService.update(queuedTaskHolder);

                    return null;
                  } catch (RuntimeException | ServiceException | SecurityServiceException e) {
                    status.setRollbackOnly();
                    return TaskExecutionResult.failed(e);
                  } finally {
                    if (Thread.currentThread().isInterrupted()) {
                      // Save the information (seems to be cleared by the transaction template)
                      interruptedFlag.setTrue();
                    }
                  }
                }
              });

      if (taskExecutionResult != null && TaskResult.FATAL.equals(taskExecutionResult.getResult())) {
        final TaskStatus status =
            taskManagementTransactionTemplate.execute(
                new TransactionCallback<TaskStatus>() {
                  @Override
                  public TaskStatus doInTransaction(TransactionStatus status) {
                    try {
                      QueuedTaskHolder queuedTaskHolder =
                          queuedTaskHolderService.getById(queuedTaskHolderId);
                      final TaskStatus taskStatus;

                      if (queuedTaskHolder == null) {
                        LOGGER.error(
                            "Task {} could not be found; skipped execution.",
                            queuedTaskHolderId,
                            taskExecutionResult);
                        return null;
                      }

                      if (interruptedFlag.isTrue()) {
                        LOGGER.error(
                            "An interrupt has occured while starting task {}",
                            queuedTaskHolder,
                            taskExecutionResult.getThrowable());
                        taskStatus = onInterruptStatus(taskExecutionResult);
                      } else {
                        LOGGER.error(
                            "An error has occured while starting task {}",
                            queuedTaskHolder,
                            taskExecutionResult.getThrowable());
                        taskStatus = onFailStatus(taskExecutionResult);
                      }
                      endTask(queuedTaskHolder, taskStatus);
                      return taskStatus;
                    } catch (RuntimeException
                        | JsonProcessingException
                        | ServiceException
                        | SecurityServiceException e) {
                      throw new RuntimeException(e);
                    }
                  }
                });

        // If we could not switch to "RUNNING", stop right now.
        onAfterRun(status);
        return;
      }

      onBeforeExecute();

      taskExecutionResult =
          taskExecutionTransactionTemplate.execute(
              new TransactionCallback<TaskExecutionResult>() {
                @Override
                public TaskExecutionResult doInTransaction(TransactionStatus status) {
                  try {
                    /*
                     * The result may contain a business exception, caught in doTask.
                     * We don't have to propagate it, the task will be considered in error and onFailStatus()
                     * will be called.
                     */
                    TaskExecutionResult executionResult = doTask();
                    Objects.requireNonNull(executionResult, "executionResult must not be null");

                    /*
                     * If the task execution caught a business exception in doTask without propagating it
                     * (so as to preserve a batch report), we roll back the transaction.
                     */
                    if (TaskResult.FATAL.equals(executionResult.getResult())) {
                      status.setRollbackOnly();
                    }

                    return executionResult;
                  } catch (InterruptedException e) {
                    status.setRollbackOnly();
                    Thread.currentThread().interrupt();
                    return TaskExecutionResult.failed(e);
                  } catch (Exception e) {
                    status.setRollbackOnly();
                    return TaskExecutionResult.failed(e);
                  } finally {
                    if (Thread.currentThread().isInterrupted()) {
                      // Save the information (seems to be cleared by the transaction template)
                      interruptedFlag.setTrue();
                    }
                  }
                }
              });

      // Should not happen, but just in case...
      if (taskExecutionResult == null) {
        throw new RuntimeException();
      }

      final TaskStatus status;

      if (!TaskResult.FATAL.equals(taskExecutionResult.getResult())) {
        // Success case
        status =
            taskManagementTransactionTemplate.execute(
                new TransactionCallback<TaskStatus>() {
                  @Override
                  public TaskStatus doInTransaction(TransactionStatus status) {
                    try {
                      QueuedTaskHolder queuedTaskHolder =
                          queuedTaskHolderService.getById(queuedTaskHolderId);
                      endTask(queuedTaskHolder, TaskStatus.COMPLETED);
                      return TaskStatus.COMPLETED;
                    } catch (RuntimeException
                        | JsonProcessingException
                        | ServiceException
                        | SecurityServiceException e) {
                      throw new RuntimeException(e);
                    }
                  }
                });
      } else {
        // Error case
        status =
            taskManagementTransactionTemplate.execute(
                new TransactionCallback<TaskStatus>() {
                  @Override
                  public TaskStatus doInTransaction(TransactionStatus status) {
                    try {
                      QueuedTaskHolder queuedTaskHolder =
                          queuedTaskHolderService.getById(queuedTaskHolderId);
                      final TaskStatus taskStatus;

                      if (interruptedFlag.isTrue()) {
                        LOGGER.error(
                            "An interrupt has occured while executing task {}",
                            queuedTaskHolder,
                            taskExecutionResult.getThrowable());
                        taskStatus = onInterruptStatus(taskExecutionResult);
                      } else {
                        LOGGER.error(
                            "An error has occured while executing task {}",
                            queuedTaskHolder,
                            taskExecutionResult.getThrowable());
                        taskStatus = onFailStatus(taskExecutionResult);
                      }
                      endTask(queuedTaskHolder, taskStatus);
                      return taskStatus;
                    } catch (RuntimeException
                        | JsonProcessingException
                        | ServiceException
                        | SecurityServiceException e) {
                      throw new RuntimeException(e);
                    }
                  }
                });
      }

      onAfterExecute(taskExecutionResult, status);
      onAfterRun(status);

      taskExecutionResult = null;
    } finally {
      if (interruptedFlag.isTrue()) {
        Thread.currentThread().interrupt();
      }
    }
  }

  /**
   * Called before starting a task, i.e. before marking it "RUNNING".
   *
   * <p>This method is not executed in a transaction. If the implementor requires one, he should use
   * {@link #getTaskExecutionTransactionTemplate()} or {@link
   * #getTaskManagementTransactionTemplate()}.
   */
  protected void onBeforeStart() {
    // Default: do nothing. Override this if necessary.
  }

  /**
   * Called before executing a task, i.e. when it's marked <code>RUNNING</code>, but <code>doTask()
   * </code> hasn't been called yet.
   *
   * <p>This method is not executed in a transaction. If the implementor requires one, he should use
   * {@link #getTaskExecutionTransactionTemplate()} or {@link
   * #getTaskManagementTransactionTemplate()}.
   */
  protected void onBeforeExecute() {
    // Default: do nothing. Override this if necessary.
  }

  /**
   * Called after having executed a task, i.e. when <code>doTask()</code> has been called and the
   * task has been marked with its post-execution status.
   *
   * <p><strong>WARNING:</strong> This method is <strong>not</strong> called if the task could not
   * be started.
   *
   * <p>Altering <code>result</code> will have no effect on the persisted {@link QueuedTaskHolder}.
   *
   * <p>This method is not executed in a transaction. If the implementor requires one, he should use
   * {@link #getTaskExecutionTransactionTemplate()} or {@link
   * #getTaskManagementTransactionTemplate()}.
   *
   * @see #onAfterRun(TaskStatus)
   */
  protected void onAfterExecute(TaskExecutionResult result, TaskStatus status) {
    // Default: do nothing. Override this if necessary.
  }

  /**
   * Called after having executed <code>run()</code> and after having the task marked with the given
   * status.
   *
   * <p>This method is executed either when a task failed to start, or when the task has actually
   * been executed. It is always executed after all other <code>onXX()</code> methods.
   *
   * <p>This method is not executed in a transaction. If the implementor requires one, he should use
   * {@link #getTaskExecutionTransactionTemplate()} or {@link
   * #getTaskManagementTransactionTemplate()}.
   */
  protected void onAfterRun(TaskStatus status) {
    // Default: do nothing. Override this if necessary.
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
   *
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
   * @param executionResult permet de choisir le statut en fonction du résultat d'exécution (ex :
   *     CANCELLED si exception métier, FAILED si exception autre).
   */
  @JsonIgnore
  public TaskStatus onFailStatus(TaskExecutionResult executionResult) {
    return TaskStatus.FAILED;
  }

  /**
   * Permet principalement de définir si une tâche ayant été interrompue doit être passée au statut
   * CANCELLED (ne se relance pas automatiquement si on redémarre la file), INTERRUPTED ou FAILED.
   *
   * @param executionResult permet de choisir le statut en fonction du résultat d'exécution (ex :
   *     CANCELLED si exception métier, FAILED si exception autre).
   */
  @JsonIgnore
  public TaskStatus onInterruptStatus(TaskExecutionResult executionResult) {
    return TaskStatus.INTERRUPTED;
  }
}
