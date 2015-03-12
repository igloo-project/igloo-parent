package fr.openwide.core.jpa.more.business.search.query;

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
import fr.openwide.core.spring.util.StringUtils;

public abstract class AbstractHibernateSearchSearchQuery<T, S extends ISort<SortField>> implements ISearchQuery<T, S> /* NOT Serializable */ {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHibernateSearchSearchQuery.class);
	
	private static final Function<AbstractBinding<?, String>, String> BINDING_TO_PATH_FUNCTION = new BindingToPathFunction();
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private final Class<T> clazz;
	
	private BooleanJunction<?> junction;
	private FullTextQuery query;
	private QueryBuilder defaultQueryBuilder;
	private FullTextEntityManager fullTextEntityManager;
	private Map<Class<?>, Analyzer> analyzerCache = new HashMap<Class<?>, Analyzer>();
	private List<S> defaultSorts;
	
	
	@SafeVarargs
	protected AbstractHibernateSearchSearchQuery(Class<T> clazz, S ... defaultSorts) {
		this.clazz = clazz;
		this.defaultSorts = ImmutableList.copyOf(defaultSorts);
	}
	
	@PostConstruct
	private void init() {
		fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		
		defaultQueryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(clazz).get();
		
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
		return getAnalyzer(clazz);
	}
	
	protected QueryBuilder getDefaultQueryBuilder() {
		return defaultQueryBuilder;
	}

	@Override
	public ISearchQuery<T, S> sort(Map<S, SortOrder> sortMap) {
		getFullTextQuery().setSort(SortUtils.getLuceneSortWithDefaults(sortMap, defaultSorts));
		return this;
	}

	protected void must(Query query) {
		if (query != null) {
			junction.must(query);
		}
	}
	
	protected void mustIfNotNull(BooleanJunction<?> junction, Query ... queries) {
		for (Query query : queries) {
			if (query != null) {
				junction.must(query);
			}
		}
	}
	
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
	
	protected <P> Query matchIfGiven(QueryBuilder builder, AbstractBinding<?, P> binding, P value) {
		if (value != null) {
			return builder.keyword()
					.onField(binding.getPath())
					.matching(value)
					.createQuery();
		}
		return null;
	}

	protected <P> Query matchIfGiven(AbstractBinding<?, P> binding, P value) {
		return matchIfGiven(defaultQueryBuilder, binding, value);
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
	
	protected <P> Query matchIfGiven(String fieldPath, P value) {
		if (value != null) {
			return defaultQueryBuilder.keyword()
					.onField(fieldPath)
					.matching(value)
					.createQuery();
		}
		
		return null;
	}
	
	protected Query matchIfGiven(QueryBuilder builder, AbstractBinding<?, String> binding, String terms) {
		return matchIfGiven(builder, binding.getPath(), terms);
	}
	
	protected Query matchIfGiven(AbstractBinding<?, String> binding, String terms) {
		return matchIfGiven(defaultQueryBuilder, binding.getPath(), terms);
	}
	
	protected Query matchIfGiven(QueryBuilder builder, String fieldPath, String terms) {
		return matchOneTermIfGiven(builder, fieldPath, terms);
	}
	
	protected Query matchIfGiven(String fieldPath, String terms) {
		return matchOneTermIfGiven(defaultQueryBuilder, fieldPath, terms);
	}
	
	protected Query matchOneTermIfGiven(QueryBuilder builder, AbstractBinding<?, String> binding, String terms) {
		return matchOneTermIfGiven(builder, binding.getPath(), terms);
	}
	
	protected Query matchOneTermIfGiven(AbstractBinding<?, String> binding, String terms) {
		return matchOneTermIfGiven(defaultQueryBuilder, binding.getPath(), terms);
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
	
	protected Query matchOneTermIfGiven(String fieldPath, String terms) {
		if (StringUtils.hasText(terms)) {
			return defaultQueryBuilder.keyword()
					.onField(fieldPath)
					.matching(terms)
					.createQuery();
		}
		
		return null;
	}
	
	protected Query matchAllTermsIfGiven(Analyzer analyzer, AbstractBinding<?, String> binding, String terms) {
		return matchAllTermsIfGiven(analyzer, binding.getPath(), terms);
	}
	
	protected Query matchAllTermsIfGiven(AbstractBinding<?, String> binding, String terms) {
		return matchAllTermsIfGiven(getAnalyzer(), binding.getPath(), terms);
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
	
	protected Query matchAllTermsIfGiven(String fieldPath, String terms) {
		if (StringUtils.hasText(terms)) {
			QueryParser parser = new QueryParser(Version.LUCENE_36, fieldPath, getAnalyzer());
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
	
	protected Query matchAllTermsMultifieldIfGiven(String terms, Iterable<String> fieldPaths) {
		if (StringUtils.hasText(terms)) {
			MultiFieldQueryParser parser = new MultiFieldQueryParser(Environment.DEFAULT_LUCENE_MATCH_VERSION,
					Iterables.toArray(fieldPaths, String.class),
					getAnalyzer()
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
	
	protected <P> Query beIncludedIfGiven(QueryBuilder builder, AbstractBinding<?, ? extends Collection<P>> binding, P value) {
		if (value != null) {
			return builder.keyword()
					.onField(binding.getPath())
					.matching(value)
					.createQuery();
		}
		
		return null;
	}
	
	protected <P> Query beIncludedIfGiven(AbstractBinding<?, ? extends Collection<P>> binding, P value) {
		if (value != null) {
			return defaultQueryBuilder.keyword()
					.onField(binding.getPath())
					.matching(value)
					.createQuery();
		}
		
		return null;
	}
	
	protected <P> Query matchOneIfGiven(QueryBuilder builder, AbstractBinding<?, P> binding, Collection<? extends P> possibleValues) {
		if (possibleValues != null && !possibleValues.isEmpty()) {
			BooleanJunction<?> subJunction = builder.bool();
			for (P possibleValue : possibleValues) {
				subJunction.should(builder.keyword()
						.onField(binding.getPath())
						.matching(possibleValue)
						.createQuery());
			}
			return subJunction.createQuery();
		}
		
		return null;
	}
	
	protected <P> Query matchOneIfGiven(AbstractBinding<?, P> binding, Collection<? extends P> possibleValues) {
		if (possibleValues != null && !possibleValues.isEmpty()) {
			BooleanJunction<?> subJunction = defaultQueryBuilder.bool();
			for (P possibleValue : possibleValues) {
				subJunction.should(defaultQueryBuilder.keyword()
						.onField(binding.getPath())
						.matching(possibleValue)
						.createQuery());
			}
			return subJunction.createQuery();
		}
		
		return null;
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
	
	protected <P> Query matchAllIfGiven(AbstractBinding<?, ? extends Collection<P>> binding, Collection<? extends P> values) {
		if (values != null && !values.isEmpty()) {
			BooleanJunction<?> subJunction = defaultQueryBuilder.bool();
			for (P possibleValue : values) {
				subJunction.must(defaultQueryBuilder.keyword()
						.onField(binding.getPath())
						.matching(possibleValue)
						.createQuery());
			}
			return subJunction.createQuery();
		}
		
		return null;
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
	
	protected Query matchIfTrue(AbstractBinding<?, Boolean> binding, boolean value, Boolean mustMatch) {
		if (mustMatch != null && mustMatch) {
			return defaultQueryBuilder.keyword()
					.onField(binding.getPath())
					.matching(value)
					.createQuery();
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

	protected FullTextQuery getFullTextQuery() {
		if (query == null) {
			query = fullTextEntityManager.createFullTextQuery(junction.createQuery(), clazz);
		}
		return query;
	}
}