package fr.openwide.core.infinispan.model;

public interface ILockRequest {

	IRole getRole();

	ILock getLock();

	IPriorityQueue getPriorityQueue();

}
