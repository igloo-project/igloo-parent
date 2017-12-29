package org.iglooproject.basicapp.core.test.metamodel;

import org.junit.Test;

import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.basicapp.core.test.AbstractBasicApplicationTestCase;

public class TestMetaModel extends AbstractBasicApplicationTestCase {

	@Test
	public void testMetaModel() throws NoSuchFieldException, SecurityException {
		super.testMetaModel(PostalCode.class);
	}

}
