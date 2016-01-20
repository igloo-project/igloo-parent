package fr.openwide.core.test.jpa.more.business.util.transaction.model;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;

public class TestCreateAfterCommitTask extends TestAbstractTransactionSynchronizationTask
		implements ITransactionSynchronizationAfterCommitTask {

	private static final long serialVersionUID = 20642307623916853L;
	
	private Collection<GenericEntityReference<Long, TestEntity>> createdEntities = Lists.newArrayListWithExpectedSize(1);

	@Override
	public void run() throws Exception {
		createdEntities.add(transactionSynchronizationTaskService.createInNewTransaction());
	}
	
	public Collection<GenericEntityReference<Long, TestEntity>> getCreatedEntities() {
		return Collections.unmodifiableCollection(createdEntities);
	}

}
