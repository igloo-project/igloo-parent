package fr.openwide.core.jpa.more.business.task.model;

import fr.openwide.core.infinispan.model.ILock;
import fr.openwide.core.infinispan.model.IPriorityQueue;

public interface IInfinispanQueue extends IQueueId {

	ILock getLock();

	IPriorityQueue getPriorityQueue();

	boolean handleInfinispan();

}
