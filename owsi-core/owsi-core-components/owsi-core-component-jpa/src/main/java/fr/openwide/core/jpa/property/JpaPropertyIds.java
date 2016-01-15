package fr.openwide.core.jpa.property;

import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class JpaPropertyIds {

	/*
	 * Mutable Properties
	 */
	
	
	
	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<Integer> HIBERNATE_SEARCH_REINDEX_BATCH_SIZE = new ImmutablePropertyId<>("hibernate.search.reindex.batchSize");
	public static final ImmutablePropertyId<Integer> HIBERNATE_SEARCH_REINDEX_LOAD_THREADS = new ImmutablePropertyId<>("hibernate.search.reindex.loadThreads");
	
}
