package org.iglooproject.jpa.more.business.task.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import org.iglooproject.infinispan.model.ILock;
import org.iglooproject.infinispan.model.ILockRequest;
import org.iglooproject.infinispan.model.IPriorityQueue;
import org.iglooproject.infinispan.model.impl.LockRequest;
import org.iglooproject.infinispan.service.IInfinispanClusterService;

/**
 * A consumer thread with the ability to try stopping gracefully.
 * @see #stop(int)
 */
class ConsumerInfinispanAwareThread extends ConsumerThread {
	
	@Autowired
	private IInfinispanClusterService infinispanClusterService;
	
	private final ILock lock;
	private final IPriorityQueue priorityQueue;
	
	public ConsumerInfinispanAwareThread(String name, long startDelay, TaskQueue queue,
			ILock lock, IPriorityQueue priorityQueue) {
		super(name, startDelay, queue);
		this.lock = lock;
		this.priorityQueue = priorityQueue;
	}
	
	@Override
	public void run() {
		try {
			if (startDelay > 0) {
				Thread.sleep(startDelay);
			}
			/*
			 * condition: permits thread to finish gracefully (stop was
			 * signaled, last taken element had been consumed, we can
			 * stop without any other action)
			 */
			while (active && !Thread.currentThread().isInterrupted()) {
				// if there are tasks to consume, rateLimiter is not limiting due to task consumption's duration
				// this allow to have a chance to read working flag when there is no job to be done.
				rateLimiter.acquire();
				try {
					ILockRequest lockRequest = LockRequest.with(null, lock, priorityQueue);
					// check role / lock with priority and perform
					infinispanClusterService.doWithLockPriority(lockRequest, new Runnable() {
						@Override
						public void run() {
							try {
								Long queuedTaskHolderId = queue.poll(100, TimeUnit.MILLISECONDS);
								
								if (queuedTaskHolderId != null) {
									setWorking(true);
									// if not active, we are about to stop, ignoring task
									if (active) {
										entityManagerUtils.openEntityManager();
										try {
											tryConsumeTask(queuedTaskHolderId);
										} finally {
											entityManagerUtils.closeEntityManager();
										}
									}
								}
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							} finally {
								setWorking(false);
							}
						}
					});
				} catch (ExecutionException e) {
					throw new IllegalStateException(e);
				}
			}
		} catch (InterruptedException interrupted) {
			Thread.currentThread().interrupt();
		}
	}
}
