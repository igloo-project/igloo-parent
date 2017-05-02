package fr.openwide.core.jpa.more.business.task.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.infinispan.model.ILock;
import fr.openwide.core.infinispan.model.ILockRequest;
import fr.openwide.core.infinispan.model.IPriorityQueue;
import fr.openwide.core.infinispan.model.impl.LockRequest;
import fr.openwide.core.infinispan.service.IInfinispanClusterService;

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
									setWorking(working);
									entityManagerUtils.openEntityManager();
									try {
										tryConsumeTask(queuedTaskHolderId);
									} finally {
										entityManagerUtils.closeEntityManager();
									}
								}
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}
						}
					});
				} catch (ExecutionException e) {
					throw new IllegalStateException(e);
				} finally {
					setWorking(false);
				}
			}
		} catch (InterruptedException interrupted) {
			Thread.currentThread().interrupt();
		}
	}
}
