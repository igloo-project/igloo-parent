package igloo.hibernatesearchv5;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.bindgen.BindingRoot;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.iglooproject.functional.Function2;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.spring.util.lucene.search.LuceneUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Deprecated
public class HibernateSearchLuceneQueryFactoryImpl implements IHibernateSearchLuceneQueryFactory {

  private static final Function2<BindingRoot<?, String>, String> BINDING_TO_PATH_FUNCTION =
      input -> {
        if (input == null) {
          throw new IllegalStateException("Path may not be null.");
        }
        return input.getPath();
      };

  @PersistenceContext private EntityManager entityManager;

  @Autowired private IHibernateSearchService hibernateSearchService;

  private FullTextEntityManager fullTextEntityManager;

  private Map<Class<?>, QueryBuilder> queryBuilderCache = new HashMap<>();
  private Map<Class<?>, Analyzer> analyzerCache = Maps.newHashMap();

  private Class<?> defaultClass;

  @PostConstruct
  private void init() {
    this.fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
  }

  protected FullTextEntityManager getFullTextEntityManager() {
    return fullTextEntityManager;
  }

  @Override
  public Class<?> getDefaultClass() {
    return defaultClass;
  }

  @Override
  public void setDefaultClass(Class<?> defaultClass) {
    this.defaultClass = defaultClass;
  }

  @Override
  public QueryBuilder getDefaultQueryBuilder() {
    return getQueryBuilder(defaultClass);
  }

  @Override
  public QueryBuilder getQueryBuilder(Class<?> clazz) {
    QueryBuilder queryBuilder = queryBuilderCache.get(clazz);
    if (queryBuilder == null) {
      queryBuilder = createQueryBuilder(fullTextEntityManager, clazz);
      queryBuilderCache.put(clazz, queryBuilder);
    }
    return queryBuilder;
  }

  protected QueryBuilder createQueryBuilder(
      FullTextEntityManager fullTextEntityManager, Class<?> clazz) {
    return fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
  }

  @Override
  public Analyzer getDefaultAnalyzer() {
    return getAnalyzer(defaultClass);
  }

  @Override
  public Analyzer getAnalyzer(Class<?> clazz) {
    Analyzer analyzer = analyzerCache.get(clazz);
    if (analyzer == null) {
      analyzer = createAnalyzer(fullTextEntityManager, clazz);
      analyzerCache.put(clazz, analyzer);
    }
    return analyzer;
  }

  protected Analyzer createAnalyzer(FullTextEntityManager fullTextEntityManager, Class<?> clazz) {
    // Igloo 6.x: no longer any « smart » lookup
    return new StandardAnalyzer();
  }

  // 	Any/all
  @Override
  public Query all(Query... queries) {
    return all(getDefaultQueryBuilder(), queries);
  }

  @Override
  public Query all(QueryBuilder builder, Query... queries) {
    BooleanJunction<?> junction = null;
    for (Query query : queries) {
      if (query != null) {
        junction = junction != null ? junction : builder.bool();
        junction.must(query);
      }
    }
    return junction != null ? junction.createQuery() : null;
  }

  @Override
  public Query any(Query... queries) {
    return any(getDefaultQueryBuilder(), queries);
  }

  @Override
  public Query any(QueryBuilder builder, Query... queries) {
    BooleanJunction<?> junction = null;
    for (Query query : queries) {
      if (query != null) {
        junction = junction != null ? junction : builder.bool();
        junction.should(query);
      }
    }
    return junction != null ? junction.createQuery() : null;
  }

  // 	> Match null
  /**
   * <strong>Be careful</strong>: using this method needs null values to be indexed. You can use
   * {@link NullEncodingGenericEntityIdFieldBridge} instead of the classical {@link
   * GenericEntityIdFieldBridge} for example.
   */
  @Override
  public final Query matchNull(BindingRoot<?, ?> binding) {
    return matchNull(getDefaultQueryBuilder(), binding);
  }

  @Override
  public final Query matchNull(QueryBuilder builder, BindingRoot<?, ?> binding) {
    return matchNull(builder, binding.getPath());
  }

  @Override
  public final Query matchNull(String fieldPath) {
    return matchNull(getDefaultQueryBuilder(), fieldPath);
  }

