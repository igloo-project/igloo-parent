package fr.openwide.core.jpa.more.business.search.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.bindgen.binding.AbstractBinding;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.ISort.SortOrder;
import fr.openwide.core.jpa.more.business.sort.SortUtils;

public abstract class AbstractHibernateSearchSearchQuery<T, S extends ISort<SortField>> implements ISearchQuery<T, S> /* NOT Serializable */ {

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

	protected FullTextQuery getFullTextQuery() {
		if (query == null) {
			query = fullTextEntityManager.createFullTextQuery(junction.createQuery(), clazz);
		}
		return query;
	}
}