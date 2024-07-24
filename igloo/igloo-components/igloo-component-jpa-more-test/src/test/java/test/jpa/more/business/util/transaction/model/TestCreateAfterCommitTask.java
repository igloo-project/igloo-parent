package test.jpa.more.business.util.transaction.model;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import test.jpa.more.business.entity.model.TestEntity;

public class TestCreateAfterCommitTask extends TestAbstractTransactionSynchronizationTask
    implements ITransactionSynchronizationAfterCommitTask {

  private static final long serialVersionUID = 20642307623916853L;

  private Collection<GenericEntityReference<Long, TestEntity>> createdEntities =
      Lists.newArrayListWithExpectedSize(1);

  @Override
  public void run() throws Exception {
    createdEntities.add(transactionSynchronizationTaskService.createInNewTransaction());
  }

  public Collection<GenericEntityReference<Long, TestEntity>> getCreatedEntities() {
    return Collections.unmodifiableCollection(createdEntities);
  }
}
