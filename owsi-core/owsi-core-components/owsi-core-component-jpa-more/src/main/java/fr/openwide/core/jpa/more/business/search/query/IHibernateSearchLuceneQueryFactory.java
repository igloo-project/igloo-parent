package fr.openwide.core.jpa.more.business.search.query;

import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.bindgen.binding.AbstractBinding;
import org.hibernate.search.query.dsl.QueryBuilder;

public interface IHibernateSearchLuceneQueryFactory {

	Analyzer getAnalyzer(Class<?> clazz);

	Analyzer getDefaultAnalyzer();

	QueryBuilder getQueryBuilder(Class<?> clazz);

	QueryBuilder getDefaultQueryBuilder();

	void setDefaultClass(Class<?> defaultClass);

	Class<?> getDefaultClass();

	Query any(QueryBuilder builder, Query ... queries);

	Query any(Query ... queries);

	Query all(QueryBuilder builder, Query ... queries);

	Query all(Query ... queries);

	Query matchNull(AbstractBinding<?, ?> binding);

	Query matchNull(QueryBuilder builder, AbstractBinding<?, ?> binding);

	Query matchNull(String fieldPath);

	Query matchNull(QueryBuilder builder, String fieldPath);

	<P> Query matchIfGiven(AbstractBinding<?, P> binding, P value);

	<P> Query matchIfGiven(QueryBuilder builder, AbstractBinding<?, P> binding, P value);

	<P> Query matchIfGiven(String fieldPath, P value);

	<P> Query matchIfGiven(QueryBuilder builder, String fieldPath, P value);

	Query matchOneTermIfGiven(AbstractBinding<?, String> binding, String terms);

	Query matchOneTermIfGiven(QueryBuilder builder, AbstractBinding<?, String> binding, String terms);

	Query matchOneTermIfGiven(String fieldPath, String terms);

	Query matchOneTermIfGiven(QueryBuilder builder, String fieldPath, String terms);

	Query matchAllTermsIfGiven(Analyzer analyzer, String terms, AbstractBinding<?, String> binding,
			@SuppressWarnings("unchecked") AbstractBinding<?, String> ... otherBindings);

	Query matchAllTermsIfGiven(String terms, AbstractBinding<?, String> binding,
			@SuppressWarnings("unchecked") AbstractBinding<?, String> ... otherBindings);

	Query matchAllTermsIfGiven(String terms, Iterable<String> fieldPaths);

	Query matchAllTermsIfGiven(Analyzer analyzer, String terms, Iterable<String> fieldPaths);

	Query matchAutocompleteIfGiven(Analyzer analyzer, String terms, AbstractBinding<?, String> binding,
			@SuppressWarnings("unchecked") AbstractBinding<?, String> ... otherBindings);

	Query matchAutocompleteIfGiven(String terms, AbstractBinding<?, String> binding,
			@SuppressWarnings("unchecked") AbstractBinding<?, String> ... otherBindings);

	Query matchAutocompleteIfGiven(String terms, Iterable<String> fieldPaths);

	Query matchAutocompleteIfGiven(Analyzer analyzer, String terms, Iterable<String> fieldPaths);

	Query matchFuzzyIfGiven(Analyzer analyzer, String terms, Integer maxEditDistance, AbstractBinding<?, String> binding,
			@SuppressWarnings("unchecked") AbstractBinding<?, String> ... otherBindings);

	Query matchFuzzyIfGiven(String terms, Integer maxEditDistance, AbstractBinding<?, String> binding,
			@SuppressWarnings("unchecked") AbstractBinding<?, String> ... otherBindings);

	Query matchFuzzyIfGiven(String terms, Integer maxEditDistance, Iterable<String> fieldPaths);

	Query matchFuzzyIfGiven(Analyzer analyzer, String terms, Integer maxEditDistance, Iterable<String> fieldPaths);

	<P> Query beIncludedIfGiven(AbstractBinding<?, ? extends Collection<P>> binding, P value);

	<P> Query beIncludedIfGiven(QueryBuilder builder, AbstractBinding<?, ? extends Collection<P>> binding, P value);

	<P> Query beIncludedIfGiven(String fieldPath, P value);

	<P> Query beIncludedIfGiven(QueryBuilder builder, String fieldPath, P value);

	<P> Query matchOneIfGiven(AbstractBinding<?, P> binding, Collection<? extends P> possibleValues);

	<P> Query matchOneIfGiven(QueryBuilder builder, AbstractBinding<?, P> binding, Collection<? extends P> possibleValues);

	<P> Query matchOneIfGiven(String fieldPath, Collection<? extends P> possibleValues);

	<P> Query matchOneIfGiven(QueryBuilder builder, String fieldPath, Collection<? extends P> possibleValues);

	<P> Query matchAllIfGiven(AbstractBinding<?, ? extends Collection<P>> binding, Collection<? extends P> possibleValues);

	<P> Query matchAllIfGiven(QueryBuilder builder, AbstractBinding<?, ? extends Collection<P>> binding, Collection<? extends P> possibleValues);

	<P> Query matchAllIfGiven(String fieldPath, Collection<? extends P> possibleValues);

	<P> Query matchAllIfGiven(QueryBuilder builder, String fieldPath, Collection<? extends P> values);

	Query matchIfTrue(AbstractBinding<?, Boolean> binding, boolean value, Boolean mustMatch);

	Query matchIfTrue(QueryBuilder builder, AbstractBinding<?, Boolean> binding, boolean value, Boolean mustMatch);

	<P> Query matchIfTrue(String fieldPath, boolean value, Boolean mustMatch);

	Query matchIfTrue(QueryBuilder builder, String fieldPath, boolean value, Boolean mustMatch);

	<P> Query matchRangeMin(AbstractBinding<?, P> binding, P min);

	<P> Query matchRangeMin(QueryBuilder builder, AbstractBinding<?, P> binding, P min);

	<P> Query matchRangeMin(String fieldPath, P min);

	<P> Query matchRangeMin(QueryBuilder builder, String fieldPath, P min);

	<P> Query matchRangeMax(AbstractBinding<?, P> binding, P max);

	<P> Query matchRangeMax(QueryBuilder builder, AbstractBinding<?, P> binding, P max);

	<P> Query matchRangeMax(String fieldPath, P max);

	<P> Query matchRangeMax(QueryBuilder builder, String fieldPath, P max);

	<P> Query matchRange(AbstractBinding<?, P> binding, P min, P max);

	<P> Query matchRange(QueryBuilder builder, AbstractBinding<?, P> binding, P min, P max);

	<P> Query matchRange(String fieldPath, P min, P max);

	<P> Query matchRange(QueryBuilder builder, String fieldPath, P min, P max);

}
