package org.iglooproject.jpa.more.util.transaction.service;

import igloo.jpa.batch.util.IBeforeClearListener;
import jakarta.persistence.EntityManager;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationTaskRollbackAware;

public interface ITransactionSynchronizationTaskManagerService extends IBeforeClearListener {

  /**
   * Push a task to be executed <strong>before</strong> commit.
   *
   * <p>The task may optionally implement {@link ITransactionSynchronizationTaskRollbackAware}, in
   * which case the {@link ITransactionSynchronizationTaskRollbackAware#afterRollback()} method will
   * be called upon rollback.
   */
  void push(ITransactionSynchronizationBeforeCommitTask beforeCommitTask);

  /**
   * Push a task to be executed <strong>after</strong> commit.
   *
   * <p>The task may optionally implement {@link ITransactionSynchronizationTaskRollbackAware}, in
   * which case the {@link ITransactionSynchronizationTaskRollbackAware#afterRollback()} method will
   * be called upon rollback.
   */
  void push(ITransactionSynchronizationAfterCommitTask afterCommitTask);

  /**
   * Warn the service that a {@link EntityManager#clear()} operation is about to be performed, so
   * that tasks {@link ITransactionSynchronizationBeforeCommitTask#shouldRunBeforeClear() wanting to
   * be executed before a clear} will be executed.
   *
   * <p>This method is made available to application code because Hibernate does not provide any way
   * to add a "before clear" listener. Thus event handling must be done manually. <br>
   * This means that:
   *
   * <ul>
   *   <li>If neither you nor a class you use does warn the {@link
   *       ITransactionSynchronizationTaskManagerService} before a clear, <strong>{@link
   *       #beforeClear()} will not be executed</strong>
   *   <li>If you (or a class you use) does warn the {@link
   *       ITransactionSynchronizationTaskManagerService} but does not perform the clear, the
   *       resulting behavior is unexpected.
   */
  @Override
  void beforeClear();
}
