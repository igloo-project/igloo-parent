package org.iglooproject.jpa.more.util.jparepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class JpaRepositoryUtils {
  public static PageRequest createPageRequest(
      Map<? extends ISort<Order>, SortOrder> sorts, int offset, int limit) {
    return PageRequest.of(offset != 0 ? offset / limit : 0, limit, createSort(sorts));
  }

  public static Sort createSort(Map<? extends ISort<Order>, SortOrder> sorts) {
    return Sort.by(
        sorts.entrySet().stream()
            .flatMap(entry -> entry.getKey().getSortFields(entry.getValue()).stream())
            .toList());
  }

  public static List<jakarta.persistence.criteria.Order> createCriteriaOrders(
      Map<? extends ISort<Order>, SortOrder> sorts, CriteriaBuilder cb, Root<?> root) {
    return sorts.entrySet().stream()
        .flatMap(entry -> entry.getKey().getSortFields(entry.getValue()).stream())
        .map(
            o ->
                o.getDirection().isAscending()
                    ? cb.asc(root.get(o.getProperty()))
                    : cb.desc(root.get(o.getProperty())))
        .toList();
  }
}
