package org.iglooproject.jpa.more.business.task.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.toSortOrder;

import java.util.List;
import java.util.function.Function;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;

public enum QueuedTaskHolderSort implements ISort<Function<SearchSortFactory, SortFinalStep>> {
  ID {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.field(QueuedTaskHolder.ID).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  },
  NAME {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.field(QueuedTaskHolder.NAME_SORT).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  CREATION_DATE {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      //			return List.of(
      //				SortUtils.luceneStringSortField(this, sortOrder, QueuedTaskHolder.END_DATE,
      // NullSortValue.GREATEST),
      //				SortUtils.luceneStringSortField(this, sortOrder, QueuedTaskHolder.START_DATE,
      // NullSortValue.GREATEST),
      //				SortUtils.luceneSortField(this, sortOrder, SortField.Type.STRING,
      // QueuedTaskHolder.CREATION_DATE)
      //			);
      return List.of(
          f -> f.field(QueuedTaskHolder.END_DATE).order(toSortOrder(this, sortOrder)),
          f -> f.field(QueuedTaskHolder.START_DATE).order(toSortOrder(this, sortOrder)),
          f -> f.field(QueuedTaskHolder.CREATION_DATE).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  };
}
