package fr.openwide.core.infinispan.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.infinispan.Cache;
import org.infinispan.filter.CollectionKeyFilter;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.cachelistener.event.CacheEntryEvent;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.infinispan.remoting.transport.jgroups.JGroupsAddress;
import org.javatuples.Pair;
import org.jgroups.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;

import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.infinispan.action.RebalanceAction;
import fr.openwide.core.infinispan.action.RoleCaptureAction;
import fr.openwide.core.infinispan.action.RoleReleaseAction;
import fr.openwide.core.infinispan.action.SwitchRoleResult;
import fr.openwide.core.infinispan.listener.CacheEntryCreateEventListener;
import fr.openwide.core.infinispan.listener.ViewChangedEventCoordinatorListener;
import fr.openwide.core.infinispan.model.IAction;
import fr.openwide.core.infinispan.model.IAttribution;
import fr.openwide.core.infinispan.model.ILeaveEvent;
import fr.openwide.core.infinispan.model.ILock;
import fr.openwide.core.infinispan.model.ILockAttribution;
import fr.openwide.core.infinispan.model.ILockRequest;
import fr.openwide.core.infinispan.model.INode;
import fr.openwide.core.infinispan.model.IPriorityQueue;
import fr.openwide.core.infinispan.model.IRole;
import fr.openwide.core.infinispan.model.IRoleAttribution;
import fr.openwide.core.infinispan.model.impl.Attribution;
import fr.openwide.core.infinispan.model.impl.LeaveEvent;
import fr.openwide.core.infinispan.model.impl.LockAttribution;
import fr.openwide.core.infinispan.model.impl.Node;
import fr.openwide.core.infinispan.model.impl.RoleAttribution;

