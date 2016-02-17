package fr.openwide.core.basicapp.core.test.metamodel;

import org.junit.Test;

import fr.openwide.core.basicapp.core.business.common.model.CodePostal;
import fr.openwide.core.basicapp.core.test.AbstractBasicApplicationTestCase;

public class TestMetaModel extends AbstractBasicApplicationTestCase {

	@Test
	public void testMetaModel() throws NoSuchFieldException, SecurityException {
		super.testMetaModel(CodePostal.class);
	}

}
