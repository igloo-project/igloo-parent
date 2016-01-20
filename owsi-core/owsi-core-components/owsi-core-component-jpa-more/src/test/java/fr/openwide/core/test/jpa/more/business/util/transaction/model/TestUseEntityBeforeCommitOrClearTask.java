package fr.openwide.core.test.jpa.more.business.util.transaction.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationTaskRollbackAware;
import fr.openwide.core.jpa.util.EntityManagerUtils;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;

public class TestUseEntityBeforeCommitOrClearTask extends TestAbstractTransactionSynchronizationTask
		implements ITransactionSynchronizationBeforeCommitTask, ITransactionSynchronizationTaskRollbackAware {

	private static final long serialVersionUID = 20642307623916853L;
	
	@Autowired
	private EntityManagerUtils entityManagerUtils;
	
	private TestEntity entity;
	
	private int executionCount = 0;
	
	private int rollbackCount = 0;

	public TestUseEntityBeforeCommitOrClearTask(TestEntity newEntity) {
		super();
		this.entity = newEntity;
	}

	@Override
	public void run() throws Exception {
		++executionCount;
		EntityManager entityManager = entityManagerUtils.getCurrentEntityManager();
		if (!entityManager.contains(entity)) {
			throw new PersistenceException("Attempted to use a detached entity");
		}
	}
	
	@Override
	public boolean shouldRunBeforeClear() {
		return true;
	}
	
	@Override
	public void afterRollback() throws Exception {
		++rollbackCount;
	}
	
	public int getExecutionCount() {
		return executionCount;
	}
	
	public int getRollbackCount() {
		return rollbackCount;
	}
}
