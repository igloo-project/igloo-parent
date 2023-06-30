package test.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.hibernate.search.engine.search.common.ValueConvert;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.util.common.SearchException;
import org.iglooproject.jpa.more.autoconfigure.HibernateSearchJpaMoreAutoConfiguration;
import org.iglooproject.jpa.more.autoconfigure.JpaMoreAutoConfiguration;
import org.iglooproject.jpa.more.autoconfigure.JpaMoreModelAutoConfiguration;
import org.iglooproject.jpa.more.autoconfigure.JpaMorePropertyRegistryAutoConfiguration;
import org.iglooproject.jpa.more.autoconfigure.TaskAutoConfiguration;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;
import org.iglooproject.jpa.search.bridge.GenericEntityReferenceIdBridge;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.iglooproject.test.jpa.junit.EntityManagerExecutionListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import test.search.EnumCollectionEntity.MyEnum;

/**
 * Tests demonstrating bridge behavior for Hibernate search. It show how to use {@link GenericEntityIdBridge},
 * {@link GenericEntityReferenceIdBridge}.
 * 
 * It shows how cases formerly handled by {@code EnumCollectionFieldBridge}, {@code StringCollectionFieldBridge},
 * {@code GenericEntityLongIdFieldBridge}, {@code GenericEntityCollectionIdFieldBridge} are now handled by Hibernate
 * Search natively.
 * 
 * It shows how null/empty value are know handled explicitly by checking is a field exists or not.
 * {@¢ode NullEncoding*FieldBridge} are no longer provided.
 * 
 * Igloo only provides Long-aware {@link GenericEntityIdBridge}. Other cases must be handled by projects.
 */
@SpringBootTest(classes =  TestConfiguration.class)
@EnableAutoConfiguration(exclude = { JpaMoreAutoConfiguration.class, JpaMoreModelAutoConfiguration.class,
		HibernateSearchJpaMoreAutoConfiguration.class,
		JpaMorePropertyRegistryAutoConfiguration.class, TaskAutoConfiguration.class })
@TestExecutionListeners(listeners = {
	DependencyInjectionTestExecutionListener.class,
	EntityManagerExecutionListener.class,
	TransactionalTestExecutionListener.class
})
@TestPropertySource(properties = {
	"spring.jpa.properties.hibernate.search.backend.directory.type=local-heap",
	"spring.jpa.hibernate.ddl-auto=create-drop",
	// for BridgeEntity#reference embeddable
	"spring.jpa.igloo.component-path.enabled=true"
})
class TestBridge {

	@Autowired
	private EntityManagerUtils entityManagerUtils;

	@Configuration
	@EntityScan(basePackageClasses = BridgeEntity.class)
	public static class TestConfiguration {
	}

	@Test
	@Transactional(readOnly = false)
	void test_bridge() {
		// b1.l = l1
		// b2.l = null
		// l2 orphan
		BridgeLinkedEntity l1 = new BridgeLinkedEntity();
		BridgeLinkedEntity l2 = new BridgeLinkedEntity();
		BridgeEntity b1 = new BridgeEntity();
		BridgeEntity b2 = new BridgeEntity();
		b1.linked = l1;
		EntityManager em = entityManagerUtils.getEntityManager();
		em.persist(l1);
		em.persist(l2);
		em.persist(b1);
		em.persist(b2);
		TestTransaction.flagForCommit();
		TestTransaction.end();
		
		// linked to l1 -> b1
		assertThat(Search.session(em)
			.search(BridgeEntity.class)
			.where(p -> p.match().field("linked").matching(l1))
			.fetchAllHits()).contains(b1);
		// linked to l2 -> none
		assertThat(Search.session(em)
			.search(BridgeEntity.class)
			.where(p -> p.match().field("linked").matching(l2))
			.fetchAllHits()).isEmpty();
		// linked to any -> b1
		assertThat(Search.session(em)
			.search(BridgeEntity.class)
			.where(p -> p.exists().field("linked"))
			.fetchAllHits()).contains(b1);
		// not linked -> b2
		assertThat(Search.session(em)
				.search(BridgeEntity.class)
				.where(p -> p.bool().mustNot(p.exists().field("linked")))
				.fetchAllHits()).contains(b2);
	}

