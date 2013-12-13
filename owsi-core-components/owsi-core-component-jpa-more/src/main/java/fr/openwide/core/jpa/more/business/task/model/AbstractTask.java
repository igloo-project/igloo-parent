package fr.openwide.core.jpa.more.business.task.model;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Throwables;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderManager;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;

public abstract class AbstractTask implements Runnable, Serializable {
	private static final long serialVersionUID = 7734300264023051135L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Autowired
	protected IQueuedTaskHolderService queuedTaskHolderService;

	@Autowired
	protected IQueuedTaskHolderManager queuedTaskHolderManager;

	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	protected Long queuedTaskHolderId;

	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	protected String report;

	protected Date triggeringDate;

	protected String taskName;

	protected String taskType;

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

	@Override
	public void run() {
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		final Exception beforeTaskResult = transactionTemplate.execute(new TransactionCallback<Exception>() {
			@Override
			public Exception doInTransaction(TransactionStatus status) {
				try {
					QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);

					if (queuedTaskHolder == null) {
						throw new IllegalArgumentException("No task found with id " + getQueuedTaskHolderId());
					}

					queuedTaskHolder.setStartDate(new Date());
					queuedTaskHolder.setStatus(TaskStatus.RUNNING);
					queuedTaskHolderService.update(queuedTaskHolder);

					return null;
				} catch (Exception e) {
					status.setRollbackOnly();
					return e;
				}
			}
		});

		if (beforeTaskResult != null) {
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					try {
						QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);

						LOGGER.error("An error has occured while executing task " + queuedTaskHolder, beforeTaskResult);

						queuedTaskHolder.setStatus(onFailStatus());
						queuedTaskHolder.setEndDate(new Date());
						queuedTaskHolder.setReport(report);
						queuedTaskHolder.setResult(Throwables.getStackTraceAsString(beforeTaskResult));
						queuedTaskHolderService.update(queuedTaskHolder);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}

		final Exception taskResult = transactionTemplate.execute(new TransactionCallback<Exception>() {
			@Override
			public Exception doInTransaction(TransactionStatus status) {
				try {
					QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);

					doTask(queuedTaskHolder);

					queuedTaskHolder.setEndDate(new Date());
					queuedTaskHolder.setStatus(TaskStatus.COMPLETED);
					queuedTaskHolder.setReport(report);
					queuedTaskHolderService.update(queuedTaskHolder);

					return null;
				} catch (Exception e) {
					status.setRollbackOnly();
					return e;
				}
			}
		});

		if (taskResult != null) {
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					try {
						QueuedTaskHolder queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);

						LOGGER.error("An error has occured while executing task " + queuedTaskHolder, taskResult);

						queuedTaskHolder.setStatus(onFailStatus());
						queuedTaskHolder.setEndDate(new Date());
						queuedTaskHolder.setReport(report);
						queuedTaskHolder.setResult(Throwables.getStackTraceAsString(taskResult));
						queuedTaskHolderService.update(queuedTaskHolder);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
	}

	protected abstract void doTask(QueuedTaskHolder queuedTaskHolder) throws Exception;

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

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	public TaskStatus onFailStatus() {
		return TaskStatus.FAILED;
	}
}
