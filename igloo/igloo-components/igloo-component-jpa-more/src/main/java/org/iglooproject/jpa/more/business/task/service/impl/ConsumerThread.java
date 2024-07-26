package org.iglooproject.jpa.more.business.task.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Longs;
import com.google.common.util.concurrent.RateLimiter;
import java.io.IOException;
import org.iglooproject.jpa.more.business.task.model.AbstractTask;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderManager;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderService;
import org.iglooproject.jpa.more.config.spring.AbstractTaskManagementConfig;
import org.iglooproject.jpa.more.property.JpaMoreTaskPropertyIds;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.SpringBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

/**
 * A consumer thread with the ability to try stopping gracefully.
 *
 * @see #stop(int)
 */
class ConsumerThread extends Thread {

  public static final Logger LOGGER = LoggerFactory.getLogger(ConsumerThread.class);

  private static final long MAX_STOP_TIMEOUT_WAIT_INCREMENT_MS = 100L;

  @Autowired protected ApplicationContext applicationContext;

  @Autowired protected IQueuedTaskHolderService queuedTaskHolderService;

  @Autowired protected EntityManagerUtils entityManagerUtils;

  @Autowired
  @Qualifier(AbstractTaskManagementConfig.OBJECT_MAPPER_BEAN_NAME)
  private ObjectMapper queuedTaskHolderObjectMapper;

  @Autowired private IPropertyService propertyService;

  @Autowired private IQueuedTaskHolderManager queuedTaskHolderManager;

  @Autowired private IRendererService rendererService;

  protected final TaskQueue queue;

  /** A flag indicating whether new tasks should be consumed. */
  protected volatile boolean active = false;

  /**
   * A flag indicating whether the thread is currently executing a task.
   *
   * <p>Differs from <code>isAlive()</code> in that <code>isAlive()</code> returns true even if the
   * thread is only waiting for a task to be offered in the queue.
   */
  protected volatile boolean working = false;

  protected final long startDelay;

  /**
   * task consumption take some time, so rateLimiter only limit rate when there is no task to
   * consume.
   */
  protected final RateLimiter rateLimiter = RateLimiter.create(0.5);

  public ConsumerThread(String name, long startDelay, TaskQueue queue) {
    super(name);
    this.startDelay = startDelay;
    this.queue = queue;
  }

  @Override
  public synchronized void start() {
    active = true;
    try {
      super.start();
    } catch (RuntimeException e) {
      active = false;
      throw e;
    }
  }

  public void stop(long stopTimeout) {
    /*
     * Signal the run() method that it should not consume any more tasks.
     */
    active = false;

    try {
      /*
       * If a task is currently being handled, we wait for it to complete within the given time limit.
       */
      long timeRemaining = stopTimeout;
      while (timeRemaining > 0 && isWorking()) {
        /*
         * Wait for small durations of time, so that we'll stop waiting as
         * soon as the consumer thread stops even if the timeout is huge.
         */
        long step = Longs.min(MAX_STOP_TIMEOUT_WAIT_INCREMENT_MS, timeRemaining);
        Thread.sleep(step); // NOSONAR findbugs:SWL_SLEEP_WITH_LOCK_HELD
        /*
         * Sleep in synchronized method does not harm because there is no
         * high concurrency on this method (we simply don't want concurrent
         * execution)
         */
        timeRemaining -= step;
      }
    } catch (InterruptedException e) {
      /*
       * The current thread (the one waiting for the consumer thread) was interrupted
       * Just put back the interrupt marker on the current thread before we interrupt the consumer thread
       */
      Thread.currentThread().interrupt();
    } finally {
      /*
       * If the current thread (the one waiting for the consumer thread) was interrupted, or if the timeout
       * was reached when waiting for the task to complete, or if the task completed in time, we order the
       * consumer thread to stop ASAP.
       */
      this.interrupt();
      LOGGER.info("Interrupt sent to {}", this.getName());
    }
  }

  public synchronized boolean isWorking() {
    return working;
  }

  public synchronized boolean isActive() {
    return active;
  }

  public synchronized void setWorking(boolean working) {
    this.working = working;
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

      /*
       * Before starting tasks consumption we check that required
       * execution context can be opened if needed
       */
      if (propertyService.get(
          JpaMoreTaskPropertyIds.queueStartExecutionContextWaitReady(queue.getId()))) {
        while (!rendererService.context().isReady()) {
          Thread.sleep(5000l);
        }
      }

      while (active && !Thread.currentThread().isInterrupted()) {
        Long queuedTaskHolderId;
        // if there are tasks to consume, rateLimiter is not limiting due to task consumption's
        // duration
        // this allow to have a chance to read working flag when there is no job to be done.
        rateLimiter.acquire();
        try {
          queuedTaskHolderId = queue.take();
          setWorking(true);
          // if not active, we are about to stop
          if (active) {
            entityManagerUtils.openEntityManager();
            try {
              tryConsumeTask(queuedTaskHolderId);
            } finally {
              entityManagerUtils.closeEntityManager();
            }
          }
        } finally {
          setWorking(false);
        }
      }
    } catch (InterruptedException interrupted) {
      Thread.currentThread().interrupt();
    }
  }

  /** TOTALLY safe, NEVER throws any exception. */
  protected void tryConsumeTask(Long queuedTaskHolderId) {
    QueuedTaskHolder queuedTaskHolder;
    try {
      queuedTaskHolder = queuedTaskHolderService.getById(queuedTaskHolderId);
    } catch (RuntimeException e) {
      LOGGER.error(
          "Error while trying to fetch a task from database before run (holder: "
              + queuedTaskHolderId
              + ").",
          e);
      return;
    }

    AbstractTask runnableTask;
    try {
      runnableTask =
          queuedTaskHolderObjectMapper.readValue(
              queuedTaskHolder.getSerializedTask(), AbstractTask.class);
    } catch (RuntimeException | IOException e) {
      LOGGER.error(
          "Error while trying to deserialize a task before run (holder: " + queuedTaskHolder + ").",
          e);
      return;
    }

    try {
      runnableTask.setQueuedTaskHolderId(queuedTaskHolder.getId());
      SpringBeanUtils.autowireBean(applicationContext, runnableTask);
    } catch (RuntimeException e) {
      LOGGER.error(
          "Error while trying to initialize a task before run (holder: " + queuedTaskHolder + ").",
          e);
      return;
    }

    try {
      runnableTask.run();
    } catch (RuntimeException e) {
      LOGGER.error(
          "Error while trying to consume a task (holder: "
              + queuedTaskHolder
              + "); the task holder was probably left in a stale state.",
          e);
    }

    try {
      queuedTaskHolderManager.onTaskFinish(queuedTaskHolder.getId());
    } catch (RuntimeException e) {
      LOGGER.error("Error while signaling task finish for {}", queuedTaskHolderId);
    }
  }
}
