package org.iglooproject.jpa.more.business.task.model;

import org.iglooproject.infinispan.model.ILock;
import org.iglooproject.infinispan.model.IPriorityQueue;

public interface IInfinispanQueue extends IQueueId {

	ILock getLock();

	IPriorityQueue getPriorityQueue();

	boolean handleInfinispan();

}
