package fr.openwide.core.test.commons.util.validator;

import org.junit.Assert;
import org.junit.Test;

import fr.openwide.core.commons.util.validator.PermissivePhoneNumberValidator;

public class TestPermissivePhoneNumberValidator {

	@Test
	public void testValidation() {
		Assert.assertTrue(PermissivePhoneNumberValidator.getInstance().isValid("+1 (555) 555-5555"));
		Assert.assertTrue(PermissivePhoneNumberValidator.getInstance().isValid("+33 3 33 33 33 33"));
		Assert.assertFalse(PermissivePhoneNumberValidator.getInstance().isValid("++3 3 33 33 33 33"));
		Assert.assertFalse(PermissivePhoneNumberValidator.getInstance().isValid("33 3 33 33 33 33+"));
		Assert.assertFalse(PermissivePhoneNumberValidator.getInstance().isValid("some text"));
	}

}
