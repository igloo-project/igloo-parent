package fr.openwide.core.infinispan.model.impl;

import java.io.Serializable;

import fr.openwide.core.infinispan.model.ILock;
import fr.openwide.core.infinispan.model.ILockRequest;
import fr.openwide.core.infinispan.model.IPriorityQueue;
import fr.openwide.core.infinispan.model.IRole;

public class LockRequest implements ILockRequest, Serializable{
	
	private static final long serialVersionUID = 2970721656938054479L;
	
	private IRole role;
	private ILock lock;
	private IPriorityQueue priorityQueue;

	public static LockRequest with(IRole role, ILock lock) {
		return with(role, lock, null);
	}

	public static LockRequest with(IRole role, ILock lock, IPriorityQueue priorityQueue) {
		return new LockRequest(role, lock, priorityQueue);
	}

	private LockRequest() {
	}

	private LockRequest(IRole role, ILock lock, IPriorityQueue priorityQueue) {
		super();
		this.role = role;
		this.lock = lock;
		this.priorityQueue = priorityQueue;
	}

	@Override
	public IRole getRole() {
		return role;
	}

	@Override
	public ILock getLock() {
		return lock;
	}

	@Override
	public IPriorityQueue getPriorityQueue() {
		return priorityQueue;
	}

}