	@Test
	void test_fieldLinked_notSortable() {
		EntityManager em = entityManagerUtils.getEntityManager();
		{
			var query = Search.session(em)
					.search(BridgeEntity.class)
					.where(p -> p.matchAll());
			assertThatCode(() -> query.sort(f -> f.field("linked")))
				.isInstanceOf(SearchException.class).hasMessageContaining("sortable");
		}
	}

	@Test
	void test_fieldLinked2_sortable() {
		EntityManager em = entityManagerUtils.getEntityManager();
		{
			var query = Search.session(em)
					.search(BridgeEntity.class)
					.where(p -> p.matchAll());
			assertThatCode(() -> query.sort(f -> f.field("linked2")))
				.doesNotThrowAnyException();
		}
	}

	@Test
	@Transactional(readOnly = false)
	void test_fieldLinked_projection() {
		BridgeLinkedEntity l1 = new BridgeLinkedEntity();
		BridgeEntity b1 = new BridgeEntity();
		b1.linked = l1;
		EntityManager em = entityManagerUtils.getEntityManager();
		em.persist(l1);
		em.persist(b1);
		TestTransaction.flagForCommit();
		TestTransaction.end();
		
		assertThat(Search.session(em)
				.search(BridgeEntity.class)
				.select(f -> f.field("linked", ValueConvert.NO))
				.where(p -> p.match().field("linked").matching(l1))
				.fetchAllHits()).containsExactly(l1.getId());
	}

	@Test
	@Transactional(readOnly = false)
	void test_enumCollection() {
		EnumCollectionEntity b1 = new EnumCollectionEntity();
		b1.enums.add(MyEnum.VALUE1);
		b1.enums.add(MyEnum.VALUE2);
		b1.enumsInt.add(MyEnum.VALUE1);
		b1.enumsInt.add(MyEnum.VALUE2);
		EntityManager em = entityManagerUtils.getEntityManager();
		em.persist(b1);
		TestTransaction.flagForCommit();
		TestTransaction.end();
		assertEnums(b1, em, "enums");
		assertEnums(b1, em, "enumsInt");
	}

	private void assertEnums(EnumCollectionEntity b1, EntityManager em, String fieldName) {
		assertThat(Search.session(em)
				.search(EnumCollectionEntity.class)
				.where(p -> p.match().field(fieldName).matching(MyEnum.VALUE1))
				.fetchAllHits()).containsExactly(b1);
		assertThat(Search.session(em)
				.search(EnumCollectionEntity.class)
				.where(p -> p.match().field(fieldName).matching(MyEnum.VALUE2))
				.fetchAllHits()).containsExactly(b1);
		assertThat(Search.session(em)
				.search(EnumCollectionEntity.class)
				.where(p -> p.match().field(fieldName).matching(MyEnum.VALUE3))
				.fetchAllHits()).isEmpty();
		assertThat(Search.session(em)
				.search(EnumCollectionEntity.class)
				.select(f -> f.field(fieldName).multi())
				.where(p -> p.match().field(fieldName).matching(MyEnum.VALUE1))
				.fetchAllHits()).hasSize(1).first().asList().containsExactlyInAnyOrder(MyEnum.VALUE1, MyEnum.VALUE2);
	}

