package fr.openwide.core.jpa.more.business.task.dao;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;

public interface IQueuedTaskHolderDao extends IGenericEntityDao<Integer, QueuedTaskHolder> {

	/**
	 * Récupère en base la tâche suivante à exécuter.
	 * 
	 * @param taskType type de tâche à récupérer
	 * 
	 * @return cette tâche, <code>null</code> si aucune tâche à exécuter.
	 */
	QueuedTaskHolder getNextTaskForExecution(String taskType);

	/**
	 * Renvoi aléatoirement l'une tâche considérée comme bloquée, c'est à dire dont le temps d'exécution est jugé
	 * trop long. La limite du temps d'exécution est fixée par la propriété 'sitra.tasks.executionTimeLimitInSeconds'
	 * dans la configuration.
	 * 
	 * @param taskType type de tâche à récupérer
	 * @param executionTimeLimitInSeconds le temps d'exécution au-delà duquel la tâche est considrée comme bloquée.
	 * 
	 * @return l'une des tâches bloquées, <code>null</code> si aucune tâche n'est bloquée.
	 */
	QueuedTaskHolder getStalledTask(String taskType, int executionTimeLimitInSeconds);
}
