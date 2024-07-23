package org.iglooproject.wicket.more.markup.head;

import com.google.common.collect.Ordering;
import java.util.Objects;
import org.apache.wicket.markup.head.PriorityFirstComparator;
import org.apache.wicket.markup.head.ResourceAggregator.RecordedHeaderItem;

public class CoreHeaderItemComparator extends PriorityFirstComparator {

  private static final long serialVersionUID = -4792334405533293330L;

  private static final CoreHeaderItemComparator INSTANCE =
      new CoreHeaderItemComparator(CoreHeaderItemSpecificOrder.get(), true);

  private final IHeaderItemSpecificOrder headerItemSpecificOrder;

  public CoreHeaderItemComparator(
      IHeaderItemSpecificOrder headerItemSpecificOrder, boolean renderPageFirst) {
    super(renderPageFirst);
    this.headerItemSpecificOrder = Objects.requireNonNull(headerItemSpecificOrder);
  }

  public static final CoreHeaderItemComparator get() {
    return INSTANCE;
  }

  @Override
  public int compare(RecordedHeaderItem o1, RecordedHeaderItem o2) {
    int order = 0;

    order =
        Ordering.natural()
            .nullsLast()
            .compare(headerItemSpecificOrder.order(o1), headerItemSpecificOrder.order(o2));

    if (order == 0) {
      order = super.compare(o1, o2);
    }

    return order;
  }
}