  @Override
  public Query matchNull(QueryBuilder builder, String fieldPath) {
    return builder.keyword().onField(fieldPath).matching(null).createQuery();
  }

  // 	>	Match if given
  @Override
  public final <P> Query matchIfGiven(BindingRoot<?, P> binding, P value) {
    return matchIfGiven(getDefaultQueryBuilder(), binding, value);
  }

  @Override
  public final <P> Query matchIfGiven(QueryBuilder builder, BindingRoot<?, P> binding, P value) {
    return matchIfGiven(builder, binding.getPath(), value);
  }

  @Override
  public final <P> Query matchIfGiven(String fieldPath, P value) {
    return matchIfGiven(getDefaultQueryBuilder(), fieldPath, value);
  }

  @Override
  public <P> Query matchIfGiven(QueryBuilder builder, String fieldPath, P value) {
    if (value == null) {
      return null;
    }

    return builder.keyword().onField(fieldPath).matching(value).createQuery();
  }

  // 	>	Match one term if given
  @Override
  public final Query matchOneTermIfGiven(BindingRoot<?, String> binding, String terms) {
    return matchOneTermIfGiven(getDefaultQueryBuilder(), binding.getPath(), terms);
  }

  @Override
  public final Query matchOneTermIfGiven(
      QueryBuilder builder, BindingRoot<?, String> binding, String terms) {
    return matchOneTermIfGiven(builder, binding.getPath(), terms);
  }

  @Override
  public final Query matchOneTermIfGiven(String fieldPath, String terms) {
    return matchOneTermIfGiven(getDefaultQueryBuilder(), fieldPath, terms);
  }

  @Override
  public Query matchOneTermIfGiven(QueryBuilder builder, String fieldPath, String terms) {
    if (!StringUtils.hasText(terms)) {
      return null;
    }
    return builder.keyword().onField(fieldPath).matching(terms).createQuery();
  }

