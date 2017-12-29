package org.iglooproject.test.jpa.batch;

import org.springframework.beans.factory.annotation.Autowired;

import org.iglooproject.jpa.batch.executor.BatchExecutorCreator;
import org.iglooproject.jpa.batch.executor.SimpleHibernateBatchExecutor;

public class TestSimpleHibernateBatchExecutorCommitOnEnd extends AbstractTestSimpleHibernateBatchExecutor {

	@Autowired
	private BatchExecutorCreator executorCreator;

	@Override
	protected SimpleHibernateBatchExecutor newSimpleHibernateBatchExecutor() {
		return executorCreator.newSimpleHibernateBatchExecutor().commitOnEnd();
	}

}
