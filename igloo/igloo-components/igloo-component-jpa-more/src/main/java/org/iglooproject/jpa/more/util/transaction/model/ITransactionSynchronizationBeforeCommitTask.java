package org.iglooproject.jpa.more.util.transaction.model;

import javax.persistence.EntityManager;
import org.iglooproject.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;

/**
 * A task that should be executed before committing the transaction.
 *
 * <p>If
 */
public interface ITransactionSynchronizationBeforeCommitTask
    extends ITransactionSynchronizationTask {

  /**
   * Whether or not the task should be exceptionally executed during the transaction if an {@link
   * EntityManager#clear() EntityManager clear} happens after the task has been pushed, but before
   * the transaction has been committed.
   *
   * <p>If this happens, the task is guaranteed to be only executed once.
   *
   * <p><strong>WARNING:</strong> As hibernate does not provide any way to add a "before clear"
   * listener, this feature will be effective <strong>only when the {@link
   * ITransactionSynchronizationTaskManagerService} is explicitely warned of an imminent
   * clear</strong>.
   *
   * @see {@link ITransactionSynchronizationTaskManagerService#beforeClear()} for caveats.
   */
  boolean shouldRunBeforeClear();
}
