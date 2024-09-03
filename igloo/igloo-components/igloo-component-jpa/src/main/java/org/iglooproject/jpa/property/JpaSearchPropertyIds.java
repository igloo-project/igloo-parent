package org.iglooproject.jpa.property;

import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;

public final class JpaSearchPropertyIds extends AbstractPropertyIds {

  /*
   * Mutable Properties
   */

  /*
   * Immutable Properties
   */
  public static final ImmutablePropertyId<Integer> LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT =
      immutable("lucene.booleanQuery.maxClauseCount");

  public static final ImmutablePropertyId<Integer> HIBERNATE_SEARCH_REINDEX_BATCH_SIZE =
      immutable("hibernate.search.reindex.batchSize");
  public static final ImmutablePropertyId<Integer> HIBERNATE_SEARCH_REINDEX_LOAD_THREADS =
      immutable("hibernate.search.reindex.loadThreads");
}