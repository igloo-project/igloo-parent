package fr.openwide.core.infinispan.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.jgroups.Address;

import fr.openwide.core.infinispan.model.ILock;
import fr.openwide.core.infinispan.model.ILockAttribution;
import fr.openwide.core.infinispan.model.ILockRequest;
import fr.openwide.core.infinispan.model.INode;
import fr.openwide.core.infinispan.model.IRole;
import fr.openwide.core.infinispan.model.IRoleAttribution;

public interface IInfinispanClusterService {

	/**
	 * Cluster instance identifier ; if there is a cluster split, each cluster has its own cluster identifier.
	 * Derived from cluster's node addresses.
	 */
	String getClusterIdentifier();

	/**
	 * Is this cluster active (if there is a split, only one cluster should be active)
	 */
	boolean isClusterActive();

	/**
	 * Perform a runnable with a lock. lock is owned only if available. lock is released when method returns.
	 * Already owned lock is ignored.
	 * 
	 * @param needed lock - must be available and will be locked and release during execution
	 * @runnable
	 * @return true if {@link Runnable} is run, false otherwise
	 * @throws ExecutionException if runnable throw an exception
	 */
	boolean doWithLock(ILock withLock, Runnable runnable) throws ExecutionException;

	/**
	 * Perform a runnable if owned role and lock available (not-owned)
	 * @param lockRequest needed role - must be already owned, and optional lock to be taken
	 * @param runnable
	 * @return true if {@link Runnable} is run, false otherwise (if role is not owned, or if lock is already taken)
	 * @throws ExecutionException if runnable throw an exception
	 */
	boolean doIfRoleWithLock(ILockRequest lockRequest, Runnable runnable) throws ExecutionException;

	/**
	 * Check a priority queue ; return true if identifier can run its task now, false if it must wait.
	 * 
	 * Warning: priority handling can be achieved only if each node call this method at most 1 time concurrently.
	 * 
	 * @throws ExecutionException 
	 */
	boolean doWithLockPriority(ILockRequest withLock, Runnable runnable) throws ExecutionException;

	void init();

	void stop();

	void onViewChangedEvent(ViewChangedEvent viewChangedEvent);

	List<Address> getMembers();

	List<INode> getNodes();
	
	List<INode> getAllNodes();

	Set<ILock> getLocks();
	
	ILockAttribution getLockAttribution(ILock iLock);
	
	Set<IRole> getRoles();
	
	IRoleAttribution getRoleAttribution(IRole iRole);
	
	void deleteRole(IRole iRole);
	
	void assignRole(IRole iRole, INode iNode);

	void doRebalanceRoles();

}
