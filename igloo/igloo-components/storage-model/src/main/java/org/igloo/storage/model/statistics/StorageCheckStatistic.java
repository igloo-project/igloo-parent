package org.igloo.storage.model.statistics;

import java.time.Duration;
import java.time.LocalDateTime;
import org.igloo.storage.model.atomic.IStorageUnitType;

public class StorageCheckStatistic {

  private Long storageUnitId;
  private IStorageUnitType storageUnitType;
  private LocalDateTime lastOn;
  private LocalDateTime lastChecksumOn;
  private Duration lastDuration;
  private Duration lastChecksumDuration;
  private Duration lastAge;
  private Duration lastChecksumAge;

  public StorageCheckStatistic() {
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

  public LocalDateTime getLastOn() {
    return lastOn;
  }

  public void setLastOn(LocalDateTime lastOn) {
    this.lastOn = lastOn;
  }

  public LocalDateTime getLastChecksumOn() {
    return lastChecksumOn;
  }

  public void setLastChecksumOn(LocalDateTime lastChecksumOn) {
    this.lastChecksumOn = lastChecksumOn;
  }

  public Duration getLastDuration() {
    return lastDuration;
  }

  public void setLastDuration(Duration lastDuration) {
    this.lastDuration = lastDuration;
  }

  public Duration getLastChecksumDuration() {
    return lastChecksumDuration;
  }

  public void setLastChecksumDuration(Duration lastChecksumDuration) {
    this.lastChecksumDuration = lastChecksumDuration;
  }

  public Duration getLastAge() {
    return lastAge;
  }

  public void setLastAge(Duration lastAge) {
    this.lastAge = lastAge;
  }

  public Duration getLastChecksumAge() {
    return lastChecksumAge;
  }

  public void setLastChecksumAge(Duration lastCheckumAge) {
    this.lastChecksumAge = lastCheckumAge;
  }
}
