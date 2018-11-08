package test.core.metamodel;

import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.junit.Test;

import test.core.AbstractBasicApplicationTestCase;

public class TestMetaModel extends AbstractBasicApplicationTestCase {

	@Test
	public void testMetaModel() throws NoSuchFieldException, SecurityException {
		// Class<?> est utilisé sur GenericEntityReference ; ATTENTION,
		// l'annotation @Type est nécessaire pour un traitement correct par Hibernate.
		super.testMetaModel(PostalCode.class, Class.class, Comparable.class);
	}

}
