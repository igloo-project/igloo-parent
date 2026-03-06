package org.iglooproject.jpa.more.business.history.model.atomic;

public interface IHistoryLogEventType<HLET extends Enum<HLET>> {

  IHistoryLogEventTypeMergeGroup<HLET> getMergeGroup();
}
