package igloo.jpa.batch.processor;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import igloo.jpa.batch.monitor.ProcessorMonitorContext;
import igloo.jpa.batch.monitor.ThreadLocalInitializingCallable;
import igloo.jpa.batch.util.TransactionWrapperCallable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.iglooproject.functional.Function2;
import org.iglooproject.spring.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionOperations;

public class ThreadedProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ThreadedProcessor.class);

  private static final RejectedExecutionHandler THROW_EXCEPTION_ON_REJECTED_EXECUTION =
      new ThreadPoolExecutor.AbortPolicy();

  private static final Function2<Runnable, Callable<Object>> RUNNABLE_TO_CALLABLE =
      input -> input == null ? null : Executors.callable(input);

  private final int threadPoolSize;
  private final int keepAliveTime;
  private final TimeUnit keepAliveTimeUnit;
  private final int maxTotalDuration;
  private final TimeUnit maxTotalDurationTimeUnit;

  private final boolean abortAllOnError;

  private final Integer loggingCheckIntervalTime;
  private final TimeUnit loggingCheckIntervalTimeUnit;
  private final Integer maxLoggingTime;
  private final TimeUnit maxLoggingTimeUnit;
  private final Integer maxLoggingIncrement;
  private final Logger progressLogger;

  private ProcessorMonitorContext monitorContext;

  public ThreadedProcessor(
      int threadPoolSize,
      int maxTotalDuration,
      TimeUnit maxTotalDurationUnit,
      int keepAliveTime,
      TimeUnit keepAliveTimeUnit,
      boolean abortAllOnError) {
    this(
        threadPoolSize,
        maxTotalDuration,
        maxTotalDurationUnit,
        keepAliveTime,
        keepAliveTimeUnit,
        abortAllOnError,
        null,
        null,
        null,
        null,
        null,
        null);
  }

  public ThreadedProcessor(
      int threadPoolSize,
      int maxTotalDuration,
      TimeUnit maxTotalDurationUnit,
      int keepAliveTime,
      TimeUnit keepAliveTimeUnit,
      boolean abortAllOnError,
      Integer loggingCheckIntervalTime,
      TimeUnit loggingCheckIntervalTimeUnit,
      Integer maxLoggingTime,
      TimeUnit maxLoggingTimeUnit,
      Integer maxLoggingIncrement,
      Logger progressLogger) {
    super();
    this.threadPoolSize = threadPoolSize;
    this.keepAliveTime = keepAliveTime;
    this.keepAliveTimeUnit = keepAliveTimeUnit;
    this.abortAllOnError = abortAllOnError;
    this.maxTotalDuration = maxTotalDuration;
    this.maxTotalDurationTimeUnit = maxTotalDurationUnit;
    this.loggingCheckIntervalTime = loggingCheckIntervalTime;
    this.loggingCheckIntervalTimeUnit = loggingCheckIntervalTimeUnit;
    this.maxLoggingTime = maxLoggingTime;
    this.maxLoggingTimeUnit = maxLoggingTimeUnit;
    this.maxLoggingIncrement = maxLoggingIncrement;
    this.progressLogger = progressLogger;
  }

  public void runWithoutTransaction(String loggerContext, Collection<? extends Runnable> runnables)
      throws ExecutionException {
    runWithoutTransaction(loggerContext, runnables, null);
  }

  public void runWithoutTransaction(
      String loggerContext, Collection<? extends Runnable> runnables, Integer totalItems)
      throws ExecutionException {
    runWithTransaction(loggerContext, runnables, null, totalItems);
  }

  public void runWithTransaction(
      final String loggerContext,
      Collection<? extends Runnable> runnables,
      TransactionOperations TransactionOperations,
      Integer totalItems)
      throws ExecutionException {
    callWithTransaction(
        loggerContext,
        runnables.stream().map(RUNNABLE_TO_CALLABLE).collect(Collectors.toList()),
        TransactionOperations,
        totalItems);
  }

  public <T> List<T> callWithoutTransaction(
      String loggerContext, Collection<? extends Callable<T>> callables) throws ExecutionException {
    return callWithoutTransaction(loggerContext, callables, null);
  }

  public <T> List<T> callWithoutTransaction(
      String loggerContext, Collection<? extends Callable<T>> callables, Integer totalItems)
      throws ExecutionException {
    return callWithTransaction(loggerContext, callables, null, totalItems);
  }

  public <T> List<T> callWithTransaction(
      final String loggerContext,
      Collection<? extends Callable<T>> callables,
      TransactionOperations TransactionOperations,
      Integer totalItems)
      throws ExecutionException {
    List<ListenableFuture<T>> futures = Lists.newArrayList();

    // ThreadedPoolExecutor cannot be reused after shutdown, so we must instantiate it on each call.
    BlockingQueue<Runnable> workingQueue = new LinkedBlockingQueue<>();
    ThreadPoolExecutor threadPoolExecutor =
        new ThreadPoolExecutor(
            threadPoolSize,
            threadPoolSize,
            keepAliveTime,
            keepAliveTimeUnit,
            workingQueue,
            THROW_EXCEPTION_ON_REJECTED_EXECUTION);
    ListeningExecutorService executor = MoreExecutors.listeningDecorator(threadPoolExecutor);

    threadPoolExecutor.prestartAllCoreThreads();

    Thread loggingThread = null;
    this.monitorContext = new ProcessorMonitorContext();
    if (progressLogger != null) {
      loggingThread = new Thread(new LoggingRunnable(loggerContext));
    }
    if (totalItems != null) {
      this.monitorContext.getTotalItems().set(totalItems);
    }
    try {
      if (loggingThread != null) {
        loggingThread.start();
      }

      try {
        List<FutureCallback<? super T>> callbacks = Lists.newArrayList();
        if (abortAllOnError) {
          callbacks.add(
              new ThreadPoolExecutorAbortFutureCallback(threadPoolExecutor, loggerContext));
        }
        for (Callable<T> callable : callables) {
          Callable<T> wrappedCallable =
              new ThreadLocalInitializingCallable<>(
                  callable, ProcessorMonitorContext.getThreadLocal(), monitorContext);
          if (TransactionOperations != null) {
            // All executions are wrapped in a separate transaction
            // (if the template was provided)
            wrappedCallable =
                new TransactionWrapperCallable<>(TransactionOperations, wrappedCallable);
          }

          ListenableFuture<T> future = executor.submit(wrappedCallable);

          for (FutureCallback<? super T> callback : callbacks) {
            Futures.addCallback(future, callback, MoreExecutors.directExecutor());
          }

          futures.add(future);
        }
      } catch (RejectedExecutionException e) {
        LOGGER.error(
            "{} - Some tasks could not be submitted to the ThreadPoolExecutor."
                + " Submitted tasks: {} of {}."
                + " This is probably due to an error in another task causing the ThreadPoolExecutor to shut down.",
            loggerContext,
            futures.size(),
            callables.size());
      }

      executor.shutdown();

      List<T> results = Lists.newArrayList();
      List<ExecutionException> executionExceptions = Lists.newArrayList();
      boolean interrupted = false;

      try {
        boolean terminated = executor.awaitTermination(maxTotalDuration, maxTotalDurationTimeUnit);
        if (!terminated) {
          LOGGER.error(
              "{} - Tasks haven't terminated before the timeout of {} {}",
              loggerContext,
              maxTotalDuration,
              maxTotalDurationTimeUnit.name());
        }
        LOGGER.info("{} - {} elements treated", loggerContext, this.monitorContext.getDoneItems());
        if (this.monitorContext.getFailedItems().get() > 0) {
          LOGGER.error(
              "{} - {} elements in error",
              loggerContext,
              this.monitorContext.getFailedItems().get());
        }
        if (this.monitorContext.getIgnoredItems().get() > 0) {
          LOGGER.info(
              "{} - {} elements ignored",
              loggerContext,
              this.monitorContext.getIgnoredItems().get());
        }

        for (Future<T> future : futures) {
          try {
            T result = future.get(0, TimeUnit.SECONDS);
            results.add(result);
          } catch (ExecutionException e) {
            executionExceptions.add(e);
          } catch (CancellationException | TimeoutException timeoutException) {
            // already notified by a warn
            future.cancel(true);
            interrupted = true;
          }
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException("Batch error: work interrupted.", e);
      }

      if (!executionExceptions.isEmpty()) {
        Iterator<ExecutionException> it = executionExceptions.iterator();
        ExecutionException e = new ExecutionException(it.next().getCause());
        while (it.hasNext()) {
          e.addSuppressed(it.next().getCause());
        }
        throw e;
      }

      if (interrupted) {
        throw new IllegalStateException("Batch error: a work thread was interrupted.");
      }

      return results;
    } finally {
      ProcessorMonitorContext.unset();
      if (loggingThread != null) {
        loggingThread.interrupt();
        try {
          loggingThread.join();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          LOGGER.warn(
              "{} - Thread interrupted while waiting for the end of the logging thread execution.",
              loggerContext);
        }
      }
    }
  }

  protected class LoggingRunnable implements Runnable {

    private final String loggerContext;

    private long startTime;

    private long lastLoggingTime;

    private int lastDoneItems;

    public LoggingRunnable(String loggerContext) {
      super();
      this.loggerContext = loggerContext;
    }

    @Override
    public void run() {
      startTime = System.currentTimeMillis();
      lastLoggingTime = startTime;
      lastDoneItems = 0;
      while (!Thread.currentThread().isInterrupted()) {
        try {
          Thread.sleep(loggingCheckIntervalTimeUnit.toMillis(loggingCheckIntervalTime));
          log(false);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          log(true);
          LOGGER.info("Logging thread interrupted.");
          return;
        }
      }
      log(true);
    }

    public void log(boolean force) {
      if (progressLogger.isInfoEnabled()) {
        long currentTime = System.currentTimeMillis();
        int totalItems = monitorContext.getTotalItems().get();
        int doneItems = monitorContext.getDoneItems().get();
        int ignoredItems = monitorContext.getIgnoredItems().get();
        if (force
            || (currentTime - lastLoggingTime) > maxLoggingTimeUnit.toMillis(maxLoggingTime)
            || (doneItems - lastDoneItems) > maxLoggingIncrement) {
          Float speedSinceStart = (float) doneItems / (float) (currentTime - startTime);
          int roundedSpeedSinceStart = Math.round(speedSinceStart * 1000);

          Float speedSinceLast =
              (float) (doneItems - lastDoneItems) / (float) (currentTime - lastLoggingTime);
          int roundedSpeedSinceLast = Math.round(speedSinceLast * 1000);

          lastLoggingTime = currentTime;
          lastDoneItems = doneItems;

          StringBuilder sb = new StringBuilder();
          if (StringUtils.hasText(loggerContext)) {
            sb.append(loggerContext).append(" - ");
          }
          sb.append(
              "In progress: {} / {} ({} ignored, {} items/s since start, {} items/s since last log)");
          progressLogger.info(
              sb.toString(),
              doneItems,
              totalItems - ignoredItems,
              ignoredItems,
              roundedSpeedSinceStart,
              roundedSpeedSinceLast);
        }
      }
      if (progressLogger.isDebugEnabled()) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.hasText(loggerContext)) {
          sb.append(loggerContext).append(" - ");
        }
        sb.append("Available memory: {} / {}");
        progressLogger.debug(
            sb.toString(),
            StringUtils.humanReadableByteCount(Runtime.getRuntime().freeMemory(), true),
            StringUtils.humanReadableByteCount(Runtime.getRuntime().totalMemory(), true));
      }
    }
  }

  private static class ThreadPoolExecutorAbortFutureCallback implements FutureCallback<Object> {

    private final ThreadPoolExecutor executor;

    private final String loggerContext;

    public ThreadPoolExecutorAbortFutureCallback(
        ThreadPoolExecutor executor, String loggerContext) {
      super();
      this.executor = executor;
      this.loggerContext = loggerContext;
    }

    @Override
    public void onSuccess(Object result) {
      // Do nothing
    }

    @Override
    public void onFailure(Throwable t) {
      Collection<Runnable> nonExecutedTasks = abort();
      // Avoids multiple useless message due to the other tasks's cancellation when aborting
      if (!nonExecutedTasks.isEmpty()) {
        LOGGER.error(
            "{} - Aborted execution of {} tasks because the following exception was caught when running a task",
            loggerContext,
            nonExecutedTasks.size(),
            t);
      }
    }

    private Collection<Runnable> abort() {
      // Stop accepting tasks
      executor.shutdown();

      // Remove all tasks that were not already executed
      List<Runnable> nonExecutedTasks = new ArrayList<>();
      executor.getQueue().drainTo(nonExecutedTasks);

      // Cancel these tasks (if relevant)
      for (Runnable nonExecutedTask : nonExecutedTasks) {
        if (nonExecutedTask instanceof Future<?>) {
          Future<?> future = (Future<?>) nonExecutedTask;
          future.cancel(false);
        }
      }

      return nonExecutedTasks;
    }
  }
}
