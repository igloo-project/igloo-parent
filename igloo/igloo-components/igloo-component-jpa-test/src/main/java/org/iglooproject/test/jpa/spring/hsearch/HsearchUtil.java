package org.iglooproject.test.jpa.spring.hsearch;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;

public class HsearchUtil {
  private HsearchUtil() {}

  public static void cleanIndexes(EntityManagerFactory entityManagerFactory) {
    SearchMapping searchMapping = Search.mapping(entityManagerFactory);
    searchMapping.scope(Object.class).workspace().purge();
  }
}
