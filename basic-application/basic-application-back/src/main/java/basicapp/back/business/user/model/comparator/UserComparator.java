package basicapp.back.business.user.model.comparator;

import basicapp.back.business.user.model.User;
import basicapp.back.util.binding.Bindings;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;

public class UserComparator extends AbstractGenericEntityComparator<Long, User> {

  private static final long serialVersionUID = 1L;

  private static final UserComparator INSTANCE = new UserComparator();

  public static UserComparator get() {
    return INSTANCE;
  }

  @Override
  protected int compareNotNullObjects(User left, User right) {
    int order =
        ComparisonChain.start()
            .compare(
                Bindings.user().username().apply(left),
                Bindings.user().username().apply(right),
                Ordering.natural())
            .compare(
                Bindings.user().lastName().apply(left),
                Bindings.user().lastName().apply(right),
                Ordering.natural())
            .compare(
                Bindings.user().firstName().apply(left),
                Bindings.user().firstName().apply(right),
                Ordering.natural())
            .result();

    if (order == 0) {
      order = super.compareNotNullObjects(left, right);
    }

    return order;
  }
}
