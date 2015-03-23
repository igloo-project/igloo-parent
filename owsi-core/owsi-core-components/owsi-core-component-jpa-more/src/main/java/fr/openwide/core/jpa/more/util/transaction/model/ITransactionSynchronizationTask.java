package fr.openwide.core.jpa.more.util.transaction.model;

import java.io.Serializable;

public interface ITransactionSynchronizationTask<T> extends Serializable {

	void run() throws Exception;

}