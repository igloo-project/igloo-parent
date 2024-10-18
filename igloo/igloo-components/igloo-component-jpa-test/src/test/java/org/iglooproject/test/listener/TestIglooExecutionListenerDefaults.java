package org.iglooproject.test.listener;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.assertj.core.api.Assertions;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.testing.transaction.TransactionUtil;
import org.iglooproject.test.jpa.spring.IIglooTestListener;
import org.iglooproject.test.jpa.spring.cache.Level2CacheIglooTestListener;
import org.iglooproject.test.jpa.spring.hsearch.HsearchIglooTestListener;
import org.iglooproject.test.jpa.spring.hsearch.HsearchUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

/**
 * Test provided Igloo provided {@link IIglooTestListener} (database, hibernate-search, second-level
 * cache, ...).
 */
class TestIglooExecutionListenerDefaults {
  /**
   * Check that jpa + hibernate-search setup invokes index cleaning listener.
   *
   * @see HsearchIglooTestListener
   */
  @TestPropertySource(properties = {"spring.jpa.properties.hibernate.search.enabled=true"})
  @BaseTest
  @BaseJpaConfiguration
  static class HibernateSearch {
    @Autowired EventRecorder eventRecorder;
    @Autowired EntityManagerFactory entityManagerFactory;

    @Test
    void testCleanListener() {
      Assertions.assertThat(eventRecorder.getEvents())
          .anyMatch(e -> "hibernate-search:clean".equals(e.getSource()));
    }

    @Test
    void testClean() {
      TransactionUtil.doInJPA(
          () -> entityManagerFactory,
          em -> {
            em.persist(new DummyEntity());
          });

      TransactionUtil.doInJPA(
          () -> entityManagerFactory,
          em -> {
            Assertions.assertThat(countDummy(em)).isOne();
          });

      HsearchUtil.cleanIndexes(entityManagerFactory);

      TransactionUtil.doInJPA(
          () -> entityManagerFactory,
          em -> {
            Assertions.assertThat(countDummy(em)).isZero();
            Assertions.assertThat(
                    em.createQuery("SELECT count(*) FROM DummyEntity", Long.class)
                        .getSingleResult())
                .isEqualTo(1);
          });
    }

    private long countDummy(EntityManager em) {
      return Search.session(em)
          .search(DummyEntity.class)
          .where(f -> f.matchAll())
          .fetchTotalHitCount();
    }
  }

  /** Check that jpa without hibernate-search does not invoke index cleaning. */
  @TestPropertySource(properties = {"spring.jpa.properties.hibernate.search.enabled=false"})
  @BaseTest
  @BaseJpaConfiguration
  static class JpaHibernateSearchLess {
    @Autowired EventRecorder eventRecorder;

    @Test
    void testNoClean() {
      Assertions.assertThat(eventRecorder.getEvents())
          .noneMatch(e -> "hibernate-search:clean".equals(e.getSource()));
    }
  }

  /**
   * Check that jpa + second level cache invokes cache cleaning.
   *
   * @see Level2CacheIglooTestListener
   */
  @TestPropertySource(
      properties = {
        "spring.jpa.properties.hibernate.cache.use_second_level_cache=true",
        "spring.jpa.properties.hibernate.cache.region.factory_class=jcache"
      })
  @BaseTest
  @BaseJpaConfiguration
  static class JpaLevel2Cache {
    @Autowired EventRecorder eventRecorder;

    @Test
    void testNoClean() {
      Assertions.assertThat(eventRecorder.getEvents())
          .anyMatch(e -> "hibernate-cache:clean".equals(e.getSource()));
    }
  }

  /** Check that jpa without second level cache does not invoke cache cleaning. */
  @TestPropertySource(
      properties = {"spring.jpa.properties.hibernate.cache.use_second_level_cache=false"})
  @BaseTest
  @BaseJpaConfiguration
  static class JpaLevel2CacheLess {
    @Autowired EventRecorder eventRecorder;

    @Test
    void testNoClean() {
      Assertions.assertThat(eventRecorder.getEvents())
          .noneMatch(e -> "hibernate-cache:clean".equals(e.getSource()));
    }
  }
}
