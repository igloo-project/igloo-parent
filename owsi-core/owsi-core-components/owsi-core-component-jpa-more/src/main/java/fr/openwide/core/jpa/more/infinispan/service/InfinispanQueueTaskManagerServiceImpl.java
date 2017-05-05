package fr.openwide.core.jpa.more.infinispan.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

import fr.openwide.core.infinispan.model.INode;
import fr.openwide.core.infinispan.service.IInfinispanClusterService;
import fr.openwide.core.jpa.more.business.task.model.IQueueId;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderManager;
import fr.openwide.core.jpa.more.infinispan.action.QueueTaskManagerStatusAction;
import fr.openwide.core.jpa.more.infinispan.model.QueueTaskManagerStatus;
import fr.openwide.core.jpa.more.infinispan.model.TaskQueueStatus;

public class InfinispanQueueTaskManagerServiceImpl implements IInfinispanQueueTaskManagerService{

	private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanQueueTaskManagerServiceImpl.class);

	@Autowired
	private IInfinispanClusterService infinispanClusterService;

	@Autowired
	private IQueuedTaskHolderManager queuedTaskHolderManager;

	@Override
	public QueueTaskManagerStatus getQueueTaskManagerStatus(INode node) {

		QueueTaskManagerStatus result = null;

		try {
			result = infinispanClusterService.syncedAction(
					QueueTaskManagerStatusAction.get(node.getAddress()),
					10,
					TimeUnit.SECONDS);
		} catch (ExecutionException e) {
			LOGGER.error("Erreur lors de la récupération du QueueTaskManagerStatus", e);
		} catch (TimeoutException e) {
			LOGGER.error("Timeout lors de la récupération du QueueTaskManagerStatus");
		}
		return result;
	}

	@Override
	public QueueTaskManagerStatus createQueueTaskManagerStatus(){
		QueueTaskManagerStatus queueTaskManagerStatus = new QueueTaskManagerStatus();

		queueTaskManagerStatus.setQueueManagerActive(queuedTaskHolderManager.isActive());

		Map<String, IQueueId> queuesById = new HashMap<>();
		Map<String, TaskQueueStatus> taskQueueStatusById = Maps.newHashMap();

		for(IQueueId queueId : queuedTaskHolderManager.getQueueIds()){
			String id = queueId.getUniqueStringId();
			queuesById.put(id, queueId);
			taskQueueStatusById.put(
					id,
					new TaskQueueStatus(
							queueId.getUniqueStringId(),
							queuedTaskHolderManager.isTaskQueueActive(id),
							queuedTaskHolderManager.getNumberOfRunningTasks(id),
							queuedTaskHolderManager.getNumberOfWaitingTasks(id),
							queuedTaskHolderManager.getNumberOfTaskConsumer(id)
					)
			);
		}
		queueTaskManagerStatus.setQueuesById(queuesById);
		queueTaskManagerStatus.setTaskQueueStatusById(taskQueueStatusById);

		return queueTaskManagerStatus;
	}

}
