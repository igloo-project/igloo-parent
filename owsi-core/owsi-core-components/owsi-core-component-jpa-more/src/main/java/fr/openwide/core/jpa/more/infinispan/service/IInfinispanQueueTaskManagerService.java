package fr.openwide.core.jpa.more.infinispan.service;

import fr.openwide.core.infinispan.model.INode;
import fr.openwide.core.jpa.more.infinispan.action.SwitchStatusQueueTaskManagerResult;
import fr.openwide.core.jpa.more.infinispan.model.QueueTaskManagerStatus;

public interface IInfinispanQueueTaskManagerService {

	QueueTaskManagerStatus getQueueTaskManagerStatus(INode node);

	QueueTaskManagerStatus createQueueTaskManagerStatus();

	SwitchStatusQueueTaskManagerResult start();

	SwitchStatusQueueTaskManagerResult stop();

	SwitchStatusQueueTaskManagerResult startQueueManager(INode node);

	SwitchStatusQueueTaskManagerResult stopQueueManager(INode node);

}
