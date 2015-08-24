package fr.openwide.core.jpa.more.business.search.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.bindgen.binding.AbstractBinding;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.SortUtils;
import fr.openwide.core.jpa.util.EntityManagerUtils;
import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.core.spring.util.lucene.search.LuceneUtils;

public abstract class AbstractHibernateSearchSearchQuery<T, S extends ISort<SortField>> extends AbstractSearchQuery<T, S> /* NOT Serializable */ {
	
	private static final Function<AbstractBinding<?, String>, String> BINDING_TO_PATH_FUNCTION = new BindingToPathFunction();
	
	@Autowired
	private EntityManagerUtils entityManagerUtils;
	
	private final Class<? extends T> mainClass;
	private final Class<? extends T>[] classes;
	
	/**
	 * Use only during the builder phase: you need a fresh EntityManager (ie not the one which might have been injected
	 * in another request) to execute the query.
	 */
	private FullTextEntityManager builderFullTextEntityManager;
	
	private BooleanJunction<?> junction;
	private QueryBuilder defaultQueryBuilder;
	private Map<Class<?>, Analyzer> analyzerCache = new HashMap<Class<?>, Analyzer>();
	
	@SuppressWarnings("unchecked")
	@SafeVarargs
	protected AbstractHibernateSearchSearchQuery(Class<? extends T> clazz, S ... defaultSorts) {
		this(new Class[] { clazz }, defaultSorts);
	}
	
	@SafeVarargs
	protected AbstractHibernateSearchSearchQuery(Class<? extends T>[] classes, S ... defaultSorts) {
		super(defaultSorts);
		this.mainClass = classes[0];
		this.classes = Arrays.copyOf(classes, classes.length);
	}
	
	@PostConstruct
	private void init() {
		builderFullTextEntityManager = getFullTextEntityManager();
		
		defaultQueryBuilder = builderFullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(mainClass).get();
		
		junction = defaultQueryBuilder.bool().must(defaultQueryBuilder.all().createQuery());
	}
	
	protected FullTextEntityManager getFullTextEntityManager() {
		return Search.getFullTextEntityManager(entityManagerUtils.getCurrentEntityManager());
	}
	
	protected Analyzer getAnalyzer(Class<?> clazz) {
		if (analyzerCache.containsKey(clazz)) {
			return analyzerCache.get(clazz);
		} else {
			Analyzer analyzer = builderFullTextEntityManager.getSearchFactory().getAnalyzer(clazz);
			analyzerCache.put(clazz, analyzer);
			return analyzer;
		}
	}
	
	protected Analyzer getAnalyzer() {
		return getAnalyzer(mainClass);
	}
	