public class InfinispanClusterServiceImpl implements IInfinispanClusterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanClusterServiceImpl.class);

	private static final String CACHE_ACTIONS = "__ACTIONS__";
	private static final String CACHE_ACTIONS_RESULTS = "__ACTIONS_RESULTS__";
	private static final String CACHE_LEAVE = "__LEAVE__";
	private static final String CACHE_LOCKS = "__LOCKS__";
	private static final String CACHE_NODES = "__NODES__";
	private static final String CACHE_PRIORITY_QUEUES = "__PRIORITY_QUEUES__";
	private static final String CACHE_ROLES = "__ROLES__";
	private static final String CACHE_ROLES_REQUESTS = "__ROLES_REQUESTS__";

	private static final String CACHE_KEY_ACTION_ROLES_REBALANCE = "ROLES_REBALANCE";

	private static final List<String> CACHES;
	static {
		Builder<String> builder = ImmutableList.builder();
		builder.add(
				CACHE_ACTIONS,
				CACHE_LEAVE,
				CACHE_LOCKS,
				CACHE_NODES,
				CACHE_PRIORITY_QUEUES,
				CACHE_ROLES,
				CACHE_ROLES_REQUESTS
		);
		CACHES = builder.build();
	}

	private final String nodeName;
	private final EmbeddedCacheManager cacheManager;
	private final IRolesProvider rolesProvider;
	private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
	// 1 rebalance each 15s. max
	private final RateLimiter rebalanceRateLimiter = RateLimiter.create(1.0/15);
	private final IActionFactory actionFactory;

	private final ConcurrentMap<String, Object> actionMonitors = Maps.<String, Object>newConcurrentMap();

	private boolean initialized = false;
	private boolean stopped = false;

	public InfinispanClusterServiceImpl(String nodeName,
			EmbeddedCacheManager cacheManager,
			IRolesProvider rolesProvider,
			IActionFactory actionFactory) {
		super();
		this.nodeName = nodeName;
		this.cacheManager = cacheManager;
		this.rolesProvider = rolesProvider;
		this.actionFactory = actionFactory;
		// don't wait for delayed tasks after shutdown (even if already planned)
		this.executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		this.executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		this.executor.prestartAllCoreThreads();
	}

	@Override
	public synchronized void init() {
		LOGGER.debug("Starting cacheManager");
		cacheManager.start();
		LOGGER.debug("cacheManager started");
		String address = String.format("[%s]", getAddress());
		if ( ! initialized) {
			LOGGER.debug("{} Initializing {}", address, toStringClusterNode());
			
			LOGGER.debug("{} Starting caches", address);
			for (String cacheName : CACHES) {
				if ( ! cacheManager.isRunning(cacheName)) {
					cacheManager.getCache(cacheName);
					if (address == null) {
						address = String.format("[%s]", getAddress());
					}
					LOGGER.debug("{} Cache {} started", address, cacheName);
				} else {
					LOGGER.debug("{} Cache {} already running", address, cacheName);
				}
			}
			LOGGER.debug("{} Caches started", address);
			
			LOGGER.debug("{} Viewed members {}", address, Joiner.on(",").join(cacheManager.getMembers()));
			Node node = Node.from(getAddress(), nodeName);
			LOGGER.debug("{} Register node informations {}", address, node);
			getNodesCache().put(getAddress(), node);
			
			LOGGER.debug("{} Register listeners", address);
			// view change
			cacheManager.addListener(new ViewChangedEventCoordinatorListener(this));
			
			// action queue
			getActionsCache().addListener(new CacheEntryCreateEventListener<IAction<?>>() {
				@Override
				public void onAction(CacheEntryEvent<String, IAction<?>> value) {
					InfinispanClusterServiceImpl.this.onAction(value);
				}
			});
			
			// result queue
			getActionsResultsCache().addListener(new CacheEntryCreateEventListener<IAction<?>>() {
				@Override
				public void onAction(CacheEntryEvent<String, IAction<?>> value) {
					InfinispanClusterServiceImpl.this.onResult(value);
				}
			});
			
			executor.schedule(new Runnable() {
				@Override
				public void run() {
					InfinispanClusterServiceImpl.this.rebalanceRoles();
				}
			}, 5, TimeUnit.SECONDS);
			
			initialized = true;
		}
	}

	@Override
	public List<Address> getMembers() {
		return ImmutableList.<Address>copyOf(Lists.transform(cacheManager.getMembers(), JGROUPS_ADDRESS_TO_ADDRESS));
	}
	
	@Override
	public List<INode> getNodes() {
		return ImmutableList.copyOf(Lists.transform(
				getMembers(),
				new Function<Address, INode>() {
					@Override
					public INode apply(Address input) {
						return getNodesCache().get(input);
					}
				}
		));
	}

	@Override
	public List<INode> getAllNodes() {
		return ImmutableList.copyOf(getNodesCache().values());
	}
	
	@Override
	public Set<ILock> getLocks(){
		return ImmutableSet.copyOf(getLocksCache().keySet());
	}
	
	@Override
	public ILockAttribution getLockAttribution(ILock iLock){
		return getLocksCache().get(iLock);
	}
	
	@Override
	public Set<IRole> getAllRolesForAssignation() {
		return ImmutableSet.<IRole>builder().addAll(rolesProvider.getRoles()).addAll(getRolesCache().keySet()).build();
	}
	
	@Override
	public Set<IRole> getAllRolesForRolesRequests() {
		return ImmutableSet.<IRole>builder().addAll(rolesProvider.getRoles()).addAll(getRolesRequestsCache().keySet()).build();
	}
	
	@Override
	public IRoleAttribution getRoleAttribution(IRole iRole){
		return getRolesCache().get(iRole);
	}

	@Override
	public synchronized void stop() {
		String address = String.format("[%s]", getAddress());
		if (initialized) {
			LOGGER.warn("Stopping {}", InfinispanClusterServiceImpl.class.getSimpleName());
			if (stopped) {
				LOGGER.warn("{} Stop seems be called twice on {}", address, toStringClusterNode());
			}
			
			Date leaveDate = new Date();
			
			INode previousNode = getNodesCache().get(getAddress());
			Node node = Node.from(previousNode, leaveDate);
			
			getLeaveCache().put(getAddress(), LeaveEvent.from(leaveDate));
			getNodesCache().put(getAddress(), node);
			
			cacheManager.stop();
			// stop accepting new tasks
			executor.shutdown();
			
			try {
				executor.awaitTermination(3, TimeUnit.SECONDS);
			} catch (InterruptedException e) {} // NOSONAR
			
			List<Runnable> runnables = executor.shutdownNow();
			if ( ! runnables.isEmpty()) {
				LOGGER.warn("{} tasks dropped by {}.executor", runnables.size(), InfinispanClusterServiceImpl.class.getSimpleName());
			}
			
			stopped = true;
			LOGGER.warn("Stopped {}", InfinispanClusterServiceImpl.class.getSimpleName());
		} else {
			LOGGER.warn("{} Ignored stop event as cluster not initialized {}", address, toStringClusterNode());
		}
	}

	private String toStringClusterNode() {
		return String.format("%s:%s:%s", getClass().getSimpleName(), cacheManager.getClusterName(), getAddress());
	}

	@Override
	public String getClusterIdentifier() {
		List<Address> members = getMembers();
		Collections.sort(members);
		return Joiner.on(",").join(members);
	}

	@Override
	public boolean isClusterActive() {
		// TODO Auto-generated method stub
		// check database for actives clusters
		// order active clusters
		// true if first active
		return true;
	}

	@Override
	public boolean doIfRoleWithLock(ILockRequest lockRequest, Runnable runnable) throws ExecutionException {
		// try to retrieve lock
		IRoleAttribution roleAttribution = getRolesCache().getOrDefault(lockRequest.getRole(), null);
		try {
			if (roleAttribution.match(getAddress())) {
				// if lock is owned, we can run
				if (lockRequest.getLock() != null) {
					return doWithLock(lockRequest.getLock(), runnable);
				} else {
					runnable.run();
				}
				return true;
			} else {
				// else return false
				return false;
			}
		} catch (Exception e) {
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			throw new ExecutionException(e);
		}
	}

	@Override
	public boolean doWithLock(ILock withLock, Runnable runnable) throws ExecutionException {
		// try to retrieve lock
		ILockAttribution lockAttribution =
				getLocksCache().putIfAbsent(withLock, LockAttribution.from(getAddress(), new Date()));
		try {
			if (lockAttribution == null) {
				// if lock was absent we can run
				runnable.run();
				return true;
			} else {
				// else return false
				return false;
			}
		} catch (Exception e) {
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			throw new ExecutionException(e);
		} finally {
			ILockAttribution removedAttribution = getLocksCache().remove(withLock);
			if (removedAttribution == null || ! removedAttribution.match(getAddress())) {
				// as we put value only if absent, we must not retrieve another value than `Address` here
				LOGGER.warn("Inconsistent address removal: expected `{}` ; removed `{}`",
						getAddress(),
						removedAttribution != null ? removedAttribution.getOwner() : "null");
			}
		}
	}

	@Override
	public boolean doWithLockPriority(ILockRequest lockRequest, Runnable runnable) throws ExecutionException {
		Cache<IPriorityQueue, List<IAttribution>> cache = getPriorityQueuesCache();
		// if startBatch returns false, then there is already a batch running
		if ( ! cache.startBatch()) {
			throw new IllegalStateException("Nested batch detected!");
		}
		boolean commit = true;
		boolean priorityAllowed = true;
		try {
			// get lock values
			cache.getAdvancedCache().lock(lockRequest.getPriorityQueue());
			List<IAttribution> values = cache.getOrDefault(lockRequest.getPriorityQueue(), Lists.<IAttribution>newArrayList());
			if ( ! values.contains(cacheManager.getAddress())) {
				// get a priority slot if absent
				values.add(Attribution.from(getAddress(), new Date()));
				cache.put(lockRequest.getPriorityQueue(), values);
			}
			
			if (values.size() > 0 && values.get(0).match(getAddress())) {
				// priority is allowed (first slot taken)
				// allow second phase and remove priority slot
				priorityAllowed = true;
				values.remove(0);
			} else {
				// priority is not allowed
				priorityAllowed = false;
			}
		} finally {
			cache.endBatch(commit);
		}
		
		if (priorityAllowed) {
			// return doWithLock result (true - job done ; false - job not launch)
			return doWithLock(lockRequest.getLock(), runnable);
		} else {
			return false;
		}
	}
	
	@Override
	public Address getLocalAddress() {
		return JGROUPS_ADDRESS_TO_ADDRESS.apply(cacheManager.getAddress());
	}

	private Address getAddress() {
		if (cacheManager.getAddress() != null) {
			return JGROUPS_ADDRESS_TO_ADDRESS.apply(cacheManager.getAddress());
		} else {
			return null;
		}
	}

	private Cache<IRole, IRoleAttribution> getRolesCache() {
		return cacheManager.<IRole, IRoleAttribution>getCache(CACHE_ROLES);
	}

	private Cache<IPriorityQueue, List<IAttribution>> getPriorityQueuesCache() {
		return cacheManager.<IPriorityQueue, List<IAttribution>>getCache(CACHE_PRIORITY_QUEUES);
	}

	private Cache<Address, ILeaveEvent> getLeaveCache() {
		return cacheManager.<Address, ILeaveEvent>getCache(CACHE_LEAVE);
	}

	private Cache<IRole, IAttribution> getRolesRequestsCache() {
		return cacheManager.<IRole, IAttribution>getCache(CACHE_ROLES_REQUESTS);
	}

	private Cache<ILock, ILockAttribution> getLocksCache() {
		return cacheManager.<ILock, ILockAttribution>getCache(CACHE_LOCKS);
	}

	private Cache<Address, INode> getNodesCache() {
		return cacheManager.<Address, INode>getCache(CACHE_NODES);
	}

	private Cache<String, IAction<?>> getActionsCache() {
		return cacheManager.<String, IAction<?>>getCache(CACHE_ACTIONS);
	}

	private Cache<String, IAction<?>> getActionsResultsCache() {
		return cacheManager.<String, IAction<?>>getCache(CACHE_ACTIONS_RESULTS);
	}

	public void rebalanceRoles() {
		// TODO refactor
		getActionsCache().put(CACHE_KEY_ACTION_ROLES_REBALANCE, RebalanceAction.rebalance(getAddress()));
	}

	@Override
	public void onViewChangedEvent(ViewChangedEvent viewChangedEvent) {
		// NOTE : lists cannot be null
		List<Address> newMembers = Lists.transform(viewChangedEvent.getNewMembers(), JGROUPS_ADDRESS_TO_ADDRESS);
		List<Address> oldMembers = Lists.transform(viewChangedEvent.getOldMembers(), JGROUPS_ADDRESS_TO_ADDRESS);
		
		List<Address> added = Lists.newArrayList(newMembers);
		added.removeAll(oldMembers);
		List<Address> removed = Lists.newArrayList(oldMembers);
		removed.removeAll(newMembers);
		
		LOGGER.debug("Processing view removed nodes ({}) {}", removed.size(), toStringClusterNode());
		for (Address removedItem : removed) {
			LOGGER.debug("Processing view removed node {}", removedItem, toStringClusterNode());
			ILeaveEvent leaveEvent = getLeaveCache().getOrDefault(removedItem, null);
			INode node = getNodesCache().getOrDefault(removedItem, null);
			if (leaveEvent == null) {
				if (node == null) {
					LOGGER.warn("Unknown node {} left cluster without leave event {}", removedItem, toStringClusterNode());
				} else {
					LOGGER.warn("Node {} left cluster without leave event {}", node, toStringClusterNode());
				}
			} else {
				Date now = new Date();
				long elapsed = now.getTime() - leaveEvent.getLeaveDate().getTime();
				String timing = String.format("%d ms.", elapsed);
				if (node == null) {
					LOGGER.warn("Unknown node {} left cluster with leave event {} (elapsed {})", removedItem, toStringClusterNode(), timing);
				} else {
					LOGGER.info("Node {} left cluster without leave event {} (elapsed {})", node, toStringClusterNode(), timing);
				}
			}
		}
		
		LOGGER.debug("Processing view added items ({}) {}", added.size(), toStringClusterNode());
		for (Address addedItem : added) {
			if (viewChangedEvent.isMergeView()) {
				LOGGER.warn("Merge node {} {}", addedItem, toStringClusterNode());
			} else {
				LOGGER.debug("New node {} {}", addedItem, toStringClusterNode());
			}
		}
		
		// if view change is due to a split, roles kept assigned
		// rebalance should be safe as only unassigned roles may be rebalanced
		rebalanceRoles();
	}
	
	@Override
	public void unassignRole(IRole iRole){
		getRolesCache().remove(iRole);
	}
	
	@Override
	public void removeRoleRequest(IRole iRole){
		getRolesRequestsCache().remove(iRole);
	}
	
	@Override
	public Pair<SwitchRoleResult, String> assignRole(IRole iRole, INode iNode) {
		// push request, ask for release, ask for capture, return
		Stopwatch switchWatch = Stopwatch.createStarted();
		
		// request (replace old element is allowed)
		getRolesRequestsCache().put(iRole, RoleAttribution.from(iNode.getAddress(), new Date()));
		IRoleAttribution roleAttribution = getRolesCache().get(iRole);
		
		Pair<SwitchRoleResult, String> stepResult = null;
		
		// release
		if (roleAttribution != null) {
			Stopwatch releaseWatch = Stopwatch.createStarted();
			
			try {
				stepResult = syncedAction(RoleReleaseAction.release(roleAttribution.getOwner(), iRole), 10, TimeUnit.SECONDS);
			} catch (ExecutionException e) {
				return Pair.with(SwitchRoleResult.SWITCH_UNKNOWN_ERROR, String.format("Unknown exception during release - %s", e.getCause().getMessage()));
			} catch (TimeoutException e) {
				return Pair.with(SwitchRoleResult.SWITCH_RELEASE_TIMEOUT, String.format("Timeout during release (%d ms. elapsed)", releaseWatch.elapsed(TimeUnit.MILLISECONDS)));
			}
			
			if ( ! SwitchRoleResult.SWITCH_STEP_SUCCESS.equals(stepResult.getValue0())) {
				return stepResult;
			}
		}
		
		// capture
		{
			Stopwatch captureWatch = Stopwatch.createStarted();
			try {
				stepResult = syncedAction(RoleCaptureAction.capture(iNode.getAddress(), iRole), 10, TimeUnit.SECONDS);
			} catch (ExecutionException e) {
				return Pair.with(SwitchRoleResult.SWITCH_UNKNOWN_ERROR, String.format("Unknown exception during capture - %s", e.getCause().getMessage()));
			} catch (TimeoutException e) {
				return Pair.with(SwitchRoleResult.SWITCH_CAPTURE_TIMEOUT, String.format("Timeout during release (%d ms. elapsed)", captureWatch.elapsed(TimeUnit.MILLISECONDS)));
			}
		}
		
		// return
		if ( ! SwitchRoleResult.SWITCH_STEP_SUCCESS.equals(stepResult.getValue0())) {
			return stepResult;
		} else {
			return Pair.with(SwitchRoleResult.SWITCH_SUCCESS, String.format("Switch done in %d ms.", switchWatch.elapsed(TimeUnit.MILLISECONDS)));
		}
	}

	@Override
	public void doRebalanceRoles() {
		doRebalanceRoles(1000);
	}

	@Override
	public Pair<SwitchRoleResult, String> doReleaseRole(IRole role) {
		if ( ! getRolesCache().getAdvancedCache().startBatch()) {
			throw new IllegalStateException("Nested batch detected!");
		}
		boolean commit = true;
		boolean releaseDone = false;
		getRolesCache().getAdvancedCache().lock(role);
		try {
			IRoleAttribution attribution = getRolesCache().get(role);
			if (attribution != null && attribution.match(getAddress())) {
				getRolesCache().remove(role);
				releaseDone = true;
			}
		} finally {
			getRolesCache().getAdvancedCache().endBatch(commit);
		}
		
		if (getRolesCache().get(role) == null) {
			return Pair.with(SwitchRoleResult.SWITCH_STEP_SUCCESS, "OK");
		} else if (releaseDone) {
			return Pair.with(SwitchRoleResult.SWITCH_STEP_INCONSISTENCY, "Map remove done, but role not released !");
		} else {
			return Pair.with(SwitchRoleResult.SWITCH_STEP_INCONSISTENCY, "Map remove cannot be done !");
		}
	}

	@Override
	public Pair<SwitchRoleResult, String> doCaptureRole(IRole role) {
		// role request checked in a insecure way (but role capture is safe)
		if (getRolesRequestsCache().get(role) != null && ! getRolesRequestsCache().get(role).match(getAddress())) {
			return Pair.with(SwitchRoleResult.SWITCH_STEP_INCONSISTENCY, "Role is already requested by another node");
		}
		
		getRolesCache().putIfAbsent(role, RoleAttribution.from(getAddress(), new Date()));
		
		// request release not safe as it can conflict with another capture
		getRolesRequestsCache().remove(role);
		
		if (getRolesCache().get(role) == null) {
			return Pair.with(SwitchRoleResult.SWITCH_STEP_INCONSISTENCY, "Role is available but was not captured");
		} else if ( ! getRolesCache().get(role).match(getAddress())) {
			return Pair.with(SwitchRoleResult.SWITCH_CAPTURE_NOT_AVAILABLE, "Role is not available at capture time");
		} else {
			return Pair.with(SwitchRoleResult.SWITCH_STEP_SUCCESS, "OK");
		}
	}

	public synchronized void doRebalanceRoles(int waitWeight) {
		List<IRole> roles = Lists.newArrayList(rolesProvider.getRoles());
		
		LOGGER.debug("Starting role rebalance {}", toStringClusterNode());
		List<IRole> acquiredRoles = Lists.newArrayList();
		List<IRole> newRoles = Lists.newArrayList();
		while (roles.size() > 0) {
			try {
				// we wait rand(0-1s) + (aquiredRoles number s. * waitWeight)
				// the most roles we acquire, the more we wait (to let other nodes a chance to acquire new roles)
				TimeUnit.MILLISECONDS.sleep((waitWeight * acquiredRoles.size()) + Math.round(Math.random() * 1000));
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				LOGGER.debug("Interrupted while rebalancing {}", toStringClusterNode());
				return;
			}
			IRole role = roles.remove(0);
			LOGGER.debug("Starting role rebalance - trying {} {}", role, toStringClusterNode());
			IAttribution request = getRolesRequestsCache().getOrDefault(role, null);
			if (request != null) {
				if (request.match(getAddress())) {
					IAttribution previousAttribution =
							getRolesCache().putIfAbsent(role, RoleAttribution.from(getAddress(), new Date()));
					if (previousAttribution != null) {
						if ( ! previousAttribution.match(getAddress())) {
							LOGGER.debug("Role rebalance - request on {} fails; already attributed to {} {}",
									role, previousAttribution, toStringClusterNode());
						} else {
							LOGGER.debug("Role rebalance - request on {} uselessly; already attributed {}",
									role, toStringClusterNode());
							acquiredRoles.add(role);
						}
					} else {
						LOGGER.info("Role rebalance - request on {} succeeded {}",
									role, toStringClusterNode());
						acquiredRoles.add(role);
						newRoles.add(role);
					}
				} else {
					// TODO timeout request (?)
					LOGGER.debug("Role rebalance - {} skipped because it exists a running request on it {}",
							role, toStringClusterNode());
				}
			} else {
				IAttribution previousAttribution =
						getRolesCache().putIfAbsent(role, RoleAttribution.from(getAddress(), new Date()));
				if (previousAttribution != null) {
					if ( ! previousAttribution.match(getAddress())) {
						LOGGER.debug("Role rebalance - try {} uselessly; already attributed to {} {}",
								role, previousAttribution, toStringClusterNode());
					} else {
						LOGGER.debug("Role rebalance - try {} uselessly; already attributed {}",
								role, toStringClusterNode());
						acquiredRoles.add(role);
					}
				} else {
					LOGGER.info("Role rebalance - try on {} succeeded {}",
								role, toStringClusterNode());
					acquiredRoles.add(role);
					newRoles.add(role);
				}
			}
			
			// get rid of request if is acquired
			if (acquiredRoles.contains(role) && request != null && request.match(getAddress())) {
				if (getRolesRequestsCache().remove(role, request)) {
					LOGGER.info("Role rebalance - honored request {} removed {}", request, toStringClusterNode());
				}
			}
		}
		
		LOGGER.debug("Ending role rebalance {}", toStringClusterNode());
		
		if ( ! newRoles.isEmpty()) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Role rebalance - new roles acquired: {} {}", Joiner.on(",").join(newRoles), toStringClusterNode());
				LOGGER.debug("Role rebalance - roles: {} {}", Joiner.on(",").join(acquiredRoles), toStringClusterNode());
			}
		}
	}

	protected void onAction(CacheEntryEvent<String, IAction<?>> value) {
		final String key = value.getKey();
		final IAction<?> action = value.getValue();
		if (CACHE_KEY_ACTION_ROLES_REBALANCE.equals(value.getKey())) {
			LOGGER.debug("Rebalance action received");
			if (rebalanceRateLimiter.tryAcquire()) {
				// perform asynchronously
				executor.submit(new Runnable() {
					@Override
					public void run() {
						try {
							doRebalanceRoles();
							LOGGER.debug("Rebalance action performed");
						} finally {
							getActionsCache().remove(key);
						}
					}
				});
			} else {
				LOGGER.debug("Rebalance action skipped due to rate limiter");
			}
		} else {
			if (getAddress().equals(value.getValue().getTarget())) {
				executor.submit(new Runnable() {
					@Override
					public void run() {
						try {
							if (actionFactory != null) {
								actionFactory.prepareAction(action);
							}
							action.run();
							getActionsResultsCache().put(value.getKey(), action);
						} finally {
							getActionsCache().remove(key);
						}
					}
				});
			}
		}
	}

	protected void onResult(CacheEntryEvent<String, IAction<?>> value) {
		if (actionMonitors.containsKey(value.getKey())) {
			Object monitor = actionMonitors.get(value.getKey());
			synchronized (monitor){
				monitor.notifyAll();
			}
		}
		actionMonitors.remove(value.getKey());
	}

	@SuppressWarnings("unchecked")
	private <K> void addListener(String cacheName, Object listener, K... keys) {
		addListener(cacheName, listener, true, keys);
	}

	@SuppressWarnings("unchecked")
	private <K> void addListener(String cacheName, Object listener, boolean includeKeys, K... keys) {
		cacheManager.<K, Object>getCache(cacheName).addListener(listener, new CollectionKeyFilter<K>(ImmutableList.<K>copyOf(keys), includeKeys));
	}

	private <A extends IAction<V>, V> V syncedAction(A action, int timeout, TimeUnit unit) throws ExecutionException, TimeoutException {
		String uniqueID = UUID.randomUUID().toString();
		
		// actionMonitors allow to optimize wakeup when we wait for a result.
		Object monitor = new Object();
		actionMonitors.putIfAbsent(uniqueID, monitor);
		
		getActionsCache().put(uniqueID, action);
		
		Stopwatch stopwatch = Stopwatch.createUnstarted();
		while (timeout == -1 || stopwatch.elapsed(TimeUnit.MILLISECONDS) < unit.toMillis(timeout)) {
			if (!stopwatch.isRunning()) {
				stopwatch.start();
			}
			@SuppressWarnings("unchecked")
			A result = (A) getActionsResultsCache().remove(uniqueID);
			if (result != null && result.isDone()) {
				try {
					return result.get();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			} else if (result != null && result.isCancelled()) {
				throw new CancellationException();
			}
			synchronized (monitor) {
				try {
					unit.timedWait(monitor, timeout);
				} catch (InterruptedException e) {} // NOSONAR
			}
		}
		
		throw new TimeoutException();
	}

	private static final class JGroupsAddressToAddress implements SerializableFunction<org.infinispan.remoting.transport.Address, Address> {
		private static final long serialVersionUID = -6249484113042442830L;

		@Override
		public Address apply(org.infinispan.remoting.transport.Address input) {
			return ((JGroupsAddress) input).getJGroupsAddress();
		}
	}

	private static final class JGroupsToJgroupsAddress implements SerializableFunction<Address, org.infinispan.remoting.transport.Address> {
		private static final long serialVersionUID = -6249484113042442830L;

		@Override
		public org.infinispan.remoting.transport.Address apply(Address input) {
			return new JGroupsAddress(input);
		}
	}

	private static final JGroupsAddressToAddress JGROUPS_ADDRESS_TO_ADDRESS = new JGroupsAddressToAddress();
	private static final JGroupsToJgroupsAddress ADDRESS_TO_GROUPS_ADDRESS = new JGroupsToJgroupsAddress();

}
