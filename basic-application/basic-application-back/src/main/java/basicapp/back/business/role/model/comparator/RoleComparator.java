package basicapp.back.business.role.model.comparator;

import basicapp.back.business.role.model.Role;
import basicapp.back.util.binding.Bindings;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;

public class RoleComparator extends AbstractGenericEntityComparator<Long, Role> {

  private static final long serialVersionUID = 1L;

  private static final RoleComparator INSTANCE = new RoleComparator();

  public static RoleComparator get() {
    return INSTANCE;
  }

  @Override
  protected int compareNotNullObjects(Role left, Role right) {
    int order =
        ComparisonChain.start()
            .compare(
                Bindings.role().title().getSafelyWithRoot(left),
                Bindings.role().title().getSafelyWithRoot(right),
                Ordering.natural())
            .result();

    if (order == 0) {
      order = super.compareNotNullObjects(left, right);
    }

    return order;
  }
}
