package org.iglooproject.basicapp.core.business.user.search;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.lucene.search.SortField;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;

// TODO: switch to final List<SortField> attribute ?
public enum UserGroupSort implements ISort<SortField> {
  @SuppressWarnings("common-java:DuplicatedBlocks")
  NAME {
    @Override
    public List<SortField> getSortFields(SortOrder sortOrder) {
      return ImmutableList.of(
          SortUtils.luceneSortField(this, sortOrder, SortField.Type.STRING, UserGroup.NAME_SORT));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  @SuppressWarnings("common-java:DuplicatedBlocks")
  ID {
    @Override
    public List<SortField> getSortFields(SortOrder sortOrder) {
      return ImmutableList.of(
          SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, UserGroup.ID));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  };
}
