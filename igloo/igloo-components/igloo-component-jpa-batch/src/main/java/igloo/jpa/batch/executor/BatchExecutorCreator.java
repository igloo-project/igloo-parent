package igloo.jpa.batch.executor;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BatchExecutorCreator {

  @Autowired
  private ObjectFactory<SimpleHibernateBatchExecutor> simpleHibernateBatchExecutorFactory;

  @Autowired private ObjectFactory<MultithreadedBatchExecutor> multithreadedBatchExecutorFactory;

  public SimpleHibernateBatchExecutor newSimpleHibernateBatchExecutor() {
    return simpleHibernateBatchExecutorFactory.getObject();
  }

  public MultithreadedBatchExecutor newMultithreadedBatchExecutor() {
    return multithreadedBatchExecutorFactory.getObject();
  }
}