	protected QueryBuilder getDefaultQueryBuilder() {
		return defaultQueryBuilder;
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
	
	protected void mustIfNotNull(BooleanJunction<?> junction, Query ... queries) {
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
	
	protected void shouldIfNotNull(BooleanJunction<?> junction, Query ... queries) {
		for (Query query : queries) {
			if (query != null) {
				junction.should(query);
			}
		}
	}
	
	private FullTextQuery getFullTextQuery() {
		return getFullTextEntityManager().createFullTextQuery(junction.createQuery(), classes);
	}
	
	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<T> list(long offset, long limit) {
		FullTextQuery fullTextQuery = getFullTextQuery();
		
		if (Long.valueOf(offset) != null) {
			fullTextQuery.setFirstResult((int) offset);
		}
		
		if (Long.valueOf(limit) != null) {
			fullTextQuery.setMaxResults((int) limit);
		}
		
		Sort sort = SortUtils.getLuceneSortWithDefaults(sortMap, defaultSorts);
		if (sort != null && sort.getSort().length > 0) {
			fullTextQuery.setSort(sort);
		}
		
		return fullTextQuery.getResultList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public long count() {
		return getFullTextQuery().getResultSize();
	}
	
	// Query factory
	// 	>	Match if given
	protected <P> Query matchIfGiven(AbstractBinding<?, P> binding, P value) {
		return matchIfGiven(defaultQueryBuilder, binding, value);
	}
	
	protected <P> Query matchIfGiven(QueryBuilder builder, AbstractBinding<?, P> binding, P value) {
		return matchIfGiven(builder, binding.getPath(), value);
	}
	
	protected <P> Query matchIfGiven(String fieldPath, P value) {
		return matchIfGiven(defaultQueryBuilder, fieldPath, value);
	}
	
	protected <P> Query matchIfGiven(QueryBuilder builder, String fieldPath, P value) {
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			throw new IllegalArgumentException("If you want to match a String, use matchOneTermIfGiven or matchAllTermIfGiven instead.");
		}
		
		return builder.keyword()
				.onField(fieldPath)
				.matching(value)
				.createQuery();
	}
	
	// 	>	Match one term if given
	protected Query matchOneTermIfGiven(AbstractBinding<?, String> binding, String terms) {
		return matchOneTermIfGiven(defaultQueryBuilder, binding.getPath(), terms);
	}
	
	protected Query matchOneTermIfGiven(QueryBuilder builder, AbstractBinding<?, String> binding, String terms) {
		return matchOneTermIfGiven(builder, binding.getPath(), terms);
	}
	
	protected Query matchOneTermIfGiven(String fieldPath, String terms) {
		return matchOneTermIfGiven(defaultQueryBuilder, fieldPath, terms);
	}
	
	protected Query matchOneTermIfGiven(QueryBuilder builder, String fieldPath, String terms) {
		if (!StringUtils.hasText(terms)) {
			return null;
		}
		return builder.keyword()
				.onField(fieldPath)
				.matching(terms)
				.createQuery();
	}
	
	// 	>	Match all terms if given
	@SafeVarargs
	protected final Query matchAllTermsIfGiven(Analyzer analyzer, String terms, AbstractBinding<?, String> binding, AbstractBinding<?, String> ... otherBindings) {
		return matchAllTermsIfGiven(analyzer, terms, Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
	}
	
	@SafeVarargs
	protected final Query matchAllTermsIfGiven(String terms, AbstractBinding<?, String> binding, AbstractBinding<?, String> ... otherBindings) {
		return matchAllTermsIfGiven(getAnalyzer(), terms, Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
	}

	protected Query matchAllTermsIfGiven(String terms, Iterable<String> fieldPaths) {
		return matchAllTermsIfGiven(getAnalyzer(), terms, fieldPaths);
	}
	
	protected Query matchAllTermsIfGiven(Analyzer analyzer, String terms, Iterable<String> fieldPaths) {
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
	@SafeVarargs
	protected final Query matchAutocompleteIfGiven(Analyzer analyzer, String terms, AbstractBinding<?, String> binding, AbstractBinding<?, String> ... otherBindings) {
		return matchAutocompleteIfGiven(analyzer, terms, Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
	}
	
	@SafeVarargs
	protected final Query matchAutocompleteIfGiven(String terms, AbstractBinding<?, String> binding, AbstractBinding<?, String> ... otherBindings) {
		return matchAutocompleteIfGiven(getAnalyzer(), terms, Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
	}

	protected Query matchAutocompleteIfGiven(String terms, Iterable<String> fieldPaths) {
		return matchAutocompleteIfGiven(getAnalyzer(), terms, fieldPaths);
	}
	
	protected Query matchAutocompleteIfGiven(Analyzer analyzer, String terms, Iterable<String> fieldPaths) {
		if (!StringUtils.hasText(terms)) {
			return null;
		}
		return LuceneUtils.getAutocompleteQuery(fieldPaths, analyzer, terms);
	}

	// 	>	Match fuzzy
	@SafeVarargs
	protected final Query matchFuzzyIfGiven(Analyzer analyzer, String terms, Integer maxEditDistance,
			AbstractBinding<?, String> binding, AbstractBinding<?, String> ... otherBindings) {
		return matchFuzzyIfGiven(analyzer, terms, maxEditDistance, Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
	}
	
	@SafeVarargs
	protected final Query matchFuzzyIfGiven(String terms, Integer maxEditDistance,
			AbstractBinding<?, String> binding, AbstractBinding<?, String> ... otherBindings) {
		return matchFuzzyIfGiven(getAnalyzer(), terms, maxEditDistance, Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
	}

	protected Query matchFuzzyIfGiven(String terms, Integer maxEditDistance, Iterable<String> fieldPaths) {
		return matchFuzzyIfGiven(getAnalyzer(), terms, maxEditDistance, fieldPaths);
	}
	
	protected Query matchFuzzyIfGiven(Analyzer analyzer, String terms, Integer maxEditDistance, Iterable<String> fieldPaths) {
		if (!StringUtils.hasText(terms)) {
			return null;
		}
		return LuceneUtils.getSimilarityQuery(fieldPaths, analyzer, terms, maxEditDistance);
	}
	
	// 	>	Be included if given
	protected <P> Query beIncludedIfGiven(AbstractBinding<?, ? extends Collection<P>> binding, P value) {
		return beIncludedIfGiven(defaultQueryBuilder, binding, value);
	}
	
	protected <P> Query beIncludedIfGiven(QueryBuilder builder, AbstractBinding<?, ? extends Collection<P>> binding, P value) {
		return beIncludedIfGiven(builder, binding.getPath(), value);
	}
	
	protected <P> Query beIncludedIfGiven(String fieldPath, P value) {
		return beIncludedIfGiven(defaultQueryBuilder, fieldPath, value);
	}
	
	protected <P> Query beIncludedIfGiven(QueryBuilder builder, String fieldPath, P value) {
		if (value == null) {
			return null;
		}
		return builder.keyword()
				.onField(fieldPath)
				.matching(value)
				.createQuery();
		
	}
	
	// 	>	Match one if given
	protected <P> Query matchOneIfGiven(AbstractBinding<?, P> binding, Collection<? extends P> possibleValues) {
		return matchOneIfGiven(defaultQueryBuilder, binding, possibleValues);
	}
	
	protected <P> Query matchOneIfGiven(QueryBuilder builder, AbstractBinding<?, P> binding, Collection<? extends P> possibleValues) {
		return matchOneIfGiven(builder, binding.getPath(), possibleValues);
	}
	
	protected <P> Query matchOneIfGiven(String fieldPath, Collection<? extends P> possibleValues) {
		return matchOneIfGiven(defaultQueryBuilder, fieldPath, possibleValues);
	}
	
	protected <P> Query matchOneIfGiven(QueryBuilder builder, String fieldPath, Collection<? extends P> possibleValues) {
		if (possibleValues == null || possibleValues.isEmpty()) {
			return null;
		}
		BooleanJunction<?> subJunction = builder.bool();
		for (P possibleValue : possibleValues) {
			subJunction.should(builder.keyword()
					.onField(fieldPath)
					.matching(possibleValue)
					.createQuery());
		}
		return subJunction.createQuery();
	}
	
	// 	>	Match all if given
	protected <P> Query matchAllIfGiven(AbstractBinding<?, P> binding, Collection<? extends P> possibleValues) {
		return matchAllIfGiven(defaultQueryBuilder, binding, possibleValues);
	}

	protected <P> Query matchAllIfGiven(QueryBuilder builder, AbstractBinding<?, P> binding,
			Collection<? extends P> possibleValues) {
		return matchAllIfGiven(builder, binding.getPath(), possibleValues);
	}

	protected <P> Query matchAllIfGiven(String fieldPath, Collection<? extends P> possibleValues) {
		return matchAllIfGiven(defaultQueryBuilder, fieldPath, possibleValues);
	}
	
	protected <P> Query matchAllIfGiven(QueryBuilder builder, String fieldPath, Collection<? extends P> values) {
		if (values == null || values.isEmpty()) {
			return null;
		}
		BooleanJunction<?> subJunction = builder.bool();
		for (P possibleValue : values) {
			subJunction.must(builder.keyword()
					.onField(fieldPath)
					.matching(possibleValue)
					.createQuery());
		}
		return subJunction.createQuery();
	}
	
	// 	>	Match if true
	protected Query matchIfTrue(AbstractBinding<?, Boolean> binding, boolean value, Boolean mustMatch) {
		return matchIfTrue(defaultQueryBuilder, binding, value, mustMatch);
	}
	
	protected Query matchIfTrue(QueryBuilder builder, AbstractBinding<?, Boolean> binding, boolean value, Boolean mustMatch) {
		return matchIfTrue(builder, binding.getPath(), value, mustMatch);
	}
	
	protected <P> Query matchIfTrue(String fieldPath, boolean value, Boolean mustMatch) {
		return matchIfTrue(defaultQueryBuilder, fieldPath, value, mustMatch);
	}
	
	protected Query matchIfTrue(QueryBuilder builder, String fieldPath, boolean value, Boolean mustMatch) {
		if (mustMatch == null || !mustMatch) {
			return null;
		}
		return builder.keyword()
				.onField(fieldPath)
				.matching(value)
				.createQuery();
	}
	
	// 	>	Match range (min, max, both)
	protected <P> Query matchRangeMin(AbstractBinding<?, P> binding, P min) {
		return matchRangeMin(defaultQueryBuilder, binding, min);
	}
	
	protected <P> Query matchRangeMin(QueryBuilder builder, AbstractBinding<?, P> binding, P min) {
		return matchRangeMin(builder, binding.getPath(), min);
	}
	
	protected <P> Query matchRangeMin(String fieldPath, P min) {
		return matchRangeMin(defaultQueryBuilder, fieldPath, min);
	}
	
	protected <P> Query matchRangeMin(QueryBuilder builder, String fieldPath, P min) {
		if (min == null) {
			return null;
		}
		return builder.range()
				.onField(fieldPath)
				.above(min)
				.createQuery();
	}
	
	protected <P> Query matchRangeMax(AbstractBinding<?, P> binding, P max) {
		return matchRangeMax(defaultQueryBuilder, binding, max);
	}
	
	protected <P> Query matchRangeMax(QueryBuilder builder, AbstractBinding<?, P> binding, P max) {
		return matchRangeMax(builder, binding.getPath(), max);
	}
	
	protected <P> Query matchRangeMax(String fieldPath, P max) {
		return matchRangeMax(defaultQueryBuilder, fieldPath, max);
	}
	
	protected <P> Query matchRangeMax(QueryBuilder builder, String fieldPath, P max) {
		if (max == null) {
			return null;
		}
		return builder.range()
				.onField(fieldPath)
				.below(max)
				.createQuery();
	}
	
	protected <P> Query matchRange(AbstractBinding<?, P> binding, P min, P max) {
		return matchRange(defaultQueryBuilder, binding.getPath(), min, max);
	}
	
	protected <P> Query matchRange(QueryBuilder builder, AbstractBinding<?, P> binding, P min, P max) {
		return matchRange(builder, binding.getPath(), min, max);
	}
	
	protected <P> Query matchRange(String fieldPath, P min, P max) {
		return matchRange(defaultQueryBuilder, fieldPath, min, max);
	}
	
	protected <P> Query matchRange(QueryBuilder builder, String fieldPath, P min, P max) {
		if (max != null && min != null) {
			return builder.range()
					.onField(fieldPath)
					.from(min).to(max)
					.createQuery();
		} else if (min != null) {
			return matchRangeMin(builder, fieldPath, min);
		} else if (max != null) {
			return matchRangeMax(builder, fieldPath, max);
		} else {
			return null;
		}
	}
	
	private static class BindingToPathFunction implements Function<AbstractBinding<?, String>, String> {
		@Override
		public String apply(AbstractBinding<?, String> input) {
			if (input == null) {
				throw new IllegalStateException("Path may not be null.");
			}
			return input.getPath();
		}
	}

}
