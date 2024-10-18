package igloo.test.listener.hsearch;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;

public class HsearchUtil {
  private HsearchUtil() {}

  /** Clean all indexes. */
  public static void cleanIndexes(EntityManagerFactory entityManagerFactory) {
    SearchMapping searchMapping = Search.mapping(entityManagerFactory);
    searchMapping.scope(Object.class).workspace().purge();
  }

  public static void reindexAll(EntityManagerFactory entityManagerFactory) {
    SearchMapping searchMapping = Search.mapping(entityManagerFactory);
    try {
      searchMapping.scope(Object.class).massIndexer().startAndWait();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Mass indexer interrupted during reindexation.");
    }
  }
}
