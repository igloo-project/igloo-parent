package fr.openwide.core.jpa.more.business.task.executor;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.ITaskTypeProvider;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;
import fr.openwide.core.spring.util.SpringBeanUtils;

public abstract class AbstractPersistentTaskExecutor extends ThreadPoolTaskExecutor {
	private static final long serialVersionUID = 8795027800414538701L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPersistentTaskExecutor.class);

	// Permet d'inclure la classe de l'objet dans le Json obtenu, pour faciliter la déserialisation
	protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enableDefaultTyping(DefaultTyping.NON_FINAL);

	@Autowired
	private IQueuedTaskHolderService queuedTaskHolderService;
	
	@Autowired
	private ApplicationContext applicationContext;

	private String taskType;

	public AbstractPersistentTaskExecutor(ITaskTypeProvider taskTypeProvider) {
		super();
		this.taskType = taskTypeProvider.getTaskType();
	}

	/**
	 * Pousse une tâche dans la queue afin qu'elle soit exécutée par le scheduler.
	 * Pré-requis : cette méthode doit obligatoirement être appelée à l'intérieur d'une transaction.
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void execute(Runnable runnable) {
		AbstractTask task;
		try {
			task = AbstractTask.class.cast(runnable);
		} catch (ClassCastException e) {
			LOGGER.error("Seuls les objets héritant de AbstractTask peuvent être placés en file d'attente");
			throw new IllegalArgumentException("Invalid task : " + runnable, e);
		}
		
		String serializedTask;
		try {
			serializedTask = OBJECT_MAPPER.writeValueAsString(task);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Serialized task: " + OBJECT_MAPPER.writeValueAsString(task));
			}
			
			QueuedTaskHolder newQueuedTaskHolder = new QueuedTaskHolder(
					task.getTaskName(),
					task.getTaskType(),
					serializedTask);
			newQueuedTaskHolder.setTriggeringDate(task.getTriggeringDate());
			
			queuedTaskHolderService.create(newQueuedTaskHolder);
		} catch (IOException e) {
			LOGGER.error("Erreur durant la sérialisation de la tâche " + task, e);
		} catch (Exception e) {
			LOGGER.error("Erreur durant la sauvegarde en base de la tâche " + task, e);
		}
	}

	/**
	 * Exécute les tâches présentes dans la queue les unes après les autres jusqu'à ce qu'il n'en reste plus.
	 */
	public void runTasks() {
		QueuedTaskHolder lockedTask = null;
		while ((lockedTask = tryLockTask()) != null) {
			try {
				AbstractTask runnableTask = OBJECT_MAPPER.readValue(lockedTask.getSerializedTask(), AbstractTask.class);
				runnableTask.setQueuedTaskHolderId(lockedTask.getId());
				SpringBeanUtils.autowireBean(applicationContext, runnableTask);
				runnableTask.run();
			} catch (IOException e) {
				LOGGER.error("Erreur lors de la désérialisation de la tâche " + lockedTask, e);
			}
		}
	}

	protected QueuedTaskHolder tryLockTask() {
		int tries = getLockTriesBeforeGivingUp();
		
		while (tries > 0) {
			try {
				return obtainLockedTask();
			} catch (OptimisticLockingFailureException e) {
				tries--;
				StringBuilder errorMsg = new StringBuilder("Impossible d'obtenir le verrou pour la tâche suivante. ");
				errorMsg.append(tries == 0 ? "Abandon." : "Nouvel essai...");
				LOGGER.warn(errorMsg.toString());
			}
		}
		return null;
	}

	@Transactional
	protected QueuedTaskHolder obtainLockedTask() {
		QueuedTaskHolder queuedTask = queuedTaskHolderService.getNextTaskForExecution(taskType);
		if (queuedTask != null) {
			queuedTask.setStartDate(new Date());
		}
		return queuedTask;
	}

	/**
	 * Réinitialise les tâches bloquées, i.e. les tâches ayant dépassé leur timeout d'exécution.
	 */
	public void hypervisor() {
		while (tryResetStalledTask());
	}

	protected boolean tryResetStalledTask() {
		int tries = getLockTriesBeforeGivingUp();
		
		while (tries > 0) {
			try {
				return resetStalledTask() != null;
			} catch (OptimisticLockingFailureException e) {
				tries--;
				StringBuilder errorMsg = new StringBuilder("Impossible d'obtenir le verrou pour une tâche bloquée. ");
				errorMsg.append(tries == 0 ? "Abandon." : "Nouvel essai...");
				LOGGER.warn(errorMsg.toString());
			}
		}
		return false;
	}

	@Transactional
	protected QueuedTaskHolder resetStalledTask() {
		QueuedTaskHolder stalledTask = queuedTaskHolderService.getRandomStalledTask(taskType, getExecutionTimeLimitInSeconds());
		if (stalledTask != null) {
			stalledTask.setStartDate(null);
		}
		return stalledTask;
	}

	protected abstract int getExecutionTimeLimitInSeconds();

	protected abstract int getLockTriesBeforeGivingUp();
}
