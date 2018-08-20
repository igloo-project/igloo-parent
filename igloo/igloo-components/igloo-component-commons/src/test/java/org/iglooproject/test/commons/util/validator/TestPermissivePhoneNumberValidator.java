package org.iglooproject.test.commons.util.validator;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import org.iglooproject.commons.util.validator.PermissivePhoneNumberValidator;

@RunWith(Parameterized.class)
public class TestPermissivePhoneNumberValidator {

	@Parameters(name = "{index} - \"{0}\" expecting {1}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ "+1 (555) 555-5555", true },
				{ "+33 3 33 33 33 33", true },
				{ "++3 3 33 33 33 33", false },
				{ "33 3 33 33 33 33+", false },
				{ "some text", false },
		});
	}
	
	@Parameter(0)
	public String phoneNumber;
	
	@Parameter(1)
	public boolean expectedValid;
	
	@Test
	public void testValidation() {
		PermissivePhoneNumberValidator validator = PermissivePhoneNumberValidator.getInstance();
		if (expectedValid) {
			Assert.assertTrue(validator.isValid(phoneNumber));
		} else {
			Assert.assertFalse(validator.isValid(phoneNumber));
		}
	}
}
