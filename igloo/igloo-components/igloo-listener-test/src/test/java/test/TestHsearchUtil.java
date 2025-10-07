package test;

import igloo.test.listener.hsearch.HsearchUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.assertj.core.api.Assertions;
import org.hibernate.search.mapper.orm.Search;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.ApplicationEventsTestExecutionListener;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import test.model.DummyEntity;
import test.spring.BaseJpaConfiguration;

/**
 * Check that jpa + hibernate-search setup invokes index cleaning listener.
 *
 * @see HsearchUtil
 */
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({
  // enable dependency injection and events
  ApplicationEventsTestExecutionListener.class,
  DependencyInjectionTestExecutionListener.class,
})
@TestPropertySource(
    properties = {
      "testContainer.database.dockerImageName=postgres:alpine",
      "testContainer.database.exposedPorts=5432",
      "testContainer.database.name=test_database",
      "testContainer.database.userName=test_database",
      "testContainer.database.password=test_database",
    })
@TestPropertySource(
    properties = {
      "spring.jpa.hibernate.ddl-auto=create",
      "spring.jpa.properties.hibernate.search.enabled=true",
      "spring.jpa.properties.hibernate.search.backend.directory.type=local-heap"
    })
@BaseJpaConfiguration
class TestHsearchUtil {
  @Autowired EntityManagerFactory entityManagerFactory;

  private static final Logger log = Logger.getLogger(TestHsearchUtil.class);

  @Test
  void testClean() {
    doInJPA(
        () -> entityManagerFactory,
        em -> {
          em.persist(new DummyEntity());
        });

    doInJPA(
        () -> entityManagerFactory,
        em -> {
          Assertions.assertThat(countDummy(em)).isOne();
        });

    HsearchUtil.cleanIndexes(entityManagerFactory);

    doInJPA(
        () -> entityManagerFactory,
        em -> {
          Assertions.assertThat(countDummy(em)).isZero();
          Assertions.assertThat(
                  em.createQuery("SELECT count(*) FROM DummyEntity", Long.class).getSingleResult())
              .isEqualTo(1);
        });
  }

  private long countDummy(EntityManager em) {
    return Search.session(em)
        .search(DummyEntity.class)
        .where(f -> f.matchAll())
        .fetchTotalHitCount();
  }

  /**
   * Copy and simplification from Hibernate Tests. Hibernate-test dependency make conflicts with
   * junit igloo version.
   */
  private void doInJPA(
      Supplier<EntityManagerFactory> factorySupplier, Consumer<EntityManager> function) {
    EntityTransaction txn = null;
    try (EntityManager entityManager = factorySupplier.get().createEntityManager()) {
      txn = entityManager.getTransaction();
      txn.begin();
      function.accept(entityManager);
      if (!txn.getRollbackOnly()) {
        txn.commit();
      } else {
        rollback(txn);
      }
    } catch (Throwable t) {
      if (txn != null && txn.isActive()) {
        rollback(txn);
      }
      throw t;
    }
  }

  private void rollback(EntityTransaction txn) {
    try {
      txn.rollback();
    } catch (Exception e) {
      log.error("Rollback failure", e);
    }
  }
}
