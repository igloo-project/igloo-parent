package org.iglooproject.jpa.more.business.task.transaction;

import org.iglooproject.jpa.util.EntityManagerUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Fake TransactionTemplate which open entity manager but doesn't open any transaction. For the
 * following usages:
 *
 * <ul>
 *   <li>read-only task
 *   <li>long task with inner atomic transactions, to avoid huge database lock
 * </ul>
 */
public class OpenEntityManagerWithNoTransactionTransactionTemplate extends TransactionTemplate {

  private static final long serialVersionUID = 753594542926029239L;

  private EntityManagerUtils entityManagerUtils;

  public OpenEntityManagerWithNoTransactionTransactionTemplate(
      EntityManagerUtils entityManagerUtils,
      PlatformTransactionManager transactionManager,
      boolean readOnly) {
    super(transactionManager);
    this.entityManagerUtils = entityManagerUtils;
    setReadOnly(readOnly);
  }

  @Override
  public <T> T execute(TransactionCallback<T> action) throws TransactionException {
    try {
      entityManagerUtils.openEntityManager();
      // We do not  call super() here, no transaction will be opened.
      // Note : it doesn't exist any transaction manager which doesn't open transaction while
      // keeping entity manager open.
      return action.doInTransaction(new SimpleTransactionStatus());
    } finally {
      entityManagerUtils.closeEntityManager();
    }
  }
}
