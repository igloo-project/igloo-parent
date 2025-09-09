package basicapp.back.business.role.search;

import basicapp.back.business.role.model.Role_;
import java.util.List;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public enum RoleSort implements ISort<Order> {
  ID {
    @Override
    public List<Order> getSortFields(SortOrder sortOrder) {
      return List.of(Order.by(Role_.ID).with(Direction.fromString(sortOrder.toString())));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  },
  TITLE {
    @Override
    public List<Order> getSortFields(SortOrder sortOrder) {
      return List.of(Order.by(Role_.TITLE).with(Direction.fromString(sortOrder.toString())));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  };
}
