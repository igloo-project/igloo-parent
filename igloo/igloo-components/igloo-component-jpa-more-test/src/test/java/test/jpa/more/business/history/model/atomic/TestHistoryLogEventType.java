package test.jpa.more.business.history.model.atomic;

import org.iglooproject.jpa.more.business.history.model.atomic.IHistoryLogEventType;
import org.iglooproject.jpa.more.business.history.model.atomic.IHistoryLogEventTypeMergeGroup;

public enum TestHistoryLogEventType implements IHistoryLogEventType<TestHistoryLogEventType> {
  EVENT1,
  EVENT2;

  @Override
  public IHistoryLogEventTypeMergeGroup<TestHistoryLogEventType> getMergeGroup() {
    return null;
  }
}
