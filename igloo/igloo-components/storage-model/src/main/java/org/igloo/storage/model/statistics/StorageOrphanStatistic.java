package org.igloo.storage.model.statistics;

import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageFailureStatus;

public class StorageOrphanStatistic {

  private Long storageUnitId;
  private IStorageUnitType storageUnitType;
  private StorageFailureStatus failureStatus;
  private Integer count;

  public StorageOrphanStatistic() {
    super();
  }

  public Long getStorageUnitId() {
    return storageUnitId;
  }

  public void setStorageUnitId(Long storageUnitId) {
    this.storageUnitId = storageUnitId;
  }

  public IStorageUnitType getStorageUnitType() {
    return storageUnitType;
  }

  public void setStorageUnitType(IStorageUnitType storageUnitType) {
    this.storageUnitType = storageUnitType;
  }

  public StorageFailureStatus getFailureStatus() {
    return failureStatus;
  }

  public void setFailureStatus(StorageFailureStatus failureStatus) {
    this.failureStatus = failureStatus;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
