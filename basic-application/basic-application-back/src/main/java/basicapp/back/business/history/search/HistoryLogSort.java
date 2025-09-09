package basicapp.back.business.history.search;

import basicapp.back.business.history.model.HistoryLog_;
import java.util.List;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public enum HistoryLogSort implements ISort<Order> {
  ID {
    @Override
    public List<Order> getSortFields(SortOrder sortOrder) {
      return List.of(Order.by(HistoryLog_.ID).with(Direction.fromString(sortOrder.toString())));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  DATE {
    @Override
    public List<Order> getSortFields(SortOrder sortOrder) {
      return List.of(Order.by(HistoryLog_.DATE).with(Direction.fromString(sortOrder.toString())));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  };
}
