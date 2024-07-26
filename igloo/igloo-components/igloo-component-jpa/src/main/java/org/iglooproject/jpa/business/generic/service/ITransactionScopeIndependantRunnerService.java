package org.iglooproject.jpa.business.generic.service;

import java.util.concurrent.Callable;

/**
 * This service must inherit from {@link ITransactionalAspectAwareService} for everything to work
 * correctly even if we are not in a transaction.
 */
public interface ITransactionScopeIndependantRunnerService
    extends ITransactionalAspectAwareService {

  <T> T run(Callable<T> callable);

  <T> T run(boolean readOnly, Callable<T> callable);
}
