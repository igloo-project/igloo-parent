package basicapp.back.business.announcement.search;

import basicapp.back.business.announcement.model.Announcement_;
import basicapp.back.business.announcement.model.embeddable.AnnouncementDate_;
import java.util.List;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public enum AnnouncementSort implements ISort<Order> {
  ID {
    @Override
    public List<Order> getSortFields(SortOrder sortOrder) {
      return List.of(
          Order.by(Announcement_.ID).with(Sort.Direction.fromString(sortOrder.toString())));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  },
  PUBLICATION_START_DATE_TIME {
    @Override
    public List<Order> getSortFields(SortOrder sortOrder) {
      return List.of(
          Order.by(Announcement_.PUBLICATION + "." + AnnouncementDate_.START_DATE_TIME)
              .with(Sort.Direction.fromString(sortOrder.toString())));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  };
}
