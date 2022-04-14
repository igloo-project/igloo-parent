package test;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.cfg.AvailableSettings;
import org.igloo.jpa.test.EntityManagerFactoryExtension;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class TestGenericEntityReferenceUserType {

	@RegisterExtension
	private EntityManagerFactoryExtension extension = new EntityManagerFactoryExtension(
			AvailableSettings.DIALECT, org.hibernate.dialect.H2Dialect.class.getName(),
			AvailableSettings.HBM2DDL_AUTO, "create",
			AvailableSettings.JPA_JDBC_DRIVER, org.h2.Driver.class.getName(),
			AvailableSettings.JPA_JDBC_URL, "jdbc:h2:mem:jpa_patterns;INIT=create schema if not exists jpa_patterns;TRACE_LEVEL_FILE=3;TRACE_LEVEL_SYSTEM_OUT=3",
			AvailableSettings.LOADED_CLASSES, Arrays.asList(MyEntity.class),
			AvailableSettings.XML_MAPPING_ENABLED, Boolean.FALSE.toString()
	);

	@Test
	void testType(EntityManagerFactory entityManagerFactory) {
		doInTransactionWithoutResult(entityManagerFactory, (em) -> {
			em.persist(new MyEntity(1l, new Bateau(1l)));
			em.persist(new MyEntity(2l, new Bateau(2l)));
			em.persist(new MyEntity(3l, new Navire(3l)));
		});
	}

	void doInTransactionWithoutResult(EntityManagerFactory entityManagerFactory, Consumer<EntityManager> r) {
		doInTransaction(entityManagerFactory, (em) -> { r.accept(em); return null; });
	}

	<T> T doInTransaction(EntityManagerFactory entityManagerFactory, Function<EntityManager, T> f) {
		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			em.getTransaction().begin();
			return f.apply(em);
		} catch (RuntimeException e1) {
			throw e1;
		} catch (Exception e2) {
			throw new IllegalStateException("Wrapped checked exception", e2);
		} finally {
			em.flush();
			em.getTransaction().commit();
			em.close();
		}
	}

	@Entity
	public static class MyEntity {
		@Id
		public Long id;
		@Type(type = "org.igloo.jpa.type.GenericEntityReferenceType")
		@Columns(columns = {
				@Column(name = "reference_classname"),
				@Column(name = "reference_id")
		})
		public GenericEntityReference<Long, ?> reference;

		public MyEntity(Long id, GenericEntity<Long, ?> reference) {
			this.id = id;
			this.reference = GenericEntityReference.of(reference);
		}
	}

	public class Bateau extends GenericEntity<Long, Bateau> {
		private static final long serialVersionUID = 1L;
		private Long id;

		public Bateau(Long id) {
			this.id = id;
		}

		@Override
		public Long getId() {
			return id;
		}

		@Override
		public void setId(Long id) {
			this.id = id;
		}
	}

	public class Navire extends GenericEntity<Long, Navire> {
		private static final long serialVersionUID = 1L;
		private Long id;

		public Navire(Long id) {
			this.id = id;
		}

		@Override
		public Long getId() {
			return id;
		}

		@Override
		public void setId(Long id) {
			this.id = id;
		}
	}
}
