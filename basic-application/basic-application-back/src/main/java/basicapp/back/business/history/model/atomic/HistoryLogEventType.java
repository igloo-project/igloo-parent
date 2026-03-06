package basicapp.back.business.history.model.atomic;

import org.iglooproject.jpa.more.business.history.model.atomic.IHistoryLogEventType;
import org.iglooproject.jpa.more.business.history.model.atomic.IHistoryLogEventTypeMergeGroup;

public enum HistoryLogEventType implements IHistoryLogEventType<HistoryLogEventType> {
  CREATE(HistoryLogEventTypeMergeGroup.SAVE),
  UPDATE(HistoryLogEventTypeMergeGroup.SAVE),
  DELETE,
  DISABLE,
  ENABLE,
  SIGN_IN,
  SIGN_IN_FAIL,
  PASSWORD_RESET_REQUEST,
  PASSWORD_CREATION_REQUEST,
  PASSWORD_UPDATE;

  private final HistoryLogEventTypeMergeGroup mergeGroup;

  HistoryLogEventType() {
    this(null);
  }

  HistoryLogEventType(HistoryLogEventTypeMergeGroup mergeGroup) {
    this.mergeGroup = mergeGroup;
  }

  @Override
  public IHistoryLogEventTypeMergeGroup<HistoryLogEventType> getMergeGroup() {
    return mergeGroup;
  }
}
