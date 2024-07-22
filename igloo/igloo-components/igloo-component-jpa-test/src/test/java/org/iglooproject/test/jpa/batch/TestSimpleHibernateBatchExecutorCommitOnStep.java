package org.iglooproject.test.jpa.batch;

import igloo.jpa.batch.executor.BatchExecutorCreator;
import igloo.jpa.batch.executor.SimpleHibernateBatchExecutor;
import org.iglooproject.test.config.spring.SpringBootTestJpaBatchSearch;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTestJpaBatchSearch
public class TestSimpleHibernateBatchExecutorCommitOnStep
    extends AbstractTestSimpleHibernateBatchExecutor {

  @Autowired private BatchExecutorCreator executorCreator;

  @Override
  protected SimpleHibernateBatchExecutor newSimpleHibernateBatchExecutor() {
    return executorCreator.newSimpleHibernateBatchExecutor().commitOnStep();
  }
}
