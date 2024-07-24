package org.iglooproject.jpa.more.business.referencedata.search;

import java.util.Map;
import org.apache.lucene.search.SortField;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;

public class GenericReferenceDataSearchQueryImpl<
        T extends GenericReferenceData<? super T, ?>,
        S extends ISort<SortField>,
        Q extends IGenericReferenceDataSearchQuery<T, S, Q>>
    extends AbstractHibernateSearchSearchQuery<T, S>
    implements IGenericReferenceDataSearchQuery<T, S, Q> {

  @SafeVarargs
  protected GenericReferenceDataSearchQueryImpl(Class<T> clazz, S... defaultSorts) {
    super(clazz, defaultSorts);
  }

  @Override
  public Q enabled(EnabledFilter enabledFilter) {
    if (enabledFilter != null && !EnabledFilter.ALL.equals(enabledFilter)) {
      switch (enabledFilter) {
        case ENABLED_ONLY:
          must(p -> p.match().field(GenericReferenceData.ENABLED).matching(true));
          break;
        case DISABLED_ONLY:
          must(p -> p.match().field(GenericReferenceData.ENABLED).matching(false));
          break;
        default:
          throw new IllegalSwitchValueException(enabledFilter);
      }
    }
    return thisAsQ();
  }

  @SuppressWarnings("unchecked")
  protected final Q thisAsQ() {
    return (Q) this;
  }

  @Override
  public Q sort(Map<S, SortOrder> sortMap) {
    super.sort(sortMap);
    return thisAsQ();
  }
}
