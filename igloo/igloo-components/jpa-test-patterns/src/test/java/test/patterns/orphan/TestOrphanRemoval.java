package test.patterns.orphan;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.cfg.AvailableSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import test.patterns.EntityManagerFactoryExtension;

class TestOrphanRemoval {

	@RegisterExtension
	EntityManagerFactoryExtension extension = new EntityManagerFactoryExtension(
			AvailableSettings.DIALECT, org.hibernate.dialect.H2Dialect.class.getName(),
			AvailableSettings.HBM2DDL_AUTO, "create",
			AvailableSettings.JPA_JDBC_DRIVER, org.h2.Driver.class.getName(),
			AvailableSettings.JPA_JDBC_URL, "jdbc:h2:mem:jpa_patterns;INIT=create schema if not exists jpa_patterns",
			AvailableSettings.LOADED_CLASSES, Arrays.asList(OrphanOwner.class, OrphanItem.class)
	);

	/**
	 * Test orphanRemoval behavior. {@link OrphanOwner} is an entity with a list of {@link OrphanItem} (@OneToMany).
	 * 
	 * Check that unset orphanItem.owner (owning-side of the relation) delete OrphanItem entity.
	 * 
	 * Parameters are injected with {@link EntityManagerFactoryExtension}.
	 */
	@Test
	void testOrphanRemoval_nominal(EntityManager entityManager, EntityTransaction transaction) {
		// setup
		long entityId = 1l;
		OrphanOwner owner;
		OrphanItem item;
		setupAndCheckOrphans(entityManager, entityId);
		
		// unlink
		owner = entityManager.find(OrphanOwner.class, entityId);
		item = entityManager.find(OrphanItem.class, entityId);
		// clear is needed, else item is kept with a null owner_id field
		owner.items.clear();
		// this is optional ; entity removal is done by collection clearing
		item.owner = null;
		entityManager.flush();
		entityManager.clear();
		
		// check remaining entities
		assertThat(list(entityManager, OrphanOwner.class)).hasSize(1);
		assertThat(list(entityManager, OrphanItem.class)).isEmpty();
		entityManager.flush();
		entityManager.clear();
	}

	/**
	 * Test orphanRemoval behavior. {@link OrphanOwner} is an entity with a list of {@link OrphanItem} (@OneToMany).
	 * 
	 * Check that unset orphanItem.owner (owning-side of the relation) delete OrphanItem entity.
	 * 
	 * Parameters are injected with {@link EntityManagerFactoryExtension}.
	 */
	@Test
	void testOrphanRemoval_keepOrphan(EntityManager entityManager, EntityTransaction transaction) {
		// setup
		long entityId = 1l;
		OrphanItem item;
		setupAndCheckOrphans(entityManager, entityId);
		
		// unlink
		entityManager.find(OrphanOwner.class, entityId);
		item = entityManager.find(OrphanItem.class, entityId);
		// when child entity only is modified, it is not removed as removal is only triggered by collection
		item.owner = null;
		entityManager.flush();
		entityManager.clear();
		
		// check remaining entities
		// OrphanOwner losts its child as owning side is modified
		assertThat(list(entityManager, OrphanOwner.class)).hasSize(1);
		assertThat(list(entityManager, OrphanOwner.class)).first().satisfies(o -> assertThat(o.items).isEmpty());
		// OrphanItem is kept, as no modification was triggered on the collection
		assertThat(list(entityManager, OrphanItem.class)).hasSize(1);
		assertThat(list(entityManager, OrphanItem.class)).first().satisfies(o -> assertThat(o.owner).isNull());
		entityManager.flush();
		entityManager.clear();
	}

	private void setupAndCheckOrphans(EntityManager entityManager, long entityId) {
		OrphanOwner owner = new OrphanOwner();
		owner.id = entityId;
		entityManager.persist(owner);
		OrphanItem item = new OrphanItem();
		item.id = entityId;
		item.owner = owner;
		owner.items.add(item);
		entityManager.persist(item);
		entityManager.flush();
		entityManager.clear();
		
		// check setup
		assertThat(list(entityManager, OrphanOwner.class)).hasSize(1);
		assertThat(list(entityManager, OrphanItem.class)).hasSize(1);
		entityManager.flush();
		entityManager.clear();
	}

	private <T> List<T> list(EntityManager em, Class<T> clazz) {
		CriteriaQuery<T> q = em.getCriteriaBuilder().createQuery(clazz);
		Root<T> t = q.from(clazz);
		q.select(t);
		return em.createQuery(q).getResultList();
	}
}
