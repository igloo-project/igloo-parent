package fr.openwide.core.test.jpa.more.business.util.transaction.service;

import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.more.util.transaction.model.TransactionSynchronizationTasks;
import fr.openwide.core.jpa.more.util.transaction.service.ITransactionSynchronizationTaskMergerService;

@Service
public class TestTransactionSynchronizationTaskMergerServiceImpl implements ITransactionSynchronizationTaskMergerService {

	@Override
	public void merge(TransactionSynchronizationTasks tasks) {
		// nothing to do
	}

}
