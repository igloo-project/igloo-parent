package basicapp.back.business.user.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.toSortOrder;

import basicapp.back.business.user.model.BasicUser;
import java.util.List;
import java.util.function.Function;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.iglooproject.jpa.more.business.sort.ISort;

public enum BasicUserSort implements ISort<Function<SearchSortFactory, SortFinalStep>> {
  SCORE {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.score());
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  },
  ID {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.field(BasicUser.ID).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  },
  LAST_NAME {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.field(BasicUser.LAST_NAME_SORT).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  FIRST_NAME {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.field(BasicUser.FIRST_NAME_SORT).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  FULL_NAME {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(
          f -> f.field(BasicUser.LAST_NAME_SORT).order(toSortOrder(this, sortOrder)),
          f -> f.field(BasicUser.FIRST_NAME_SORT).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  };
}