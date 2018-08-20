package org.iglooproject.infinispan.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.iglooproject.infinispan.action.SwitchRoleResult;
import org.iglooproject.infinispan.listener.ViewChangedEventCoordinatorListener;
import org.iglooproject.infinispan.model.AddressWrapper;
import org.iglooproject.infinispan.model.DoIfRoleWithLock;
import org.iglooproject.infinispan.model.IAction;
import org.iglooproject.infinispan.model.IAttribution;
import org.iglooproject.infinispan.model.ILock;
import org.iglooproject.infinispan.model.ILockAttribution;
import org.iglooproject.infinispan.model.ILockRequest;
import org.iglooproject.infinispan.model.INode;
import org.iglooproject.infinispan.model.IRole;
import org.iglooproject.infinispan.model.IRoleAttribution;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.infinispan.remoting.transport.jgroups.JGroupsAddress;
import org.javatuples.Pair;

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
	 * @return see {@link DoIfRoleWithLock}
	 * @throws ExecutionException if runnable throw an exception
	 */
	DoIfRoleWithLock doIfRoleWithLock(ILockRequest lockRequest, Runnable runnable) throws ExecutionException;

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

	/**
	 * @see ViewChangedEventCoordinatorListener
	 * Handle cluster view changes ; handled only by coordinator.
	 * 
	 * @param viewChangedEvent
	 */
	void onViewChangedEvent(ViewChangedEvent viewChangedEvent);

	/**
	 * @return members list as jgroups address (that are serializable)
	 */
	List<AddressWrapper> getMembers();

	/**
	 * @return nodes list ; includes only connected nodes.
	 */
	List<INode> getNodes();

	/**
	 * @return nodes list ; includes all known nodes, even disconnected ones.
	 */
	List<INode> getAllNodes();

	/**
	 * @return current locks list
	 */
	Set<ILock> getLocks();

	/**
	 * @param iLock
	 * @return {@link ILockAttribution} of the given lock
	 */
	ILockAttribution getLockAttribution(ILock iLock);

	/**
	 * List both known roles (from rolesProvider) and assigned roles (from current role assignation).
	 */
	Set<IRole> getAllRolesForAssignation();

	/**
	 * List both known roles (from rolesProvider) and requested roles (from current role requests).
	 */
	Set<IRole> getAllRolesForRolesRequests();

	/**
	 * @param iRole
	 * @return {@link IRoleAttribution} of the given role
	 */
	IRoleAttribution getRoleAttribution(IRole iRole);

	/**
	 * @param iRole
	 * @return {@link IAttribution} of the given role's request
	 */
	IAttribution getRoleRequestAttribution(IRole iRole);

	/**
	 * Force a role unassignment (current owner is not involved in the process)
	 * 
	 * @param iRole
	 */
	void unassignRole(IRole iRole);

	/**
	 * Handle full and clean reassignment of a role ; role release by owner, then role capture by the target
	 * 
	 * @param iRole role to reassign
	 * @param iNode new owner
	 * @return pair of {@link SwitchRoleResult} and error message ; if {@link SwitchRoleResult#SWITCH_SUCCESS}, then
	 *   reassignment is a success
	 */
	Pair<SwitchRoleResult, String> assignRole(IRole iRole, INode iNode);

	/**
	 * Perform a role rebalance (try to capture role one by one) so that all unassigned role should be captured at the
	 * rebalance end. Delay between capture allows to perform a fair distribution of roles.
	 */
	void doRebalanceRoles();

	/**
	 * @return {@link JGroupsAddress} of local node
	 */
	AddressWrapper getLocalAddress();

	/**
	 * @see IInfinispanClusterService#assignRole(IRole, INode)
	 * Handle release phase of role reassign on current owner node.
	 * 
	 * @param role to release
	 * @return pair of {@link SwitchRoleResult} and error message ; if {@link SwitchRoleResult#SWITCH_STEP_SUCCESS},
	 *   then release is a success
	 */
	Pair<SwitchRoleResult, String> doReleaseRole(IRole role);

	/**
	 * @see IInfinispanClusterService#assignRole(IRole, INode)
	 * Handle capture phase of role reassign on new owner node.
	 * 
	 * @param role to capture
	 * @return pair of {@link SwitchRoleResult} and error message ; if {@link SwitchRoleResult#SWITCH_STEP_SUCCESS},
	 *   then release is a success
	 */
	Pair<SwitchRoleResult, String> doCaptureRole(IRole role);

	/**
	 * Force deletion of a role request.
	 * 
	 * @param iRole the role we want to delete the role request
	 */
	void removeRoleRequest(IRole iRole);

	/**
	 * Provides raw cache manager. May be used to add and modify custom caches. May not be used to modify
	 * {@link IInfinispanClusterService} handled caches
	 * 
	 * @return
	 */
	EmbeddedCacheManager getCacheManager();

	/**
	 * Perform an action on a local/remote node (if targeted node is local node, infinispan communication is still used).
	 * Action may be result less ({@link IAction#needsResult()}) or a broadcast ({@link IAction#isBroadcast()}).
	 * Currently, result-less actions are performed asynchronously and timeouts are ignored (when method returns,
	 * action may or may not be performed).
	 * Result and broadcast {@link IAction} is not handled.
	 * Targeted and result actions are performed ; caller waits at most for the given timeout. Once the timeout is
	 * exhausted, not already performed action can still be performed (no auto-cancellation).
	 * 
	 * All actions are performed by targeted nodes in a common thread, so a current long action can cause following
	 * action's timeout.
	 * 
	 * @param action action to perform
	 * @param timeout maximum amout of time to wait the result of a targeted (no-broadcast) {@link IAction}
	 * @param unit unit of the provided timeout
	 * @return action result
	 * @throws ExecutionException encountered exception
	 * @throws TimeoutException if timeout reached before result availability (this timeout includes all the action
	 *   handling - send to target node, action execution, result sent back).
	 */
	<A extends IAction<V>, V> V syncedAction(A action, int timeout, TimeUnit unit) throws ExecutionException, TimeoutException;

	/**
	 * Check if roles are all assigned  (always) and if balance is fair (only if checkFairness is true). For each node
	 * with too much or too few roles, a comment is pushed in return String collection.
	 * 
	 * @param checkFairness true if check balance fairness is to be checked
	 * @return <true if fair/false if unfair, comments>
	 */

	Pair<Boolean, List<String>> checkRoles(boolean checkFairness);

	/**
	 * Trigger a rebalance. clearRoles is done by target node. Rebalance involves all nodes.
	 * 
	 * @param clearRoles true if clear all roles is needed before rebalance
	 * @param rolesToKeep if clearRoles, role's keys of roles not to reassign
	 */
	void rebalanceRoles(boolean clearRoles, Collection<String> rolesToKeep);

}
