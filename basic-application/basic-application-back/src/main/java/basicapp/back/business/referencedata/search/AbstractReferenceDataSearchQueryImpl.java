package basicapp.back.business.referencedata.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.wildcardTokensOr;

import basicapp.back.business.referencedata.model.ReferenceData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;

public abstract class AbstractReferenceDataSearchQueryImpl<
        T extends ReferenceData<? super T>,
        S extends ISort<Function<SearchSortFactory, SortFinalStep>>,
        D extends AbstractReferenceDataSearchQueryData<T>>
    implements IAbstractReferenceDataSearchQuery<T, S, D> {

  @PersistenceContext private EntityManager entityManager;

  private final Class<T> clazz;

  protected AbstractReferenceDataSearchQueryImpl(Class<T> clazz) {
    this.clazz = Objects.requireNonNull(clazz);
  }

  @Override
  public Collection<T> list(D data, Map<S, SortOrder> sorts, Integer offset, Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    return Search.session(entityManager)
        .search(clazz)
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchHits(offset, limit);
  }

  @Override
  public long size(D data) {
    return Search.session(entityManager)
        .search(clazz)
        .where(predicateContributor(data))
        .fetchTotalHitCount();
  }

  protected BiConsumer<SearchPredicateFactory, SimpleBooleanPredicateClausesCollector<?, ?>>
      predicateContributor(D data) {
    return (f, root) -> {
      root.add(f.matchAll());
      if (data.getLabel() != null) {
        root.add(
            f.simpleQueryString()
                .field(ReferenceData.LABEL_FR_AUTOCOMPLETE)
                .field(ReferenceData.LABEL_EN_AUTOCOMPLETE)
                .matching(wildcardTokensOr(data.getLabel())));
      }
      if (data.getEnabledFilter() != null
          && !Objects.equals(data.getEnabledFilter(), EnabledFilter.ALL)) {
        boolean active =
            switch (data.getEnabledFilter()) {
              case DISABLED_ONLY -> false;
              case ENABLED_ONLY -> true;
              default -> throw new IllegalSwitchValueException(data.getEnabledFilter());
            };
        root.add(f.match().field(ReferenceData.ENABLED).matching(active));
      }
    };
  }
}
