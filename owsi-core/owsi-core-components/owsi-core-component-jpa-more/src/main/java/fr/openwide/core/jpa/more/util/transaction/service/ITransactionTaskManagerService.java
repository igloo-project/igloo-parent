package fr.openwide.core.jpa.more.util.transaction.service;

import fr.openwide.core.jpa.more.util.transaction.model.IAfterCommitOperation;

public interface ITransactionTaskManagerService {

	void addAfterCommitOperation(IAfterCommitOperation afterCommitOperation);
	
}