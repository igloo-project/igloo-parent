package fr.openwide.core.test.jpa.batch;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.batch.executor.BatchExecutorCreator;
import fr.openwide.core.jpa.batch.executor.SimpleHibernateBatchExecutor;

public class TestSimpleHibernateBatchExecutorCommitOnStep extends AbstractTestSimpleHibernateBatchExecutor {

	@Autowired
	private BatchExecutorCreator executorCreator;

	@Override
	protected SimpleHibernateBatchExecutor newSimpleHibernateBatchExecutor() {
		return executorCreator.newSimpleHibernateBatchExecutor().commitOnStep();
	}

}
