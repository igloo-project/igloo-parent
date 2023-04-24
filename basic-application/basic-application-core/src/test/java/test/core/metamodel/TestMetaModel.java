package test.core.metamodel;

import org.junit.jupiter.api.Test;

import test.core.AbstractBasicApplicationTestCase;

class TestMetaModel extends AbstractBasicApplicationTestCase {

	@Test
	void testMetaModel() throws NoSuchFieldException, SecurityException {
		// Class<?> est utilisé sur GenericEntityReference ; ATTENTION,
		// l'annotation @Type est nécessaire pour un traitement correct par Hibernate.
		//TODO: igloo-boot
		//super.testMetaModel(PostalCode.class, Class.class, Comparable.class);
	}

}
