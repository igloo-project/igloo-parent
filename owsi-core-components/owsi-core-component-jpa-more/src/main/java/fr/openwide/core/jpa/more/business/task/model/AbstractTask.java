package fr.openwide.core.jpa.more.business.task.model;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.commons.util.ExceptionUtils;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;

public abstract class AbstractTask implements Runnable, Serializable {
	private static final long serialVersionUID = 7734300264023051135L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Autowired
	protected IQueuedTaskHolderService queuedTaskHolderService;

	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	protected QueuedTaskHolder queuedTaskHolder;

	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	protected Long queuedTaskHolderId;

	protected Date triggeringDate;

	protected String taskName;

	protected String taskType;

	protected AbstractTask() {

	}

	public AbstractTask(String taskName, ITaskTypeProvider taskTypeProvider, Date triggeringDate) {
		setTaskName(taskName);
		setTaskType(taskTypeProvider.getTaskType());
		setTriggeringDate(triggeringDate);
	}

	@Override
	public void run() {
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					beforeTask();
					doTask();
					afterTask();
				} catch (Exception e) {
					LOGGER.error("An unexpected error has occured while executing task " + queuedTaskHolder, e);
					queuedTaskHolder.setStatus(TaskStatus.FAILED);
					queuedTaskHolder.setEndDate(new Date());
					queuedTaskHolder.setResult(ExceptionUtils.getStackTraceAsString(e));
				}
			}
		});
	}

	protected abstract void doTask();
	
	private void beforeTask() {
		queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);

		if (queuedTaskHolder == null) {
			throw new IllegalArgumentException("No task found with id " + getQueuedTaskHolderId());
		}

		queuedTaskHolder.setStartDate(new Date());
		queuedTaskHolder.setStatus(TaskStatus.RUNNING);
	}

	private void afterTask() {
		queuedTaskHolder.setEndDate(new Date());
		queuedTaskHolder.setStatus(TaskStatus.COMPLETED);
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
}
