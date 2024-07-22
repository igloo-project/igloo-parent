package igloo.jpa.batch.executor;

import com.google.common.collect.Lists;
import igloo.jpa.batch.processor.ThreadedProcessor;
import igloo.jpa.batch.runnable.IBatchRunnable;
import igloo.jpa.batch.util.ProcessorProgressLogger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MultithreadedBatchExecutor extends AbstractBatchExecutor<MultithreadedBatchExecutor> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MultithreadedBatchExecutor.class);

  private static final Logger PROGRESS_LOGGER =
      LoggerFactory.getLogger(ProcessorProgressLogger.class);

  private int threads = 2;

  private int timeoutInMinutes = 15;

  private boolean abortAllOnExecutionError = true;

  public MultithreadedBatchExecutor threads(int threads) {
    this.threads = threads;
    return this;
  }

  public MultithreadedBatchExecutor timeoutInMinutes(int timeout) {
    this.timeoutInMinutes = timeout;
    return this;
  }

  public MultithreadedBatchExecutor abortAllOnExecutionError(boolean abortAllOnExecutionError) {
    this.abortAllOnExecutionError = abortAllOnExecutionError;
    return this;
  }

  public void run(
      String context, final List<Long> entityIds, final IBatchRunnable<Long> batchRunnable) {
    Instant startTime = Instant.now();

    LOGGER.info("Beginning batch for %1$s: %2$d objects", context, entityIds.size());

    TransactionTemplate transactionTemplate =
        newTransactionTemplate(
            batchRunnable.getWriteability(), TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    try {
      LOGGER.info("    preExecute start");

      try {
        transactionTemplate.execute(
            new TransactionCallback<Void>() {
              @Override
              public Void doInTransaction(TransactionStatus status) {
                batchRunnable.preExecute();
                return null;
              }
            });
      } catch (RuntimeException e) {
        throw new ExecutionException("Exception on preExecute()", e);
      }

      LOGGER.info("    preExecute end");

      LOGGER.info("    starting batch executions");

      List<List<Long>> entityIdsPartitions = Lists.partition(entityIds, batchSize);
      List<Runnable> runnables = Lists.newArrayList();
      for (final List<Long> entityPartition : entityIdsPartitions) {
        Runnable runnable =
            new Runnable() {
              @Override
              public void run() {
                batchRunnable.executePartition(entityPartition);
              }
            };
        runnables.add(runnable);
      }

      createThreadedProcessor(batchSize, timeoutInMinutes)
          .runWithTransaction(context, runnables, transactionTemplate, entityIds.size());

      LOGGER.info("    end of batch executions");

      LOGGER.info("    postExecute start");

      try {
        transactionTemplate.execute(
            new TransactionCallback<Void>() {
              @Override
              public Void doInTransaction(TransactionStatus status) {
                batchRunnable.postExecute();
                return null;
              }
            });
      } catch (RuntimeException e) {
        throw new ExecutionException("Exception on postExecute()", e);
      }

      LOGGER.info("    postExecute end");

      logEnd(context, startTime, null);
    } catch (ExecutionException e) {
      logEnd(context, startTime, e);
      try {
        LOGGER.info("    onError start");
        batchRunnable.onError(e);
        LOGGER.info("    onError end (exception was NOT propagated)");
      } catch (RuntimeException e2) {
        LOGGER.info("    onError end (exception WAS propagated)");
        throw e2;
      }
    }
  }

  protected final ThreadedProcessor createThreadedProcessor(
      int maxLoggingIncrement, int timeoutInMinutes) {
    return new ThreadedProcessor(
        threads,
        timeoutInMinutes,
        TimeUnit.MINUTES,
        1,
        TimeUnit.MINUTES,
        abortAllOnExecutionError,
        2,
        TimeUnit.SECONDS, // intervalle minimum pour le logging
        30,
        TimeUnit.SECONDS, // intervalle de temps maxi entre deux logs
        maxLoggingIncrement, // intervalle de nombre d'éléments traités maxi entre deux logs
        PROGRESS_LOGGER);
  }

  protected void logEnd(String context, Instant startTime, Exception e) {
    long duration = Duration.between(startTime, Instant.now()).toMillis();

    StringBuilder sb = new StringBuilder(String.format("%1$s - Migrated items ", context));
    if (duration < 1000) {
      sb.append(String.format("in %1$d ms", duration));
    } else if (duration < 60000) {
      sb.append(
          String.format(
              "in %1$s s",
              BigDecimal.valueOf(duration / 1000f).setScale(3, RoundingMode.HALF_UP).toString()));
    } else {
      sb.append(
          String.format(
              "in %1$s mn",
              BigDecimal.valueOf(duration / 60000f).setScale(2, RoundingMode.HALF_UP).toString()));
    }
    if (e != null) {
      // Log this as info anyway, since error handling is done elsewhere
      sb.append(String.format(", but caught exception '%s'.", e));
    }
    PROGRESS_LOGGER.info(sb.toString());
  }

  @Override
  protected MultithreadedBatchExecutor thisAsT() {
    return this;
  }
}
