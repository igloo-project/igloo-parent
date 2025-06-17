package igloo.hibernatesearchv5;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.bindgen.BindingRoot;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetingRequest;
import org.iglooproject.jpa.more.business.search.query.AbstractSearchQuery;
import org.iglooproject.jpa.more.business.search.query.SearchQueryDefaultResult;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;
import org.iglooproject.spring.util.lucene.search.LuceneUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class OldAbstractHibernateSearchSearchQuery<T, S extends ISort<SortField>>
    extends AbstractSearchQuery<T, S> /* NOT Serializable */ {

  private final Class<? extends T> mainClass;
  private final Class<? extends T>[] classes;

  private BooleanJunction<?> junction;

  private FullTextEntityManager fullTextEntityManager;

  @Autowired private IHibernateSearchLuceneQueryFactory factory;

  private FullTextQuery fullTextQuery;

  private SearchQueryDefaultResult defaultResult = SearchQueryDefaultResult.EVERYTHING;

  @SuppressWarnings("unchecked")
  @SafeVarargs
  protected OldAbstractHibernateSearchSearchQuery(Class<? extends T> clazz, S... defaultSorts) {
    this(new Class[] {clazz}, defaultSorts);
  }

  @SafeVarargs
  protected OldAbstractHibernateSearchSearchQuery(Class<? extends T>[] classes, S... defaultSorts) {
    super(defaultSorts);
    this.mainClass = classes[0];
    this.classes = Arrays.copyOf(classes, classes.length);
  }

  @PostConstruct
  private void init() {
    this.fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    this.factory.setDefaultClass(mainClass);

    this.junction = getDefaultQueryBuilder().bool();
  }

  protected Analyzer getAnalyzer(Class<?> clazz) {
    return this.factory.getAnalyzer(clazz);
  }

  protected Analyzer getDefaultAnalyzer() {
    return this.factory.getDefaultAnalyzer();
  }

  protected QueryBuilder getDefaultQueryBuilder() {
    return this.factory.getDefaultQueryBuilder();
  }

  protected IHibernateSearchLuceneQueryFactory getFactory() {
    return this.factory;
  }

  protected FullTextEntityManager getFullTextEntityManager() {
    return fullTextEntityManager;
  }

  protected final SearchQueryDefaultResult getDefaultResult() {
    return defaultResult;
  }

  protected final void setDefaultResult(SearchQueryDefaultResult defaultResult) {
    this.defaultResult = defaultResult;
  }

  // Junction appender
  // 	>	Must
  protected void must(Query query) {
    if (query != null) {
      junction.must(query);
    }
  }

  protected void mustNot(Query query) {
    if (query != null) {
      junction.must(query).not();
    }
  }

  protected void mustIfNotNull(BooleanJunction<?> junction, Query... queries) {
    for (Query query : queries) {
      if (query != null) {
        junction.must(query);
      }
    }
  }

  // 	>	Should
  protected void should(Query query) {
    if (query != null) {
      junction.should(query);
    }
  }

  protected void shouldIfNotNull(BooleanJunction<?> junction, Query... queries) {
    for (Query query : queries) {
      if (query != null) {
        junction.should(query);
      }
    }
  }

  // List and count
  /**
   * Allow to add filter before generating the full text query.<br>
   * Sample:
   *
   * <ul>
   *   <li><code>must(matchIfGiven(Bindings.company().manager().organization(), organization))
   *       </code>
   *   <li><code>must(matchIfGiven(Bindings.company().status(), CompanyStatus.ACTIVE))</code>
   * </ul>
   */
  protected void addFilterBeforeCreateQuery() {
    // Nothing
  }

  private FullTextQuery getFullTextQuery() {
    if (fullTextQuery == null) {
      addFilterBeforeCreateQuery();

      if (junction.isEmpty()) {
        switch (getDefaultResult()) {
          case EVERYTHING:
            junction.must(getDefaultQueryBuilder().all().createQuery());
            break;
          case NOTHING:
            junction.must(LuceneUtils.NO_RESULT_QUERY);
            break;
        }
      }

      fullTextQuery = fullTextEntityManager.createFullTextQuery(junction.createQuery(), classes);
    }
    return fullTextQuery;
  }

  private FullTextQuery getFullTextQueryList(Long offset, Long limit) {
    FullTextQuery fullTextQuery = getFullTextQuery();

    if (offset != null) {
      fullTextQuery.setFirstResult(Ints.saturatedCast(offset));
    }
    if (limit != null) {
      fullTextQuery.setMaxResults(Ints.saturatedCast(limit));
    }

    Sort sort = SortUtils.getLuceneSortWithDefaults(sortMap, defaultSorts);
    if (sort != null && sort.getSort().length > 0) {
      SortFieldUtil.setSort(fullTextQuery, fullTextEntityManager, mainClass, sort);
    }
    return fullTextQuery;
  }

  @Override
  @Transactional(readOnly = true)
  @SuppressWarnings("unchecked")
  public final List<T> fullList() {
    return getFullTextQueryList(null, null).getResultList();
  }

  @Override
  @Transactional(readOnly = true)
  @SuppressWarnings("unchecked")
  public final List<T> list(long offset, long limit) {
    if (limit == 0) {
      return List.of();
    }
    return getFullTextQueryList(offset, limit).getResultList();
  }

  /**
   * <b>Warning : </b>To be projected, the field should be stored in the document.<br>
   * You can store the field in the document with the following method : {@link Field#store()}.
   *
   * @param offset
   * @param limit
   * @param field The field path
   */
  @SuppressWarnings("unchecked")
  protected <Q> List<Q> listProjection(Long offset, Long limit, String field) {
    List<Object[]> projections =
        getFullTextQueryList(offset, limit).setProjection(field).getResultList();
    return Lists.transform(projections, input -> (Q) input[0]);
  }

  @Override
  @Transactional(readOnly = true)
  public long count() {
    return getFullTextQuery().getResultSize();
  }

  // Query factory
  // 	> Match null
  /**
   * <strong>Be careful</strong>: using this method needs null values to be indexed. You can use
   * {@link NullEncodingGenericEntityIdFieldBridge} instead of the classical {@link
   * GenericEntityIdFieldBridge} for example.
   */
  protected Query matchNull(BindingRoot<?, ?> binding) {
    return getFactory().matchNull(binding);
  }

  protected Query matchNull(QueryBuilder builder, BindingRoot<?, ?> binding) {
    return getFactory().matchNull(builder, binding);
  }

  protected Query matchNull(String fieldPath) {
    return getFactory().matchNull(fieldPath);
  }

  protected Query matchNull(QueryBuilder builder, String fieldPath) {
    return getFactory().matchNull(builder, fieldPath);
  }

  // 	>	Match if given
  protected <P> Query matchIfGiven(BindingRoot<?, P> binding, P value) {
    return getFactory().matchIfGiven(binding, value);
  }

  protected <P> Query matchIfGiven(QueryBuilder builder, BindingRoot<?, P> binding, P value) {
    return getFactory().matchIfGiven(builder, binding, value);
  }

  protected <P> Query matchIfGiven(String fieldPath, P value) {
    return getFactory().matchIfGiven(fieldPath, value);
  }

  protected <P> Query matchIfGiven(QueryBuilder builder, String fieldPath, P value) {
    return getFactory().matchIfGiven(builder, fieldPath, value);
  }

  // 	>	Match one term if given
  protected Query matchOneTermIfGiven(BindingRoot<?, String> binding, String terms) {
    return getFactory().matchOneTermIfGiven(binding, terms);
  }

  protected Query matchOneTermIfGiven(
      QueryBuilder builder, BindingRoot<?, String> binding, String terms) {
    return getFactory().matchOneTermIfGiven(builder, binding, terms);
  }

  protected Query matchOneTermIfGiven(String fieldPath, String terms) {
    return getFactory().matchOneTermIfGiven(fieldPath, terms);
  }

  protected Query matchOneTermIfGiven(QueryBuilder builder, String fieldPath, String terms) {
    return getFactory().matchOneTermIfGiven(builder, fieldPath, terms);
  }

  // 	>	Match all terms if given
  @SafeVarargs
  protected final Query matchAllTermsIfGiven(
      Analyzer analyzer,
      String terms,
      BindingRoot<?, String> binding,
      BindingRoot<?, String>... otherBindings) {
    return getFactory().matchAllTermsIfGiven(analyzer, terms, binding, otherBindings);
  }

  @SafeVarargs
  protected final Query matchAllTermsIfGiven(
      String terms, BindingRoot<?, String> binding, BindingRoot<?, String>... otherBindings) {
    return getFactory().matchAllTermsIfGiven(terms, binding, otherBindings);
  }

  protected Query matchAllTermsIfGiven(String terms, String fieldPath, String... otherFieldPaths) {
    return matchAllTermsIfGiven(terms, Lists.asList(fieldPath, otherFieldPaths));
  }

  protected Query matchAllTermsIfGiven(
      Analyzer analyzer, String terms, String fieldPath, String... otherFieldPaths) {
    return matchAllTermsIfGiven(analyzer, terms, Lists.asList(fieldPath, otherFieldPaths));
  }

  protected Query matchAllTermsIfGiven(String terms, Iterable<String> fieldPaths) {
    return getFactory().matchAllTermsIfGiven(terms, fieldPaths);
  }

  protected Query matchAllTermsIfGiven(
      Analyzer analyzer, String terms, Iterable<String> fieldPaths) {
    return getFactory().matchAllTermsIfGiven(analyzer, terms, fieldPaths);
  }

  // 	>	Match autocomplete
  @SafeVarargs
  protected final Query matchAutocompleteIfGiven(
      Analyzer analyzer,
      String terms,
      BindingRoot<?, String> binding,
      BindingRoot<?, String>... otherBindings) {
    return getFactory().matchAutocompleteIfGiven(analyzer, terms, binding, otherBindings);
  }

  @SafeVarargs
  protected final Query matchAutocompleteIfGiven(
      String terms, BindingRoot<?, String> binding, BindingRoot<?, String>... otherBindings) {
    return getFactory().matchAutocompleteIfGiven(terms, binding, otherBindings);
  }

  protected Query matchAutocompleteIfGiven(String terms, Iterable<String> fieldPaths) {
    return getFactory().matchAutocompleteIfGiven(terms, fieldPaths);
  }

  protected Query matchAutocompleteIfGiven(String terms, String fieldPath) {
    return getFactory().matchAutocompleteIfGiven(terms, Collections.singleton(fieldPath));
  }

  protected Query matchAutocompleteIfGiven(
      Analyzer analyzer, String terms, Iterable<String> fieldPaths) {
    return getFactory().matchAutocompleteIfGiven(analyzer, terms, fieldPaths);
  }

  // 	>	Match fuzzy
  @SafeVarargs
  protected final Query matchFuzzyIfGiven(
      Analyzer analyzer,
      String terms,
      Integer maxEditDistance,
      BindingRoot<?, String> binding,
      BindingRoot<?, String>... otherBindings) {
    return getFactory().matchFuzzyIfGiven(analyzer, terms, maxEditDistance, binding, otherBindings);
  }

  @SafeVarargs
  protected final Query matchFuzzyIfGiven(
      String terms,
      Integer maxEditDistance,
      BindingRoot<?, String> binding,
      BindingRoot<?, String>... otherBindings) {
    return getFactory().matchFuzzyIfGiven(terms, maxEditDistance, binding, otherBindings);
  }

  protected Query matchFuzzyIfGiven(
      String terms, Integer maxEditDistance, Iterable<String> fieldPaths) {
    return getFactory().matchFuzzyIfGiven(terms, maxEditDistance, fieldPaths);
  }

  protected Query matchFuzzyIfGiven(
      Analyzer analyzer, String terms, Integer maxEditDistance, Iterable<String> fieldPaths) {
    return getFactory().matchFuzzyIfGiven(analyzer, terms, maxEditDistance, fieldPaths);
  }

  // 	>	Be included if given
  protected <P> Query beIncludedIfGiven(BindingRoot<?, ? extends Collection<P>> binding, P value) {
    return getFactory().beIncludedIfGiven(binding, value);
  }

  protected <P> Query beIncludedIfGiven(
      QueryBuilder builder, BindingRoot<?, ? extends Collection<P>> binding, P value) {
    return getFactory().beIncludedIfGiven(builder, binding, value);
  }

  protected <P> Query beIncludedIfGiven(String fieldPath, P value) {
    return getFactory().beIncludedIfGiven(fieldPath, value);
  }

  protected <P> Query beIncludedIfGiven(QueryBuilder builder, String fieldPath, P value) {
    return getFactory().beIncludedIfGiven(builder, fieldPath, value);
  }

  // 	>	Match one if given
  protected <P> Query matchOneIfGiven(
      BindingRoot<?, P> binding, Collection<? extends P> possibleValues) {
    return getFactory().matchOneIfGiven(binding, possibleValues);
  }

  protected <P> Query matchOneIfGiven(
      QueryBuilder builder, BindingRoot<?, P> binding, Collection<? extends P> possibleValues) {
    return getFactory().matchOneIfGiven(builder, binding, possibleValues);
  }

  protected <P> Query matchOneIfGiven(String fieldPath, Collection<? extends P> possibleValues) {
    return getFactory().matchOneIfGiven(fieldPath, possibleValues);
  }

  protected <P> Query matchOneIfGiven(
      QueryBuilder builder, String fieldPath, Collection<? extends P> possibleValues) {
    return getFactory().matchOneIfGiven(builder, fieldPath, possibleValues);
  }

  // 	>	Match all if given
  protected <P> Query matchAllIfGiven(
      BindingRoot<?, ? extends Collection<P>> binding, Collection<? extends P> possibleValues) {
    return getFactory().matchAllIfGiven(binding, possibleValues);
  }

  protected <P> Query matchAllIfGiven(
      QueryBuilder builder,
      BindingRoot<?, ? extends Collection<P>> binding,
      Collection<? extends P> possibleValues) {
    return getFactory().matchAllIfGiven(builder, binding, possibleValues);
  }

  protected <P> Query matchAllIfGiven(String fieldPath, Collection<? extends P> possibleValues) {
    return getFactory().matchAllIfGiven(fieldPath, possibleValues);
  }

  protected <P> Query matchAllIfGiven(
      QueryBuilder builder, String fieldPath, Collection<? extends P> values) {
    return getFactory().matchAllIfGiven(builder, fieldPath, values);
  }

  // 	>	Match if true
  protected Query matchIfTrue(BindingRoot<?, Boolean> binding, boolean value, Boolean mustMatch) {
    return getFactory().matchIfTrue(binding, value, mustMatch);
  }

  protected Query matchIfTrue(
      QueryBuilder builder, BindingRoot<?, Boolean> binding, boolean value, Boolean mustMatch) {
    return getFactory().matchIfTrue(builder, binding, value, mustMatch);
  }

  protected Query matchIfTrue(String fieldPath, boolean value, Boolean mustMatch) {
    return getFactory().matchIfTrue(fieldPath, value, mustMatch);
  }

  protected Query matchIfTrue(
      QueryBuilder builder, String fieldPath, boolean value, Boolean mustMatch) {
    return getFactory().matchIfTrue(builder, fieldPath, value, mustMatch);
  }

  // 	>	Match range (min, max, both)
  protected <P> Query matchRangeMin(BindingRoot<?, P> binding, P min) {
    return getFactory().matchRangeMin(binding, min);
  }

  protected <P> Query matchRangeMin(QueryBuilder builder, BindingRoot<?, P> binding, P min) {
    return getFactory().matchRangeMin(builder, binding, min);
  }

  protected <P> Query matchRangeMin(String fieldPath, P min) {
    return getFactory().matchRangeMin(fieldPath, min);
  }

  protected <P> Query matchRangeMin(QueryBuilder builder, String fieldPath, P min) {
    return getFactory().matchRangeMin(builder, fieldPath, min);
  }

  protected <P> Query matchRangeMax(BindingRoot<?, P> binding, P max) {
    return getFactory().matchRangeMax(binding, max);
  }

  protected <P> Query matchRangeMax(QueryBuilder builder, BindingRoot<?, P> binding, P max) {
    return getFactory().matchRangeMax(builder, binding, max);
  }

  protected <P> Query matchRangeMax(String fieldPath, P max) {
    return getFactory().matchRangeMax(fieldPath, max);
  }

  protected <P> Query matchRangeMax(QueryBuilder builder, String fieldPath, P max) {
    return getFactory().matchRangeMax(builder, fieldPath, max);
  }

  protected <P> Query matchRange(BindingRoot<?, P> binding, P min, P max) {
    return getFactory().matchRange(binding, min, max);
  }

  protected <P> Query matchRange(QueryBuilder builder, BindingRoot<?, P> binding, P min, P max) {
    return getFactory().matchRange(builder, binding, min, max);
  }

  protected <P> Query matchRange(String fieldPath, P min, P max) {
    return getFactory().matchRange(fieldPath, min, max);
  }

  protected <P> Query matchRange(QueryBuilder builder, String fieldPath, P min, P max) {
    return getFactory().matchRange(builder, fieldPath, min, max);
  }

  // Facets management

  private FacetManager getFacetManager() {
    return getFullTextQuery().getFacetManager();
  }

  protected void registerFacet(FacetingRequest facetingRequest) {
    getFacetManager().enableFaceting(facetingRequest);
  }

  protected void registerFacet(String facetName, String fieldPath) {
    FacetingRequest facetingRequest =
        getFactory()
            .getDefaultQueryBuilder()
            .facet()
            .name(facetName)
            .onField(fieldPath)
            .discrete()
            .includeZeroCounts(false)
            .createFacetingRequest();

    registerFacet(facetingRequest);
  }

  protected Collection<Facet> getFacets(String facetName) {
    return getFacetManager().getFacets(facetName);
  }

  public final List<String> listFacetValues(String facetName) {
    return getFacets(facetName).stream()
        .map(facet -> facet.getValue())
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
