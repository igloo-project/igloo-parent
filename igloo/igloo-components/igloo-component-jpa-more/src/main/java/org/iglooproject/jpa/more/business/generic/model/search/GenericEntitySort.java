package org.iglooproject.jpa.more.business.generic.model.search;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.lucene.search.SortField;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;

public enum GenericEntitySort implements ISort<SortField> {
  SCORE {
    @Override
    public List<SortField> getSortFields(SortOrder sortOrder) {
      return ImmutableList.of(SortField.FIELD_SCORE); // Order is irrelevant
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  ID {
    @Override
    public List<SortField> getSortFields(SortOrder sortOrder) {
      return ImmutableList.of(
          SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, GenericEntity.ID));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  };
}
