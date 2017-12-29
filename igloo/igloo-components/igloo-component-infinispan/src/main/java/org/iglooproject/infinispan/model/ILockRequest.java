package org.iglooproject.infinispan.model;

public interface ILockRequest {

	IRole getRole();

	ILock getLock();

	IPriorityQueue getPriorityQueue();

}
