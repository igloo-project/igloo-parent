package test.business.model;

import org.iglooproject.jpa.more.business.history.model.atomic.IHistoryLogEventType;
import org.iglooproject.jpa.more.business.history.model.atomic.IHistoryLogEventTypeMergeGroup;

public enum TestHistoryLogEventType implements IHistoryLogEventType<TestHistoryLogEventType> {
  EVENT1(TestHistoryLogEventTypeMergeGroup.GROUP1),
  EVENT2,
  EVENT3(TestHistoryLogEventTypeMergeGroup.GROUP1);

  private final TestHistoryLogEventTypeMergeGroup mergeGroup;

  TestHistoryLogEventType() {
    this(null);
  }

  TestHistoryLogEventType(TestHistoryLogEventTypeMergeGroup mergeGroup) {
    this.mergeGroup = mergeGroup;
  }

  @Override
  public IHistoryLogEventTypeMergeGroup<TestHistoryLogEventType> getMergeGroup() {
    return mergeGroup;
  }
}