  // 	>	Match all terms if given
  @Override
  @SafeVarargs
  public final Query matchAllTermsIfGiven(
      Analyzer analyzer,
      String terms,
      BindingRoot<?, String> binding,
      BindingRoot<?, String>... otherBindings) {
    return matchAllTermsIfGiven(
        analyzer,
        terms,
        Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
  }

  @Override
  @SafeVarargs
  public final Query matchAllTermsIfGiven(
      String terms, BindingRoot<?, String> binding, BindingRoot<?, String>... otherBindings) {
    return matchAllTermsIfGiven(
        getDefaultAnalyzer(),
        terms,
        Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
  }

  @Override
  public final Query matchAllTermsIfGiven(String terms, Iterable<String> fieldPaths) {
    return matchAllTermsIfGiven(getDefaultAnalyzer(), terms, fieldPaths);
  }

  @Override
  public Query matchAllTermsIfGiven(Analyzer analyzer, String terms, Iterable<String> fieldPaths) {
    if (!StringUtils.hasText(terms)) {
      return null;
    }
    Map<String, Float> fields = Maps.newHashMap();
    for (String fieldPath : fieldPaths) {
      fields.put(fieldPath, 1.0f);
    }
    SimpleQueryParser parser = new SimpleQueryParser(analyzer, fields);
    parser.setDefaultOperator(BooleanClause.Occur.MUST);
    return parser.parse(terms);
  }

  // 	>	Match autocomplete
  @Override
  @SafeVarargs
  public final Query matchAutocompleteIfGiven(
      Analyzer analyzer,
      String terms,
      BindingRoot<?, String> binding,
      BindingRoot<?, String>... otherBindings) {
    return matchAutocompleteIfGiven(
        analyzer,
        terms,
        Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
  }

  @Override
  @SafeVarargs
  public final Query matchAutocompleteIfGiven(
      String terms, BindingRoot<?, String> binding, BindingRoot<?, String>... otherBindings) {
    return matchAutocompleteIfGiven(
        getDefaultAnalyzer(),
        terms,
        Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
  }

  @Override
  public final Query matchAutocompleteIfGiven(String terms, Iterable<String> fieldPaths) {
    return matchAutocompleteIfGiven(getDefaultAnalyzer(), terms, fieldPaths);
  }

  @Override
  public Query matchAutocompleteIfGiven(
      Analyzer analyzer, String terms, Iterable<String> fieldPaths) {
    if (!StringUtils.hasText(terms)) {
      return null;
    }
    return LuceneUtils.getAutocompleteQuery(fieldPaths, analyzer, terms);
  }

  // 	>	Match fuzzy
  @Override
  @SafeVarargs
  public final Query matchFuzzyIfGiven(
      Analyzer analyzer,
      String terms,
      Integer maxEditDistance,
      BindingRoot<?, String> binding,
      BindingRoot<?, String>... otherBindings) {
    return matchFuzzyIfGiven(
        analyzer,
        terms,
        maxEditDistance,
        Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
  }

  @Override
  @SafeVarargs
  public final Query matchFuzzyIfGiven(
      String terms,
      Integer maxEditDistance,
      BindingRoot<?, String> binding,
      BindingRoot<?, String>... otherBindings) {
    return matchFuzzyIfGiven(
        getDefaultAnalyzer(),
        terms,
        maxEditDistance,
        Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
  }

  @Override
  public final Query matchFuzzyIfGiven(
      String terms, Integer maxEditDistance, Iterable<String> fieldPaths) {
    return matchFuzzyIfGiven(getDefaultAnalyzer(), terms, maxEditDistance, fieldPaths);
  }

  @Override
  public Query matchFuzzyIfGiven(
      Analyzer analyzer, String terms, Integer maxEditDistance, Iterable<String> fieldPaths) {
    if (!StringUtils.hasText(terms)) {
      return null;
    }
    return LuceneUtils.getSimilarityQuery(fieldPaths, analyzer, terms, maxEditDistance);
  }

  // 	>	Be included if given
  @Override
  public final <P> Query beIncludedIfGiven(
      BindingRoot<?, ? extends Collection<P>> binding, P value) {
    return beIncludedIfGiven(getDefaultQueryBuilder(), binding, value);
  }

  @Override
  public final <P> Query beIncludedIfGiven(
      QueryBuilder builder, BindingRoot<?, ? extends Collection<P>> binding, P value) {
    return beIncludedIfGiven(builder, binding.getPath(), value);
  }

  @Override
  public final <P> Query beIncludedIfGiven(String fieldPath, P value) {
    return beIncludedIfGiven(getDefaultQueryBuilder(), fieldPath, value);
  }

  @Override
  public <P> Query beIncludedIfGiven(QueryBuilder builder, String fieldPath, P value) {
    if (value == null) {
      return null;
    }
    return builder.keyword().onField(fieldPath).matching(value).createQuery();
  }

  // 	>	Match one if given
  @Override
  public final <P> Query matchOneIfGiven(
      BindingRoot<?, P> binding, Collection<? extends P> possibleValues) {
    return matchOneIfGiven(getDefaultQueryBuilder(), binding, possibleValues);
  }

  @Override
  public final <P> Query matchOneIfGiven(
      QueryBuilder builder, BindingRoot<?, P> binding, Collection<? extends P> possibleValues) {
    return matchOneIfGiven(builder, binding.getPath(), possibleValues);
  }

  @Override
  public final <P> Query matchOneIfGiven(String fieldPath, Collection<? extends P> possibleValues) {
    return matchOneIfGiven(getDefaultQueryBuilder(), fieldPath, possibleValues);
  }

  @Override
  public <P> Query matchOneIfGiven(
      QueryBuilder builder, String fieldPath, Collection<? extends P> possibleValues) {
    if (possibleValues == null || possibleValues.isEmpty()) {
      return null;
    }
    BooleanJunction<?> subJunction = builder.bool();
    for (P possibleValue : possibleValues) {
      subJunction.should(
          builder.keyword().onField(fieldPath).matching(possibleValue).createQuery());
    }
    return subJunction.createQuery();
  }

  // 	>	Match all if given
  @Override
  public final <P> Query matchAllIfGiven(
      BindingRoot<?, ? extends Collection<P>> binding, Collection<? extends P> possibleValues) {
    return matchAllIfGiven(getDefaultQueryBuilder(), binding, possibleValues);
  }

  @Override
  public final <P> Query matchAllIfGiven(
      QueryBuilder builder,
      BindingRoot<?, ? extends Collection<P>> binding,
      Collection<? extends P> possibleValues) {
    return matchAllIfGiven(builder, binding.getPath(), possibleValues);
  }

  @Override
  public final <P> Query matchAllIfGiven(String fieldPath, Collection<? extends P> possibleValues) {
    return matchAllIfGiven(getDefaultQueryBuilder(), fieldPath, possibleValues);
  }

  @Override
  public <P> Query matchAllIfGiven(
      QueryBuilder builder, String fieldPath, Collection<? extends P> values) {
    if (values == null || values.isEmpty()) {
      return null;
    }
    BooleanJunction<?> subJunction = builder.bool();
    for (P possibleValue : values) {
      subJunction.must(builder.keyword().onField(fieldPath).matching(possibleValue).createQuery());
    }
    return subJunction.createQuery();
  }

  // 	>	Match if true
  @Override
  public final Query matchIfTrue(
      BindingRoot<?, Boolean> binding, boolean value, Boolean mustMatch) {
    return matchIfTrue(getDefaultQueryBuilder(), binding, value, mustMatch);
  }

  @Override
  public final Query matchIfTrue(
      QueryBuilder builder, BindingRoot<?, Boolean> binding, boolean value, Boolean mustMatch) {
    return matchIfTrue(builder, binding.getPath(), value, mustMatch);
  }

  @Override
  public final Query matchIfTrue(String fieldPath, boolean value, Boolean mustMatch) {
    return matchIfTrue(getDefaultQueryBuilder(), fieldPath, value, mustMatch);
  }

  @Override
  public Query matchIfTrue(
      QueryBuilder builder, String fieldPath, boolean value, Boolean mustMatch) {
    if (mustMatch == null || !mustMatch) {
      return null;
    }
    return builder.keyword().onField(fieldPath).matching(value).createQuery();
  }

  // 	>	Match range (min, max, both)
  @Override
  public final <P> Query matchRangeMin(BindingRoot<?, P> binding, P min) {
    return matchRangeMin(getDefaultQueryBuilder(), binding, min);
  }

  @Override
  public final <P> Query matchRangeMin(QueryBuilder builder, BindingRoot<?, P> binding, P min) {
    return matchRangeMin(builder, binding.getPath(), min);
  }

  @Override
  public final <P> Query matchRangeMin(String fieldPath, P min) {
    return matchRangeMin(getDefaultQueryBuilder(), fieldPath, min);
  }

  @Override
  public <P> Query matchRangeMin(QueryBuilder builder, String fieldPath, P min) {
    if (min == null) {
      return null;
    }
    return builder.range().onField(fieldPath).above(min).createQuery();
  }

  @Override
  public final <P> Query matchRangeMax(BindingRoot<?, P> binding, P max) {
    return matchRangeMax(getDefaultQueryBuilder(), binding, max);
  }

  @Override
  public final <P> Query matchRangeMax(QueryBuilder builder, BindingRoot<?, P> binding, P max) {
    return matchRangeMax(builder, binding.getPath(), max);
  }

  @Override
  public final <P> Query matchRangeMax(String fieldPath, P max) {
    return matchRangeMax(getDefaultQueryBuilder(), fieldPath, max);
  }

  @Override
  public <P> Query matchRangeMax(QueryBuilder builder, String fieldPath, P max) {
    if (max == null) {
      return null;
    }
    return builder.range().onField(fieldPath).below(max).createQuery();
  }

  @Override
  public final <P> Query matchRange(BindingRoot<?, P> binding, P min, P max) {
    return matchRange(getDefaultQueryBuilder(), binding.getPath(), min, max);
  }

  @Override
  public final <P> Query matchRange(QueryBuilder builder, BindingRoot<?, P> binding, P min, P max) {
    return matchRange(builder, binding.getPath(), min, max);
  }

  @Override
  public final <P> Query matchRange(String fieldPath, P min, P max) {
    return matchRange(getDefaultQueryBuilder(), fieldPath, min, max);
  }

  @Override
  public <P> Query matchRange(QueryBuilder builder, String fieldPath, P min, P max) {
    if (max != null && min != null) {
      return builder.range().onField(fieldPath).from(min).to(max).createQuery();
    } else if (min != null) {
      return matchRangeMin(builder, fieldPath, min);
    } else if (max != null) {
      return matchRangeMax(builder, fieldPath, max);
    } else {
      return null;
    }
  }
}
