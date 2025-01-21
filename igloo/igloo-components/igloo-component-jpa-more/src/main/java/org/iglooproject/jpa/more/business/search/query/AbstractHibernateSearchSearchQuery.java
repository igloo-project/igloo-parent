package org.iglooproject.jpa.more.business.search.query;

import com.google.common.primitives.Ints;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.util.CollectionUtils;

public abstract class AbstractHibernateSearchSearchQuery<
        T, S extends ISort<Function<SearchSortFactory, SortFinalStep>>>
    extends AbstractSearchQuery<T, S> /* NOT Serializable */ {

  private final Class<T> mainClass;

  private SearchSession session;
  private SearchPredicateFactory pf;
  private List<SearchPredicate> predicates = new ArrayList<>();

  @SafeVarargs
  protected AbstractHibernateSearchSearchQuery(Class<T> clazz, S... defaultSorts) {
    super(defaultSorts);
    this.mainClass = clazz;
  }

  @PostConstruct
  private void init() {
    this.session = Search.session(entityManager);
    this.pf = session.scope(mainClass).predicate();
    addFilterBeforeCreateQuery();
  }

  protected SearchPredicateFactory predicateFactory() {
    return pf;
  }

  protected void predicate(SearchPredicate p) {
    predicates.add(p);
  }

  protected void must(Function<? super SearchPredicateFactory, ? extends PredicateFinalStep> f) {
    predicate(predicateFactory().bool().must(f).toPredicate());
  }

  // List and count
  /** Allow to add filter before generating the full text query. */
  protected void addFilterBeforeCreateQuery() {
    // Nothing
  }

  private SearchPredicate topLevelPredicate() {
    return pf.bool(
            b -> {
              b.must(SearchPredicateFactory::matchAll);
              for (SearchPredicate predicate : predicates) {
                b.must(predicate);
              }
            })
        .toPredicate();
  }

  @Override
  public List<T> fullList() {
    return session
        .search(mainClass)
        .where(topLevelPredicate())
        .sort(sortContributor())
        .fetchAllHits();
  }

  @Override
  public List<T> list(long offset, long limit) {
    return session
        .search(mainClass)
        .where(topLevelPredicate())
        .sort(sortContributor())
        .fetchHits(Ints.saturatedCast(offset), (Ints.saturatedCast(limit)));
  }

  @Override
  public long count() {
    return session
        .search(mainClass)
        .where(topLevelPredicate())
        .sort(sortContributor())
        .fetchTotalHitCount();
  }

  protected Function<SearchSortFactory, SortFinalStep> sortContributor() {
    return CollectionUtils.isEmpty(sortMap)
        ? SearchSortFactory::composite
        : f ->
            f.composite(
                c ->
                    sortMap.forEach(
                        (sort, order) ->
                            sort.getSortFields(order).forEach(s -> c.add(s.apply(f)))));
  }
}
