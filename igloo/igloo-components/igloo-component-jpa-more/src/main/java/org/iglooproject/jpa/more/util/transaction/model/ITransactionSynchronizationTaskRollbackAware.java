package org.iglooproject.jpa.more.util.transaction.model;

public interface ITransactionSynchronizationTaskRollbackAware {

  void afterRollback() throws Exception;
}