	@Test
	@Transactional(readOnly = false)
	void test_stringCollection() {
		StringCollectionEntity b1 = new StringCollectionEntity();
		b1.strings.add("V1");
		b1.strings.add("V2");
		EntityManager em = entityManagerUtils.getEntityManager();
		em.persist(b1);
		TestTransaction.flagForCommit();
		TestTransaction.end();
		
		assertThat(Search.session(em)
				.search(StringCollectionEntity.class)
				.where(p -> p.match().field("strings").matching("V1"))
				.fetchAllHits()).containsExactly(b1);
		assertThat(Search.session(em)
				.search(StringCollectionEntity.class)
				.where(p -> p.match().field("strings").matching("V2"))
				.fetchAllHits()).containsExactly(b1);
		assertThat(Search.session(em)
				.search(StringCollectionEntity.class)
				.where(p -> p.match().field("strings").matching("V3"))
				.fetchAllHits()).isEmpty();
		assertThat(Search.session(em)
				.search(StringCollectionEntity.class)
				.select(f -> f.field("strings").multi())
				.where(p -> p.match().field("strings").matching("V1"))
				.fetchAllHits()).hasSize(1).first().asList().containsExactlyInAnyOrder("V1", "V2");
	}

	@Test
	@Transactional(readOnly = false)
	void test_entityCollection() {
		BridgeLinkedEntity l1 = new BridgeLinkedEntity();
		BridgeLinkedEntity l2 = new BridgeLinkedEntity();
		BridgeLinkedEntity l3 = new BridgeLinkedEntity();
		BridgeEntityCollection b1 = new BridgeEntityCollection();
		b1.linked.add(l1);
		b1.linked.add(l2);
		EntityManager em = entityManagerUtils.getEntityManager();
		em.persist(l1);
		em.persist(l2);
		em.persist(l3);
		em.persist(b1);
		TestTransaction.flagForCommit();
		TestTransaction.end();
		
		assertThat(Search.session(em)
				.search(BridgeEntityCollection.class)
				.where(p -> p.match().field("linked").matching(l1))
				.fetchAllHits()).containsExactly(b1);
		assertThat(Search.session(em)
				.search(BridgeEntityCollection.class)
				.where(p -> p.match().field("linked").matching(l2))
				.fetchAllHits()).containsExactly(b1);
		assertThat(Search.session(em)
				.search(BridgeEntityCollection.class)
				.where(p -> p.match().field("linked").matching(l3))
				.fetchAllHits()).isEmpty();
		assertThat(Search.session(em)
				.search(BridgeEntityCollection.class)
				.select(f -> f.field("linked", ValueConvert.NO).multi())
				.where(p -> p.match().field("linked").matching(l1))
				.fetchAllHits()).hasSize(1).first().asList().containsExactlyInAnyOrder(l1.getId(), l2.getId());
	}

	@Test
	@Transactional(readOnly = false)
	void test_entityReference() {
		BridgeLinkedEntity l1 = new BridgeLinkedEntity();
		BridgeLinkedEntity l2 = new BridgeLinkedEntity();
		BridgeEntity b1 = new BridgeEntity();
		EntityManager em = entityManagerUtils.getEntityManager();
		em.persist(l1);
		em.persist(l2);
		em.flush();
		BridgeLinkedEntity.Reference r1 = new BridgeLinkedEntity.Reference(l1);
		BridgeLinkedEntity.Reference r2 = new BridgeLinkedEntity.Reference(l2);
		b1.reference = r1;
		em.persist(b1);
		TestTransaction.flagForCommit();
		TestTransaction.end();
		
		assertThat(Search.session(em)
				.search(BridgeEntity.class)
				.where(p -> p.match().field("reference").matching(r1))
				.fetchAllHits()).containsExactly(b1);
		assertThat(Search.session(em)
				.search(BridgeEntity.class)
				.where(p -> p.match().field("reference").matching(r2))
				.fetchAllHits()).isEmpty();
		assertThat(Search.session(em)
				.search(BridgeEntity.class)
				.select(f -> f.field("reference", ValueConvert.NO))
				.where(p -> p.match().field("reference").matching(r1))
				.fetchAllHits()).hasSize(1).first().isEqualTo("test.search.BridgeLinkedEntity|" + l1.getId());
	}

}
