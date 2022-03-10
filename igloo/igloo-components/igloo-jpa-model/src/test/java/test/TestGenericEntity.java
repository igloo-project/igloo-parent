package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.util.Optional;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.junit.jupiter.api.Test;

class TestGenericEntity {

	/**
	 * Check that GenericEntity can be used without Hibernate on classpath. This must be launched with surefire
	 * appropriately configured. An assumption prevent running with m2e (as hibernate dependency is not excluded).
	 */
	@Test
	void testGenericEntityHibernateLess() {
		assumeNotEclipse();
		assertHibernateLess();
		MyEntity entity1 = new MyEntity(1l);
		MyEntity entity2 = new MyEntity(2l);
		// check codes that conditionaly uses Hibernate.getClass
		assertThat(entity1).isNotEqualTo(entity2);
		assertThat(new GenericEntityReference<>(entity1).getType()).isEqualTo(MyEntity.class);
	}
	
	/**
	 * see {@link TestGenericEntity#testGenericEntityHibernateLess()}
	 */
	@Test
	void testGenericEntityReferenceHibernateLess() {
		assumeNotEclipse();
		assertHibernateLess();
		MyEntity entity1 = new MyEntity(1l);
		assertThat(new GenericEntityReference<>(entity1).getType()).isEqualTo(MyEntity.class);
	}

	private void assertHibernateLess() {
		assertThatThrownBy(() -> Class.forName("org.hibernate.Hibernate")).isInstanceOf(ClassNotFoundException.class);
		assertThatThrownBy(() -> Class.forName("org.hibernate.search.annotations.Analyzer")).isInstanceOf(ClassNotFoundException.class);
	}

	private void assumeNotEclipse() {
		assumeThat(System.getProperty("sun.java.command", null)).as("Eclipse/m2e environment detected").doesNotContain("eclipse");
	}

	public static final class MyEntity extends GenericEntity<Long, MyEntity> {
		private static final long serialVersionUID = 8301422572480953947L;
		
		private Long id;
		
		public MyEntity(Long id) {
			this.id = id;
		}
		
		@Override
		public int compareTo(MyEntity o) {
			return Long.compare(Optional.ofNullable(this.getId()).orElse(0l), Optional.ofNullable(o).map(MyEntity::getId).orElse(0l	));
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
