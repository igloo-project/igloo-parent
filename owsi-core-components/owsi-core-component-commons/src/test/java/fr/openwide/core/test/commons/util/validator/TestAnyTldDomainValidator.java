package fr.openwide.core.test.commons.util.validator;

import org.junit.Assert;
import org.junit.Test;

import fr.openwide.core.commons.util.validator.AnyTldDomainValidator;

public class TestAnyTldDomainValidator {
	
	@Test
	public void testValidation() {
		Assert.assertTrue(AnyTldDomainValidator.getInstance().isValid("www.google.fr"));
		Assert.assertTrue(AnyTldDomainValidator.getInstance().isValid("google.fr"));
		Assert.assertTrue(AnyTldDomainValidator.getInstance().isValid("google9.fr"));
		Assert.assertTrue(AnyTldDomainValidator.getInstance().isValid("gîte-lasoldanelle.com"));
		Assert.assertTrue(AnyTldDomainValidator.getInstance().isValid("www.gîte-lasoldanelle.com"));
		Assert.assertTrue(AnyTldDomainValidator.getInstance().isValid("île-lasoldanelle.com"));
		Assert.assertFalse(AnyTldDomainValidator.getInstance().isValid("-test-plop.fr"));
		Assert.assertFalse(AnyTldDomainValidator.getInstance().isValid("test-plop-.fr"));
		Assert.assertFalse(AnyTldDomainValidator.getInstance().isValid("test-plop-.fr"));
		Assert.assertFalse(AnyTldDomainValidator.getInstance().isValid("test@-plop.fr"));
		Assert.assertFalse(AnyTldDomainValidator.getInstance().isValid("test,-plop.fr"));
	}

}
