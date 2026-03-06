package basicapp.back.business.history.model.atomic;

import org.iglooproject.jpa.more.business.history.model.atomic.IHistoryLogEventTypeMergeGroup;

public enum HistoryLogEventTypeMergeGroup
    implements IHistoryLogEventTypeMergeGroup<HistoryLogEventType> {
  SAVE;
}
