package org.iglooproject.jpa.more.infinispan.service;

import org.iglooproject.infinispan.model.INode;
import org.iglooproject.jpa.more.infinispan.action.SwitchStatusQueueTaskManagerResult;
import org.iglooproject.jpa.more.infinispan.model.QueueTaskManagerStatus;

public interface IInfinispanQueueTaskManagerService {

	QueueTaskManagerStatus getQueueTaskManagerStatus(INode node);

	QueueTaskManagerStatus createQueueTaskManagerStatus();

	SwitchStatusQueueTaskManagerResult start();

	SwitchStatusQueueTaskManagerResult stop();

	SwitchStatusQueueTaskManagerResult startQueueManager(INode node);

	SwitchStatusQueueTaskManagerResult stopQueueManager(INode node);

}
