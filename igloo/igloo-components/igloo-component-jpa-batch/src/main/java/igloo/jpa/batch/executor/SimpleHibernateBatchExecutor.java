package igloo.jpa.batch.executor;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import igloo.jpa.batch.runnable.IBatchRunnable;
import igloo.jpa.batch.runnable.Writeability;
import igloo.jpa.batch.util.IBeforeClearListener;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.iglooproject.functional.Joiners;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityCollectionReference;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.query.IQuery;
import org.iglooproject.jpa.query.Queries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionOperations;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimpleHibernateBatchExecutor
    extends AbstractBatchExecutor<SimpleHibernateBatchExecutor> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHibernateBatchExecutor.class);

  @Autowired(required = false)
  private Collection<IBeforeClearListener> clearListeners = ImmutableList.of();

  private ExecutionStrategyFactory executionStrategyFactory =
      ExecutionStrategyFactory.COMMIT_ON_END;

  private List<Class<?>> classesToReindex = Lists.newArrayListWithCapacity(0);

  /**
   * Use one transaction for the whole execution, periodically flushing the Hibernate session and
   * the Hibernate Search indexes.
   *
   * <p>This is the default.
   *
   * <p>If an error occurs, this strategy will leave the database unchanged (with no modification),
   * but <strong>will leave Hibernate Search indexes half-changed</strong> (with only the changes of
   * the successfully executed previous steps).
   *
   * <p>Thus this strategy is fully transactional, but may <strong>corrupt your indexes</strong> on
   * error, leaving them out-of-sync with your database. This requires particular caution when
   * handling errors (for instance full reindex).
   */
  public SimpleHibernateBatchExecutor commitOnEnd() {
    this.executionStrategyFactory = ExecutionStrategyFactory.COMMIT_ON_END;
    return this;
  }

  /**
   * Use one transaction for each step of the execution:
   *
   * <ul>
   *   <li>{@link IBatchRunnable#preExecute()}
   *   <li>each {@link IBatchRunnable#executePartition(List)}
   *   <li>{@link IBatchRunnable#postExecute()}
   * </ul>
   *
   * <p>If an error occurs, this strategy will leave the database and the Hibernate Search indices
   * half-changed (with only the changes of the successfully executed previous steps).
   *
   * <p>Thus this strategy <strong>is not fully transactional</strong>, but will ensure your indexes
   * stay in sync with your database on error.
   */
  public SimpleHibernateBatchExecutor commitOnStep() {
    this.executionStrategyFactory = ExecutionStrategyFactory.COMMIT_ON_STEP;
    return this;
  }

  /**
   * Triggers a full reindex of the given classes at the end of the execution.
   *
   * <p>Mostly useful when using {@link #commitOnEnd()} transaction strategy.
   */
  public SimpleHibernateBatchExecutor reindexClasses(Class<?> clazz, Class<?>... classes) {
    classesToReindex = Lists.asList(clazz, classes);
    return this;
  }

  /** Runs a batch execution against a given list of entities (given by their IDs). */
  public <E extends GenericEntity<Long, ?>> void run(
      final Class<E> clazz, final List<Long> entityIds, final IBatchRunnable<E> batchRunnable) {
    GenericEntityCollectionReference<Long, E> reference =
        new GenericEntityCollectionReference<>(clazz, entityIds);
    runNonConsuming(
        String.format("class %s", clazz), entityService.getQuery(reference), batchRunnable);
  }

  /**
   * Runs a batch execution against a {@link IQuery query}'s result, <strong>expecting that the
   * execution won't change the query's results</strong>.
   *
   * <p>This last requirement allows us to safely break down the execution this way: first execute
   * on <code>query.list(0, 100)</code>, then (if there was a result) <code>query.list(100, 100)
   * </code>, and so on.
   *
   * <p>The <code>query</code> may be:
   *
   * <ul>
   *   <li>Your own implementation (of IQuery, or more particularly of ISearchQuery)
   *   <li>Retrieved from a DAO that used {@link
   *       Queries#fromQueryDsl(com.querydsl.core.support.FetchableQueryBase)} to adapt a QueryDSL
   *       query
   * </ul>
   */
  public <E extends GenericEntity<Long, ?>> void runNonConsuming(
      String loggerContext, final IQuery<E> query, final IBatchRunnable<E> batchRunnable) {
    doRun(loggerContext, query, false, batchRunnable);
  }

  /**
   * Runs a batch execution against a {@link IQuery query}'s result, <strong>expecting that the
   * execution will remove all processed elements from the query's results</strong>.
   *
   * <p>This last requirement allows us to safely break down the execution this way: first execute
   * on <code>query.list(0, 100)</code>, then (if there was a result) execute on <code>
   * query.list(0, 100)</code> <strong>again</strong>, and so on.
   *
   * <p>The <code>query</code> may be:
   *
   * <ul>
   *   <li>Your own implementation (of IQuery, or more particularly of ISearchQuery)
   *   <li>Retrieved from a DAO that used {@link
   *       Queries#fromQueryDsl(com.querydsl.core.support.FetchableQueryBase)} to adapt a QueryDSL
   *       query
   * </ul>
   */
  public <E extends GenericEntity<Long, ?>> void runConsuming(
      String loggerContext, final IQuery<E> query, final IBatchRunnable<E> batchRunnable) {
    Preconditions.checkArgument(
        Writeability.READ_WRITE.equals(batchRunnable.getWriteability()),
        "runConsuming() must be used with a read/write runnable, but %s is read-only",
        batchRunnable);
    doRun(loggerContext, query, true, batchRunnable);
  }

  private <E extends GenericEntity<Long, ?>> void doRun(
      final String loggerContext,
      final IQuery<E> query,
      final boolean consuming,
      final IBatchRunnable<E> batchRunnable) {
    final ExecutionStrategy<E> executionStrategy =
        executionStrategyFactory.create(this, query, batchRunnable);
    executionStrategy
        .getExecuteTransactionOperations()
        .execute(
            new TransactionCallbackWithoutResult() {
              @Override
              protected void doInTransactionWithoutResult(TransactionStatus status) {
                doRun(executionStrategy, loggerContext, consuming);
              }
            });
  }

  private <E extends GenericEntity<Long, ?>> void doRun(
      ExecutionStrategy<E> executionStrategy, String loggerContext, boolean consuming) {
    long expectedTotalCount = executionStrategy.getExpectedTotalCount();
    long offset = 0;
    LOGGER.info("Beginning batch for %1$s: %2$d objects", loggerContext, expectedTotalCount);

    try {
      LOGGER.info("    preExecute start");

      try {
        executionStrategy.preExecute();
      } catch (RuntimeException e) {
        throw new ExecutionException("Exception on preExecute", e);
      }

      LOGGER.info("    preExecute end");

      LOGGER.info("    starting batch executions");

      try {
        int partitionSize;
        do {
          partitionSize =
              executionStrategy.executePartition(
                  // Don't use the offset if the runnable is consuming the query's results
                  consuming ? 0 : offset, batchSize);

          offset += partitionSize;
          LOGGER.info("        treated %1$d/%2$d objects", offset, expectedTotalCount);
        } while (partitionSize > 0);
      } catch (RuntimeException e) {
        throw new ExecutionException("Exception on executePartition", e);
      }

      LOGGER.info("    end of batch executions");

      LOGGER.info("    postExecute start");

      try {
        executionStrategy.postExecute();
      } catch (RuntimeException e) {
        throw new ExecutionException("Exception on postExecute", e);
      }

      LOGGER.info("    postExecute end");

      if (classesToReindex.size() > 0) {
        LOGGER.info("    reindexing classes %1$s", Joiners.onComma().join(classesToReindex));
        try {
          hibernateSearchService.reindexClasses(classesToReindex);
        } catch (ServiceException e) {
          LOGGER.error("    reindexing failure", e);
        }
        LOGGER.info("    end of reindexing");
      }

      LOGGER.info(
          "End of batch for %1$s: %2$d/%3$d objects treated",
          loggerContext, offset, expectedTotalCount);
    } catch (ExecutionException e) {
      LOGGER.info(
          "End of batch for %1$s: %2$d/%3$d objects treated, but caught exception '%s'",
          loggerContext, offset, expectedTotalCount, e);
      try {
        LOGGER.info("    onError start");
        executionStrategy.onError(e);
        LOGGER.info("    onError end (exception was NOT propagated)");
      } catch (RuntimeException e2) {
        LOGGER.info("    onError end (exception WAS propagated)");
        throw e2;
      }
    }
  }

  protected <E extends GenericEntity<Long, ?>> List<E> listEntitiesByIds(
      Class<E> clazz, Collection<Long> entityIds) {
    return entityService.listEntity(clazz, entityIds);
  }

  @Override
  protected SimpleHibernateBatchExecutor thisAsT() {
    return this;
  }

  private enum ExecutionStrategyFactory {
    COMMIT_ON_STEP {
      @Override
      <T> ExecutionStrategy<T> create(
          SimpleHibernateBatchExecutor executor, IQuery<T> query, IBatchRunnable<T> runnable) {
        return new CommitOnStepExecutionStrategy<>(executor, query, runnable);
      }
    },
    COMMIT_ON_END {
      @Override
      <T> ExecutionStrategy<T> create(
          SimpleHibernateBatchExecutor executor, IQuery<T> query, IBatchRunnable<T> runnable) {
        return new CommitOnEndExecutionStrategy<>(executor, query, runnable);
      }
    };

    abstract <T> ExecutionStrategy<T> create(
        SimpleHibernateBatchExecutor executor, IQuery<T> query, IBatchRunnable<T> runnable);
  }

  private abstract static class ExecutionStrategy<T> {

    protected final SimpleHibernateBatchExecutor executor;

    protected final IQuery<T> query;

    protected final IBatchRunnable<T> runnable;

    public ExecutionStrategy(
        SimpleHibernateBatchExecutor executor, IQuery<T> query, IBatchRunnable<T> runnable) {
      super();
      this.executor = executor;
      this.query = query;
      this.runnable = runnable;
    }

    public abstract TransactionOperations getExecuteTransactionOperations();

    public abstract long getExpectedTotalCount();

    public abstract void preExecute();

    /**
     * @param offset
     * @param limit
     * @return The number of items in the partition.
     */
    public abstract int executePartition(long offset, long limit);

    public abstract void postExecute();

    public void onError(ExecutionException e) {
      runnable.onError(e);
    }
  }

  private static final class CommitOnEndExecutionStrategy<T> extends ExecutionStrategy<T> {
    private final boolean isReadOnly = Writeability.READ_ONLY.equals(runnable.getWriteability());

    private CommitOnEndExecutionStrategy(
        SimpleHibernateBatchExecutor executor, IQuery<T> query, IBatchRunnable<T> runnable) {
      super(executor, query, runnable);
    }

    @Override
    public TransactionOperations getExecuteTransactionOperations() {
      return executor.newTransactionTemplate(
          runnable.getWriteability(), TransactionDefinition.PROPAGATION_REQUIRED);
    }

    @Override
    public long getExpectedTotalCount() {
      return query.count();
    }

    private void afterStep() {
      if (!isReadOnly) {
        executor.entityService.flush();
      }
      for (IBeforeClearListener beforeClearListener : executor.clearListeners) {
        beforeClearListener.beforeClear();
      }
      if (!isReadOnly) {
        executor.hibernateSearchService.flushToIndexes();
      }
      executor.entityService.clear();
    }

    @Override
    public void preExecute() {
      runnable.preExecute();
      afterStep();
    }

    @Override
    public int executePartition(long offset, long limit) {
      final List<T> partition = query.list(offset, limit);
      int partitionSize = partition.size();
      if (partitionSize > 0) {
        runnable.executePartition(partition);
        afterStep();
      }

      return partitionSize;
    }

    @Override
    public void postExecute() {
      runnable.postExecute();
      afterStep();
    }
  }

  private static final class CommitOnStepExecutionStrategy<T> extends ExecutionStrategy<T> {
    private final TransactionOperations stepTransactionTemplate =
        executor.newTransactionTemplate(
            runnable.getWriteability(), TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    private CommitOnStepExecutionStrategy(
        SimpleHibernateBatchExecutor executor, IQuery<T> query, IBatchRunnable<T> runnable) {
      super(executor, query, runnable);
    }

    @Override
    public TransactionOperations getExecuteTransactionOperations() {
      return new TransactionOperations() {
        @Override
        public <T2> T2 execute(TransactionCallback<T2> action) throws TransactionException {
          return action.doInTransaction(new SimpleTransactionStatus());
        }
      };
    }

    @Override
    public long getExpectedTotalCount() {
      return stepTransactionTemplate.execute(
          new TransactionCallback<Long>() {
            @Override
            public Long doInTransaction(TransactionStatus status) {
              return query.count();
            }
          });
    }

    @Override
    public void preExecute() {
      stepTransactionTemplate.execute(
          new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
              runnable.preExecute();
            }
          });
    }

    @Override
    public int executePartition(final long offset, final long limit) {
      return stepTransactionTemplate.execute(
          new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus status) {
              final List<T> partition = query.list(offset, limit);
              int partitionSize = partition.size();
              if (partitionSize > 0) {
                runnable.executePartition(partition);
              }
              return partitionSize;
            }
          });
    }

    @Override
    public void postExecute() {
      stepTransactionTemplate.execute(
          new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
              runnable.postExecute();
            }
          });
    }
  }
}
