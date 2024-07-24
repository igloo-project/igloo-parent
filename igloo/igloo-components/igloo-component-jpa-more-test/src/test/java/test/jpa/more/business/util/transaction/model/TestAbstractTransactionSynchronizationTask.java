package test.jpa.more.business.util.transaction.model;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import test.jpa.more.business.util.transaction.service.ITestTransactionSynchronizationTaskService;

public class TestAbstractTransactionSynchronizationTask implements Serializable {

  private static final long serialVersionUID = -1235254300180063058L;

  @Autowired
  protected ITestTransactionSynchronizationTaskService transactionSynchronizationTaskService;
}
