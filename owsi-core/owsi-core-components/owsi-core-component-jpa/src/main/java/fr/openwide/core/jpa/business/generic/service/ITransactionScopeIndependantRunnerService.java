package fr.openwide.core.jpa.business.generic.service;

import java.util.concurrent.Callable;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;

/**
 * Ce service doit étendre {@link ITransactionalAspectAwareService} pour que tout fonctionne correctement <strong>même</strong>
 * si aucune transaction n'est en cours.
 */
public interface ITransactionScopeIndependantRunnerService extends ITransactionalAspectAwareService {

	<T> T run(Callable<T> callable);

	<T> T run(boolean readOnly, Callable<T> callable);

}
