package org.iglooproject.test.jpa.batch;

import org.iglooproject.jpa.batch.executor.BatchExecutorCreator;
import org.iglooproject.jpa.batch.executor.SimpleHibernateBatchExecutor;
import org.springframework.beans.factory.annotation.Autowired;

public class TestSimpleHibernateBatchExecutorCommitOnStep
    extends AbstractTestSimpleHibernateBatchExecutor {

  @Autowired private BatchExecutorCreator executorCreator;

  @Override
  protected SimpleHibernateBatchExecutor newSimpleHibernateBatchExecutor() {
    return executorCreator.newSimpleHibernateBatchExecutor().commitOnStep();
  }
}
