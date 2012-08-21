package fr.openwide.core.jpa.more.business.task.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;

public abstract class AbstractTask implements Runnable, Serializable {
	private static final long serialVersionUID = 7734300264023051135L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);

	@Autowired
	private IQueuedTaskHolderService queuedTaskHolderService;

	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private QueuedTaskHolder queuedTaskHolder;

	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private Long queuedTaskHolderId;

	private Date triggeringDate;

	private String taskName;

	private String taskType;

	protected AbstractTask() {
	}

	public AbstractTask(String taskName, ITaskTypeProvider taskTypeProvider, Date triggeringDate) {
		setTaskName(taskName);
		setTaskType(taskTypeProvider.getTaskType());
		setTriggeringDate(triggeringDate);
	}

	@Override
	public void run() {
		try {
			transactionalOperations();
		} catch (RuntimeException e) {
			LOGGER.warn("Rollback durant le traitement d'une tâche", e);
			freeTask();
		}
	}

	@Transactional
	private void transactionalOperations() {
		beforeTask();
		doTaskInTransaction();
		afterTask();
	}

	private void beforeTask() {
		queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);
		if (queuedTaskHolder == null) {
			throw new IllegalArgumentException("Aucune tâche existante avec l'id " + getQueuedTaskHolderId());
		}
		if (queuedTaskHolder.getStartDate() == null || queuedTaskHolder.getCompletionDate() != null) {
			throw new IllegalArgumentException("Données incohérentes pour la tâche " + queuedTaskHolder);
		}
	}

	private void afterTask() {
		// Une fois l'opération exécutée, on marque la tâche comme complétée
		queuedTaskHolder.setCompletionDate(new Date());
	}

	@Transactional
	private void freeTask() {
		QueuedTaskHolder queuedTaskHolder = this.queuedTaskHolderService.getById(this.getQueuedTaskHolderId());
		queuedTaskHolder.setStartDate(null);
	}

	public abstract void doTaskInTransaction();

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
