package fr.openwide.core.jpa.property;

import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class JpaPropertyIds extends AbstractPropertyIds {

	/*
	 * Mutable Properties
	 */
	
	// None
	
	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<Integer> LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT = immutable("lucene.booleanQuery.maxClauseCount");
	
	public static final ImmutablePropertyId<Integer> HIBERNATE_SEARCH_REINDEX_BATCH_SIZE = immutable("hibernate.search.reindex.batchSize");
	public static final ImmutablePropertyId<Integer> HIBERNATE_SEARCH_REINDEX_LOAD_THREADS = immutable("hibernate.search.reindex.loadThreads");
	
}
