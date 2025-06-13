package test.patterns.indexingdependency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.time.Instant;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.cfg.HibernateOrmMapperSettings;
import org.igloo.jpa.test.EntityManagerFactoryExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class TestIndexingDependencyRemoval {

  @RegisterExtension
  EntityManagerFactoryExtension extension =
      new EntityManagerFactoryExtension(
          AvailableSettings.DIALECT,
          org.hibernate.dialect.H2Dialect.class.getName(),
          AvailableSettings.HBM2DDL_AUTO,
          "create",
          AvailableSettings.JAKARTA_JDBC_DRIVER,
          org.h2.Driver.class.getName(),
          AvailableSettings.JAKARTA_JDBC_URL,
          "jdbc:h2:mem:jpa_patterns;INIT=create schema if not exists jpa_patterns",
          AvailableSettings.LOADED_CLASSES,
          Arrays.asList(FirstLevel.class, SecondLevel.class, ThirdLevel.class),
          AvailableSettings.XML_MAPPING_ENABLED,
          Boolean.FALSE.toString(),
          HibernateOrmMapperSettings.ENABLED,
          Boolean.TRUE.toString(),
          "hibernate.search.backend.directory.type",
          "local-heap");

  /** Vérification de la disponibilité Hibernate Search */
  @Test
  void test_hibernateSearch(EntityManager entityManager) {
    assertDoesNotThrow(() -> Search.session(entityManager).indexingPlan());
  }

  @Test
  void test_search(EntityManager entityManager) throws InterruptedException {
    doInTransaction(
        entityManager,
        e -> {
          ThirdLevel thirdLevel = new ThirdLevel(1l);
          SecondLevel secondLevel = new SecondLevel(1l);
          secondLevel.thirdLevel = thirdLevel;
          FirstLevel firstLevel = new FirstLevel(1l);
          firstLevel.attribute = "first";
          firstLevel.secondLevel = secondLevel;
          e.persist(thirdLevel);
          e.persist(secondLevel);
          e.persist(firstLevel);
        });
    // nécessaire pour que les contraposées soient correctes
    entityManager.clear();
    assertCount(entityManager, FirstLevel.class, 1);

    // update du thirdLevel, vérification de la prise en compte sur le firstLevel
    doInTransaction(
        entityManager,
        e -> {
          ThirdLevel thirdLevel = e.find(ThirdLevel.class, 1l);
          thirdLevel.attribute = "test";
        });
    // nécessaire pour que les contraposées soient correctes
    entityManager.clear();
    assertCount(
        entityManager,
        FirstLevel.class,
        pf -> pf.match().field("secondLevel.thirdLevel.attribute").matching("test"),
        1);
    assertCount(
        entityManager,
        FirstLevel.class,
        pf -> pf.match().field("thirdLevel.attribute").matching("test"),
        1);
  }

  @Test
  void test_ignoreTransient(EntityManager entityManager) throws InterruptedException {
    doInTransaction(
        entityManager,
        e -> {
          ThirdLevel thirdLevel = new ThirdLevel(1l);
          SecondLevel secondLevel = new SecondLevel(1l);
          secondLevel.thirdLevel = thirdLevel;
          FirstLevel firstLevel = new FirstLevel(1l);
          firstLevel.attribute = "first";
          firstLevel.secondLevel = secondLevel;
          e.persist(thirdLevel);
          e.persist(secondLevel);
          e.persist(firstLevel);
        });
    // nécessaire pour que les contraposées soient correctes
    entityManager.clear();
    assertCount(entityManager, FirstLevel.class, 1);

    // update du thirdLevel sur un attribut non indexé
    Instant before = Instant.now();
    doInTransaction(
        entityManager,
        e -> {
          ThirdLevel thirdLevel = e.find(ThirdLevel.class, 1l);
          thirdLevel.other = "test";
        });
    Thread.sleep(1);
    Instant after = Instant.now();
    // nécessaire pour que les contraposées soient correctes
    entityManager.clear();
    assertCount(
        entityManager, FirstLevel.class, pf -> pf.range().field("indexedOn").lessThan(before), 1);
    assertCount(
        entityManager, ThirdLevel.class, pf -> pf.range().field("indexedOn").lessThan(before), 1);
  }

  private void doInTransaction(EntityManager entityManager, Consumer<EntityManager> actions) {
    EntityTransaction t = entityManager.getTransaction();
    t.begin();
    try {
      actions.accept(entityManager);
    } finally {
      t.commit();
    }
  }

  protected void assertCount(EntityManager entityManager, Class<?> clazz, int count) {
    assertCount(entityManager, clazz, pf -> pf.matchAll(), 1);
  }

  protected void assertCount(
      EntityManager entityManager,
      Class<?> clazz,
      Function<? super SearchPredicateFactory, ? extends PredicateFinalStep> where,
      int count) {
    assertThat(Search.session(entityManager).search(clazz).where(where).fetchTotalHitCount())
        .isEqualTo(count);
  }
}
