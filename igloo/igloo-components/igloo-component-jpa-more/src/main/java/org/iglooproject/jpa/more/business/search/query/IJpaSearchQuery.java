package org.iglooproject.jpa.more.business.search.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface IJpaSearchQuery<
        T, S extends ISort<OrderSpecifier<?>>, D extends ISearchQueryData<T>>
    extends IMigrationSearchQuery<T, S, D> {

  default void sortContributor(JPAQuery<?> query, Map<S, ISort.SortOrder> sorts) {
    Objects.requireNonNull(query);

    if (sorts == null) {
      return;
    }

    query.orderBy(
        sorts.entrySet().stream()
            .map(e -> e.getKey().getSortFields(e.getValue()))
            .flatMap(List::stream)
            .toArray(OrderSpecifier<?>[]::new));
  }

  default void hitsContributor(JPAQuery<?> query, Integer offset, Integer limit) {
    Optional.ofNullable(offset).ifPresent(query::offset);
    Optional.ofNullable(limit).ifPresent(query::limit);
  }
}
