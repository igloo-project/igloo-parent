package org.iglooproject.jpa.batch.executor;

import org.iglooproject.jpa.batch.runnable.Writeability;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class AbstractBatchExecutor<T extends AbstractBatchExecutor<T>> {

  protected int batchSize;

  @Autowired private PlatformTransactionManager transactionManager;

  @Autowired protected IHibernateSearchService hibernateSearchService;

  @Autowired protected IEntityService entityService;

  @Autowired protected EntityManagerUtils entityManagerUtils;

  public T batchSize(int batchSize) {
    this.batchSize = batchSize;
    return thisAsT();
  }

  public TransactionTemplate newTransactionTemplate(Writeability writeability, int propagation) {
    DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute(propagation);
    transactionAttribute.setReadOnly(Writeability.READ_ONLY.equals(writeability));
    return new TransactionTemplate(transactionManager, transactionAttribute);
  }

  protected abstract T thisAsT();
}
