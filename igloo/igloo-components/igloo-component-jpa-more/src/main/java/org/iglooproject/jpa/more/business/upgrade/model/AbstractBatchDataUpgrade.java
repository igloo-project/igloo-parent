package org.iglooproject.jpa.more.business.upgrade.model;

import com.google.common.collect.Lists;
import java.util.List;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public abstract class AbstractBatchDataUpgrade<T> implements IDataUpgrade {

  private static final int DEFAULT_BATCH_SIZE = 100;

  private int batchSize;

  protected AbstractBatchDataUpgrade() {
    this(DEFAULT_BATCH_SIZE);
  }

  protected AbstractBatchDataUpgrade(int batchSize) {
    this.batchSize = batchSize;
  }

  @Override
  public void perform() throws ServiceException, SecurityServiceException {
    List<T> allIds = listIds();
    List<List<T>> batches = Lists.partition(allIds, batchSize);

    for (List<T> batch : batches) {
      handleBatch(batch);
      batchEnd();
    }
  }

  protected abstract List<T> listIds();

  protected abstract void handleBatch(List<T> batchIds)
      throws ServiceException, SecurityServiceException;

  protected abstract void batchEnd();
}
