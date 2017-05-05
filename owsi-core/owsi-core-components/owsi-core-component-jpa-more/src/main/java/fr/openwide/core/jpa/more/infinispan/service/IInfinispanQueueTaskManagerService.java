package fr.openwide.core.jpa.more.infinispan.service;

import fr.openwide.core.infinispan.model.INode;
import fr.openwide.core.jpa.more.infinispan.model.QueueTaskManagerStatus;

public interface IInfinispanQueueTaskManagerService {

	QueueTaskManagerStatus getQueueTaskManagerStatus(INode node);

	QueueTaskManagerStatus createQueueTaskManagerStatus();

}
