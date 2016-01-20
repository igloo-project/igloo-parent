package fr.openwide.core.jpa.more.util.transaction.model;

import fr.openwide.core.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;

/**
 * An interface for {@link ITransactionSynchronizationTask synchronization tasks} that want
 * to be notified before a clear happend (because they hold references to objects in the session, for instance).
 * <p><strong>WARNING:</strong> As hibernate does not provide any way to add a "before clear" listener, this interface
 * will be used <strong>only when the {@link ITransactionSynchronizationTaskManagerService} is explicitely warned of
 * an imminent clear</strong>. See {@link ITransactionSynchronizationTaskManagerService#beforeClear()} for more
 * information.
 * <br/>This means that:
 * <ul>
 * 	<li>If neither you nor a class you use does warn the {@link ITransactionSynchronizationTaskManagerService} before
 * a clear, <strong>{@link #beforeClear()} will not be executed</strong>
 * 	<li>If you (or a class you use) does warn the {@link ITransactionSynchronizationTaskManagerService} but does not
 * perform the clear, the resulting behavior is unexpected.
 *
 */
public interface ITransactionSynchronizationTaskClearAware {

	void beforeClear() throws Exception;

}
