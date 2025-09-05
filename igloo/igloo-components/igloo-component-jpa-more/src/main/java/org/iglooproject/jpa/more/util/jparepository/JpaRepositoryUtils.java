package org.iglooproject.jpa.more.util.jparepository;

import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class JpaRepositoryUtils {
  public static PageRequest createPageRequest(
      Map<? extends ISort<Order>, SortOrder> sorts, int offset, int limit) {
    return PageRequest.of(
        offset != 0 ? offset / limit : 0,
        limit,
        Sort.by(
            sorts.entrySet().stream()
                .flatMap(entry -> entry.getKey().getSortFields(entry.getValue()).stream())
                .toList()));
  }
}
