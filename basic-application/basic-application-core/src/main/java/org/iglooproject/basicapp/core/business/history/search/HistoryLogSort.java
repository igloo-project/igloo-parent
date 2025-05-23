package org.iglooproject.basicapp.core.business.history.search;

import com.querydsl.core.types.OrderSpecifier;
import java.util.List;
import org.iglooproject.basicapp.core.business.history.model.QHistoryLog;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;

public enum HistoryLogSort implements ISort<OrderSpecifier<?>> {
  ID {
    @Override
    public List<OrderSpecifier<?>> getSortFields(SortOrder sortOrder) {
      return List.of(SortUtils.orderSpecifier(this, sortOrder, QHistoryLog.historyLog.id));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  DATE {
    @Override
    public List<OrderSpecifier<?>> getSortFields(SortOrder sortOrder) {
      return List.of(SortUtils.orderSpecifier(this, sortOrder, QHistoryLog.historyLog.date));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  };
}
