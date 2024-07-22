package igloo.hibernatesearchv5;

import java.util.Collection;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.bindgen.BindingRoot;
import org.hibernate.search.query.dsl.QueryBuilder;

public interface IHibernateSearchLuceneQueryFactory {

  Analyzer getAnalyzer(Class<?> clazz);

  Analyzer getDefaultAnalyzer();

  QueryBuilder getQueryBuilder(Class<?> clazz);

  QueryBuilder getDefaultQueryBuilder();

  void setDefaultClass(Class<?> defaultClass);

  Class<?> getDefaultClass();

  Query any(QueryBuilder builder, Query... queries);

  Query any(Query... queries);

  Query all(QueryBuilder builder, Query... queries);

  Query all(Query... queries);

  Query matchNull(BindingRoot<?, ?> binding);

  Query matchNull(QueryBuilder builder, BindingRoot<?, ?> binding);

  Query matchNull(String fieldPath);

  Query matchNull(QueryBuilder builder, String fieldPath);

  <P> Query matchIfGiven(BindingRoot<?, P> binding, P value);

  <P> Query matchIfGiven(QueryBuilder builder, BindingRoot<?, P> binding, P value);

  <P> Query matchIfGiven(String fieldPath, P value);

  <P> Query matchIfGiven(QueryBuilder builder, String fieldPath, P value);

  Query matchOneTermIfGiven(BindingRoot<?, String> binding, String terms);

  Query matchOneTermIfGiven(QueryBuilder builder, BindingRoot<?, String> binding, String terms);

  Query matchOneTermIfGiven(String fieldPath, String terms);

  Query matchOneTermIfGiven(QueryBuilder builder, String fieldPath, String terms);

  Query matchAllTermsIfGiven(
      Analyzer analyzer,
      String terms,
      BindingRoot<?, String> binding,
      @SuppressWarnings("unchecked") BindingRoot<?, String>... otherBindings);

  Query matchAllTermsIfGiven(
      String terms,
      BindingRoot<?, String> binding,
      @SuppressWarnings("unchecked") BindingRoot<?, String>... otherBindings);

  Query matchAllTermsIfGiven(String terms, Iterable<String> fieldPaths);

  Query matchAllTermsIfGiven(Analyzer analyzer, String terms, Iterable<String> fieldPaths);

  Query matchAutocompleteIfGiven(
      Analyzer analyzer,
      String terms,
      BindingRoot<?, String> binding,
      @SuppressWarnings("unchecked") BindingRoot<?, String>... otherBindings);

  Query matchAutocompleteIfGiven(
      String terms,
      BindingRoot<?, String> binding,
      @SuppressWarnings("unchecked") BindingRoot<?, String>... otherBindings);

  Query matchAutocompleteIfGiven(String terms, Iterable<String> fieldPaths);

  Query matchAutocompleteIfGiven(Analyzer analyzer, String terms, Iterable<String> fieldPaths);

  Query matchFuzzyIfGiven(
      Analyzer analyzer,
      String terms,
      Integer maxEditDistance,
      BindingRoot<?, String> binding,
      @SuppressWarnings("unchecked") BindingRoot<?, String>... otherBindings);

  Query matchFuzzyIfGiven(
      String terms,
      Integer maxEditDistance,
      BindingRoot<?, String> binding,
      @SuppressWarnings("unchecked") BindingRoot<?, String>... otherBindings);

  Query matchFuzzyIfGiven(String terms, Integer maxEditDistance, Iterable<String> fieldPaths);

  Query matchFuzzyIfGiven(
      Analyzer analyzer, String terms, Integer maxEditDistance, Iterable<String> fieldPaths);

  <P> Query beIncludedIfGiven(BindingRoot<?, ? extends Collection<P>> binding, P value);

  <P> Query beIncludedIfGiven(
      QueryBuilder builder, BindingRoot<?, ? extends Collection<P>> binding, P value);

  <P> Query beIncludedIfGiven(String fieldPath, P value);

  <P> Query beIncludedIfGiven(QueryBuilder builder, String fieldPath, P value);

  <P> Query matchOneIfGiven(BindingRoot<?, P> binding, Collection<? extends P> possibleValues);

  <P> Query matchOneIfGiven(
      QueryBuilder builder, BindingRoot<?, P> binding, Collection<? extends P> possibleValues);

  <P> Query matchOneIfGiven(String fieldPath, Collection<? extends P> possibleValues);

  <P> Query matchOneIfGiven(
      QueryBuilder builder, String fieldPath, Collection<? extends P> possibleValues);

  <P> Query matchAllIfGiven(
      BindingRoot<?, ? extends Collection<P>> binding, Collection<? extends P> possibleValues);

  <P> Query matchAllIfGiven(
      QueryBuilder builder,
      BindingRoot<?, ? extends Collection<P>> binding,
      Collection<? extends P> possibleValues);

  <P> Query matchAllIfGiven(String fieldPath, Collection<? extends P> possibleValues);

  <P> Query matchAllIfGiven(QueryBuilder builder, String fieldPath, Collection<? extends P> values);

  Query matchIfTrue(BindingRoot<?, Boolean> binding, boolean value, Boolean mustMatch);

  Query matchIfTrue(
      QueryBuilder builder, BindingRoot<?, Boolean> binding, boolean value, Boolean mustMatch);

  Query matchIfTrue(String fieldPath, boolean value, Boolean mustMatch);

  Query matchIfTrue(QueryBuilder builder, String fieldPath, boolean value, Boolean mustMatch);

  <P> Query matchRangeMin(BindingRoot<?, P> binding, P min);

  <P> Query matchRangeMin(QueryBuilder builder, BindingRoot<?, P> binding, P min);

  <P> Query matchRangeMin(String fieldPath, P min);

  <P> Query matchRangeMin(QueryBuilder builder, String fieldPath, P min);

  <P> Query matchRangeMax(BindingRoot<?, P> binding, P max);

  <P> Query matchRangeMax(QueryBuilder builder, BindingRoot<?, P> binding, P max);

  <P> Query matchRangeMax(String fieldPath, P max);

  <P> Query matchRangeMax(QueryBuilder builder, String fieldPath, P max);

  <P> Query matchRange(BindingRoot<?, P> binding, P min, P max);

  <P> Query matchRange(QueryBuilder builder, BindingRoot<?, P> binding, P min, P max);

  <P> Query matchRange(String fieldPath, P min, P max);

  <P> Query matchRange(QueryBuilder builder, String fieldPath, P min, P max);
}
