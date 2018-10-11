package org.iglooproject.basicapp.core.test.metamodel;

import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.basicapp.core.test.AbstractBasicApplicationTestCase;
import org.junit.Test;

public class TestMetaModel extends AbstractBasicApplicationTestCase {

	@Test
	public void testMetaModel() throws NoSuchFieldException, SecurityException {
		// Class<?> est utilisé sur GenericEntityReference ; ATTENTION,
		// l'annotation @Type est nécessaire pour un traitement correct par Hibernate.
		super.testMetaModel(PostalCode.class, Class.class, Comparable.class);
	}

}
