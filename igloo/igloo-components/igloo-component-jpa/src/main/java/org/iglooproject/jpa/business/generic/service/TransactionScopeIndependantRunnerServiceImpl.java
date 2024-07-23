package org.iglooproject.jpa.business.generic.service;

import java.util.concurrent.Callable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class TransactionScopeIndependantRunnerServiceImpl
    implements ITransactionScopeIndependantRunnerService {

  private TransactionTemplate readOnlyTransactionTemplate;

  private TransactionTemplate writeTransactionTemplate;

  @Autowired
  public void setPlatformTransactionManager(PlatformTransactionManager transactionManager) {
    DefaultTransactionAttribute readOnlyTransactionAttribute =
        new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
    readOnlyTransactionAttribute.setReadOnly(true);
    readOnlyTransactionTemplate =
        new TransactionTemplate(transactionManager, readOnlyTransactionAttribute);

    DefaultTransactionAttribute writeTransactionAttribute =
        new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
    writeTransactionAttribute.setReadOnly(false);
    writeTransactionTemplate =
        new TransactionTemplate(transactionManager, writeTransactionAttribute);
  }

  @Override
  public <T> T run(final Callable<T> callable) {
    return run(true, callable);
  }

  @Override
  public <T> T run(boolean readOnly, final Callable<T> callable) {
    TransactionTemplate transactionTemplate =
        readOnly ? readOnlyTransactionTemplate : writeTransactionTemplate;

    return transactionTemplate.execute(
        new TransactionCallback<T>() {
          @Override
          public T doInTransaction(TransactionStatus transactionStatus) {
            try {
              return callable.call();
            } catch (Exception e) {
              if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
              }
              throw new IllegalStateException(
                  String.format("Erreur durant l'execution du callable %s", callable), e);
            }
          }
        });
  }
}
