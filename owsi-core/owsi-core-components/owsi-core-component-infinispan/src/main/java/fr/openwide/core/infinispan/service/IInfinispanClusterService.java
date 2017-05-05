package fr.openwide.core.infinispan.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.javatuples.Pair;
import org.jgroups.Address;

import fr.openwide.core.infinispan.action.SwitchRoleResult;
import fr.openwide.core.infinispan.model.IAction;
import fr.openwide.core.infinispan.model.IAttribution;
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
	
	/**
	 * List both known roles (from rolesProvider) and assigned roles (from current role assignation).
	 */
	Set<IRole> getAllRolesForAssignation();
	
	/**
	 * List both known roles (from rolesProvider) and requested roles (from current role requests).
	 */
	Set<IRole> getAllRolesForRolesRequests();
	
	IRoleAttribution getRoleAttribution(IRole iRole);
	
	IAttribution getRoleRequestAttribution(IRole iRole);
	
	void unassignRole(IRole iRole);
	
	Pair<SwitchRoleResult, String> assignRole(IRole iRole, INode iNode);

	void doRebalanceRoles();
	
	Address getLocalAddress();

	Pair<SwitchRoleResult, String> doReleaseRole(IRole role);

	Pair<SwitchRoleResult, String> doCaptureRole(IRole role);

	void removeRoleRequest(IRole iRole);

	EmbeddedCacheManager getCacheManager();

	<A extends IAction<V>, V> V syncedAction(A action, int timeout, TimeUnit unit) throws ExecutionException, TimeoutException;

}
