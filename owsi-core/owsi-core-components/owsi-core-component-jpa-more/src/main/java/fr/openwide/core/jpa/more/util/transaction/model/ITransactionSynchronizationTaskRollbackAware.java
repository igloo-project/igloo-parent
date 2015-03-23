package fr.openwide.core.jpa.more.util.transaction.model;

public interface ITransactionSynchronizationTaskRollbackAware {

	void afterRollback() throws Exception;

}
