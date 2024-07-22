package org.iglooproject.jpa.more.util.transaction.model;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.iglooproject.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;

public class TransactionSynchronizationTasks {

  private final List<ITransactionSynchronizationBeforeCommitTask> beforeCommitTasks =
      Lists.newLinkedList();

  private final List<ITransactionSynchronizationAfterCommitTask> afterCommitTasks =
      Lists.newLinkedList();

  private final List<ITransactionSynchronizationTaskRollbackAware> alreadyExecutedBeforeClearTasks =
      Lists.newLinkedList();

  private boolean frozen = false;

  public List<ITransactionSynchronizationBeforeCommitTask> getBeforeCommitTasks() {
    if (isFrozen()) {
      return Collections.unmodifiableList(beforeCommitTasks);
    }
    return beforeCommitTasks;
  }

  public List<ITransactionSynchronizationAfterCommitTask> getAfterCommitTasks() {
    if (isFrozen()) {
      return Collections.unmodifiableList(afterCommitTasks);
    }
    return afterCommitTasks;
  }

  /**
   * @return The tasks that have already been executed {@link
   *     ITransactionSynchronizationTaskManagerService#beforeClear() before a clear operation.}.
   *     These tasks are only kept in memory so that they can be notified if a rollback occurs.
   */
  public List<ITransactionSynchronizationTaskRollbackAware> getAlreadyExecutedBeforeClearTasks() {
    if (isFrozen()) {
      return Collections.unmodifiableList(alreadyExecutedBeforeClearTasks);
    }
    return alreadyExecutedBeforeClearTasks;
  }

  public TransactionSynchronizationTasks freeze() {
    frozen = true;
    return this;
  }

  public boolean isFrozen() {
    return frozen;
  }
}
