package test;

import igloo.test.listener.hsearch.HsearchUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.assertj.core.api.Assertions;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.testing.transaction.TransactionUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import test.model.DummyEntity;

/**
 * Check that jpa + hibernate-search setup invokes index cleaning listener.
 *
 * @see HsearchUtil
 */
@TestPropertySource(
    properties = {
      "spring.jpa.properties.hibernate.search.enabled=true",
      "spring.jpa.properties.hibernate.search.backend.directory.type=local-heap"
    })
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({
  DependencyInjectionTestExecutionListener.class,
})
@ContextConfiguration(
    classes = {
      DataSourceAutoConfiguration.class,
      HibernateJpaAutoConfiguration.class,
    })
@EntityScan(basePackageClasses = TestIglooTestExecutionListener.class)
class TestHsearchUtil {
  @Autowired EntityManagerFactory entityManagerFactory;

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
}
