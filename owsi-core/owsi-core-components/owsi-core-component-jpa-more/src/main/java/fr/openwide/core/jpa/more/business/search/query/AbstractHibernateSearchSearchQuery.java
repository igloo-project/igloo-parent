package fr.openwide.core.jpa.more.business.search.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.Version;
import org.bindgen.binding.AbstractBinding;
import org.hibernate.search.Environment;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.ISort.SortOrder;
import fr.openwide.core.jpa.more.business.sort.SortUtils;
import fr.openwide.core.jpa.search.bridge.GenericEntityIdFieldBridge;
import fr.openwide.core.jpa.search.bridge.NullEncodingGenericEntityIdFieldBridge;
import fr.openwide.core.spring.util.StringUtils;

public abstract class AbstractHibernateSearchSearchQuery<T, S extends ISort<SortField>> implements ISearchQuery<T, S> /* NOT Serializable */ {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHibernateSearchSearchQuery.class);
	
	private static final Function<AbstractBinding<?, String>, String> BINDING_TO_PATH_FUNCTION = new BindingToPathFunction();
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private final Class<? extends T> mainClass;
	private final Class<? extends T>[] classes;
	
	private BooleanJunction<?> junction;
	private FullTextQuery query;
	private QueryBuilder defaultQueryBuilder;
	private FullTextEntityManager fullTextEntityManager;
	private Map<Class<?>, Analyzer> analyzerCache = new HashMap<Class<?>, Analyzer>();
	private List<S> defaultSorts;
	
	@SuppressWarnings("unchecked")
	@SafeVarargs
	protected AbstractHibernateSearchSearchQuery(Class<? extends T> clazz, S ... defaultSorts) {
		this(new Class[] { clazz }, defaultSorts);
	}
	
	@SafeVarargs
	protected AbstractHibernateSearchSearchQuery(Class<? extends T>[] classes, S ... defaultSorts) {
		this.mainClass = classes[0];
		this.classes = Arrays.copyOf(classes, classes.length);
		this.defaultSorts = ImmutableList.copyOf(defaultSorts);
	}
	
	@PostConstruct
	private void init() {
		fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		
		defaultQueryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(mainClass).get();
		
		junction = defaultQueryBuilder.bool().must(defaultQueryBuilder.all().createQuery());
	}
	
	protected Analyzer getAnalyzer(Class<?> clazz) {
		if (analyzerCache.containsKey(clazz)) {
			return analyzerCache.get(clazz);
		} else {
			Analyzer analyzer = fullTextEntityManager.getSearchFactory().getAnalyzer(clazz);
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

	@Override
	public ISearchQuery<T, S> sort(Map<S, SortOrder> sortMap) {
		getFullTextQuery().setSort(SortUtils.getLuceneSortWithDefaults(sortMap, defaultSorts));
		return this;
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
	
	// List and count
	@Override
	@Transactional(readOnly = true)
	public List<T> fullList() {
		return list(0L, Integer.MAX_VALUE);
	}
	
	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<T> list(long offset, long limit) {
		
		if (Long.valueOf(offset) != null) {
			getFullTextQuery().setFirstResult((int) offset);
		}
		
		if (Long.valueOf(limit) != null) {
			getFullTextQuery().setMaxResults((int) limit);
		}
		
		return getFullTextQuery().getResultList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public long count() {
		return getFullTextQuery().getResultSize();
	}
	
	// Query factory
	// 	> Match null
	/**
	 * <strong>Be careful</strong>: using this method needs null values to be indexed.
	 * You can use {@link NullEncodingGenericEntityIdFieldBridge} instead of the classical {@link GenericEntityIdFieldBridge} for example.
	 */
	protected Query matchNull(AbstractBinding<?, ?> binding) {
		return matchNull(defaultQueryBuilder, binding);
	}
	
	protected Query matchNull(QueryBuilder builder, AbstractBinding<?, ?> binding) {
		return matchNull(builder, binding.getPath());
	}
	
	protected Query matchNull(String fieldPath) {
		return matchNull(defaultQueryBuilder, fieldPath);
	}
	
	protected Query matchNull(QueryBuilder builder, String fieldPath) {
		return builder.keyword()
				.onField(fieldPath)
				.matching(null)
				.createQuery();
	}
	
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
		if (value != null) {
			return builder.keyword()
					.onField(fieldPath)
					.matching(value)
					.createQuery();
		}
		
		return null;
	}
	
	protected Query matchIfGiven(AbstractBinding<?, String> binding, String terms) {
		return matchIfGiven(defaultQueryBuilder, binding.getPath(), terms);
	}
	
	protected Query matchIfGiven(QueryBuilder builder, AbstractBinding<?, String> binding, String terms) {
		return matchIfGiven(builder, binding.getPath(), terms);
	}
	
	protected Query matchIfGiven(String fieldPath, String terms) {
		return matchOneTermIfGiven(defaultQueryBuilder, fieldPath, terms);
	}
	
	protected Query matchIfGiven(QueryBuilder builder, String fieldPath, String terms) {
		return matchOneTermIfGiven(builder, fieldPath, terms);
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
		if (StringUtils.hasText(terms)) {
			return builder.keyword()
					.onField(fieldPath)
					.matching(terms)
					.createQuery();
		}
		
		return null;
	}
	
	// 	>	Match all terms if given
	protected Query matchAllTermsIfGiven(Analyzer analyzer, AbstractBinding<?, String> binding, String terms) {
		return matchAllTermsIfGiven(analyzer, binding.getPath(), terms);
	}
	
	protected Query matchAllTermsIfGiven(AbstractBinding<?, String> binding, String terms) {
		return matchAllTermsIfGiven(getAnalyzer(), binding.getPath(), terms);
	}
	
	protected Query matchAllTermsIfGiven(String fieldPath, String terms) {
		return matchAllTermsIfGiven(getAnalyzer(), fieldPath, terms);
	}
	
	protected Query matchAllTermsIfGiven(Analyzer analyzer, String fieldPath, String terms) {
		if (StringUtils.hasText(terms)) {
			QueryParser parser = new QueryParser(Version.LUCENE_36, fieldPath, analyzer);
			parser.setDefaultOperator(Operator.AND);
			try {
				return parser.parse(QueryParser.escape(terms));
			} catch (ParseException e) {
				LOGGER.error("Erreur lors du parsing d'une chaîne échapée (a priori impossible ?)", e);
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected Query matchAllTermsMultifieldIfGiven(Analyzer analyzer, String terms, AbstractBinding<?, String> binding, AbstractBinding<?, String> ... otherBindings) {
		return matchAllTermsMultifieldIfGiven(analyzer, terms, Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
	}
	
	@SuppressWarnings("unchecked")
	protected Query matchAllTermsMultifieldIfGiven(String terms, AbstractBinding<?, String> binding, AbstractBinding<?, String> ... otherBindings) {
		return matchAllTermsMultifieldIfGiven(getAnalyzer(), terms, Lists.transform(Lists.asList(binding, otherBindings), BINDING_TO_PATH_FUNCTION));
	}
	
	protected Query matchAllTermsMultifieldIfGiven(String terms, Iterable<String> fieldPaths) {
		return matchAllTermsMultifieldIfGiven(getAnalyzer(), terms, fieldPaths);
	}
	
	protected Query matchAllTermsMultifieldIfGiven(Analyzer analyzer, String terms, Iterable<String> fieldPaths) {
		if (StringUtils.hasText(terms)) {
			MultiFieldQueryParser parser = new MultiFieldQueryParser(Environment.DEFAULT_LUCENE_MATCH_VERSION,
					Iterables.toArray(fieldPaths, String.class),
					analyzer
			);
			parser.setDefaultOperator(Operator.AND);
			try {
				return parser.parse(QueryParser.escape(terms));
			} catch (ParseException e) {
				LOGGER.error("Erreur lors du parsing d'une chaîne échapée (a priori impossible ?)", e);
			}
		}
		
		return null;
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
		if (value != null) {
			return builder.keyword()
					.onField(fieldPath)
					.matching(value)
					.createQuery();
		}
		
		return null;
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
		if (possibleValues != null && !possibleValues.isEmpty()) {
			BooleanJunction<?> subJunction = builder.bool();
			for (P possibleValue : possibleValues) {
				subJunction.should(builder.keyword()
						.onField(fieldPath)
						.matching(possibleValue)
						.createQuery());
			}
			return subJunction.createQuery();
		}
		
		return null;
	}
	
	// 	>	Match all if given
	protected <P> Query matchAllIfGiven(AbstractBinding<?, ? extends Collection<P>> binding, Collection<? extends P> values) {
		return matchAllIfGiven(defaultQueryBuilder, binding, values);
	}
	
	protected <P> Query matchAllIfGiven(QueryBuilder builder, AbstractBinding<?, ? extends Collection<P>> binding, Collection<? extends P> values) {
		if (values != null && !values.isEmpty()) {
			BooleanJunction<?> subJunction = builder.bool();
			for (P possibleValue : values) {
				subJunction.must(builder.keyword()
						.onField(binding.getPath())
						.matching(possibleValue)
						.createQuery());
			}
			return subJunction.createQuery();
		}
		
		return null;
	}
	
	// 	>	Match if true
	
	protected Query matchIfTrue(AbstractBinding<?, Boolean> binding, boolean value, Boolean mustMatch) {
		return matchIfTrue(defaultQueryBuilder, binding, value, mustMatch);
	}
	
	protected Query matchIfTrue(QueryBuilder builder, AbstractBinding<?, Boolean> binding, boolean value, Boolean mustMatch) {
		if (mustMatch != null && mustMatch) {
			return builder.keyword()
					.onField(binding.getPath())
					.matching(value)
					.createQuery();
		}
		return null;
	}
	
	// 	>	Match range (min, max, both)
	protected <P> Query matchRangeMin(AbstractBinding<?, P> binding, P min) {
		return matchRangeMin(getDefaultQueryBuilder(), binding, min);
	}
	
	protected <P> Query matchRangeMin(QueryBuilder builder, AbstractBinding<?, P> binding, P min) {
		return matchRangeMin(builder, binding.getPath(), min);
	}
	
	protected <P> Query matchRangeMin(QueryBuilder builder, String fieldPath, P min) {
		if (min != null) {
			return builder.range()
					.onField(fieldPath)
					.above(min)
					.createQuery();
		}
		return null;
	}
	
	protected <P> Query matchRangeMax(AbstractBinding<?, P> binding, P max) {
		return matchRangeMax(getDefaultQueryBuilder(), binding, max);
	}
	
	protected <P> Query matchRangeMax(QueryBuilder builder, AbstractBinding<?, P> binding, P max) {
		return matchRangeMax(builder, binding.getPath(), max);
	}
	
	protected <P> Query matchRangeMax(QueryBuilder builder, String fieldPath, P max) {
		if (max != null) {
			return builder.range()
					.onField(fieldPath)
					.below(max)
					.createQuery();
		}
		return null;
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
			matchRangeMin(builder, fieldPath, min);
		} else if (max != null) {
			matchRangeMax(builder, fieldPath, max);
		}
		return null;
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
	
	/**
	 * Allow to add filter before generating the full text query.
	 */
	protected void addFilterBeforeCreateQuery() {
		// Nothing
	}
	
	protected FullTextQuery getFullTextQuery() {
		if (query == null) {
			addFilterBeforeCreateQuery();
			query = fullTextEntityManager.createFullTextQuery(junction.createQuery(), classes);
		}
		return query;
	}
}