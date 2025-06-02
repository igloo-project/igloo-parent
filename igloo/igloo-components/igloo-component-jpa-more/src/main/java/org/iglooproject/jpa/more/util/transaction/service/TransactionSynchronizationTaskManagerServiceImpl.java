package org.iglooproject.jpa.more.util.transaction.service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.iglooproject.jpa.more.util.transaction.exception.TransactionSynchronizationException;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationTask;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationTaskRollbackAware;
import org.iglooproject.jpa.more.util.transaction.model.TransactionSynchronizationTasks;
import org.iglooproject.jpa.more.util.transaction.util.ITransactionSynchronizationTaskMerger;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class TransactionSynchronizationTaskManagerServiceImpl
    implements ITransactionSynchronizationTaskManagerService {

  private static final Class<?> TASKS_RESOURCE_KEY =
      TransactionSynchronizationTaskManagerServiceImpl.class;

  public static final String EXCEPTION_MESSAGE_NO_ACTUAL_TRANSACTION_ACTIVE =
      "No actual transaction active.";

  @Autowired(required = false)
  private Collection<ITransactionSynchronizationTaskMerger> transactionSynchronizationTaskMergers =
      List.of();

  @Autowired private ConfigurableApplicationContext configurableApplicationContext;

  @Autowired private EntityManagerUtils entityManagerUtils;

  @Override
  public void push(ITransactionSynchronizationBeforeCommitTask beforeCommitTask) {
    addSynchronizationIfNeeded();

    autowireAndInitialize(beforeCommitTask);
    getTasksIfExist().getBeforeCommitTasks().add(beforeCommitTask);
  }

  @Override
  public void push(ITransactionSynchronizationAfterCommitTask afterCommitTask) {
    addSynchronizationIfNeeded();

    autowireAndInitialize(afterCommitTask);
    getTasksIfExist().getAfterCommitTasks().add(afterCommitTask);
  }

  protected void autowireAndInitialize(ITransactionSynchronizationTask beforeCommitTask) {
    AutowireCapableBeanFactory autowireCapableBeanFactory =
        configurableApplicationContext.getAutowireCapableBeanFactory();
    autowireCapableBeanFactory.autowireBean(beforeCommitTask);
    autowireCapableBeanFactory.initializeBean(
        beforeCommitTask, beforeCommitTask.getClass().getName());
  }

  protected void addSynchronizationIfNeeded() {
    if (!TransactionSynchronizationManager.isActualTransactionActive()) {
      throw new IllegalStateException(EXCEPTION_MESSAGE_NO_ACTUAL_TRANSACTION_ACTIVE);
    }
    if (!isTaskSynchronizationActive()) {
      // Initialize the list of tasks that will be executed later
      bindContext(new TransactionSynchronizationTasks());

      // Register the synchronization adapter (that will transmit events to this service)
      TransactionSynchronizationManager.registerSynchronization(
          new TaskTransactionSynchronizationAdapter());
    }
  }

  protected boolean isTaskSynchronizationActive() {
    return TransactionSynchronizationManager.hasResource(TASKS_RESOURCE_KEY);
  }

  protected void bindContext(TransactionSynchronizationTasks tasks) {
    TransactionSynchronizationManager.bindResource(TASKS_RESOURCE_KEY, tasks);
  }

  protected TransactionSynchronizationTasks unbindContext() {
    return (TransactionSynchronizationTasks)
        TransactionSynchronizationManager.unbindResourceIfPossible(TASKS_RESOURCE_KEY);
  }

  private TransactionSynchronizationTasks getTasksIfExist() {
    return (TransactionSynchronizationTasks)
        TransactionSynchronizationManager.getResource(TASKS_RESOURCE_KEY);
  }

  protected TransactionSynchronizationTasks merge() {
    TransactionSynchronizationTasks tasks = getTasksIfExist();
    if (tasks != null) {
      for (ITransactionSynchronizationTaskMerger merger : transactionSynchronizationTaskMergers) {
        merger.merge(tasks);
      }
    }
    return tasks;
  }

  @Override
  public void beforeClear() {
    TransactionSynchronizationTasks tasks = merge();
    if (tasks == null) {
      return;
    }

    if (tasks.isFrozen()) {
      // If called after doBeforeCommit, there's nothing to do.
      return;
    }

    Iterator<ITransactionSynchronizationBeforeCommitTask> iterator =
        tasks.getBeforeCommitTasks().iterator();
    while (iterator.hasNext()) {
      ITransactionSynchronizationBeforeCommitTask beforeCommitTask = iterator.next();
      if (beforeCommitTask.shouldRunBeforeClear()) {
        try {
          beforeCommitTask.run();
          entityManagerUtils.getCurrentEntityManager().flush();
        } catch (Exception e) {
          if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
          }
          // This exception MUST be thrown, as we want to rollback if anything goes wrong.
          // We better ignore other tasks, as they will have no effect on the current transaction.
          throw new TransactionSynchronizationException(
              "Error while executing a 'before clear' task.", e);
        }

        // We mustn't execute the task again before commit
        iterator.remove();
        // ... but we must execute afterRollback() if possible.
        if (beforeCommitTask instanceof ITransactionSynchronizationTaskRollbackAware) {
          ITransactionSynchronizationTaskRollbackAware rollbackAwareTask =
              (ITransactionSynchronizationTaskRollbackAware) beforeCommitTask;
          tasks.getAlreadyExecutedBeforeClearTasks().add(rollbackAwareTask);
        }
      }
    }
  }

  private void doBeforeCommit() {
    merge();

    TransactionSynchronizationTasks tasks = merge();
    if (tasks == null) {
      return;
    }

    tasks.freeze();

    for (ITransactionSynchronizationBeforeCommitTask beforeCommitTask :
        tasks.getBeforeCommitTasks()) {
      try {
        beforeCommitTask.run();
      } catch (Exception e) {
        if (e instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }
        // This exception MUST be thrown, as we want to rollback if anything goes wrong.
        // We better ignore other tasks, as they will have no effect on the current transaction.
        throw new TransactionSynchronizationException(
            "Error while executing a 'before commit' task.", e);
      }
    }
  }

  private void doAfterCommit() {
    TransactionSynchronizationTasks tasks = getTasksIfExist();
    if (tasks == null) {
      return;
    }
    Exception firstException = null;
    for (ITransactionSynchronizationAfterCommitTask afterCommitTask : tasks.getAfterCommitTasks()) {
      try {
        afterCommitTask.run();
      } catch (Exception e) {
        if (e instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }
        if (firstException == null) {
          firstException = e;
        } else {
          firstException.addSuppressed(e);
        }
      }
    }
    if (firstException != null) {
      // We better throw an exception here, as we want the caller to know something went awry.
      throw new TransactionSynchronizationException(
          "Error while executing an 'after commit' task.", firstException);
    }
  }

  private void doOnRollback(TransactionSynchronizationTasks tasks) {
    Exception firstException = null;
    if (tasks == null) {
      return;
    }
    for (ITransactionSynchronizationTaskRollbackAware afterCommitTask :
        Iterables.filter(
            Lists.reverse(tasks.getAfterCommitTasks()),
            ITransactionSynchronizationTaskRollbackAware.class)) {
      try {
        ((ITransactionSynchronizationTaskRollbackAware) afterCommitTask).afterRollback();
      } catch (Exception e) {
        if (e instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }
        if (firstException == null) {
          firstException = e;
        } else {
          firstException.addSuppressed(e);
        }
      }
    }
    for (ITransactionSynchronizationTaskRollbackAware beforeCommitTask :
        Iterables.filter(
            Lists.reverse(tasks.getBeforeCommitTasks()),
            ITransactionSynchronizationTaskRollbackAware.class)) {
      try {
        ((ITransactionSynchronizationTaskRollbackAware) beforeCommitTask).afterRollback();
      } catch (Exception e) {
        if (e instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }
        if (firstException == null) {
          firstException = e;
        } else {
          firstException.addSuppressed(e);
        }
      }
    }
    for (ITransactionSynchronizationTaskRollbackAware beforeClearTask :
        Lists.reverse(tasks.getAlreadyExecutedBeforeClearTasks())) {
      try {
        beforeClearTask.afterRollback();
      } catch (Exception e) {
        if (e instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }
        if (firstException == null) {
          firstException = e;
        } else {
          firstException.addSuppressed(e);
        }
      }
    }
    if (firstException != null) {
      // We better throw an exception here, as we want the caller to know something went awry.
      throw new TransactionSynchronizationException(
          "Error while executing afterRollback() on a synchronization task.", firstException);
    }
  }

  public class TaskTransactionSynchronizationAdapter extends TransactionSynchronizationAdapter {

    private TransactionSynchronizationTasks suspendedTasks = null;

    @Override
    public int getOrder() {
      return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void suspend() {
      suspendedTasks = unbindContext();
    }

    @Override
    public void resume() {
      bindContext(suspendedTasks);
    }

    @Override
    public void beforeCommit(boolean readOnly) {
      doBeforeCommit();
    }

    @Override
    public void afterCommit() {
      doAfterCommit();
    }

    @Override
    public void afterCompletion(int status) {
      TransactionSynchronizationTasks tasks = null;

      // no try-finally as a failure on getTasksIfExists implies a failure on unbindContext
      if (TransactionSynchronization.STATUS_ROLLED_BACK == status) {
        // tasks are needed only for rollback
        tasks = getTasksIfExist();
      }

      // We need to unbind context before doOnRollback as synchronization is already removed by
      // caller and
      // remaining resources prevent correct transaction synchronization creation during
      // doOnRollback.
      // (isTaskSynchronizationActive will return true as resources should be present, so this
      // TransactionSynchronization would not be installed)
      unbindContext();

      if (tasks != null) {
        // populated only for rollbacks
        doOnRollback(tasks);
      }
    }
  }
}
