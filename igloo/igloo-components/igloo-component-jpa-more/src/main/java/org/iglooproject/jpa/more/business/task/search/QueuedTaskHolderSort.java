package org.iglooproject.jpa.more.business.task.search;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.lucene.search.SortField;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;

public enum QueuedTaskHolderSort implements ISort<SortField> {
  ID {
    @Override
    public List<SortField> getSortFields(SortOrder sortOrder) {
      return ImmutableList.of(
          SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, QueuedTaskHolder.ID));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  },
  NAME {
    @Override
    public List<SortField> getSortFields(SortOrder sortOrder) {
      return ImmutableList.of(
          SortUtils.luceneSortField(
              this, sortOrder, SortField.Type.STRING, QueuedTaskHolder.NAME_SORT));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  CREATION_DATE {
    @Override
    public List<SortField> getSortFields(SortOrder sortOrder) {
      return ImmutableList.of(
          SortUtils.luceneLongSortField(
              this, sortOrder, QueuedTaskHolder.END_DATE, NullSortValue.GREATEST),
          SortUtils.luceneLongSortField(
              this, sortOrder, QueuedTaskHolder.START_DATE, NullSortValue.GREATEST),
          SortUtils.luceneSortField(
              this, sortOrder, SortField.Type.LONG, QueuedTaskHolder.CREATION_DATE));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  };
}
