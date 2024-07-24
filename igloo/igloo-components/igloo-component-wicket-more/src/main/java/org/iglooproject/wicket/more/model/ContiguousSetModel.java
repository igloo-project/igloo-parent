package org.iglooproject.wicket.more.model;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import org.apache.wicket.model.IModel;
import org.iglooproject.commons.util.collections.range.time.PartitionDiscreteDomain;

public class ContiguousSetModel<C extends Comparable<?>> implements IModel<ContiguousSet<C>> {

  private static final long serialVersionUID = 6151543937199662328L;

  public static <C extends Comparable<C>> ContiguousSetModel<C> create(
      IModel<? extends Range<C>> rangeModel, DiscreteDomain<C> discreteDomain) {
    return new ContiguousSetModel<>(rangeModel, discreteDomain);
  }

  private final IModel<? extends Range<C>> rangeModel;

  private final DiscreteDomain<C> discreteDomain;

  public ContiguousSetModel(
      IModel<? extends Range<C>> rangeModel, DiscreteDomain<C> discreteDomain) {
    super();
    this.rangeModel = rangeModel;
    this.discreteDomain = discreteDomain;
  }

  @Override
  public ContiguousSet<C> getObject() {
    Range<C> range = rangeModel.getObject();
    if (range == null) {
      return null;
    } else {
      if (discreteDomain instanceof PartitionDiscreteDomain) {
        range = ((PartitionDiscreteDomain<C>) discreteDomain).alignOut(range);
      }
      return ContiguousSet.create(range, discreteDomain);
    }
  }
}
