package org.iglooproject.jpa.more.business.history.search;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.lucene.search.SortField;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;

public enum HistoryLogSort implements ISort<SortField> {
  ID {
    @Override
    public List<SortField> getSortFields(SortOrder sortOrder) {
      return ImmutableList.of(
          SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, AbstractHistoryLog.ID));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  },
  DATE {
    @Override
    public List<SortField> getSortFields(SortOrder sortOrder) {
      return ImmutableList.of(
          SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, AbstractHistoryLog.DATE),
          SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, AbstractHistoryLog.ID));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  };
}
